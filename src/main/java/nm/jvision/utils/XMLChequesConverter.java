package nm.jvision.utils;

import nm.jvision.objetos.Cheques;
import org.xml.sax.InputSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * @author David
 * @date 16/05/2016
 * @redmine #21021 - Impressora de cheques bematech DP-20
 */
public abstract class XMLChequesConverter {

    private static Marshaller marshaller;
    private static Unmarshaller unmarshaller;

    private XMLChequesConverter() {
    }

    private static synchronized Unmarshaller getUnmarshaller() throws JAXBException {

        if (XMLChequesConverter.unmarshaller == null) {
            XMLChequesConverter.unmarshaller = JAXBContext.newInstance(Cheques.class).createUnmarshaller();
        }

        return XMLChequesConverter.unmarshaller;
    }

    private static synchronized Marshaller getMarshaller() throws JAXBException {

        if (XMLChequesConverter.marshaller == null) {
            XMLChequesConverter.marshaller = JAXBContext.newInstance(Cheques.class).createMarshaller();
            XMLChequesConverter.marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            XMLChequesConverter.marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        }

        return XMLChequesConverter.marshaller;
    }

    private static InputSource stringInputSource(final String stringFile) {

        return new InputSource(new StringReader(stringFile));
    }

    public static String chequesToXml(final Cheques cheques) throws JAXBException {

        StringWriter stringWriter = new StringWriter();

        XMLChequesConverter.getMarshaller().marshal(cheques, stringWriter);

        return stringWriter.toString();
    }

    public static Cheques xmlToCheques(final File fXML) throws JAXBException {

        return (Cheques) XMLChequesConverter.getUnmarshaller().unmarshal(fXML);
    }

    public static Cheques xmlToCheques(final InputSource iSXML) throws JAXBException {

        return (Cheques) XMLChequesConverter.getUnmarshaller().unmarshal(iSXML);
    }

    public static Cheques xmlToCheques(final String stringFile) throws JAXBException {

        return (Cheques) XMLChequesConverter.getUnmarshaller().unmarshal(XMLChequesConverter.stringInputSource(stringFile));
    }
}