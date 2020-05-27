package io.github.up2jakarta.dte.jpa.views;

import io.github.up2jakarta.dte.jpa.views.commons.AComputer;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Computer view.
 */
@Entity
@org.hibernate.annotations.Immutable
@org.hibernate.annotations.Subselect("(SELECT * FROM DTE_DTN WHERE DTN_TYPE IN ('C', 'S'))")
@org.hibernate.annotations.Synchronize({"DTE_DTN", "DTE_GRP"})
public class VComputer extends AComputer {

    @Column(name = "DTN_TYPE")
    private Character type;

    /**
     * @return the computer type.
     */
    public Character getType() {
        return type;
    }
}
