package io.github.up2jakarta.dte.jpa.api.btree;

import io.github.up2jakarta.dte.exe.api.btree.PlainDecider;
import io.github.up2jakarta.dte.jpa.api.IDecider;
import io.github.up2jakarta.dte.jpa.api.ITemplate;
import io.github.up2jakarta.dte.jpa.api.Script;

/**
 * Binary tree plain decider based on script.
 *
 * @param <T> template concrete type
 */
public interface IPlainDecider<T extends ITemplate> extends Script<T>, IDecider, PlainDecider {

    @Override
    Long getId();
}
