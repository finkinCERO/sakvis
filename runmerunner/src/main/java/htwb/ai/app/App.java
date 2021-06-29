package htwb.ai.app;

import htwb.ai.ParentClass;
import htwb.ai.RunMe;
import htwb.ai.ex.NoInputException;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class App {

    public static void main(String[] args) throws NoInputException {
    	
    	AnnoVsNoAnnoMethods allMethods = new AnnoVsNoAnnoMethods();

    	if (args.length == 0) {

            NoInputException ex = new NoInputException("No Parameter [className] was given to main method!");
            System.out.println("Error: " +
                    ex.getClass().getSimpleName());
            System.out.println("Usage: java -jar path/to/jar/runmerunner.jar Packagepath.className" +"\n"
                    +"\nfor example: java -jar runmerunner-sakvis.jar htwb.ai.RunMeMethods");
            //Um die Main-Methode testbar zu machen werfen wird eine NoInputException
            throw new NoInputException(ex.getClass().getSimpleName());

        } else for (int i = 0; i < args.length; i++) {
            allMethods.searchForClass(args[i]);
        }
//    	searchForClass("htwb.ai.RunMeMethods");
    }
 }
