package nm.jvision.impressora;

import com.sun.jna.win32.StdCallLibrary;

/**
 * @author David
 * @date 13/05/2016
 * @redmine #21021 - Impressora de cheques bematech DP-20
 */
public interface Bematech extends StdCallLibrary {

    int AlteraMoeda(String moeda); // Não utilizado neste aplicativo;

    int AlteraTaxa(String taxa); // Não utilizado neste aplicativo;

    int IniciaPorta(String porta);

    int FechaPorta(String porta);

    int BematechTX(String texto, int avancaLinha); // Não funciona ou falta paramêtros;

    int CancelaRel(String opcao); // Não utilizado neste aplicativo;

    int ComandoTX(String texto, int avancaLinha); // Não utilizado neste aplicativo;

    int FormataTX(String texto, String letra, int italico, int expandido, int negrito); // não utilizado neste aplicativo; 

    int MontaValor(int valor); // não utilizado neste aplicativo;

    int ImprimeCheque(String banco, String valor, String favorecido, String cidade, String data);

    int ImprimeBarra(); // Não funciona ou falta parametros;

    int IncluiBanco(String banco, String coordenadas);

    int ExcluiBanco(String banco);

    int IncluiFavorecido(String cdFavorecido, String nomeFavorecido);

    int ExcluiFavorecido(String cdFavorecido);

    int EnviaComando(String comando, int tamanho); // Não funciona ou falta parametros e não há instruções sobre os comandos utilizados;

    int Configura(int linhaChancela, int preenchimento, int velocidade, int numeroBits, int paridade, int centavos); // Não utilizado neste aplicativo;

    int TravaDoc(int trava); // Não utilizado neste aplicativo;

    int ZeraConfig();
}