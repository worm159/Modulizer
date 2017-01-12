package Test;

import uflow.data.function.immutable.ProcessFunction;
import uflow.data.model.immutable.ProcessModel;
import uflow.data.model.immutable.ProcessStepModel;
import uflow.data.model.immutable.ProcessUnitModel;

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
        System.out.println(url);
        URL[] urls = new URL[]{url};

        ClassLoader cl = new URLClassLoader(urls);
        Class cls = cl.loadClass("Test.factory.ProcessModelFactory");

        Object o = cls.newInstance();

        ProcessModel test;
        for (Method m : cls.getMethods()) {
            if (m.getName().substring(0,6).equals("create")) {
                //Method m = cls.getMethod("createCycleTest", null);
                test = (ProcessModel) m.invoke(o, new String[0]);

                for (ProcessUnitModel unit : test.getProcessUnitModels().getValues()) {
                    System.out.println("Unit Start Process Step: " + unit.getStartProcessStep());
                    for (ProcessStepModel step : unit.getProcessStepModels().getValues()) {
                        System.out.println("Step: " + step.getName());
                        for (ProcessFunction func : step.getProcessFunctions()) {
                            System.out.println("Function: " + func);
                        }
                    }
                }
                System.out.println("============================================================================================");
            }
        }
    }
}
