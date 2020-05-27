package io.github.up2jakarta.dte.exe.api;

import io.github.up2jakarta.dte.exe.api.dtree.MixedComputer;
import io.github.up2jakarta.dte.exe.api.dtree.PlainComputer;

import java.util.Map;

/**
 * DTree Computer.
 *
 * @see MixedComputer
 * @see PlainComputer
 */
public interface Computer extends Identifiable<Long> {

    /**
     * Return the engine execution typing inputs.
     *
     * @return the typing inputs.
     */
    Map<String, ? extends Input> getTyping();

    /**
     * Return the engine execution typing outputs.
     *
     * @return the typing outputs.
     */
    Map<String, ? extends Output> getDeclaredTyping();

}
