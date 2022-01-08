package clan.midnight.kyrestia.infra.spi;

import clan.midnight.kyrestia.infra.reflect.ClassUtils;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public abstract class AbstractScanner {
    private static final String CLASS_FILE_NAME_SUFFIX = ".class";

    private final String[] packageNameList;

    protected AbstractScanner(String... packageNameList) {
        this.packageNameList = packageNameList;
    }

    public abstract void scan();

    public abstract void clear();

    protected Set<Class<?>> scanClasses() {
        try {
            Set<Class<?>> classSet = new LinkedHashSet<>();
            for (String packageName : packageNameList) {
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
        } catch (IOException e) {
            throw new IllegalStateException("[SPI] Failed to scan classes", e);
        }
    }

    private void scanJars(String formattedPackageDirName, JarFile jar, Set<Class<?>> classSet) {
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

    private void scanFiles(String packageName, String packagePath, boolean recursive, Set<Class<?>> classSet) {
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
}
