package io.github.up2jakarta.dte.jpa.api.dtree;

import io.github.up2jakarta.dte.exe.api.dtree.PlainComputer;
import io.github.up2jakarta.dte.jpa.api.IComputer;
import io.github.up2jakarta.dte.jpa.api.ITemplate;
import io.github.up2jakarta.dte.jpa.api.Script;

/**
 * Decision tree plain computer based on script.
 *
 * @param <T> template concrete type
 */
public interface IPlainComputer<T extends ITemplate> extends IComputer, Script<T>, PlainComputer {

    @Override
    Long getId();
}
