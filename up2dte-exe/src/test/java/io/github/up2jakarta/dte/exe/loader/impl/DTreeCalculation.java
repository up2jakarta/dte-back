package io.github.up2jakarta.dte.exe.loader.impl;

import io.github.up2jakarta.dte.exe.api.Computer;
import io.github.up2jakarta.dte.exe.api.Input;
import io.github.up2jakarta.dte.exe.api.Output;

import java.util.HashMap;
import java.util.Map;

public abstract class DTreeCalculation extends DTreeNode implements Computer {

    private final Map<String, ? extends Input> typing = new HashMap<>();
    private final Map<String, ? extends Output> declaredTyping = new HashMap<>();

    DTreeCalculation(final long id) {
        super(id);
    }

    @Override
    public Map<String, ? extends Input> getTyping() {
        return typing;
    }

    @Override
    public Map<String, ? extends Output> getDeclaredTyping() {
        return declaredTyping;
    }
}
