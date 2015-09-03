import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

/**
 * Created by Татьяна on 03.09.2015.
 */
public class XMLReader {
    private static final String pathToParams = "/config/params.xml";
    private static final String port = "port";
    private static final String message = "messagelimit";
    private static final String connection = "connectionlimit";

    public static Config readParams(){
        Config config = null;
        try{
            File fXmlFile = new File(pathToParams);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            Element root = doc.getDocumentElement();

            String portValue = getTextValue(root, port);
            String messagesLimitValue = getTextValue(root, message);
            String connectionLimitValue = getTextValue(root, connection);

            config = new Config(portValue, messagesLimitValue, connectionLimitValue);

        } catch (Exception e){
            e.printStackTrace();
        }

        return config;
    }

    private static String getTextValue(Element doc, String tag){
        String value = null;
        NodeList nl = doc.getElementsByTagName(tag);
        if (nl.getLength() > 0 && nl.item(0).hasChildNodes()) {
            value = nl.item(0).getFirstChild().getNodeValue();
        }

        return value;
    }
}
