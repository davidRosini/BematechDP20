package nm.jvision.utils;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author David
 * @date 23/05/2016
 * @redmine #21021 - Impressora de cheques bematech DP-20
 */
public abstract class DialogMessage {

    private DialogMessage() {
    }

    public static boolean messageDialog(final AlertType alertType, final String headerMessage, final String message) {

        final String title;
        final Alert alert = new Alert(alertType);

        switch (alertType) {
            case CONFIRMATION:
                title = "Confirmação!";
                alert.getButtonTypes().removeAll(ButtonType.OK, ButtonType.CANCEL);
                alert.getButtonTypes().addAll(new ButtonType("Sim", ButtonType.OK.getButtonData()), new ButtonType("Não", ButtonType.CANCEL.getButtonData()));
                break;

            case INFORMATION:
                title = "Aviso!";
                break;

            case WARNING:
                title = "Atenção!";
                break;

            case ERROR:
                title = "Erro!";
                break;

            case NONE:
            default:
                title = "Janela Padrão";
                break;
        }

        return DialogMessage.createAlertDialog(alert, title, headerMessage, message);
    }

    private static boolean createAlertDialog(final Alert alert, final String title, final String headerText, final String contentText) {

        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.setResizable(false);

        Platform.runLater(() -> alert.getDialogPane().requestFocus());

        Optional<ButtonType> result = alert.showAndWait();

        return ButtonType.OK.getButtonData().equals(result.get().getButtonData());
    }

    public static ButtonType createCustomAlertDialog(final AlertType alertType, final String title, final String headerText, final String contentText, final ButtonType... buttonTypes) {

        final Alert alert = new Alert(alertType);

        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.setResizable(false);
        alert.getButtonTypes().removeAll(ButtonType.OK, ButtonType.CANCEL);
        alert.getButtonTypes().addAll(buttonTypes);

        Platform.runLater(() -> {
            alert.getDialogPane().requestFocus();
        });

        return alert.showAndWait().get();
    }

    public static Map<String, String> createParametersDialog(final List<String> parameters) {

        return DialogMessage.createCustomDialog(parameters, true, "Parâmetros", "Favor entrar com os parâmetros abaixo: ", ButtonType.OK, ButtonType.CANCEL);
    }

    private static Map<String, String> createCustomDialog(final List<String> parameters, final boolean fields, final String headerText, final String contentText, final ButtonType... buttonTypes) {

        final Dialog<Map<String, String>> dialog = new Dialog<>();
        final Map<String, TextField> mapTextField = new HashMap<>();

        final GridPane gridPane = new GridPane();

        TextField textField;
        Label label;

        int rowIndex = 0;

        dialog.setTitle(headerText);
        dialog.setHeaderText(contentText);

        dialog.getDialogPane().getButtonTypes().addAll(buttonTypes);

        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.setPadding(new Insets(10, 10, 10, 10));
        for (final String parametro : parameters) {

            textField = new TextField();

            label = new Label(parametro);

            gridPane.add(label, 0, rowIndex);
            gridPane.add(textField, 1, rowIndex);

            mapTextField.put(parametro, textField);
            rowIndex++;
        }

        dialog.getDialogPane().setContent(gridPane);

        Platform.runLater(() -> {
            mapTextField.get(parameters.get(0)).requestFocus();
        });

        dialog.setResultConverter(dialogButton -> {

            if (dialogButton.equals(buttonTypes[0])) {

                final Map<String, String> mapReturn = new HashMap<>();

                parameters.forEach(parametro -> mapReturn.put(parametro, mapTextField.get(parametro).getText()));

                return mapReturn;
            }

            return null;
        });

        final Optional<Map<String, String>> result = dialog.showAndWait();

        return result.isPresent() ? result.get() : null;
    }
}