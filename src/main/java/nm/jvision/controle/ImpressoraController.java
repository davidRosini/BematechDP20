package nm.jvision.controle;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import nm.jvision.impressora.BematechImpl;
import nm.jvision.objetos.Cheque;
import nm.jvision.objetos.Cheques;
import nm.jvision.utils.ConfigFile;
import nm.jvision.utils.DialogMessage;

import java.util.Iterator;

/**
 * @author David
 * @date 19/05/2016
 * @redmine #21021 - Impressora de cheques bematech DP-20
 */
public class ImpressoraController {

    private static final ImpressoraController INSTANCE = new ImpressoraController();

    private static final BematechImpl IMPRESSORA = new BematechImpl();

    private Cheques ultimoImpresso;

    private boolean printing;

    private ImpressoraController() {

        this.printing = false;

        this.setConfigFilePortaCom();
    }

    static ImpressoraController getInstance() {
        return ImpressoraController.INSTANCE;
    }

    public Cheques getUltimoImpresso() {
        return ultimoImpresso;
    }

    public void setUltimoImpresso(Cheques ultimoImpresso) {
        this.ultimoImpresso = ultimoImpresso;
    }

    boolean isPrinting() {
        return printing;
    }

    void setPrinting(boolean printing) {
        this.printing = printing;
    }

    final void setConfigFilePortaCom() {

        final String portaComParam = ConfigFile.CONFIG_PARAMETERS.get(ConfigFile.ConfigFields.PORTA_COM);

        if (portaComParam != null && !portaComParam.isEmpty()) {

            ImpressoraController.IMPRESSORA.setPortaCom(portaComParam);
        }
    }

    final boolean carregarImpressora() {

        final boolean carregouImpressora = ImpressoraController.IMPRESSORA.carregarImpressora();

        if (!carregouImpressora) {
            DialogMessage.messageDialog(AlertType.ERROR, "Driver da Impressora DP2032.dll não foi Localizada!", "Favor instalar o programa da impressora\nou copiar o arquivo DP2032.dll para a pasta \"c:\\Windows\\System32\".");
        } else {
            this.iniciarPorta();
        }

        return carregouImpressora;
    }

    final void testarImpressao(final Cheque cheque) {

        ImpressoraController.IMPRESSORA.testarImpressao(cheque);
    }

    final void iniciarPorta() {

        if (ImpressoraController.IMPRESSORA.IniciaPorta() != 1) {

            AppController.onPortClose();
            DialogMessage.messageDialog(AlertType.WARNING, "Impressora não foi Localizada!", "Favor ir em Configurações\ne Entrar com a Porta de Comunicação aonde a Impressora está Conectada.");
        } else {
            AppController.onPortOpen();
        }
    }

    final void fecharPorta() {

        if (ImpressoraController.IMPRESSORA.FechaPorta() != 1) {

            DialogMessage.messageDialog(AlertType.WARNING, "Impressora não foi Localizada!", "Favor ir em Configurações\ne Entrar com a Porta de Comunicação aonde a Impressora está Conectada.");
        } else {

            AppController.onPortClose();
        }
    }

    final void imprimirCheque() {

        this.setPrinting(true);

        Cheque cheque;
        Cheques cheques;
        Iterator<Cheque> iterator;
        boolean continuar = false;
        boolean cancelar = false;
        ButtonType buttonType;

        ButtonType[] buttonTypes = {new ButtonType("Sim", ButtonType.OK.getButtonData()),
                new ButtonType("Reimprimir", ButtonType.FINISH.getButtonData()),
                new ButtonType("Cancelar Todo", ButtonType.CANCEL.getButtonData())};

        do {
            cheques = AppController.FILA_CHEQUES.remove(0);

            if (cheques != null
                    && cheques.getChequeList() != null
                    && !cheques.getChequeList().isEmpty()) {

                iterator = cheques.getChequeList().iterator();

                if (DialogMessage.messageDialog(AlertType.CONFIRMATION, "Cheques Recebidos!\nHá " + cheques.getChequeList().size() + " Cheque(s) à ser(em) Impresso(s).", "Deseja Começar a Impressão?")) {

                    while (iterator.hasNext() && !cancelar) {

                        cheque = iterator.next();

                        do {
                            DialogMessage.messageDialog(AlertType.INFORMATION, "Insira a Folha de Cheque na Impressora!", "Cheque a ser impresso:\n\n" + cheque.toString());

                            ImpressoraController.IMPRESSORA.ImprimeCheque(cheque.getBanco(), cheque.getFormatMonetary(), cheque.getFavorecido(), cheque.getCidade(), cheque.getData());
                            buttonType = DialogMessage.createCustomAlertDialog(AlertType.CONFIRMATION, "Confirmação!", "", "Cheque de Número: " + cheque.getNumero() + ", foi Impresso Corretamente?", buttonTypes);

                        } while (ButtonType.FINISH.getButtonData().equals(buttonType.getButtonData()));

                        cancelar = ButtonType.CANCEL.getButtonData().equals(buttonType.getButtonData());
                    }
                }

                if (!iterator.hasNext() && !cancelar) {
                    DialogMessage.messageDialog(AlertType.INFORMATION, "", "Cheques Impressos com Sucesso!");
                }
            } else {

                DialogMessage.messageDialog(AlertType.WARNING, "Cheque(s) não encontrado(s)!", "Não há Cheque(s) a ser(em) Impresso(s).");
            }

            if (!AppController.FILA_CHEQUES.isEmpty()) {
                continuar = DialogMessage.messageDialog(AlertType.CONFIRMATION, "Há Lotes de cheques a serem impressos!", "Ainda há: " + AppController.FILA_CHEQUES.size() + " Lote(s)\nDeseja Continuar com a Impressão do Próximo Lote?");
            }
        } while (continuar && !AppController.FILA_CHEQUES.isEmpty());

        this.setPrinting(false);

        AppController.FILA_CHEQUES.clear();
    }

    final void alterarMoeda(final String moeda) {
        ImpressoraController.IMPRESSORA.AlteraMoeda(moeda);
    }

    final void incluirBanco(final String banco, final String coordenadas) {
        ImpressoraController.IMPRESSORA.IncluiBanco(banco, coordenadas);
    }

    final void excluirBanco(final String banco) {
        ImpressoraController.IMPRESSORA.ExcluiBanco(banco);
    }

    final void incluirFavorecido(final String cdFavorecido, final String nomeFavorecido) {
        ImpressoraController.IMPRESSORA.IncluiFavorecido(cdFavorecido, nomeFavorecido);
    }

    final void excluirFavorecido(final String cdFavorecido) {
        ImpressoraController.IMPRESSORA.ExcluiFavorecido(cdFavorecido);
    }

    final int travarDocumento(final int trava) {
        return ImpressoraController.IMPRESSORA.TravaDoc(trava);
    }

    final void zerarConfiguracao() {
        ImpressoraController.IMPRESSORA.ZeraConfig();
    }
}
