package io.github.up2jakarta.dte.exe.loader;

/**
 * Node Parser from API to execution representation.
 *
 * @param <T> the type of converted objects.
 * @param <N> the type of nodes to be converted.
 */
public interface NodeParser<T, N> {

    /**
     * Converts the value loaded from data storage into the engine representation to executed later.
     *
     * @param value the loaded value to be converted
     * @return the converted data to be executed in DTE engine
     */
    T parse(N value);
}
