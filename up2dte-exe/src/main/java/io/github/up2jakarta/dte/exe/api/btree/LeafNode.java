package io.github.up2jakarta.dte.exe.api.btree;

import io.github.up2jakarta.dte.exe.api.Decider;

/**
 * BTree Leaf Node.
 */
public interface LeafNode extends ChildNode {

    /**
     * @return the related decider
     */
    Decider getDecider();

}
