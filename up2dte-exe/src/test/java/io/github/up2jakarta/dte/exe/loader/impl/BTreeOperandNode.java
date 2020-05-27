package io.github.up2jakarta.dte.exe.loader.impl;

import io.github.up2jakarta.dte.exe.api.Operator;
import io.github.up2jakarta.dte.exe.api.btree.ParentNode;


public class BTreeOperandNode extends BTreeChildNode implements ParentNode {

    private final Operator operator;

    public BTreeOperandNode(final long id, final ParentNode parent, final int order
            , final Operator operator, final boolean negated) {
        super(id, parent, order, negated);
        this.operator = operator;
    }

    @Override
    public Operator getOperator() {
        return operator;
    }
}
