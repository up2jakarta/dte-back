package io.github.up2jakarta.dte.exe.loader.impl;


import io.github.up2jakarta.dte.exe.api.Identifiable;

public abstract class DTreeNode implements Identifiable<Long> {

    private final long id;

    DTreeNode(final long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

}
