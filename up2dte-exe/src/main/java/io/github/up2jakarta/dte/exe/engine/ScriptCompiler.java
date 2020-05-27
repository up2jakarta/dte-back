package io.github.up2jakarta.dte.exe.engine;

import groovy.lang.Script;
import groovy.transform.CompilationUnitAware;
import groovy.transform.CompileStatic;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.classgen.GeneratorContext;
import org.codehaus.groovy.control.*;
import org.codehaus.groovy.control.CompilationUnit.PrimaryClassNodeOperation;
import org.codehaus.groovy.control.customizers.CompilationCustomizer;
import org.codehaus.groovy.control.customizers.SecureASTCustomizer;
import org.codehaus.groovy.transform.GroovyASTTransformation;
import org.codehaus.groovy.transform.sc.StaticCompileTransformation;
import io.github.up2jakarta.dte.exe.script.CompilationException;
import io.github.up2jakarta.dte.exe.script.ScriptVisitor;
import io.github.up2jakarta.dte.exe.script.StaticVisitor;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.Map;

/**
 * Decision Tree Script Validator.
 *
 * @author A.ABBESSI
 */
public final class ScriptCompiler {

    private static final SecureASTCustomizer SECURE_AST = new SecureASTCustomizer();

    static {
        SECURE_AST.setAllowedStaticStarImports(Arrays.asList(
                Math.class.getName(),
                DayOfWeek.class.getName()
        ));
        SECURE_AST.setMethodDefinitionAllowed(true);
        SECURE_AST.setClosuresAllowed(true);
    }

    /**
     * Private constructor for ScriptValidator.
     */
    private ScriptCompiler() {
    }

    /**
     * Check the syntax and types of the given {@code script} within DTE extensions.
     * Extract the output variables typing and return them.
     *
     * @param base   the script base class
     * @param script the script source code
     * @param types  the input typing
     * @return the output typing
     * @throws CompilationException if the script does not compile
     */
    public static Map<String, Class<?>> compile(final Class<? extends Script> base, final String script,
                                                final Map<String, Class<?>> types) throws CompilationException {
        final CompilerConfiguration config = new CompilerConfiguration();
        config.setScriptBaseClass(base.getName());
        final ScriptLoader loader = new ScriptLoader(config, SECURE_AST, new Customizer(types, false));
        final CompilationUnit unit = new CompilationUnit(config, null, loader);
        final SourceUnit source = unit.addSource("Script", script);

        final ScriptVisitor visitor = new ScriptVisitor(source);
        final PrimaryClassNodeOperation extractor = new PrimaryClassNodeOperation() {
            public void call(final SourceUnit su, final GeneratorContext ctx, final ClassNode node) {
                visitor.visitClass(node);
            }
        };
        unit.addPhaseOperation(extractor, Phases.CLASS_GENERATION);
        try {
            unit.compile(Phases.CLASS_GENERATION);
        } catch (CompilationFailedException ex) {
            throw new CompilationException(ex);
        }
        return visitor.getOutputs();
    }

    /**
     * Check the syntax and types of the given {@code script} within DTE extensions.
     *
     * @param base   the script base class
     * @param script the script source code
     * @param types  the variables(inputs and outputs) typing
     * @throws CompilationException if the script does not compile
     */
    public static void validate(final Class<? extends Script> base, final String script,
                                final Map<String, Class<?>> types) throws CompilationException {
        final CompilerConfiguration config = new CompilerConfiguration();
        config.setScriptBaseClass(base.getName());
        final ScriptLoader loader = new ScriptLoader(config, SECURE_AST, new Customizer(types, false));
        final CompilationUnit unit = new CompilationUnit(config, null, loader);
        unit.addSource("Script", script);
        try {
            unit.compile(Phases.CLASS_GENERATION);
        } catch (CompilationFailedException ex) {
            throw new CompilationException(ex);
        }
    }

    /**
     * Custom compilation customizer that solve the input/output variables.
     *
     * @see org.codehaus.groovy.control.customizers.ASTTransformationCustomizer
     */
    private static class Customizer extends CompilationCustomizer implements CompilationUnitAware {

        private final AnnotationNode annotationNode;
        private final StaticCompileTransformation transformation;
        private CompilationUnit compilationUnit;

        /**
         * Private constructor for Customizer.
         *
         * @param types the input typing
         * @param deep  the flag that tells the compiler to deeply check the undeclared outputs
         */
        Customizer(final Map<String, Class<?>> types, final boolean deep) {
            super(CompilePhase.INSTRUCTION_SELECTION);
            this.transformation = new StaticTransformation(types, deep);
            this.annotationNode = new AnnotationNode(ClassHelper.make(CompileStatic.class));
        }

        @Override
        public void setCompilationUnit(final CompilationUnit unit) {
            this.compilationUnit = unit;
        }

        @Override
        public void call(final SourceUnit source, final GeneratorContext context, final ClassNode classNode) {
            transformation.setCompilationUnit(compilationUnit);
            annotationNode.setSourcePosition(classNode);
            transformation.visit(new ASTNode[]{annotationNode, classNode}, source);
        }
    }

    /**
     * Handles the implementation of the {@link groovy.transform.CompileStatic} transformation.
     */
    @GroovyASTTransformation(phase = CompilePhase.INSTRUCTION_SELECTION)
    private static final class StaticTransformation extends StaticCompileTransformation {

        private final Map<String, Class<?>> types;
        private final boolean deep;

        /**
         * Private constructor for StaticTransformation.
         *
         * @param types the inputs typing
         * @param deep  the flag that tells the compiler to deeply check the undeclared outputs
         */
        StaticTransformation(final Map<String, Class<?>> types, final boolean deep) {
            this.types = types;
            this.deep = deep;
        }

        @Override
        protected StaticVisitor newVisitor(final SourceUnit unit, final ClassNode node) {
            return new StaticVisitor(unit, node, types, deep);
        }
    }
}

