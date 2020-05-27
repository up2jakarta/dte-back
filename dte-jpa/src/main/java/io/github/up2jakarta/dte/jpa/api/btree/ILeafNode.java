package io.github.up2jakarta.dte.jpa.api.btree;

import io.github.up2jakarta.dte.exe.api.btree.LeafNode;
import io.github.up2jakarta.dte.jpa.api.IDecider;

/**
 * Binary tree leaf node.
 */
public interface ILeafNode extends IChildNode, LeafNode {

    @Override
    IDecider getDecider();

}
