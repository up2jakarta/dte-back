package io.github.up2jakarta.dte.exe.engine.dtree;

import io.github.up2jakarta.dte.exe.engine.Calculation;
import io.github.up2jakarta.dte.exe.engine.Decision;

import java.util.Map;

/**
 * Calculation node for complex calculation.
 *
 * @author A.ABBESSI
 */
public interface Rule extends Node<Decision<?>>, Calculation {

    /**
     * Factory method for {@code Rule} instances.
     *
     * @param calculation the calculation rule
     * @return an instance of {@code Rule}
     */
    static Rule of(final Calculation calculation) {
        if (calculation instanceof Rule) {
            return (Rule) calculation;
        }
        return new RuleImpl(calculation);
    }

    /**
     * Return teh associated calculation.
     *
     * @return the related calculation.
     */
    Calculation getCalculation();

    /**
     * Default implementation of rule node.
     */
    class RuleImpl extends BaseImpl<Decision<?>> implements Rule {

        private final Calculation rule;

        /**
         * Private constructor.
         *
         * @param rule the calculation
         */
        private RuleImpl(final Calculation rule) {
            this.rule = rule;
        }

        @Override
        public Map<String, Object> resolve(final Map<String, Object> context) {
            return rule.resolve(context);
        }

        @Override
        public String toString() {
            return String.valueOf(rule);
        }

        @Override
        public Calculation getCalculation() {
            return rule;
        }
    }

}
