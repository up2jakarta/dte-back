package io.github.up2jakarta.dte.jpa.views;

import io.github.up2jakarta.dte.jpa.views.commons.ADecider;

import javax.persistence.Entity;

/**
 * Decider view.
 */
@Entity
@org.hibernate.annotations.Immutable
@org.hibernate.annotations.Subselect("(SELECT * FROM DTE_BTN WHERE BTN_TYPE IN ('C', 'S'))")
@org.hibernate.annotations.Synchronize({"DTE_BTN", "DTE_GRP"})
public class VDecider extends ADecider {
}
