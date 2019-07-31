package nm.jvision.conexao.websocket;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import nm.jvision.controle.AppController;
import nm.jvision.utils.DialogMessage;

import javax.websocket.*;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author David
 */
@ClientEndpoint(configurator = AuthorizationConfigurator.class,
        encoders = WSMessageEncoder.class, decoders = WSMessageDecoder.class)
public class WSClient {

    private static final Logger LOGGER = Logger.getLogger(WSClient.class.getName());

    @OnMessage
    public void onMessage(final WSMessage wsMessage) throws IOException {

        switch (wsMessage.getType()) {
            case LOGIN:
                break;
            case LOGOUT:
                break;
            case MODULE_DEPLOY:
                break;
            case MODULE_UNDEPLOY:
                break;
            case MESSAGE:
                //ponto de retorno da mensagem
                AppController.onMessage(wsMessage.getData().getMessage());
                break;
            default:
                throw new IOException("Tipo de mensagem não suportado: \"" + wsMessage.getType() + "\"");
        }
    }

    @OnOpen
    public final void onOpen() throws IOException {

        WSClient.LOGGER.info("Connection is open!");

        AppController.onOpen();
    }

    @OnClose
    public final void onClose() throws IOException {

        WSClient.LOGGER.info("Connection is close!");

        AppController.onClose();
    }

    @OnError
    public final void onError(final Throwable ex) throws IOException {

        WSClient.LOGGER.severe(ex.getMessage());

        Platform.runLater(() -> {

            AppController.closeConnection();

            DialogMessage.messageDialog(Alert.AlertType.ERROR, "Erro de Conexão", ex.getMessage());
        });
    }
}
