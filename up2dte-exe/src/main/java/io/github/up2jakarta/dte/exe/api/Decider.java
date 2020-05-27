package io.github.up2jakarta.dte.exe.api;

import io.github.up2jakarta.dte.exe.api.btree.MixedDecider;
import io.github.up2jakarta.dte.exe.api.btree.PlainDecider;

import java.util.Map;

/**
 * BTree Decider.
 *
 * @see MixedDecider
 * @see PlainDecider
 */
public interface Decider extends Negateable, Identifiable<Long> {

    /**
     * Return the execution context input mapping name/variable.
     *
     * @return The context input.
     */
    Map<String, ? extends Input> getTyping();

}
