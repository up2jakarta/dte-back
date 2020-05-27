package io.github.up2jakarta.dte.jpa.api;

import io.github.up2jakarta.dte.exe.api.Computer;
import io.github.up2jakarta.dte.exe.api.Identifiable;

import java.util.Map;

/**
 * Shareable computer.
 */
public interface IComputer extends Documented, Shareable, Computer, Identifiable<Long> {

    @Override
    Map<String, ? extends Input> getTyping();

    @Override
    Map<String, ? extends Output> getDeclaredTyping();

}
