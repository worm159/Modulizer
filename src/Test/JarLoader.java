package Test;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by augus on 28.12.2016.
 */
public class JarLoader {
    public static void main (String [ ] args) throws MalformedURLException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        File file  = new File("C:\\Users\\augus\\IdeaProjects\\Modulizer\\out\\production\\Modulizer\\Test\\factory\\ProcessModelFactory.class");

        URL url = file.toURL();
        URL[] urls = new URL[]{url};

        ClassLoader cl = new URLClassLoader(urls);
        Class cls = cl.loadClass("Test.factory.ProcessModelFactory");

        Method m = cls.getMethod("createCycleTest", null);
        System.out.println(m.invoke(cls.newInstance(), new String[0] ));
    }
}
