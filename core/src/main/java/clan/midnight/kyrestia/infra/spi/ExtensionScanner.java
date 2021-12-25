package clan.midnight.kyrestia.infra.spi;

import clan.midnight.kyrestia.infra.reflect.ClassUtils;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ExtensionScanner {
    private static final String CLASS_FILE_NAME_SUFFIX = ".class";

    private final Map<String, Map<Class<?>, Integer>> scanResult;
    private final Map<String, Map<Class<?>, Object>> instances;

    private final String[] packageNameList;

    public ExtensionScanner(String... packageNameList) {
        this.packageNameList = packageNameList;
        this.scanResult = new HashMap<>(16);
        this.instances = new HashMap<>(16);
    }

    public void scan() {
        Set<Class<?>> classSet;
        try {
            classSet = scanClasses(this.packageNameList);
        } catch (IOException e) {
            throw new IllegalStateException("[SPI] Failed to scan classes", e);
        }

        for (Class<?> currentClazz : classSet) {
            boolean present = currentClazz.isAnnotationPresent(Extension.class);
            if (present) {
                Extension currentExtension = currentClazz.getAnnotation(Extension.class);
                String group = currentExtension.group();
                Map<Class<?>, Integer> groupBindings = scanResult.computeIfAbsent(group, k -> new HashMap<>(16));
                groupBindings.put(currentClazz, currentExtension.priority());
            }
        }
    }

    protected static Set<Class<?>> scanClasses(String[] packageNames) throws IOException {
        Set<Class<?>> classSet = new LinkedHashSet<>();
        for (String packageName : packageNames) {
            String formattedPackageDirName = packageName.replace('.', '/');
            Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(formattedPackageDirName);

            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();

                if ("file".equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8.toString());
                    scanFiles(packageName, filePath, true, classSet);
                } else if ("jar".equals(protocol)) {
                    JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
                    scanJars(formattedPackageDirName, jar, classSet);
                }
            }
        }
        return classSet;
    }

    private static void scanJars(String formattedPackageDirName, JarFile jar, Set<Class<?>> classSet) {
        Enumeration<JarEntry> entries = jar.entries();

        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String entryName = entry.getName();
            int idx = entryName.lastIndexOf('/');
            if (idx == -1 || !entryName.endsWith(CLASS_FILE_NAME_SUFFIX) || entry.isDirectory()
                    || !entryName.startsWith(formattedPackageDirName)) {
                continue;
            }

            String realPackageName = entryName.substring(0, idx).replace('/', '.');
            String className = entryName.substring(realPackageName.length() + 1,
                    entryName.length() - CLASS_FILE_NAME_SUFFIX.length());
            classSet.add(ClassUtils.loadClass(realPackageName + "." + className));
        }
    }

    private static void scanFiles(String packageName, String packagePath, boolean recursive, Set<Class<?>> classSet) {
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }

        File[] filteredFiles = dir.listFiles(file ->
                (recursive && file.isDirectory()) || (file.getName().endsWith(CLASS_FILE_NAME_SUFFIX)));
        if (filteredFiles == null) {
            return;
        }

        for (File file : filteredFiles) {
            if (file.isDirectory()) {
                scanFiles(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classSet);
            } else {
                String className = file.getName().substring(0, file.getName().length() - CLASS_FILE_NAME_SUFFIX.length());
                classSet.add(ClassUtils.loadClass(packageName + "." + className));
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getExtension(String group, Class<T> expectedClazz) {
        Map<Class<?>, Integer> groupBindings = scanResult.get(group);
        if (groupBindings == null) {
            return null;
        }

        int priority = Integer.MIN_VALUE;
        Class<?> actualClazz = null;
        for (Map.Entry<Class<?>, Integer> entry : groupBindings.entrySet()) {
            if (expectedClazz.isAssignableFrom(entry.getKey()) && entry.getValue() > priority) {
                priority = entry.getValue();
                actualClazz = entry.getKey();
            }
        }

        if (actualClazz == null) {
            return null;
        }

        Map<Class<?>, Object> groupExtensions = instances.computeIfAbsent(group, k -> new HashMap<>(16));
        final Class<?> finalActualClazz = actualClazz;
        Object instance = groupExtensions.computeIfAbsent(finalActualClazz,
                k -> ClassUtils.createNewInstance(finalActualClazz));
        return (T) instance;
    }

    public void clean() {
        scanResult.clear();
        instances.clear();
    }
}
