package lk.egreen.msa.studio.bootstrap.transport.jaxrs;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import javax.ws.rs.ApplicationPath;
import java.util.logging.Logger;

/**
 * Created by dewmal on 7/2/16.
 */
@ApplicationPath("/")
public class RestJaxRsApplication extends ResourceConfig  {

    private static final Logger LOGGER = Logger.getLogger(RestJaxRsApplication.class.getName());

    public RestJaxRsApplication() {
        // Register resources and providers using package-scanning.
        packages("lk.egreen.msa.studio.bootstrap");
        // Register an instance of LoggingFilter.
        register(new LoggingFilter(LOGGER, true));
        property(ServerProperties.TRACING, "ALL");
    }


    @Override
    public ResourceConfig register(Class<?> componentClass) {
        return super.register(componentClass);
    }
}
