package lk.egreen.msa.studio.sample;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Created by dewmal on 7/3/16.
 */
@Path("/hello-sample")
public class HelloWorldApi {
    // The Java method will process HTTP GET requests
    @GET
    // The Java method will produce content identified by the MIME Media
    // type "text/plain"
    @Produces("application/json")
    public Object getMessage() {
        return new Object() {
            private String version = "Hello Hello";

            String getVersion() {
                return version;
            }

            void setVersion(String version) {
                this.version = version;
            }
        };
    }
}
