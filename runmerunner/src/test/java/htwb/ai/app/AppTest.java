package htwb.ai.app;

import htwb.ai.ParentClass;
import htwb.ai.RunMeMethods;
import htwb.ai.ex.NoInputException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {
    private String classWithMethods;
    private String classNotFound;
    private String extraClass;
	private AnnoVsNoAnnoMethods allMethods;

    Class clazz;
    Method[] methods;
    Object obj;
    private List[] results;

    private final String path = "htwb.ai.";

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    public void setUp() throws Exception {
        classWithMethods = path + "RunMeMethods";
        extraClass = path + "ExtraClass";
        classNotFound = path + "N/A";
        results = new ArrayList[2];


    }
    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void noInputTest() {
        App a = new App();
        String[] emptyArr = {};
        assertThrows(NoInputException.class, () -> {
            a.main(emptyArr);
        });
    }
    @Test
    public void noInputTestUsageMessage() throws NoInputException {
        App a = new App();
        String[] emptyArr = {};
        assertThrows(NoInputException.class, () -> {
            a.main(emptyArr);
            assertEquals(true, outContent.toString().contains("usage:"));
        });

    }

    @Test
    public void areExceptionsCatched() throws NoInputException {
        App a = new App();
        String[] arr = {"htwb.ai.RunMeMethods"};
        a.main(arr);
        assertEquals(true, outContent.toString().contains("Exception"));
    }
    @Test
    public void usageOutprinted() throws NoInputException {
        App a = new App();
        String[] arr = {"htwb.ai.RunMeMethods"};

        a.main(arr);
        boolean b = false;
        System.out.println(outContent.toString());
        //if(outContent.toString().contains("usage")) b = true;
        //assertEquals(true, b);
    }

    @Test
    public void classNotFoundTest() {
        assertThrows(ClassNotFoundException.class, () -> {
            ParentClass.create("blub");
        });
    }

    @Test
    public void noSuchMethodExTest() {
        assertThrows(NoSuchMethodException.class, () -> {
            ParentClass.create("java.io.Closeable");
        });
    }

    @Test
    public void abtractClassInstantiationExceptionTest() {
        assertThrows(InstantiationException.class, () -> {
            ParentClass.create("htwb.ai.ParentClass");
        });
    }

    @Test
    public void nullTest() {
        assertThrows(NullPointerException.class, () -> {
            ParentClass.create(null);
        });
    }

    @Test
    public void privateIllegalAccessExceptionTest() {
        assertThrows(IllegalAccessException.class, () -> {

            Class<?> c = Class.forName("htwb.ai.RunMeMethods");
            Method method = c.getDeclaredMethod("findMe4");
            method.invoke(c.getDeclaredConstructor().newInstance());
        });
    }

    @Test
    public void invocationTargetExceptionTest() throws NoInputException {

        App a = new App();
        String[] arr = {"htwb.ai.RunMeMethods"};
        a.main(arr);
        assertEquals(true, outContent.toString().contains("InvocationTargetException"));

    }


    @Test
    public void illegalArgumentExceptionTest() throws NoInputException {

        App a = new App();
        String[] arr = {"htwb.ai.RunMeMethods"};
        a.main(arr);
        assertEquals(true, outContent.toString().contains("IllegalArgumentException"));
    }

    @Test
    public void protectedIllegalAccessExceptionTest() throws NoInputException {

        App a = new App();
        String[] arr = {"htwb.ai.RunMeMethods"};
        a.main(arr);
        assertEquals(true, outContent.toString().contains("IllegalAccessException"));

    }

    @Test
    public void noSuchClassName()  {
        boolean result = allMethods.searchForClass("");
        assertFalse(result);
    }
    
    @Test
    public void classFoundReturnTrue() {
        boolean result = allMethods.searchForClass("htwb.ai.RunMeMethods");
        assertTrue(result);
    }
}
