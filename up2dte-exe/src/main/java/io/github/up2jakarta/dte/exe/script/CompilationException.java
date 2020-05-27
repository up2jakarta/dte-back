package io.github.up2jakarta.dte.exe.script;

import org.codehaus.groovy.control.CompilationFailedException;

/**
 * Superclass of any exception thrown during the validation or the compilation of the script.
 *
 * @author A.ABBESSI
 */
public class CompilationException extends RuntimeException {

    /**
     * Public constructor for CompilationException.
     *
     * @param ex the compilation errors
     */
    public CompilationException(final CompilationFailedException ex) {
        super(ex.getMessage(), ex);
    }

}

