package lk.egreen.msa.studio.extender

import javassist.ClassClassPath
import javassist.ClassPool
import javassist.CtClass
import org.osgi.framework.Bundle
import org.osgi.framework.BundleContext
import org.osgi.framework.BundleEvent
import org.osgi.util.tracker.BundleTracker
import org.osgi.util.tracker.BundleTrackerCustomizer

import javax.ws.rs.Path
import java.util.concurrent.ConcurrentHashMap
import java.util.logging.Logger

/**
 * Created by dewmal on 7/3/16.
 */
class ComponentScanner implements BundleTrackerCustomizer {

    private static final Logger LOGGER = Logger.getLogger(ComponentScanner.class.name);

    final BundleTracker bundleTracker;

    final BundleContext bundleContext;

    final Map<ClassProcessor, Set<Class<?>>> classProcessorList = new ConcurrentHashMap<>();


    public ComponentScanner(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
        this.bundleTracker = new BundleTracker(bundleContext, Bundle.STARTING, this);
    }


    public void addProcessor(ClassProcessor processor) {
        LOGGER.info("New processor added " + processor)
        Set<Class<?>> classesSet = this.classProcessorList.get(processor);

        if (classesSet == null) {
            classesSet = new HashSet<>();
        }

        this.classProcessorList.put(processor, classesSet);
    }


    @Override
    public Object addingBundle(Bundle bundle, BundleEvent event) {

        def headers = bundle.getHeaders().get("Api-Studio");

        if (headers != null && event.getType() == BundleEvent.STARTING) {

            def classPool = ClassPool.default;
            classPool.insertClassPath(new ClassClassPath(bundle.getBundleContext().class))
            def entries = bundle.findEntries("/", "*.class", true);


            for (URL fileName in entries) {

//                // Scan the component class via byte code reading (javassist library is used)
//                // more details about javassist see http://www.jboss.org/javassist/
                URL componentClassUrl = bundle.getResource(fileName.path);
                try {
                    InputStream componentClassInputStream = componentClassUrl.openStream();
                    DataInputStream dstream = new DataInputStream(componentClassInputStream);
                    CtClass ct = classPool.makeClass(dstream);

                    // DS Component name is the class name
//                String componentName = ct.getName();
                    println classProcessorList.size()
                    for (ClassProcessor processor in classProcessorList.keySet()) {
                        println ct.getName()
                        println processor.shouldProcess(ct)
                        if (processor.shouldProcess(ct)) {
//                        processor.process(ct.toClass(), bundle.getHeaders())
                            classProcessorList.get(processor).add(ct.toClass());
                        }
                    }


                    for (ClassProcessor processor in classProcessorList.keySet()) {
                        def get = classProcessorList.get(processor);
                        if (get.size() > 0) {
                            processor.process(get, bundle.getHeaders(), bundle)
                            println "Process done"
                            classProcessorList.put(processor, new ArrayList<Class<?>>());
                        }
                    }


                } catch (Exception e) {
                    System.out.println("Error Bytecode Annotation parsing.");
                    e.printStackTrace();
                }
            }
        }
        return bundle;
    }

    @Override
    public void modifiedBundle(Bundle bundle, BundleEvent event, Object object) {
    }

    @Override
    public void removedBundle(Bundle bundle, BundleEvent event, Object object) {
    }

    public void close() {
        bundleTracker.close();
    }

    public void open() {
        bundleTracker.open();
    }
}
