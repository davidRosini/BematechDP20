package nm.jvision.objetos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author David
 * @date 16/05/2016
 * @redmine #21021 - Impressora de cheques bematech DP-20
 */
@XmlRootElement(name = "cheques")
@XmlAccessorType(XmlAccessType.FIELD)
public class Cheques {

    @XmlElement(name = "cheque")
    private List<Cheque> chequeList = null;

    public Cheques() {
    }

    public List<Cheque> getChequeList() {
        return this.chequeList;
    }

    public void setChequeList(final List<Cheque> chequeList) {
        this.chequeList = chequeList;
    }
}