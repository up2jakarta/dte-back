package io.github.up2jakarta.dte.exe.script;

import groovy.lang.Binding;
import groovy.lang.MissingPropertyException;
import io.github.up2jakarta.dte.dsl.BusinessException;

/**
 * Decision Tree Script Interface.
 *
 * @author A.ABBESSI
 */
@FunctionalInterface
public interface StaticScript {

    /**
     * Factory method for {@code Script} instances.
     *
     * @param base the base script class
     * @return an new instance
     */
    static StaticScript of(Class<groovy.lang.Script> base) {
        return new GroovyScript(base);
    }

    /**
     * Evaluates the script with the given {@code context}.
     *
     * @param context the execution context
     * @return the result of evaluation
     */
    Object evaluate(ScriptContext context);


    /**
     * Default implementation of Script.
     */
    final class GroovyScript implements StaticScript {

        private final Class<groovy.lang.Script> scriptClass;

        /**
         * Private constructor.
         *
         * @param baseClass the script base class
         */
        private GroovyScript(final Class<groovy.lang.Script> baseClass) {
            this.scriptClass = baseClass;
        }

        @Override
        public Object evaluate(final ScriptContext context) {
            try {
                final groovy.lang.Script script = scriptClass.newInstance();
                script.setBinding(new Binding(context));
                return script.run();
            } catch (MissingPropertyException ex) {
                throw new ContextException(scriptClass.getSimpleName(), ex);
            } catch (BusinessException ex) {
                throw ex;
            } catch (RuntimeException | IllegalAccessException | InstantiationException ex) {
                throw new ExecutionException(scriptClass.getSimpleName(), ex);
            }
        }
    }
}
