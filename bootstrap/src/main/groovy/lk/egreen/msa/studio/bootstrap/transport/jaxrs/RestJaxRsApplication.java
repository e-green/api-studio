package lk.egreen.msa.studio.bootstrap.transport.jaxrs;

import javassist.CtClass;
import lk.egreen.msa.studio.bootstrap.SampleRestService;
import lk.egreen.msa.studio.extender.ClassProcessor;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.glassfish.jersey.server.internal.RuntimeDelegateImpl;
import org.glassfish.jersey.server.model.Resource;
import org.osgi.framework.BundleContext;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.RuntimeDelegate;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by dewmal on 7/2/16.
 */
@ApplicationPath("/")
public class RestJaxRsApplication extends ResourceConfig implements ClassProcessor {

    private static final Logger LOGGER = Logger.getLogger(RestJaxRsApplication.class.getName());
    private FeatureContext context;

    public RestJaxRsApplication(BundleContext bundleContext) {
        // Register resources and providers using package-scanning.
        packages("lk.egreen.msa.studio.bootstrap");

        // Register my custom provider - not needed if it's in my.package.
        register(SampleRestService.class);
        // Register an instance of LoggingFilter.
        register(new LoggingFilter(LOGGER, true));
        register(RolesAllowedDynamicFeature.class);

        register(new AbstractBinder() {
            @Override
            protected void configure() {
            }
        });
        // Enable Tracing support.
        property(ServerProperties.TRACING, "ALL");

//        bundleContext.addBundleListener(new BundleListener() {
//            @Override
//            public void bundleChanged(BundleEvent event) {
//
//                if (event.getType() == BundleEvent.INSTALLED) {
//                    Bundle bundle = event.getBundle();
//
//
//                    System.out.println(bundle.getLocation());
//
////                    bundle.get
//
//                    Enumeration<URL> entries = bundle.findEntries("/", "*.class", true);
//                    if (entries != null) {
//                        while (entries.hasMoreElements()) {
//                            URL url = entries.nextElement();
//                            System.out.println(url);
//                            System.out.println(url.getFile());
//                            try {
//                                Class<?> aClass = getClassLoader().loadClass(url.toString());
//
//                                System.out.println(aClass + " " );
////                                if (restEnabled) {
////                                    register(aClass);
////                                }
//                            } catch (ClassNotFoundException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }
//            }
//        });

    }

    @Override
    public boolean shouldProcess(CtClass aClass) {
        return aClass.hasAnnotation(Path.class);
    }

    @Override
    public void process(Class<?> processClass) {

    }

    @Override
    public void process(List<Class<?>> processClasses) {


        for (Class<?> aClass : processClasses) {
            System.out.println(aClass);
            RuntimeDelegate runtimeDelegate = new RuntimeDelegateImpl();
            runtimeDelegate.createEndpoint(this, aClass);
        }
    }

    @Override
    public ResourceConfig register(Class<?> componentClass) {
        return super.register(componentClass);
    }
}
