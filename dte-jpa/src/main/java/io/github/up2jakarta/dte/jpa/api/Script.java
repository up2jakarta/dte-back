package io.github.up2jakarta.dte.jpa.api;

/**
 * Tree {@link Node} based on Script.
 *
 * @param <T> template concrete type
 */
public interface Script<T extends ITemplate> extends Documented {

    /**
     * Set the script source code.
     *
     * @param script the script source code
     */
    void setScript(String script);

    /**
     * @return the script source code.
     */
    String getScript();

    /**
     * @return the script template
     */
    T getTemplate();

    /**
     * Set the script base template, the compiled script will inherit from this template.
     *
     * @param template the script template
     */
    void setTemplate(T template);
}
