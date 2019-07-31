package nm.jvision.conexao.websocket;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import nm.jvision.controle.AppController;
import nm.jvision.utils.ConfigFile;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author David
 * @date 16/05/2016
 * @redmine #21021 - Impressora de cheques bematech DP-20
 */
public class WebSocketConnection extends Service<Session> {

    private static Session session;

    private static WebSocketConnection service;

    private static String connectToServerURI = "ws://localhost:8080/jVisionWebSocket/message/BematechDP20_99999" + "?session_cookie=jvision.login";

    private WebSocketConnection() {
    }

    public static Session getSession() {
        return session;
    }

    public static WebSocketConnection getService() {
        return WebSocketConnection.service;
    }

    public static void createService() {

        WebSocketConnection.setConnectToServerURI();

        WebSocketConnection.service = new WebSocketConnection();
    }

    private static void setConnectToServerURI() {

        if (!ConfigFile.CONFIG_PARAMETERS.isEmpty()
                && ConfigFile.CONFIG_PARAMETERS.get(ConfigFile.ConfigFields.IP) != null && !ConfigFile.CONFIG_PARAMETERS.get(ConfigFile.ConfigFields.IP).isEmpty()
                && ConfigFile.CONFIG_PARAMETERS.get(ConfigFile.ConfigFields.PORTA) != null && !ConfigFile.CONFIG_PARAMETERS.get(ConfigFile.ConfigFields.PORTA).isEmpty()
                && ConfigFile.CONFIG_PARAMETERS.get(ConfigFile.ConfigFields.NOME_IMPRESSORA) != null && !ConfigFile.CONFIG_PARAMETERS.get(ConfigFile.ConfigFields.NOME_IMPRESSORA).isEmpty()) {

            WebSocketConnection.connectToServerURI = "ws://" + ConfigFile.CONFIG_PARAMETERS.get(ConfigFile.ConfigFields.IP)
                    + ":" + ConfigFile.CONFIG_PARAMETERS.get(ConfigFile.ConfigFields.PORTA)
                    + "/jVisionWebSocket/message/"
                    + ConfigFile.CONFIG_PARAMETERS.get(ConfigFile.ConfigFields.NOME_IMPRESSORA) + "?session_cookie=jvision.login";
        }
    }

    @Override
    protected Task<Session> createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {

                final WebSocketContainer container = ContainerProvider.getWebSocketContainer();

                try {

                    WebSocketConnection.session = container.connectToServer(WSClient.class, URI.create(WebSocketConnection.connectToServerURI));
                } catch (DeploymentException | IOException ex) {

                    Logger.getLogger(WebSocketConnection.class.getName()).log(Level.SEVERE, null, ex);

                    AppController.onSessionStartFailed();
                }

                return null;
            }
        };
    }
}