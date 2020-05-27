package io.github.up2jakarta.dte.jpa.api;

import io.github.up2jakarta.dte.exe.api.Identifiable;
import io.github.up2jakarta.dte.exe.api.Template;

/**
 * {@link Script} Template, useful for lazy loading.
 */
public interface ITemplate extends Identifiable<Integer>, Documented, Template {

    /**
     * @return the related group.
     */
    IGroup getGroup();

}
