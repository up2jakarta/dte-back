package io.github.up2jakarta.dte.jpa.api;

import io.github.up2jakarta.dte.exe.api.Identifiable;
import io.github.up2jakarta.dte.exe.api.Type;

/**
 * {@link Input} and {@link Output} variable type.
 */
public interface IType extends Identifiable<Integer>, Documented, Type {

    /**
     * @return the related group.
     */
    IGroup getGroup();
}
