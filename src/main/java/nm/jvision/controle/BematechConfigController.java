package nm.jvision.controle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nm.jvision.utils.ConfigFile;
import nm.jvision.utils.DialogMessage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author David
 * @date 18/05/2016
 * @redmine #21021 - Impressora de cheques bematech DP-20
 */
public class BematechConfigController implements Initializable {

    private static final String PATTERN = "pattern";

    @FXML
    private TextField txtIpGlassfish;

    @FXML
    private TextField txtNomeImpressora;

    @FXML
    private TextField txtPorta;

    @FXML
    private TextField txtPortaCom;

    @FXML
    private CheckBox ckBoxConectar;

    @Override
    public final void initialize(final URL location, final ResourceBundle resources) {

        txtIpGlassfish.setText(ConfigFile.CONFIG_PARAMETERS.get(ConfigFile.ConfigFields.IP));
        txtPorta.setText(ConfigFile.CONFIG_PARAMETERS.get(ConfigFile.ConfigFields.PORTA));
        txtNomeImpressora.setText(ConfigFile.CONFIG_PARAMETERS.get(ConfigFile.ConfigFields.NOME_IMPRESSORA));
        txtPortaCom.setText(ConfigFile.CONFIG_PARAMETERS.get(ConfigFile.ConfigFields.PORTA_COM));
        ckBoxConectar.setSelected("true".equals(ConfigFile.CONFIG_PARAMETERS.get(ConfigFile.ConfigFields.AUTO_CONNECT)));

        txtIpGlassfish.getProperties().put(PATTERN, "\\b(?:(?:2(?:[0-4][0-9]|5[0-5])|[0-1]?[0-9]?[0-9])\\.){3}(?:(?:2([0-4][0-9]|5[0-5])|[0-1]?[0-9]?[0-9]))\\b"); //IP
        txtPorta.getProperties().put(PATTERN, "\\d{1,5}"); //Porta
        txtPortaCom.getProperties().put(PATTERN, "(COM|com)\\d"); //Porta COM
    }

    @FXML
    public final void onClose(final ActionEvent event) {

        ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
    }

    @FXML
    public final void onSaveConfigFile(final ActionEvent event) {

        if (this.validateTextFields(txtIpGlassfish, txtPorta, txtNomeImpressora, txtPortaCom)) {

            ConfigFile.CONFIG_PARAMETERS.put(ConfigFile.ConfigFields.IP, txtIpGlassfish.getText());
            ConfigFile.CONFIG_PARAMETERS.put(ConfigFile.ConfigFields.PORTA, txtPorta.getText());
            ConfigFile.CONFIG_PARAMETERS.put(ConfigFile.ConfigFields.NOME_IMPRESSORA, txtNomeImpressora.getText());
            ConfigFile.CONFIG_PARAMETERS.put(ConfigFile.ConfigFields.PORTA_COM, txtPortaCom.getText().toUpperCase());

            ConfigFile.CONFIG_PARAMETERS.put(ConfigFile.ConfigFields.AUTO_CONNECT, String.valueOf(ckBoxConectar.isSelected()));

            if (ConfigFile.saveConfigFile()) {
                DialogMessage.messageDialog(AlertType.INFORMATION, "Configurações Salvas!", "Arquivo de Configurações Salvo com Sucesso!");
            }

            this.onClose(event);
        }
    }

    private boolean validateTextFields(final TextField... textFields) {

        String color;

        boolean valid = true;

        for (final TextField textField : textFields) {

            textField.setText(textField.getProperties().get(PATTERN) == null ? textField.getText().trim() : this.validatePattern(textField.getText().trim(), (String) textField.getProperties().get(PATTERN)));

            if (textField.getText().trim().isEmpty()) {

                valid = false;

                color = "red";

                textField.clear();
            } else {
                color = "none";
            }

            textField.setStyle("-fx-border-color: " + color + ";");
            textField.applyCss();
            textField.layout();
        }

        return valid;
    }

    private String validatePattern(final String text, final String pattern) {

        Matcher matcher = Pattern.compile(pattern).matcher(text);

        return matcher.find() ? matcher.group(0) : "";
    }
}