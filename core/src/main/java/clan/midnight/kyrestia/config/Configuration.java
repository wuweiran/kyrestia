package clan.midnight.kyrestia.config;

import clan.midnight.kyrestia.infra.spi.ExtensionScanner;

import java.util.concurrent.ExecutorService;

@SuppressWarnings("all")
public class Configuration {
    static final String CONFIG_EXTENSION_GROUP = "__config";

    private Configuration() {}

    public static ImplementationAccessor implementationAccessor;
    public static ExpressionEvaluator expressionEvaluator;
    public static boolean multiThreadExecution;
    public static ExecutorService executorService;
    public static IdGenerator idGenerator;

    static {
        ExtensionScanner scanner = new ExtensionScanner(Configuration.class.getPackage().getName());
        scanner.scan();
        implementationAccessor = scanner.getExtension(CONFIG_EXTENSION_GROUP, ImplementationAccessor.class);
        expressionEvaluator = scanner.getExtension(CONFIG_EXTENSION_GROUP, ExpressionEvaluator.class);
        multiThreadExecution = false;
        executorService = scanner.getExtension(CONFIG_EXTENSION_GROUP, ExecutorService.class);
        idGenerator = scanner.getExtension(CONFIG_EXTENSION_GROUP, IdGenerator.class);
    }
}
