package io.github.up2jakarta.dte.jpa.api;

/**
 * {@link IGroup} Parameter.
 *
 * @param <T> the concrete type
 */
public interface IParameter<T extends IType> extends Documented {

    /**
     * Return the value of the current variable.
     *
     * @return the value.
     */
    String getValue();

    /**
     * Set the value of the current parameter.
     *
     * @param value the parameter value
     */
    void setValue(String value);

    /**
     * Return the type of the current parameter.
     *
     * @return the parameter type.
     */
    T getType();

    /**
     * Set the type of the current parameter.
     *
     * @param type the parameter type
     */
    void setType(T type);
}
