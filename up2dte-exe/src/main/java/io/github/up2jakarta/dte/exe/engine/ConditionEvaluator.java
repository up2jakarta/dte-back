package io.github.up2jakarta.dte.exe.engine;

import io.github.up2jakarta.dte.dsl.BusinessException;
import io.github.up2jakarta.dte.exe.script.CompilationException;
import io.github.up2jakarta.dte.exe.script.ExecutionException;
import io.github.up2jakarta.dte.exe.script.ScriptContext;

import java.util.Map;

/**
 * Simple singleton that evaluates a boolean expression.
 *
 * @author A.ABBESSI
 */
final class ConditionEvaluator {

    private static final String URC_FORMAT = "Unexpected boolean result ['%s']";

    /**
     * Private constructor.
     */
    private ConditionEvaluator() {
    }

    /**
     * Evaluates the given {@code expression} within the given
     * {@code context} and returns the result.
     *
     * @param src the source code
     * @param ctx the input context
     * @return the boolean result
     * @throws BusinessException  if the script throw a business exception
     * @throws ExecutionException if a execution error has been occurred at runtime
     */
    static boolean isFulfilled(final DynamicScript src, final Map<String, Object> ctx) throws BusinessException {
        try {
            final Object r = src.getScript().evaluate(new ScriptContext(ctx));
            if (r == null) {
                return false;
            }
            if (r instanceof Boolean) {
                return (Boolean) r;
            }
            if (r instanceof Number) {
                int integer = ((Number) r).intValue();
                if (integer == 0) {
                    return false;
                }
                if (integer == 1) {
                    return true;
                }
            }
            throw new ExecutionException(src.toString(), String.format(URC_FORMAT, r));
        } catch (CompilationException sse) {
            throw new ExecutionException(src.toString(), sse);
        }
    }
}
