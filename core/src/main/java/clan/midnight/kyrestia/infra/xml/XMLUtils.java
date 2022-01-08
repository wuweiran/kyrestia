package clan.midnight.kyrestia.infra.xml;

import javax.xml.namespace.QName;

public class XMLUtils {
    private XMLUtils() {}

    public static String getQNameDisplay(QName qName) {
        if ("".equals(qName.getPrefix())) {
            return qName.getLocalPart();
        }

        return qName.getPrefix() + ":" + qName.getLocalPart();
    }
}
