package clan.midnight.kyrestia.config;

import clan.midnight.kyrestia.infra.spi.ExtensionScanner;

public class Configuration {
    static final String CONFIG_EXTENSION_GROUP = "__config";

    public static ImplementationAccessor implementationAccessor;

    static {
        ExtensionScanner scanner = new ExtensionScanner(Configuration.class.getPackage().getName());
        scanner.scan();
        implementationAccessor = scanner.getExtension(CONFIG_EXTENSION_GROUP, ImplementationAccessor.class);
    }
}
