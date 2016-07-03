package lk.egreen.msa.studio.bootstrap

import lk.egreen.msa.studio.SampleApi
import lk.egreen.msa.studio.bootstrap.transport.jaxrs.RestJaxRsApplication
import lk.egreen.msa.studio.bootstrap.transport.jaxrs.RestServletContainer
import lk.egreen.msa.studio.extender.ComponentScanner
import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.servlet.ServletContainer
import org.osgi.framework.BundleActivator
import org.osgi.framework.BundleContext
import org.osgi.framework.BundleEvent
import org.osgi.framework.BundleListener
import org.osgi.framework.FrameworkEvent
import org.osgi.framework.FrameworkListener
import org.osgi.framework.ServiceReference
import org.osgi.service.http.HttpService
import org.osgi.util.tracker.BundleTrackerCustomizer
import org.osgi.util.tracker.ServiceTracker

import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by dewmal on 6/26/16.
 */
class Bootstrap implements BundleActivator {

//    HttpService httpService;
    ServiceTracker httpTracker
    ServiceTracker apiTracker


    @Override
    void start(BundleContext bundleContext) throws Exception {
        println('Hello world 123');

//        final
        bundleContext.registerService(SampleApi.class, new SampleApi() {
            @Override
            void version(String code) {
                println "Impliment version on bootstrap " + code

            }
        }, null);

        // create a resource config that scans for JAX-RS resources and providers
        // in com.example.rest package

//        println bundleContext.getService(bundleContext.getServiceReference(HttpServiceExtension.class))


        httpTracker = new ServiceTracker(bundleContext, HttpService.class.getName(), null) {



            public void removedService(ServiceReference reference, Object service) {
                println("HTTP service is available, unregister our servlet...")
                try {
                    ((HttpService) service).unregister("/rest");
                } catch (IllegalArgumentException exception) {
                    // Ignore; servlet registration probably failed earlier on...
                }
            }

            public Object addingService(ServiceReference reference) {



                println("HTTP service is available, register our servlet...")
                HttpService httpService = (HttpService) this.context.getService(reference);
                try {
                    RestJaxRsApplication rsApplication = new RestJaxRsApplication(bundleContext);
                    RestServletContainer container = new RestServletContainer(rsApplication)
                    Dictionary containerInitParameters = new Hashtable();
//                    containerInitParameters.put("javax.ws.rs.Application","lk.egreen.msa.studio.bootstrap.SampleRestService.RestJaxRsApplication")
                    containerInitParameters.put("jersey.config.server.tracing", "ALL")


                    httpService.registerServlet("/rest", container, containerInitParameters, null);


                    ComponentScanner componetScanner = new ComponentScanner(bundleContext)
                    componetScanner.addProcessor(rsApplication);
                    componetScanner.open();


                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return httpService;
            }


        }

        httpTracker.open();

        println("working");
    }


    @Override
    void stop(BundleContext bundleContext) throws Exception {
        // stop tracking all HTTP services...
        httpTracker.close();
    }
}
