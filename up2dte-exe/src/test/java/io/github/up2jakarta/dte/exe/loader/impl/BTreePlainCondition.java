package io.github.up2jakarta.dte.exe.loader.impl;

import io.github.up2jakarta.dte.exe.api.Template;
import io.github.up2jakarta.dte.exe.api.btree.PlainDecider;

public class BTreePlainCondition extends BTreeCondition implements PlainDecider {

    private final String script;

    public BTreePlainCondition(final long id, final String script, final boolean negated) {
        super(id, negated);
        this.script = script;
    }

    @Override
    public String getScript() {
        return script;
    }

    @Override
    public Template getTemplate() {
        return DefaultTemplate.getInstance();
    }

}
