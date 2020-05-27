package io.github.up2jakarta.dte.exe.loader.impl;


import io.github.up2jakarta.dte.exe.api.Decider;
import io.github.up2jakarta.dte.exe.api.Input;

import java.util.HashMap;
import java.util.Map;

public abstract class BTreeCondition extends BTreeNode implements Decider {

    private final Map<String, ? extends Input> typing = new HashMap<>();
    private final boolean negated;

    BTreeCondition(final long id, final boolean negated) {
        super(id);
        this.negated = negated;
    }

    @Override
    public boolean isNegated() {
        return negated;
    }

    @Override
    public Map<String, ? extends Input> getTyping() {
        return typing;
    }
}
