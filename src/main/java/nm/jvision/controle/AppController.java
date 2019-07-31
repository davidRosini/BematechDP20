package nm.jvision.controle;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import nm.jvision.conexao.websocket.WebSocketConnection;
import nm.jvision.objetos.Cheques;
import nm.jvision.utils.ConfigFile;
import nm.jvision.utils.DialogMessage;
import nm.jvision.utils.XMLChequesConverter;

import javax.swing.*;
import javax.xml.bind.JAXBException;
import java.awt.*;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javafx.application.Application.STYLESHEET_MODENA;
import static javafx.application.Application.setUserAgentStylesheet;

/**
 * @author David
 * @date 23/05/2016
 * @redmine #21021 - Impressora de cheques bematech DP-20
 */
public abstract class AppController {

    static final List<Cheques> FILA_CHEQUES = Collections.synchronizedList(new LinkedList<>());
    private static final String MAIN_WINDOW_NAME = "NMBematech DP-20";
    private static final String CONFIG_WINDOW_NAME = "Configurações";
    private static final String MAIN_WINDOW_ICON = "images/icon.png";
    private static final String OS = System.getProperty("os.name").toLowerCase();
    public static boolean MODO_TESTE;

    private static Object mainObject;

    private static TrayIcon trayIcon;

    private static BematechDP20Controller bematechDP20Controller;

    private AppController() {
    }

    private static BematechDP20Controller getBematechDP20Controller() {
        return AppController.bematechDP20Controller;
    }

    public static boolean isWindows() {
        return AppController.OS.contains("win");
    }

    public static boolean isMac() {
        return AppController.OS.contains("mac");
    }

    public static boolean isUnix() {
        return (AppController.OS.contains("nix") || AppController.OS.contains("nux") || AppController.OS.contains("aix"));
    }

    public static boolean isSolaris() {
        return AppController.OS.contains("sunos");
    }

    public static void loadMainWindow(final Object clazz, final Stage primaryStage) {

        Platform.setImplicitExit(false);

        AppController.mainObject = clazz;

        setUserAgentStylesheet(STYLESHEET_MODENA);

        final FXMLLoader fXMLLoader = new FXMLLoader(AppController.mainObject.getClass().getResource("bematech-dp20.fxml"));

        try {
            primaryStage.setScene(new Scene(fXMLLoader.load()));

            primaryStage.setTitle(AppController.MAIN_WINDOW_NAME);
            primaryStage.getIcons().add(new Image(AppController.mainObject.getClass().getResource(AppController.MAIN_WINDOW_ICON).toExternalForm(), true));
            primaryStage.setMinWidth(600);
            primaryStage.setMinHeight(400);
            primaryStage.setResizable(false);
            primaryStage.show();
            primaryStage.requestFocus();

            Platform.runLater(() -> {

                AppController.bematechDP20Controller = fXMLLoader.getController();

                if (!AppController.getImpressoraController().carregarImpressora()) {

                    Platform.exit();
                } else {

                    AppController.createConnection();

                    AppController.createSystemTray(primaryStage);
                }
            });
        } catch (IOException ex) {

            DialogMessage.messageDialog(AlertType.ERROR, "Erro ao Carregar a Janela da Aplicação!", ex.getMessage());

            Logger.getLogger(AppController.class.getName()).log(Level.SEVERE, null, ex);

            Platform.exit();
        }
    }

    static void loadConfigFileWindow() {

        final Stage configFileStage = new Stage();

        final FXMLLoader loader = new FXMLLoader(AppController.mainObject.getClass().getResource("bematech-config.fxml"));

        final Parent config;

        try {
            config = loader.load();

            configFileStage.setTitle(AppController.CONFIG_WINDOW_NAME);
            configFileStage.initModality(Modality.APPLICATION_MODAL);
            configFileStage.setScene(new Scene(config));
            configFileStage.setResizable(false);
            configFileStage.setOnCloseRequest(eventClose -> AppController.restartConfiguration());
            configFileStage.show();
        } catch (IOException ex) {

            DialogMessage.messageDialog(AlertType.ERROR, "Erro ao Carregar a Janela de Configurações!", ex.getMessage());

            Logger.getLogger(AppController.class.getName()).log(Level.SEVERE, null, ex);

            Platform.exit();
        }
    }

