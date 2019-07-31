package nm.jvision.utils;

import javafx.scene.control.Alert.AlertType;
import nm.jvision.controle.AppController;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author David
 * @date 18/05/2016
 * @redmine #21021 - Impressora de cheques bematech DP-20
 */
public abstract class ConfigFile {

    public static final Map<ConfigFile.ConfigFields, String> CONFIG_PARAMETERS = new HashMap<>();
    private static final Path FILE_PATH = Paths.get(System.getProperty("user.home") + "/.bematechDP20_config");
    private static final String INDICADOR = "=";
    private static final String SEPARADOR = ";";

    private ConfigFile() {
    }

    public static boolean saveConfigFile() {
        return writeConfigFile();
    }

    public static void loadConfigParameters() {

        final String configFileStr = ConfigFile.readConfigFile();

        final List<String> configFileParameters = new LinkedList<>(configFileStr.isEmpty() ? Collections.singletonList("EMPTY") : Arrays.asList(configFileStr.split(ConfigFile.SEPARADOR)));

        for (final ConfigFile.ConfigFields configFields : ConfigFile.ConfigFields.values()) {

            CONFIG_PARAMETERS.put(configFields, ConfigFile.findParameter(configFileParameters, configFields.toString()));
        }
    }

    private static String findParameter(final List<String> parameters, String match) {

        String matchedParameter = null;

        for (String parameter : parameters) {

            if (parameter.startsWith(parameter)) {

                matchedParameter = parameter;
                break;
            }
        }

        parameters.remove(matchedParameter);

        return matchedParameter == null ? "" : matchedParameter.replaceAll(match, "");
    }

    private static String readConfigFile() {

        String line;
        final StringBuilder stringBuilder = new StringBuilder();

        if (ConfigFile.FILE_PATH.toFile().exists()) {

            if (AppController.isWindows()) {
                try {
                    ConfigFile.hideFile(Boolean.FALSE);
                } catch (IOException ex) {
                    Logger.getLogger(ConfigFile.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            try (final BufferedReader bufferedReader = Files.newBufferedReader(ConfigFile.FILE_PATH, StandardCharsets.UTF_8)) {

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            } catch (IOException ex) {

                DialogMessage.messageDialog(AlertType.WARNING, "Não foi possível carregar o arquivo de configurações!", ex.getMessage());

                Logger.getLogger(ConfigFile.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (AppController.isWindows()) {
                try {
                    ConfigFile.hideFile(Boolean.TRUE);
                } catch (IOException ex) {
                    Logger.getLogger(ConfigFile.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return stringBuilder.toString();
    }

    private static boolean writeConfigFile() {

        boolean noError = true;

        try (final BufferedWriter bufferedWriter = Files.newBufferedWriter(ConfigFile.FILE_PATH, StandardCharsets.UTF_8)) {

            for (final ConfigFile.ConfigFields configFields : ConfigFile.ConfigFields.values()) {

                bufferedWriter.write(configFields.toString());
                bufferedWriter.write(CONFIG_PARAMETERS.get(configFields) + ConfigFile.SEPARADOR);
                bufferedWriter.newLine();
            }

            bufferedWriter.flush();

            if (AppController.isWindows()) {
                ConfigFile.hideFile(Boolean.TRUE);
            }
        } catch (IOException ex) {

            noError = false;

            DialogMessage.messageDialog(AlertType.WARNING, "Não foi possível salvar o arquivo de configurações!", ex.getMessage());

            Logger.getLogger(ConfigFile.class.getName()).log(Level.SEVERE, null, ex);
        }

        return noError;
    }

    private static void hideFile(final Boolean hide) throws IOException {

        Files.setAttribute(ConfigFile.FILE_PATH, "dos:hidden", hide, LinkOption.NOFOLLOW_LINKS);
    }

    public enum ConfigFields {

        IP, PORTA, NOME_IMPRESSORA, PORTA_COM, AUTO_CONNECT, MODO_TESTE;

        @Override
        public String toString() {
            return name() + ConfigFile.INDICADOR;
        }
    }
}