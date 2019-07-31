package nm.jvision;

import javafx.application.Application;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import nm.jvision.controle.AppController;
import nm.jvision.utils.ConfigFile;
import nm.jvision.utils.DialogMessage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author David
 * @date 11/05/2016
 * @redmine #21021 - Impressora de cheques bematech DP-20
 */
public class Main extends Application {

    public static void main(final String[] args) {

        ConfigFile.loadConfigParameters();

        AppController.MODO_TESTE = "true".equals(ConfigFile.CONFIG_PARAMETERS.get(ConfigFile.ConfigFields.MODO_TESTE));

        launch();
    }

    @Override
    public final void start(final Stage primaryStage) {

        AppController.loadMainWindow(this, primaryStage);
    }

    @Override
    public final void stop() {

        try {
            AppController.stop();
        } catch (IOException ex) {

            DialogMessage.messageDialog(AlertType.ERROR, "Erro ao Finalizar Conexão", ex.getMessage());

            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}