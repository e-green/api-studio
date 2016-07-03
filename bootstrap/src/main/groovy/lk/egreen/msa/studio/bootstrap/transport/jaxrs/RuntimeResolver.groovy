package lk.egreen.msa.studio.bootstrap.transport.jaxrs

import org.glassfish.jersey.server.internal.RuntimeDelegateImpl

import javax.ws.rs.container.DynamicFeature
import javax.ws.rs.container.ResourceInfo
import javax.ws.rs.core.FeatureContext
import javax.ws.rs.ext.RuntimeDelegate

/**
 * Created by dewmal on 7/3/16.
 */
class RuntimeResolver implements DynamicFeature {

    @Override
    void configure(ResourceInfo resourceInfo, FeatureContext context) {
RuntimeDelegate delegate=new RuntimeDelegateImpl();
        delegate.createEndpoint()
    }
}
