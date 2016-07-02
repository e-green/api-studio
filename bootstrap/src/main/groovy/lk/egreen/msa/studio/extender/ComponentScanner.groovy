package lk.egreen.msa.studio.extender

import javassist.ClassPool
import javassist.CtClass
import javassist.bytecode.AnnotationsAttribute
import javassist.bytecode.ClassFile
import javassist.bytecode.MethodInfo
import javassist.bytecode.annotation.Annotation
import javassist.bytecode.annotation.MemberValue
import org.osgi.framework.Bundle
import org.osgi.framework.BundleContext
import org.osgi.framework.BundleEvent
import org.osgi.util.tracker.BundleTracker
import org.osgi.util.tracker.BundleTrackerCustomizer

import javax.ws.rs.Path

/**
 * Created by dewmal on 7/3/16.
 */
class ComponentScanner implements BundleTrackerCustomizer {
    final BundleTracker bundleTracker;

    final BundleContext bundleContext;

    public ComponentScanner(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
        this.bundleTracker = new BundleTracker(bundleContext, Bundle.STARTING, this);
    }

    @Override
    public Object addingBundle(Bundle bundle, BundleEvent event) {
//        if (event.getType() == BundleEvent.STARTED) {
        // TODO : Add regex for component classes parsing
        //        The demo code only works for one component class in MANIFEST.MF
        // TODO: Add loop for classes

//        System.out.println("Found a Bundle with a Service Component Classes : " + serviceComponentClasses);
//        String fileName = "/" + serviceComponentClasses.replace('.', '/') + ".class";


        def entries = bundle.findEntries("/", "*.class", true);

        String[] fileNames = [];

        for (URL fileName in entries) {

            println fileName.path;

//                // Scan the component class via byte code reading (javassist library is used)
//                // more details about javassist see http://www.jboss.org/javassist/
            URL componentClassUrl = bundle.getResource(fileName.path);
            try {
                InputStream componentClassInputStream = componentClassUrl.openStream();
                DataInputStream dstream = new DataInputStream(componentClassInputStream);
                CtClass ct = ClassPool.default.makeClass(dstream);

                // DS Component name is the class name
                String componentName = ct.getName();
                System.out.println("DS Component Name is : " + componentName);


                // DS component properties read component annotationc
//                AnnotationsAttribute annotationsAttribute = (AnnotationsAttribute) ct.getAttribute(AnnotationsAttribute.visibleTag);
                if ( ct.hasAnnotation(Path.class)) {
                    println ct.toClass(bundle.getClass().getClassLoader())
                }

//                // DS find activate Method
//                List<MethodInfo> methods = ct.getMethods();
//                for (MethodInfo methodInfo : methods) {
//                    AnnotationsAttribute methodAnnotationAttribute = (AnnotationsAttribute) methodInfo.getAttribute(AnnotationsAttribute.visibleTag);
//                    if(methodAnnotationAttribute != null){
//                        Annotation[] methodAnnotations = methodAnnotationAttribute.getAnnotations();
//                        if(methodAnnotations != null){
//                            for (Annotation methodAnnotation : methodAnnotations) {
//                                if(methodAnnotation.getTypeName().equals("service.component.annotations.Activate")){
//                                    System.out.println("DS activate Method name is : " + methodInfo.getName());
//                                }
//                            }
//                        }
//                    }
//                }
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
