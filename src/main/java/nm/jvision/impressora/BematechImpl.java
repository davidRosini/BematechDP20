package nm.jvision.impressora;

import com.sun.jna.Native;
import nm.jvision.objetos.Cheque;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author David
 * @date 16/05/2016
 * @redmine #21021 - Impressora de cheques bematech DP-20
 */
public class BematechImpl implements Bematech {

    private static final String DRIVER = "DP2032";

    private String portaCom = "COM1";

    private Bematech dp20;

    public BematechImpl() {
    }

    private Bematech getDp20() {
        return this.dp20;
    }

    private String getPortaCom() {
        return portaCom;
    }

    public void setPortaCom(String portaCom) {
        this.portaCom = portaCom;
    }

    public final boolean carregarImpressora() {

        System.out.println("Carregando biblioteca...");

        try {

            this.dp20 = (Bematech) Native.loadLibrary(DRIVER, Bematech.class);
        } catch (Exception ex) {

            Logger.getLogger(BematechImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return this.dp20 != null;
    }

    public final void testarImpressao(Cheque cheque) {

        System.out.println("Imprimindo cheque...");

        System.out.println("Status impressão: " + this.ImprimeCheque(cheque.getBanco(), cheque.getValor(), cheque.getFavorecido(), cheque.getCidade(), cheque.getData()));

        System.out.println("Teste Finalizado!");
    }

    @Override
    public final int AlteraMoeda(final String moeda) {
        return this.getDp20().AlteraMoeda(moeda);
    }

    @Override
    public final int AlteraTaxa(final String taxa) {
        return this.getDp20().AlteraTaxa(taxa);
    }

    public final int IniciaPorta() {
        return this.IniciaPorta(this.getPortaCom());
    }

    @Override
    public int IniciaPorta(final String porta) {

        System.out.println("Iniciando porta de comunicação com a impressora...");

        final int iRetorno = this.getDp20().IniciaPorta(porta);

        System.out.println("Status da porta " + this.getPortaCom() + ": " + iRetorno);

        return iRetorno;
    }

    public final int FechaPorta() {
        return this.FechaPorta(this.getPortaCom());
    }

    @Override
    public final int FechaPorta(final String porta) {

        System.out.println("Fechando porta de comunicação com a impressora...");

        final int iRetorno = this.getDp20().FechaPorta(porta);

        System.out.println("Status da porta " + this.getPortaCom() + ": " + iRetorno);

        return iRetorno;
    }

    @Override
    public final int BematechTX(final String texto, final int avancaLinha) {
        return this.getDp20().BematechTX(texto, avancaLinha);
    }

    @Override
    public final int CancelaRel(final String opcao) {
        return this.getDp20().CancelaRel(opcao);
    }

    @Override
    public final int ComandoTX(final String texto, final int avancaLinha) {
        return this.getDp20().ComandoTX(texto, avancaLinha);
    }

    @Override
    public final int FormataTX(final String texto, final String letra, final int italico, final int expandido, final int negrito) {
        return this.getDp20().FormataTX(texto, letra, italico, expandido, negrito);
    }

    @Override
    public final int MontaValor(final int valor) {
        return this.getDp20().MontaValor(valor);
    }

    public final int ImprimeCheque(final Cheque cheque) {
        return this.ImprimeCheque(cheque.getBanco(), cheque.getValor(), cheque.getFavorecido(), cheque.getCidade(), cheque.getData());
    }

    @Override
    public final int ImprimeCheque(final String banco, final String valor, final String favorecido, final String cidade, final String data) {

        return this.getDp20().ImprimeCheque(banco, valor, favorecido != null && !favorecido.isEmpty() ? favorecido : "", cidade != null && !cidade.isEmpty() ? cidade : "", data != null && !data.isEmpty() ? data : "");
    }

    @Override
    public final int ImprimeBarra() {
        return this.getDp20().ImprimeBarra();
    }

    @Override
    public final int IncluiBanco(final String banco, final String coordenadas) {
        return this.getDp20().IncluiBanco(banco, coordenadas);
    }

    @Override
    public final int ExcluiBanco(final String banco) {
        return this.getDp20().ExcluiBanco(banco);
    }

    @Override
    public final int IncluiFavorecido(final String cdFavorecido, final String nomeFavorecido) {
        return this.getDp20().IncluiFavorecido(cdFavorecido, nomeFavorecido);
    }

    @Override
    public final int ExcluiFavorecido(final String cdFavorecido) {
        return this.getDp20().ExcluiFavorecido(cdFavorecido);
    }

    @Override
    public final int EnviaComando(final String comando, final int tamanho) {
        return this.getDp20().EnviaComando(comando, tamanho);
    }

    @Override
    public final int Configura(final int linhaChancela, final int preenchimento, final int velocidade, final int numeroBits, final int paridade, final int centavos) {
        return this.getDp20().Configura(linhaChancela, preenchimento, velocidade, numeroBits, paridade, centavos);
    }

    @Override
    public final int TravaDoc(final int trava) {
        return this.getDp20().TravaDoc(trava);
    }

    @Override
    public final int ZeraConfig() {
        return this.getDp20().ZeraConfig();
    }
}