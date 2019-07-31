package nm.jvision.controle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Menu;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import nm.jvision.objetos.Cheque;
import nm.jvision.utils.DialogMessage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author David
 * @date 16/05/2016
 * @redmine #21021 - Impressora de cheques bematech DP-20
 */
public class BematechDP20Controller implements Initializable {

    @FXML
    private Circle circleStatusCon;

    @FXML
    private Circle circleStatusImp;

    @FXML
    private Menu mnModoTeste;

    public BematechDP20Controller() {
    }

    @Override
    public final void initialize(URL location, ResourceBundle resources) {

        mnModoTeste.setVisible(AppController.MODO_TESTE);
    }

    @FXML
    public final void onConfig(ActionEvent event) {

        AppController.loadConfigFileWindow();
    }

    @FXML
    public void onClose(ActionEvent event) {

        Platform.exit();
    }

    @FXML
    public final void onConnect(ActionEvent event) {

        AppController.createConnection();
        AppController.openConnection();
    }

    @FXML
    public final void onDesconnect(ActionEvent event) {

        AppController.closeConnection();
    }

    @FXML
    public final void onAlterarMoeda(ActionEvent event) {

        final Map<String, String> mapParameters;
        final List<String> parameters = new ArrayList<>();

        parameters.add("Moeda (Ex: Real):");

        mapParameters = DialogMessage.createParametersDialog(parameters);

        if (mapParameters != null) {
            AppController.getImpressoraController().alterarMoeda(mapParameters.get(parameters.get(0)));
        }
    }

    @FXML
    public final void onPrintOneCheque(ActionEvent event) {

        final Map<String, String> mapParameters;
        final List<String> parameters = new ArrayList<>();

        parameters.add("Banco:");
        parameters.add("Valor (0.000,00):");
        parameters.add("Favorecido:");
        parameters.add("Cidade:");
        parameters.add("Data (ddMMaa):");

        mapParameters = DialogMessage.createParametersDialog(parameters);

        if (mapParameters != null && mapParameters.size() == 5) {

            AppController.getImpressoraController().testarImpressao(new Cheque(mapParameters.get(parameters.get(0)),
                    "",
                    "",
                    mapParameters.get(parameters.get(1)),
                    "",
                    mapParameters.get(parameters.get(2)),
                    mapParameters.get(parameters.get(3)),
                    mapParameters.get(parameters.get(4))));
        }
    }

    @FXML
    public final void onIncluirBanco(ActionEvent event) {

        final Map<String, String> mapParameters;
        final List<String> parameters = new ArrayList<>();

        parameters.add("Banco:");
        parameters.add("Coordenadas:");

        mapParameters = DialogMessage.createParametersDialog(parameters);

        if (mapParameters != null) {
            AppController.getImpressoraController().incluirBanco(mapParameters.get(parameters.get(0)),
                    mapParameters.get(parameters.get(1)));
        }
    }

    @FXML
    public final void onExcluirBanco(ActionEvent event) {

        final Map<String, String> mapParameters;
        final List<String> parameters = new ArrayList<>();

        parameters.add("Banco:");

        mapParameters = DialogMessage.createParametersDialog(parameters);

        if (mapParameters != null) {
            AppController.getImpressoraController().excluirBanco(mapParameters.get(parameters.get(0)));
        }
    }

    @FXML
    public final void onIncluirFavorecido(ActionEvent event) {

        final Map<String, String> mapParameters;
        final List<String> parameters = new ArrayList<>();

        parameters.add("Código do Favorecido:");
        parameters.add("Nome do Favorecido:");

        mapParameters = DialogMessage.createParametersDialog(parameters);

        if (mapParameters != null) {
            AppController.getImpressoraController().incluirFavorecido(mapParameters.get(parameters.get(0)),
                    mapParameters.get(parameters.get(1)));
        }
    }

    @FXML
    public final void onExcluirFavorecido(ActionEvent event) {

        final Map<String, String> mapParameters;
        final List<String> parameters = new ArrayList<>();

        parameters.add("Código do Favorecido:");

        mapParameters = DialogMessage.createParametersDialog(parameters);

        if (mapParameters != null) {
            AppController.getImpressoraController().excluirFavorecido(mapParameters.get(parameters.get(0)));
        }
    }

    @FXML
    public final void onTravarDocumento(ActionEvent event) {

        final Map<String, String> mapParameters;
        final List<String> parameters = new ArrayList<>();

        parameters.add("Número da Trava(0 - Destrava; 1 - Trava):");

        mapParameters = DialogMessage.createParametersDialog(parameters);

        if (mapParameters != null) {
            AppController.getImpressoraController().travarDocumento(Integer.parseInt(mapParameters.get(parameters.get(0))));
        }
    }

    @FXML
    public final void onZerarConfiguracao(ActionEvent event) {
        AppController.getImpressoraController().zerarConfiguracao();
    }

    final void onConnectionOpen() {

        circleStatusCon.setFill(Paint.valueOf("green"));
    }

    final void onConnectionClose() {

        circleStatusCon.setFill(Paint.valueOf("red"));
    }

    final void onPortOpen() {

        circleStatusImp.setFill(Paint.valueOf("green"));
    }

    final void onPortClose() {

        circleStatusImp.setFill(Paint.valueOf("red"));
    }
}