    static ImpressoraController getImpressoraController() {

        return ImpressoraController.getInstance();
    }

    static void createConnection() {

        AppController.closeConnection();

        WebSocketConnection.createService();

        if ("true".equals(ConfigFile.CONFIG_PARAMETERS.get(ConfigFile.ConfigFields.AUTO_CONNECT))) {
            AppController.openConnection();
        }
    }

    static void openConnection() {

        if (WebSocketConnection.getService() != null && !WebSocketConnection.getService().isRunning()) {
            WebSocketConnection.getService().start();
        }
    }

    public static void closeConnection() {

        if (WebSocketConnection.getSession() != null && WebSocketConnection.getSession().isOpen()) {

            try {
                WebSocketConnection.getSession().close();
            } catch (IOException ex) {

                AppController.getBematechDP20Controller().onConnectionClose();

                DialogMessage.messageDialog(AlertType.ERROR, "Erro ao finalizar Conexão", ex.getMessage());

                Logger.getLogger(AppController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (WebSocketConnection.getService() != null && WebSocketConnection.getService().isRunning()) {
            WebSocketConnection.getService().cancel();
        }
    }

    private static void restartConfiguration() {

        AppController.getImpressoraController().fecharPorta();
        AppController.getImpressoraController().setConfigFilePortaCom();
        AppController.getImpressoraController().iniciarPorta();
        AppController.createConnection();
    }

    public static void stop() throws IOException {

        System.out.println("Finalizando programa!");

        AppController.getImpressoraController().fecharPorta();
        AppController.closeConnection();
        SystemTray.getSystemTray().remove(trayIcon);

        System.exit(0);
    }

    public static void onOpen() {

        AppController.getBematechDP20Controller().onConnectionOpen();
    }

    public static void onClose() {

        AppController.getBematechDP20Controller().onConnectionClose();
    }

    static void onPortOpen() {

        AppController.getBematechDP20Controller().onPortOpen();
    }

    static void onPortClose() {

        AppController.getBematechDP20Controller().onPortClose();
    }

    public static void onMessage(final String message) {

        Platform.runLater(() -> {

            try {
                AppController.FILA_CHEQUES.add(XMLChequesConverter.xmlToCheques(message));
            } catch (JAXBException jaxbEx) {

                DialogMessage.messageDialog(AlertType.ERROR, "Erro de Conversão de Dados", jaxbEx.getMessage());

                Logger.getLogger(AppController.class.getName()).log(Level.SEVERE, null, jaxbEx);
            }

            if (!AppController.getImpressoraController().isPrinting()) {
                AppController.getImpressoraController().imprimirCheque();
            }
        });
    }

    public static void onSessionStartFailed() {

        Platform.runLater(() -> DialogMessage.messageDialog(AlertType.ERROR,
                "Erro ao Conectar no Servidor!",
                "Verifique se o IP e a Porta estão\nConfigurados Corretamete em Configurações."));
    }

    private static void createSystemTray(final Stage stage) {

        if (SystemTray.isSupported()) {

            AppController.trayIcon = new TrayIcon(new ImageIcon(AppController.mainObject.getClass().getResource(AppController.MAIN_WINDOW_ICON), "NMBematechDP20").getImage(),
                    AppController.MAIN_WINDOW_NAME,
                    new PopupMenu());

            final MenuItem abrirItem = new MenuItem("Abrir Janela");
            final MenuItem sairItem = new MenuItem("Sair");

            abrirItem.addActionListener(event -> Platform.runLater(() -> {
                stage.show();
                stage.toFront();
                stage.requestFocus();
            }));

            sairItem.addActionListener(event -> Platform.exit());

            AppController.trayIcon.getPopupMenu().add(abrirItem);
            AppController.trayIcon.getPopupMenu().add(sairItem);

            try {
                SystemTray.getSystemTray().add(AppController.trayIcon);
                stage.hide();
                stage.iconifiedProperty().addListener((observable, oldValue, newValue) -> {

                    if (newValue) {
                        stage.hide();
                    }
                });
            } catch (AWTException awrEx) {

                Logger.getLogger(AppController.class.getName()).log(Level.SEVERE, null, awrEx);
            }
        }
    }
}