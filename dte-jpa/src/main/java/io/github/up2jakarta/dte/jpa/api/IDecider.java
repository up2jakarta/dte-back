package io.github.up2jakarta.dte.jpa.api;

import io.github.up2jakarta.dte.exe.api.Decider;
import io.github.up2jakarta.dte.jpa.api.btree.INode;

import java.util.Map;

/**
 * Shareable decider.
 */
public interface IDecider extends INode, Documented, Shareable, Decider {

    @Override
    Map<String, ? extends Input> getTyping();

}
