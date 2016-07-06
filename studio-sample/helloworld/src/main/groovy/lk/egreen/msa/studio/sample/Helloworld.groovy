package lk.egreen.msa.studio.sample

import lk.egreen.msa.studio.SampleApi
import org.glassfish.jersey.servlet.init.JerseyServletContainerInitializer
import org.osgi.framework.BundleActivator
import org.osgi.framework.BundleContext
import org.osgi.service.http.HttpService

/**
 * Created by dewmal on 7/2/16.
 */
class Helloworld implements BundleActivator {


    @Override
    void start(BundleContext context) throws Exception {
//        println "working.... Hello world"

        def reference = context.getServiceReference(SampleApi.class);
        println reference;
        def sampleApi = context.getService(reference);
//        sampleApi.version("HellWorld App 1")


        def service = context.getService(context.getServiceReference(HttpService.class));


//        service.


    }

    @Override
    void stop(BundleContext context) throws Exception {

    }
}
