package lk.egreen.msa.studio.bootstrap.transport.jaxrs;

import javassist.CtClass;
import lk.egreen.msa.studio.extender.ClassProcessor;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
//import org.glassfish.jersey.server.ResourceConfig;
//import org.glassfish.jersey.servlet.ServletContainer;

import javax.ws.rs.Path;
import java.util.List;

/**
 * Created by dewmal on 7/3/16.
 */
public class RestServletContainer extends ServletContainer implements ClassProcessor{

    public RestServletContainer(ResourceConfig resourceConfig) {
        super(resourceConfig);
    }

    public RestServletContainer() {
        super();
    }

    @Override
    public boolean shouldProcess(CtClass aClass) {
        return aClass.hasAnnotation(Path.class);
    }

    @Override
    public void process(Class<?> processClass) {
        ResourceConfig configuration = getConfiguration();

        configuration.register(processClass);
        reload(configuration);
        System.out.println("Register Class " + processClass);
    }

    @Override
    public void process(List<Class<?>> processClasses) {





//        getConfiguration().register(new DynamicFeature() {
//            @Override
//            public void configure(ResourceInfo resourceInfo, FeatureContext context) {
//
//            }
//        });


    }
}
