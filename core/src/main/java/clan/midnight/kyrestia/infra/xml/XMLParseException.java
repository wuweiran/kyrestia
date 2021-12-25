package clan.midnight.kyrestia.infra.xml;

public class XMLParseException extends Exception {
    public XMLParseException(String msg) {
        super(msg);
    }

    public XMLParseException(String msg, Throwable t) {
        super(msg, t);
    }
}
