package clan.midnight.kyrestia.infra.spi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExtensionScannerTest {
    @Test
    void testScanAndClean() {
        ExtensionScanner extensionScanner = new ExtensionScanner(ExtensionScannerTest.class.getPackage().getName());
        extensionScanner.scan();
        SuperSpi spi = extensionScanner.getExtension("test", SuperSpi.class);

        assertNotNull(spi);
        assertTrue(spi instanceof HighPrioritySubSpi);

        extensionScanner.clear();
        spi = extensionScanner.getExtension("test", SuperSpi.class);
        assertNull(spi);
    }

    @Test
    void testScanIdempotency() {
        ExtensionScanner extensionScanner = new ExtensionScanner(ExtensionScannerTest.class.getPackage().getName());
        extensionScanner.scan();
        SuperSpi spi = extensionScanner.getExtension("test", SuperSpi.class);

        assertNotNull(spi);

        extensionScanner.scan();
        spi = extensionScanner.getExtension("test", SuperSpi.class);
        assertNotNull(spi);
    }

    @Test
    void testGetExtensionSingleton() {
        ExtensionScanner extensionScanner = new ExtensionScanner(ExtensionScannerTest.class.getPackage().getName());
        extensionScanner.scan();
        SuperSpi spi0 = extensionScanner.getExtension("test", SuperSpi.class);
        SuperSpi spi1 = extensionScanner.getExtension("test", SuperSpi.class);

        assertSame(spi0, spi1);
    }

    @Test
    void testGetExtensionUnannotated() {
        ExtensionScanner extensionScanner = new ExtensionScanner(ExtensionScannerTest.class.getPackage().getName());
        extensionScanner.scan();
        UnannotatedSpi spi = extensionScanner.getExtension("test", UnannotatedSpi.class);

        assertNull(spi);
    }
}