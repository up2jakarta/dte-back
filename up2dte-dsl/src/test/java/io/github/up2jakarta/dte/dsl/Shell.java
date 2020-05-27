package io.github.up2jakarta.dte.dsl;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Utilities Class that contains useful methods for test cases.
 *
 * @author A.ABBESSI
 * @see this#eval(String, Map)
 */
public final class Shell {

    private static final AtomicLong GEN_ID = new AtomicLong(0);

    private Shell() {
    }

    /**
     * Evaluates the given script and return the result;
     *
     * @param code    the script source code
     * @param context the input context
     * @return the result
     */
    public static Object eval(String code, Map<String, Object> context) {
        final CompilerConfiguration config = new CompilerConfiguration();
        config.setScriptBaseClass(BaseScript.class.getName());
        final GroovyShell shell = new GroovyShell(new Binding(context), config);
        code = "import java.time.*\n" + code;
        return shell.run(code, "Script" + GEN_ID.incrementAndGet(), new String[]{});
    }

    /**
     * Evaluates the given script and return the result;
     *
     * @param code    the script source code
     * @param context the input context
     * @return the result
     */
    @SuppressWarnings({"unchecked", "unused"})
    public static <T> T eval(String code, Map<String, Object> context, Class<T> type) {
        return (T) eval(code, context);
    }

}
