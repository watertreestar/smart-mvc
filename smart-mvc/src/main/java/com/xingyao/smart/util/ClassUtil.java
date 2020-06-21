package com.xingyao.smart.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @Author ranger
 * @Date 2020/6/19 9:50
 * 返回类加载器，加载指定的类，获取包下所有的类
 **/
public class ClassUtil {
    private static Logger logger = LoggerFactory.getLogger(ClassUtil.class);

    private static final String FILE = "file";
    private static final String JAR = "jar";


    public static ClassLoader getClassLoader(){
        return Thread.currentThread().getContextClassLoader();
    }

    public static Class<?> loadClass(String className){
        Class<?> clz = null;
        try{
            clz = Class.forName(className,true,getClassLoader());
            return clz;
        }catch (Exception e){
            throw new RuntimeException("load class error,class not found");
        }

    }

    public static Set<Class<?>> getClass(String basePackage){
        Set<Class<?>> classSet = new HashSet<>();
        String path = basePackage.replace(".","/");
        try{
            Enumeration<URL> urls = getClassLoader().getResources(path);
            while(urls.hasMoreElements()){
                URL url = urls.nextElement();
                if(url != null){
                    String protocol = url.getProtocol();
                    // file or jar
                    if(FILE.equals(protocol)){
                        String packagePath = url.getPath().replace("%20"," ");
                        // add class
                        addClass(classSet,packagePath,basePackage);
                    }else if(JAR.equals(protocol)){
                        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                        if(jarURLConnection != null){
                            JarFile jarFile = jarURLConnection.getJarFile();
                            Enumeration<JarEntry> entries = jarFile.entries();
                            while(entries.hasMoreElements()){
                                JarEntry entry = entries.nextElement();
                                String jarEntryName = entry.getName();
                                if(jarEntryName.endsWith(".class")){
                                    String className = jarEntryName.substring(0,jarEntryName.lastIndexOf("."))
                                            .replaceAll("/",".");
                                    if(className.startsWith(basePackage)){
                                        Class<?> clz = loadClass(className);
                                        classSet.add(clz);
                                    }

                                }
                            }

                        }
                    }
                }
            }
        }catch (Exception e){

        }

        return classSet;
    }


    private static void addClass(Set<Class<?>> classSet,String packagePath,String basePackage){
        File[] files = new File(packagePath).listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.getName().endsWith(".class") && pathname.isFile()) {
                    return true;
                } else if (pathname.isDirectory()) {
                    return true;
                }
                return false;
            }
        });

        // files 中可能包含class文件或者文件夹
        for(File file : files){
            String fileName = file.getName();
            if(file.isFile()){
                String className = fileName.substring(0,fileName.lastIndexOf("."));
                if(StringUtils.isNotBlank(basePackage)){
                    className = basePackage.concat(".").concat(className);
                    Class<?> clz = loadClass(className);
                    classSet.add(clz);
                }
            }
            else if (file.isDirectory()){
                String subPackage = fileName;
                if(StringUtils.isNotBlank(basePackage)){
                    subPackage = basePackage.concat(".").concat(subPackage);
                }

                String subPackagePath = fileName;
                if(StringUtils.isNotBlank(packagePath)){
                    subPackagePath = packagePath.concat("/").concat(subPackagePath);
                }
                addClass(classSet,subPackagePath,subPackage);
            }
        }

    }

    public static void main(String[] args) throws IOException {
        Enumeration<URL> urls = getClassLoader().getResources("org/apache/commons/dbutils/handlers");
        while(urls.hasMoreElements()) {
            URL url = urls.nextElement();
            System.out.println(url.getPath());

            JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
            if(jarURLConnection != null){
                JarFile jarFile = jarURLConnection.getJarFile();
                Enumeration<JarEntry> entries = jarFile.entries();
                while(entries.hasMoreElements()){
                    JarEntry entry = entries.nextElement();
                    String jarEntryName = entry.getName();
                    if(jarEntryName.endsWith(".class")){
                        String className = jarEntryName.substring(0,jarEntryName.lastIndexOf("."))
                                .replaceAll("/",".");
                        System.out.println(className);

                    }
                }

            }
        }
    }
}
