package io.github.up2jakarta.dte.exe.script;

import groovy.transform.Generated;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.transform.sc.StaticCompilationVisitor;
import org.codehaus.groovy.transform.stc.DefaultTypeCheckingExtension;
import org.codehaus.groovy.transform.stc.TraitTypeCheckingExtension;

import java.util.Arrays;
import java.util.Map;

import static org.codehaus.groovy.ast.ClassHelper.SCRIPT_TYPE;
import static org.codehaus.groovy.transform.sc.StaticCompilationMetadataKeys.PROPERTY_OWNER;

/**
 * Extensible Groovy Code Visitor that tells the compiler how to check types statically.
 *
 * @author A.ABBESSI
 */
public class StaticVisitor extends StaticCompilationVisitor {

    private static final ClassNode GENERATED_TYPE = ClassHelper.make(Generated.class);

    /**
     * Protected constructor for StaticVisitor.
     *
     * @param u    the source unit
     * @param n    the class node
     * @param t    the inputs typing
     * @param deep the flag that tells the compiler to deeply check the undeclared outputs
     */
    public StaticVisitor(final SourceUnit u, final ClassNode n, final Map<String, Class<?>> t, final boolean deep) {
        super(u, n);
        extension = new DefaultTypeCheckingExtension(this);
        extension.addHandler(new TraitTypeCheckingExtension(this));
        extension.addHandler(new BindingExtension(this, t, deep));
        extension.addHandler(new SecurityExtension(this));
    }

    @Override
    public void visitPropertyExpression(final PropertyExpression exp) {
        super.visitPropertyExpression(exp);
        //final Expression objectExpression = exp.getObjectExpression();
        //final ClassNode classNode = findCurrentInstanceOfClass(objectExpression, getType(objectExpression));
        final ClassNode owner = exp.getObjectExpression().getNodeMetaData(PROPERTY_OWNER);
        if (owner == null || owner.isDerivedFrom(SCRIPT_TYPE) || !owner.getAnnotations(GENERATED_TYPE).isEmpty()) {
            return;
        }
        final String className = owner.getName();
        if (Arrays.stream(SecurityExtension.WHITE_PACKAGES).noneMatch(className::startsWith)) {
            addStaticTypeError("Property accesses not allowed on [" + className + "]", exp);
        } else {
            for (final Class black : SecurityExtension.BLACK_CLASSES) {
                if (className.equals(black.getName())) {
                    addStaticTypeError("Property accesses not allowed on [" + className + "]", exp);
                    break;
                }
            }
        }

    }

    /**
     * Finds the type {@link ClassNode} of the given {@code expression}.
     *
     * @param exp the groovy expression
     * @return the class node
     */
    ClassNode getType(final Expression exp) {
        return super.getType(exp);
    }
}
