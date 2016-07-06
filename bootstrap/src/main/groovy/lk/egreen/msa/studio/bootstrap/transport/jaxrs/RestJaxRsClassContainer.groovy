package lk.egreen.msa.studio.bootstrap.transport.jaxrs

import javassist.CtClass
import lk.egreen.msa.studio.extender.ClassProcessor
import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.server.model.Resource
import org.glassfish.jersey.servlet.ServletContainer
import org.osgi.framework.Bundle
import org.osgi.framework.BundleContext
import org.osgi.framework.ServiceReference
import org.osgi.service.http.HttpService
import org.osgi.util.tracker.ServiceTracker

import javax.ws.rs.Path

/**
 * Created by dewmal on 7/4/16.
 */
class RestJaxRsClassContainer implements ClassProcessor {

    private final BundleContext bundleContext;

    RestJaxRsClassContainer(BundleContext bundleContext) {
        this.bundleContext = bundleContext


    }

    @Override
    boolean shouldProcess(CtClass aClass) {
        return aClass.hasAnnotation(Path.class)
    }

    @Override
    void process(Class<?> processClass, Dictionary<?, ?> dictionary) {

    }

    @Override
    void process(Set<Class<?>> processClasses, Dictionary<?, ?> dictionary,Bundle bundle) {
//        HttpService httpService = (HttpService) this.bundleContext.getService(bundleContext.getServiceReference(HttpService.class));


        ServiceTracker httpTracker = null
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

                ResourceConfig resConfig = new ResourceConfig();


                for (final Class<?> cls : processClasses) {
                    println cls.getName()
//            Resource resource=;
                    resConfig.register(cls);
                }
                Dictionary containerInitParameters = new Hashtable();
                // containerInitParameters.put("javax.ws.rs.Application","lk.egreen.msa.studio.bootstrap.SampleRestService.RestJaxRsApplication")
                containerInitParameters.put("jersey.config.server.tracing", "ALL")
                ServletContainer container = new ServletContainer(resConfig);
                String applicationPath = "/api/" + dictionary.get("Api-Studio");
                println applicationPath

                def httpContext = httpService.createDefaultHttpContext();

                httpService.registerServlet(applicationPath, container, containerInitParameters, httpContext);

                return httpService;
            }


        }
        bundle.
        httpTracker.open();



    }
}
