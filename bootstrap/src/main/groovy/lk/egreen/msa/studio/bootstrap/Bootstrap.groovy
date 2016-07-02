package lk.egreen.msa.studio.bootstrap

import org.osgi.framework.BundleActivator
import org.osgi.framework.BundleContext
import org.osgi.framework.ServiceReference
import org.osgi.service.http.HttpService
import org.osgi.util.tracker.ServiceTracker

import javax.servlet.ServletException
import javax.servlet.http.HttpServlet
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
        httpTracker = new ServiceTracker(bundleContext, HttpService.class.getName(), null) {
            public void removedService(ServiceReference reference, Object service) {
                println("HTTP service is available, unregister our servlet...")
                try {
                    ((HttpService) service).unregister("/version");
                } catch (IllegalArgumentException exception) {
                    // Ignore; servlet registration probably failed earlier on...
                }
            }

            public Object addingService(ServiceReference reference) {
                print("HTTP service is available, register our servlet...")
                HttpService httpService = (HttpService) this.context.getService(reference);
                try {
                    httpService.registerServlet("/version", new HttpServlet() {
                        @Override
                        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                            resp.getWriter().write("Hello app post");
                        }

                        @Override
                        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                            apiTracker.open()
                            resp.getWriter().write("Hello app");

                        }
                    }, null, null);
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
