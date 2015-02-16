
import java.net.URL;
import java.security.ProtectionDomain;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Main method to start the standalone application with embedded jetty servlet engine
 * The application must be packaged, and then started with the start scipt in anon-build
 */
public class AnonJettyStarter {
    public static void main(String[] args) throws Exception
    {

        Server server = new Server(8080);
 
        ProtectionDomain domain = AnonJettyStarter.class.getProtectionDomain();
        URL location = domain.getCodeSource().getLocation();
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/anon");
        webapp.setWar(location.toExternalForm());
        webapp.setWelcomeFiles(new String []{"/anon/pages/cockpit/connect.jsf"});
        server.setHandler(webapp);
        
        server.start();
        server.join();
    }

}
