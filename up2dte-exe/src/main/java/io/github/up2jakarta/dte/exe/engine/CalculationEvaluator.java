package io.github.up2jakarta.dte.exe.engine;

import io.github.up2jakarta.dte.dsl.BusinessException;
import io.github.up2jakarta.dte.exe.script.CompilationException;
import io.github.up2jakarta.dte.exe.script.ExecutionException;
import io.github.up2jakarta.dte.exe.script.ScriptContext;

import java.util.Map;

/**
 * Simple singleton that evaluates a calculation rule.
 *
 * @author A.ABBESSI
 */
final class CalculationEvaluator {

    /**
     * Private constructor.
     */
    private CalculationEvaluator() {
    }

    /**
     * Evaluates the given {@code rule} within the given
     * input {@code context} and returns the output context.
     *
     * @param src the source code
     * @param ctx the input context
     * @return the output result
     * @throws BusinessException  if the script throw a business exception
     * @throws ExecutionException if a execution error has been occurred at runtime
     */
    static Map<String, Object> evaluate(final DynamicScript src, final Map<String, Object> ctx)
            throws BusinessException {
        try {
            final ScriptContext inOutContext = new ScriptContext(ctx);
            src.getScript().evaluate(inOutContext);
            return inOutContext.getOut();
        } catch (CompilationException sse) {
            throw new ExecutionException(src.toString(), sse);
        }
    }

}
