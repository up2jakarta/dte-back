package io.github.up2jakarta.dte.exe.script;

import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.transform.stc.AbstractTypeCheckingExtension;
import org.codehaus.groovy.transform.stc.StaticTypeCheckingVisitor;

import java.util.Map;

import static org.codehaus.groovy.syntax.Types.ASSIGN;
import static org.codehaus.groovy.syntax.Types.ASSIGNMENT_OPERATOR;

/**
 * Custom type checking extension that solve the input/output variables.
 */
class BindingExtension extends AbstractTypeCheckingExtension {

    private final Map<String, Class<?>> types;
    private final boolean tolerant;

    /**
     * Private constructor for BindingExtension.
     *
     * @param visitor the code visitor
     * @param types   the inputs typing
     * @param deep    the flag that tells the compiler to deeply check the undeclared outputs
     */
    BindingExtension(final StaticTypeCheckingVisitor visitor, final Map<String, Class<?>> types, final boolean deep) {
        super(visitor);
        this.tolerant = !deep;
        this.types = types;
    }

    @Override
    public boolean handleUnresolvedVariableExpression(final VariableExpression exp) {
        final Class<?> type = types.get(exp.getName());
        if (type != null) {
            makeDynamic(exp, ClassHelper.make(type));
            return true;
        }
        if (tolerant && super.getEnclosingBinaryExpression() != null) {
            final BinaryExpression declaration = super.getEnclosingBinaryExpression();
            final int operation = declaration.getOperation().getType();
            if (declaration.getLeftExpression() == exp && (operation == ASSIGN || operation == ASSIGNMENT_OPERATOR)) {
                makeDynamic(exp);
                return true;
            }
        }
        return false;
    }
}
