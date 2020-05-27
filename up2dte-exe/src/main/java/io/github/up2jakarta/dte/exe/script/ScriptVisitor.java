package io.github.up2jakarta.dte.exe.script;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.expr.*;
import org.codehaus.groovy.control.SourceUnit;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.codehaus.groovy.syntax.Types.ASSIGN;
import static org.codehaus.groovy.syntax.Types.ASSIGNMENT_OPERATOR;

/**
 * Groovy Code Visitor that extract output variables and theirs types from a script.
 *
 * @author A.ABBESSI
 */
public class ScriptVisitor extends ClassCodeVisitorSupport {

    private final Map<String, Class<?>> outputs;
    private final SourceUnit source;
    private boolean skip = false;
    private StaticVisitor delegate;

    /**
     * Public constructor to create an instance.
     *
     * @param source the source unit
     */
    public ScriptVisitor(final SourceUnit source) {
        this.source = source;
        this.outputs = new HashMap<>();
    }

    /**
     * @return the outputs typing of declared variables
     */
    public Map<String, Class<?>> getOutputs() {
        return outputs;
    }

    /**
     * Visit the contents of the given class {@code node}.
     *
     * @param node the class to visit
     */
    public void visitClass(final ClassNode node) {
        delegate = new StaticVisitor(source, node, Collections.emptyMap(), false);
        for (MethodNode mn : node.getMethods()) {
            this.visitMethod(mn);
        }
    }

    @Override
    public void visitMethod(final MethodNode node) {
        if (node.isScriptBody()) {
            skip = false;
            super.visitMethod(node);
        } else {
            skip = true;
            super.visitMethod(node);
            skip = true;
        }
    }

    @Override
    public void visitBinaryExpression(final BinaryExpression exp) {
        super.visitBinaryExpression(exp);
        final int op = exp.getOperation().getType();
        if (!skip && !(exp instanceof DeclarationExpression) && (exp.getLeftExpression() instanceof VariableExpression)
                && (op == ASSIGN || op == ASSIGNMENT_OPERATOR)) {
            final VariableExpression left = (VariableExpression) exp.getLeftExpression();
            outputs.put(left.getAccessedVariable().getName(), getTypeClass(exp.getRightExpression()));
            exp.getRightExpression().visit(this);
        }
    }

    @Override
    protected SourceUnit getSourceUnit() {
        return source;
    }

    /**
     * Get the {@link Class} type of the specified groovy expression.
     *
     * @param exp the groovy expression
     * @return the type of the specified groovy expression
     */
    private Class<?> getTypeClass(final Expression exp) {
        if (exp instanceof MethodCallExpression) {
            final MethodCallExpression method = (MethodCallExpression) exp;
            return getReturnType(method.getMethodTarget(), (ArgumentListExpression) method.getArguments());
        }
        return ClassUtil.wrap(delegate.getType(exp).getTypeClass());
    }

    /**
     * Get the return {@link Class} of the given {@code method} depending on the given arguments classes {@code types}.
     *
     * @param method    the method node
     * @param arguments the argument list
     * @return the method return class
     */
    private Class<?> getReturnType(final MethodNode method, final ArgumentListExpression arguments) {
        // Extracting return type from source code method
        if (method.getReturnType().isUsingGenerics()) {
            final Class<?>[] args = arguments.getExpressions().stream().map(e -> {
                if (e instanceof ConstantExpression && ((ConstantExpression) e).isNullExpression()) {
                    return null;
                }
                return getTypeClass(e);
            }).toArray(Class[]::new);
            Class<?> result = null;
            final ClassNode rt = method.getReturnType(); // Return Type
            for (int i = 0; i < Math.min(args.length, method.getParameters().length); i++) {
                final ClassNode pt = method.getParameters()[i].getType(); // Parameter Type
                if (i == method.getParameters().length - 1 && pt.isArray() && pt.isDerivedFrom(pt.getComponentType())) {
                    for (; i < args.length; i++) {
                        Class<?> p = (args[i] != null && args[i].isArray()) ? args[i].getComponentType() : args[i];
                        result = ClassUtil.commonSuperClass(result, p);
                    }
                } else if (rt.isDerivedFrom(pt)) {
                    result = ClassUtil.commonSuperClass(result, args[i]);
                } else if (pt.isArray() && pt.isDerivedFrom(pt.getComponentType())) {
                    final Class<?> p = (args[i] != null && args[i].isArray()) ? args[i].getComponentType() : args[i];
                    result = ClassUtil.commonSuperClass(result, p);
                }
            }
            return result;
        }
        return ClassUtil.wrap(method.getReturnType().getTypeClass());
    }
}
