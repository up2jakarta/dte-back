package io.github.up2jakarta.dte.exe.api.btree;

import io.github.up2jakarta.dte.exe.api.Decider;
import io.github.up2jakarta.dte.exe.api.Template;

/**
 * BTree Plain Decider, based on script code source.
 */
public interface PlainDecider extends Decider {

    /**
     * @return the script source code.
     */
    String getScript();

    /**
     * @return the script template
     */
    Template getTemplate();
}
