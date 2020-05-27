package io.github.up2jakarta.dte.exe.engine;

import org.assertj.core.util.introspection.PropertyOrFieldSupport;
import org.mockito.Mockito;
import io.github.up2jakarta.dte.dsl.BaseScript;
import io.github.up2jakarta.dte.exe.api.Identifiable;
import io.github.up2jakarta.dte.exe.loader.Finder;
import io.github.up2jakarta.dte.exe.loader.Loader;
import io.github.up2jakarta.dte.exe.script.ScriptContext;
import io.github.up2jakarta.dte.exe.script.StaticScript;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Utilities Class that contains useful methods for test cases.
 *
 * @author A.ABBESSI
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class TestHelper {

    static final ScriptLoader LOADER = new LoaderFactory().of(BaseScript.class);
    private static final AtomicLong GEN_ID = new AtomicLong(0);

    private TestHelper() {
    }

    /**
     * Evaluates the given script and return the result;
     *
     * @param code    the script source code
     * @param context the input context
     * @return the result
     */
    public static Object run(final String code, final Map<String, Object> context) {
        final StaticScript script = LOADER.parse(String.valueOf(GEN_ID.incrementAndGet()), code);
        return script.evaluate(new ScriptContext(context));
    }

    /**
     * Extract the value of the given {@code name} from the specified {@code object}.
     *
     * @param object the object instance
     * @param name   the property or field name to extract
     * @return the extracted property or field value
     */
    public static Object extract(final String name, final Object object) {
        return PropertyOrFieldSupport.EXTRACTION.getValueOf("scriptClass", object);
    }

    /**
     * Generates an unique identifier and creates an instance of {@link Condition}.
     *
     * @param script the script source code
     * @return an instance of condition
     */
    public static Condition condition(String script) {
        return Condition.of(BaseScript.class, GEN_ID.incrementAndGet(), script);
    }

    /**
     * Generates an unique identifier and creates an instance of {@link Calculation}.
     *
     * @param script the script source code
     * @return an instance {@link Calculation}
     */
    public static Calculation calculation(String script) {
        return Calculation.of(BaseScript.class, GEN_ID.incrementAndGet(), script);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Identifiable<Long>> Finder<T> mockFinder(final Class<T> type) {
        return Mockito.mock(Finder.class);
    }

    @SuppressWarnings("unchecked")
    public static <T> Loader<T> mockLoader(final Class<T> type) {
        return Mockito.mock(Loader.class);
    }

}
