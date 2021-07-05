package htwb.ai;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//TODO: #1 - 3 tasks
// a. Annotation erstellen: Name: RunMe ->in htwb.ai 
// b. Annotation: zur Laufzeit auswerten und nur für Methode verwenden 


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
//@Target(ElementType.TYPE)
public @interface RunMe {

//	public boolean enabled () default true;
	// you can define properties here: for example string name(); int  count ()


}
