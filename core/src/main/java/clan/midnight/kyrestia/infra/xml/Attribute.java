package clan.midnight.kyrestia.infra.xml;

import javax.xml.namespace.QName;
import java.util.Objects;

public class Attribute {
    private final QName key;
    private final String value;

    public Attribute(QName key, String value) {
        this.key = key;
        this.value = value;
    }

    public QName getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attribute attribute = (Attribute) o;
        return Objects.equals(key, attribute.key) && Objects.equals(value, attribute.value);
    }

    @Override
    public int hashCode() {
        return 31 * (key == null ? 0 : key.hashCode()) + (value == null ? 0 : value.hashCode());
    }

    @Override
    public String toString() {
        return (key.getPrefix() != null ? (key.getPrefix() + ":") : "") + key.getLocalPart()
                + "=\"" + value + "\"";
    }
}
