package htwb.ai;

import java.lang.reflect.InvocationTargetException;

public abstract class ParentClass {
  
    abstract int methodBOOM();
        
    /**
     * Creates an instance of a Foo class
     * 
     * @param className - the name of class to be instantiated, class must extend Foo
     * @return
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
	public static Object create (String className) 
	        throws ClassNotFoundException,
	        InstantiationException, 
	        IllegalAccessException, 
	        IllegalArgumentException, 
	        InvocationTargetException, 
	        NoSuchMethodException, 
	        SecurityException {
		
		System.out.println("ParentClass.create: " + className);
		Class<?> c = Class.forName(className);
		return  c.getDeclaredConstructor().newInstance();
	}
}
