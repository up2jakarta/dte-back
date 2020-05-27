package io.github.up2jakarta.dte.dsl;

import groovy.lang.MissingPropertyException;
import groovy.lang.ReadOnlyPropertyException;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Test cases for Engine Exceptions.
 */
@RunWith(JUnit4.class)
public class ExceptionTests {

    private final Map<String, Object> context = new HashMap<>();

    @After
    public void tearDown() {
        context.clear();
    }

    @Test(expected = MissingPropertyException.class)
    public void missingInput() {
        // when
        Shell.eval("nvl(x, 1)", context);

        // then BOOM
    }

    @Test
    public void syntaxOperators() {
        //Given
        final String msg = "expecting EOF, found '+' @ line 2, column 3";

        // when
        MultipleCompilationErrorsException exception = null;
        try {
            Shell.eval("2 +* 2", context, Object.class);
        } catch (MultipleCompilationErrorsException ex) {
            exception = ex;
        }

        // then
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains(msg));
    }

    @Test
    public void readonlyProperty() {
        //Given
        context.put("a", new TestAccess());
        ReadOnlyPropertyException exception = null;

        // when
        try {
            Shell.eval("a.finalProperty = 3", context, Object.class);
        } catch (ReadOnlyPropertyException ex) {
            exception = ex;
        }

        // then
        assertNotNull(exception);
        assertEquals("finalProperty", exception.getProperty());
    }

    /**
     * Groovy can access private properties
     */
    @Test
    public void privateProperty() {
        //Given
        context.put("a", new TestAccess());

        // when
        Shell.eval("a.privateProperty = 3", context, Object.class);

        // then BOOM
    }

    @Test(expected = RuntimeException.class)
    public void runtimeException() {
        String code = "throw new RuntimeException(\"Error\")";
        Shell.eval(code, context);
    }

    @SuppressWarnings("unused")
    private static class TestAccess {
        private final int finalProperty = 2;
        private int privateProperty = 2;

        private TestAccess() {
        }

        public int getProperty() {
            return privateProperty;
        }

        public void setProperty(int privateProperty) {
            this.privateProperty = privateProperty;
        }
    }
}
