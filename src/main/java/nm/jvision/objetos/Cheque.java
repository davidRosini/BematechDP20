package nm.jvision.objetos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

/**
 * @author David
 * @date 16/05/2016
 * @redmine #21021 - Impressora de cheques bematech DP-20
 */
@XmlRootElement(name = "cheque")
@XmlAccessorType(XmlAccessType.FIELD)
public class Cheque {

    private String banco;

    private String agencia;

    private String conta;

    private String valor;

    private String numero;

    private String favorecido;

    private String cidade;

    private String data;

    public Cheque() {
    }

    public Cheque(final String banco, final String agencia, final String conta, final String valor, final String numero, final String favorecido, final String Cidade, final String data) {
        this.banco = banco;
        this.agencia = agencia;
        this.conta = conta;
        this.valor = valor;
        this.numero = numero;
        this.favorecido = favorecido;
        this.cidade = Cidade;
        this.data = data;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(final String banco) {
        this.banco = banco;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(final String agencia) {
        this.agencia = agencia;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(final String conta) {
        this.conta = conta;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(final String valor) {
        this.valor = valor;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(final String numero) {
        this.numero = numero;
    }

    public String getFavorecido() {
        return favorecido;
    }

    public void setFavorecido(final String favorecido) {
        this.favorecido = favorecido;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(final String cidade) {
        this.cidade = cidade;
    }

    public String getData() {
        return data;
    }

    public void setData(final String data) {
        this.data = data;
    }

    public String getFormatMonetary() {

        return String.format("%,.2f", new BigDecimal(this.getValor().replaceAll("[.,]", "")).setScale(2).divide(new BigDecimal(100)).doubleValue());
    }

    public String getFormattedDate() {
        return this.getData().substring(0, 2) + "/" + this.getData().substring(2, 4) + "/" + this.getData().substring(4, 6);
    }

    @Override
    public String toString() {
        return "Número: " + this.getNumero() + "\n"
                + "Banco: " + this.getBanco() + "\n"
                + "Agência: " + this.getAgencia() + "\n"
                + "Conta: " + this.getConta() + "\n"
                + "Valor: " + this.getFormatMonetary() + "\n"
                + "Favorecido: " + this.getFavorecido() + "\n"
                + "Cidade: " + this.getCidade() + "\n"
                + "Data: " + this.getFormattedDate();
    }
}