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

    final Map<ClassProcessor, List<Class<?>>> classProcessorList = new ConcurrentHashMap<>();


    public ComponentScanner(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
        this.bundleTracker = new BundleTracker(bundleContext, Bundle.STARTING, this);
    }


    public void addProcessor(ClassProcessor processor) {
        LOGGER.info("New processor added " + processor)
        List<Class<?>> classesSet = this.classProcessorList.get(processor);

        if (classesSet == null) {
            classesSet = new ArrayList<>();
        }

        this.classProcessorList.put(processor, classesSet);
    }


    @Override
    public Object addingBundle(Bundle bundle, BundleEvent event) {
//        if (event.getType() == BundleEvent.STARTED) {
        // TODO : Add regex for component classes parsing
        //        The demo code only works for one component class in MANIFEST.MF
        // TODO: Add loop for classes

//        System.out.println("Found a Bundle with a Service Component Classes : " + serviceComponentClasses);
//        String fileName = "/" + serviceComponentClasses.replace('.', '/') + ".class";


        def classPool = ClassPool.default;
        classPool.insertClassPath(new ClassClassPath(bundle.getBundleContext().class))
        def entries = bundle.findEntries("/", "*.class", true);

        String[] fileNames = [];

        for (URL fileName in entries) {

//            println fileName.path;

//                // Scan the component class via byte code reading (javassist library is used)
//                // more details about javassist see http://www.jboss.org/javassist/
            URL componentClassUrl = bundle.getResource(fileName.path);
            try {
                InputStream componentClassInputStream = componentClassUrl.openStream();
                DataInputStream dstream = new DataInputStream(componentClassInputStream);
                CtClass ct = classPool.makeClass(dstream);

                // DS Component name is the class name
                String componentName = ct.getName();
//                println "start processing" + classProcessorList.size()
                for (ClassProcessor processor in classProcessorList.keySet()) {
                    if (processor.shouldProcess(ct)) {
                        processor.process(ct.toClass())
//                        println ct.toClass()
//                        classProcessorList.get(processor).add(ct.toClass());
                    }
                }


                for (ClassProcessor processor in classProcessorList.keySet()) {

                    def get = classProcessorList.get(processor);
                    if (get.size() > 0) {
                        processor.process(get)
                        classProcessorList.put(processor, new ArrayList<Class<?>>());
                    }
                }


            } catch (Exception e) {
                System.out.println("Error Bytecode Annotation parsing.");
                e.printStackTrace();
            }
        }
//        }
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
