package io.github.up2jakarta.dte.exe.script;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import groovy.lang.GroovySystem;
import groovy.lang.Script;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.transform.stc.AbstractTypeCheckingExtension;
import org.codehaus.groovy.transform.stc.StaticTypeCheckingVisitor;

import java.util.Arrays;

import static org.codehaus.groovy.ast.ClassHelper.SCRIPT_TYPE;

/**
 * Custom type checking extension that solve the input/output variables.
 */
class SecurityExtension extends AbstractTypeCheckingExtension {

    static final String[] WHITE_PACKAGES = {
            "groovy.lang.",
            "java.lang.",
            "java.math.",
            "java.text.",
            "java.time.",
            "java.util."
    };

    static final Class[] BLACK_CLASSES = {
            System.class,
            Runtime.class,
            ProcessBuilder.class,
            Process.class,
            Thread.class,
            ThreadLocal.class,

            GroovySystem.class,
            Script.class,
            GroovyShell.class,
            GroovyClassLoader.class
    };

    /**
     * Private constructor for SecurityExtension.
     *
     * @param visitor the code visitor
     */
    SecurityExtension(final StaticTypeCheckingVisitor visitor) {
        super(visitor);
    }

    @Override
    public void onMethodSelection(final Expression exp, final MethodNode target) {
        final String className = target.getDeclaringClass().getName();
        if (!className.equals(Script.class.getName()) && target.getDeclaringClass().isDerivedFrom(SCRIPT_TYPE)) {
            return;
        }
        if (Arrays.stream(WHITE_PACKAGES).noneMatch(className::startsWith)) {
            addStaticTypeError("Method calls not allowed on [" + className + "]", exp);
        } else {
            for (final Class black : BLACK_CLASSES) {
                if (className.equals(black.getName())) {
                    addStaticTypeError("Method calls not allowed on [" + className + "]", exp);
                    break;
                }
            }
        }
    }
}
