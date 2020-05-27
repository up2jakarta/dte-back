package io.github.up2jakarta.dte.exe.loader.impl;


import io.github.up2jakarta.dte.exe.api.Template;
import io.github.up2jakarta.dte.exe.api.dtree.PlainComputer;

public class DTreePlainCalculation extends DTreeCalculation implements PlainComputer {

    private final String script;

    public DTreePlainCalculation(final long id, final String script) {
        super(id);
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
