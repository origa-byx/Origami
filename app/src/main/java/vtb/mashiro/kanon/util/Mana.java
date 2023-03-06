package vtb.mashiro.kanon.util;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static vtb.mashiro.kanon.util.Log.Lazy.log;

/**
 * @by: origami
 * @date: {2022/5/16}
 * @info:
 **/
public class Mana {

    public static final String TAG = "Mana";

    public static List<Class<?>> scanForPackage(String name){
        List<Class<?>> classes = new ArrayList<>();
        String pathName = name.replaceAll("\\.", "/");
        log.pr(TAG, "pathName-> " + pathName);
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> resources = classLoader.getResources(pathName);
            if(resources != null){
                while (resources.hasMoreElements()){
                    URL url = resources.nextElement();
                    if(url != null){
                        String protocol = url.getProtocol();
                        log.pr(TAG, "scanForPackage name is " + protocol);
                        if(protocol.equals("file")){
                            classes.addAll(scanForFile(pathName, classLoader, new File(url.getPath())));
                        }else if(protocol.equals("jar")){
                            JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                            if(jarURLConnection != null){
                                classes.addAll(scanForJar(pathName, classLoader, jarURLConnection.getJarFile()));
                            }
                        }
                    }
                }
            }else
                log.pr(TAG, "not found resources for " + name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.pr(TAG, "***********************************");
        return classes;
    }

    private static List<Class<?>> scanForFile(String name, ClassLoader classLoader, File file){
        List<Class<?>> classes = new ArrayList<>();
        if(file != null){
            if(file.isDirectory()){
                File[] files = file.listFiles();
                if(!name.endsWith(file.getName()))
                    name += "/" + file.getName();
                if(files != null) for (File fl : files) {
                    classes.addAll(scanForFile(name, classLoader, fl));
                }
            }else {
                String fileName = file.getName();
                if(fileName.endsWith(".class") && !fileName.contains("$")){
                    String className = (name + "/" + fileName)
                            .split("\\.class")[0]
                            .replaceAll("/", ".");
                    try {
                        classes.add(classLoader.loadClass(className));
                    } catch (ClassNotFoundException e) {
                        log.pr(TAG, className + " is not found");
                    }
                }
            }
        }
        return classes;
    }

    private static List<Class<?>> scanForJar(String name, ClassLoader classLoader, JarFile file){
        List<Class<?>> classes = new ArrayList<>();
        if(file != null){
            Enumeration<JarEntry> entries = file.entries();
            if(entries != null) while (entries.hasMoreElements()){
                JarEntry jarEntry = entries.nextElement();
                if(jarEntry != null){
                    String entryName = jarEntry.getName();
                    log.pr("ALL", entryName);
                    if(entryName.startsWith("/"))
                        entryName = entryName.substring(1);
                    if(entryName.endsWith(".class") && entryName.startsWith(name)){
                        if(entryName.contains("$")) continue;
                        String className = entryName
                                .split("\\.class")[0]
                                .replaceAll("/", ".");
                        try {
                            classes.add(classLoader.loadClass(className));
                        } catch (ClassNotFoundException e) {
                            log.pr(TAG, className + " is not found");
                        }
                    }
                }
            }
        }
        return classes;
    }

}
