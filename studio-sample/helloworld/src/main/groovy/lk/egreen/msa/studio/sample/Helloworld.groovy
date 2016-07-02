package lk.egreen.msa.studio.sample

import lk.egreen.msa.studio.SampleApi
import org.osgi.framework.BundleActivator
import org.osgi.framework.BundleContext

/**
 * Created by dewmal on 7/2/16.
 */
class Helloworld implements BundleActivator {


    @Override
    void start(BundleContext context) throws Exception {
        println "working.... Hello world"

        def reference = context.getServiceReference(SampleApi.class);
        println reference;
        def sampleApi = context.getService(reference);
        sampleApi.version("HellWorld App 1")


    }

    @Override
    void stop(BundleContext context) throws Exception {

    }
}
