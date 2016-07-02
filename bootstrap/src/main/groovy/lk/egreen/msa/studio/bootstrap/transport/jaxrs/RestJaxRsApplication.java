package lk.egreen.msa.studio.bootstrap.transport.jaxrs;

import lk.egreen.msa.studio.bootstrap.SampleRestService;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.wiring.BundleWiring;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.logging.Logger;

/**
 * Created by dewmal on 7/2/16.
 */
@ApplicationPath("/")
public class RestJaxRsApplication extends ResourceConfig {

    private static final Logger LOGGER = Logger.getLogger(RestJaxRsApplication.class.getName());

    public RestJaxRsApplication(BundleContext bundleContext) {
        // Register resources and providers using package-scanning.
        packages("lk.egreen.msa.studio.bootstrap");

        // Register my custom provider - not needed if it's in my.package.
        register(SampleRestService.class);
        // Register an instance of LoggingFilter.
        register(new LoggingFilter(LOGGER, true));

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
}
