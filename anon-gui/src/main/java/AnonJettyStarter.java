
import java.net.URL;
import java.security.ProtectionDomain;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Main method to start the standalone application with embedded jetty servlet engine
 * The application must be packaged, and then started with 
 * java -jar anon-gui-0.0.1-SNAPSHOT.war
 * and then go to http://localhost:8080/anon-gui/pages/cockpit.jsf
 */
public class AnonJettyStarter {
    public static void main(String[] args) throws Exception
    {

        Server server = new Server(8080);
 
        ProtectionDomain domain = AnonJettyStarter.class.getProtectionDomain();
        URL location = domain.getCodeSource().getLocation();
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/anon-gui");
        webapp.setWar(location.toExternalForm());
        server.setHandler(webapp);
        
        server.start();
        server.join();
    }

}
