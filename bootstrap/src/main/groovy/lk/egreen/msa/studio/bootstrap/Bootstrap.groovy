package lk.egreen.msa.studio.bootstrap

import lk.egreen.msa.studio.SampleApi
import lk.egreen.msa.studio.bootstrap.transport.jaxrs.RestJaxRsApplication
import lk.egreen.msa.studio.bootstrap.transport.jaxrs.RestJaxRsClassContainer
import lk.egreen.msa.studio.extender.ComponentScanner
import org.osgi.framework.BundleActivator
import org.osgi.framework.BundleContext
import org.osgi.framework.ServiceReference
import org.osgi.service.http.HttpService
import org.osgi.util.tracker.ServiceTracker

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
        RestJaxRsClassContainer classContainer = new RestJaxRsClassContainer(bundleContext);
        ComponentScanner componentScanner = new ComponentScanner(bundleContext)
        componentScanner.addProcessor(classContainer)
        componentScanner.open();

//        httpTracker = new ServiceTracker(bundleContext, HttpService.class.getName(), null) {
//
//
//            public void removedService(ServiceReference reference, Object service) {
//                println("HTTP service is available, unregister our servlet...")
//                try {
//                    ((HttpService) service).unregister("/rest");
//                } catch (IllegalArgumentException exception) {
//                    // Ignore; servlet registration probably failed earlier on...
//                }
//            }
//
//            public Object addingService(ServiceReference reference) {
//                println("HTTP service is available, register our servlet...")
////                HttpService httpService = (HttpService) this.context.getService(reference);
//
//
//                return httpService;
//            }
//
//
//        }
//
//        httpTracker.open();

        println("working");
    }


    @Override
    void stop(BundleContext bundleContext) throws Exception {
        // stop tracking all HTTP services...
//        httpTracker.close();
    }
}
