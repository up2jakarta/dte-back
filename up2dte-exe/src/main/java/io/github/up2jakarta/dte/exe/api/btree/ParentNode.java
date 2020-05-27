package io.github.up2jakarta.dte.exe.api.btree;

import io.github.up2jakarta.dte.exe.api.Identifiable;
import io.github.up2jakarta.dte.exe.api.Negateable;
import io.github.up2jakarta.dte.exe.api.Operator;

/**
 * BTree Parent Node.
 *
 * @see MixedDecider
 */
public interface ParentNode extends Negateable, Identifiable<Long> {

    /**
     * @return the boolean operator
     */
    Operator getOperator();

}
