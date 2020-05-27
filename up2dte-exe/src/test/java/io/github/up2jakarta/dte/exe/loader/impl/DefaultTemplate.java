package io.github.up2jakarta.dte.exe.loader.impl;

import io.github.up2jakarta.dte.dsl.BaseScript;
import io.github.up2jakarta.dte.exe.api.Template;

public final class DefaultTemplate implements Template {

    private static final DefaultTemplate INSTANCE = new DefaultTemplate();

    static DefaultTemplate getInstance() {
        return INSTANCE;
    }

    @Override
    public Class<BaseScript> getBaseClass() {
        return BaseScript.class;
    }
}
