package htwb.ai.app;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import htwb.ai.ParentClass;
import htwb.ai.RunMe;

public class AnnoVsNoAnnoMethods {

    public static boolean searchForClass(String className) {
        int runme = 0, norunme = 0, total = 0;
        ArrayList<Method> withoutRunMeMethods = new ArrayList<>();
        ArrayList<Method> runMeMethods = new ArrayList<>();
        ArrayList<Method> notInvokeableMethods = new ArrayList<>();


        ArrayList<String> _withoutRunMeMethods = new ArrayList<>();
        ArrayList<String> _runMeMethods = new ArrayList<>();
        ArrayList<String> _notInvokeableMethods = new ArrayList<>();
        //ArrayList<Method> notAccessable = new ArrayList<>();
        try {
            Class<?> clazz = Class.forName(className);
            Object clazzy = clazz.newInstance();

            ParentClass.create(className);

            System.out.println("getDeclaredMethods: ");
            Method[] declMethods = clazz.getDeclaredMethods();
            System.out.println("invoking methods...");

            for (Method m : declMethods) {
                Annotation annotation = m.getAnnotation(RunMe.class);
                RunMe runMeMethod = (RunMe) annotation;

                try {
                	m.invoke(clazz.getDeclaredConstructor().newInstance());
                    // RUNME
                    if (m.isAnnotationPresent(RunMe.class) ) {
	                    	runMeMethods.add(m);
	                        ++total;
	                        runme++; 
                    } else {
                    		// add even if access is not possible
	                        withoutRunMeMethods.add(m);
                            ++total;
                            norunme++;
                    }

                } catch (IllegalAccessException | InstantiationException | IllegalArgumentException | InvocationTargetException  | SecurityException | NoSuchMethodException ex) {
                    if (m.isAnnotationPresent(RunMe.class)) {
		            	notInvokeableMethods.add(m);
		                _notInvokeableMethods.add(m.getName()+ " " + ex.getClass().getSimpleName());
		                if (m.isAnnotationPresent(RunMe.class)) runMeMethods.add(m);
		                runme++;
                    } 		                
                    if (!m.isAnnotationPresent(RunMe.class)) { 
                    	withoutRunMeMethods.add(m);
                    	norunme++;
                    }
                    ++total;
                } 
            }
            System.out.println();
            System.out.println("printing report...");
            if (withoutRunMeMethods.size() != 0) printMethods(withoutRunMeMethods, "Methods without @RunMe:");
            if (runMeMethods.size() != 0) printMethods(runMeMethods, "Methods with @RunMe:");
            if (notInvokeableMethods.size() != 0) _printMethods(_notInvokeableMethods, "not invocable:");

            System.out.printf("%nResult: Total : %d, RunMeMethods: %d, NoRunMeMethods %d%n",
                    total,
                    runme,
                    norunme);


        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
            System.out.println("Error: " + className + " --- " + e.getClass().getSimpleName());
            System.out.println("Usage: java -jar runmerunner-sakvis.jar htwb.ai.className" + "\n \n"
            		+"Examples: \n"
            		+"1. java -jar runmerunner-sakvis.jar htwb.ai.RunMeMethods --> Correct Usage" +"\n" 
            		+"2. java -jar runmerunner-sakvis.jar htwb.ai.ParentClass --> will throw NonInstantiable" +"\n"
            		+"3. java -jar runmerunner-sakvis.jar java.io.Closeable  --> will throw NonInstantiable");
            return false;
        }
		return true;
    }

   public static void printMethods(ArrayList<Method> methods, String message) {
        System.out.println("---------------");
        System.out.println(message);
        System.out.println();
        for (Method m : methods) {
            System.out.println(m.getName());
        }

    }
    public static void _printMethods(ArrayList<String> methods, String message) {
        System.out.println("---------------");
        System.out.println(message);
        System.out.println();
        for (String m : methods) {
            System.out.println(m);
        }

    }


   public static Object loadClass(Class<?> clazz) throws InstantiationException,
            IllegalAccessException,
            IllegalArgumentException,
            InvocationTargetException,
            NoSuchMethodException,
            SecurityException {
        return clazz.getDeclaredConstructor().newInstance();
    }
}