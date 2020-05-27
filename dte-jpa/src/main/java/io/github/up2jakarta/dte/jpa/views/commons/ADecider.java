package io.github.up2jakarta.dte.jpa.views.commons;

import io.github.up2jakarta.dte.jpa.views.Key;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract Decider view.
 */
@MappedSuperclass
@AttributeOverrides({
        @AttributeOverride(name = "shared", column = @Column(name = "BTN_SHARED")),
        @AttributeOverride(name = "label", column = @Column(name = "BTN_LABEL")),
        @AttributeOverride(name = "description", column = @Column(name = "BTN_DESC")),
        @AttributeOverride(name = "groupId", column = @Column(name = "BTN_GRP_ID"))
})
public abstract class ADecider extends ACalculator<Key> {

    @EmbeddedId
    private Key id;

    @Column(name = "BTN_NEGATED", nullable = false)
    private Boolean negated;

    @ElementCollection
    @CollectionTable(name = "DTE_JPA_BIP", joinColumns = {
            @JoinColumn(name = "BIP_BTN_ID", referencedColumnName = "BTN_ID"),
            @JoinColumn(name = "BIP_TYPE", referencedColumnName = "BTN_TYPE")
    })
    @MapKeyColumn(name = "BIP_NAME")
    @Column(name = "BIP_TPE_ID")
    private Map<String, Integer> inputs = new HashMap<>();

    @ElementCollection
    @CollectionTable(name = "DTE_JPA_BIP", joinColumns = {
            @JoinColumn(name = "BIP_BTN_ID", referencedColumnName = "BTN_ID"),
            @JoinColumn(name = "BIP_TYPE", referencedColumnName = "BTN_TYPE")
    })
    @Column(name = "BIP_NAME")
    @Deprecated
    private List<String> inputsFilter;

    @Override
    public Key getId() {
        return id;
    }

    /**
     * Return a flag that tells the engine to negate the result of script after evaluation.
     *
     * @return the indicator flag
     */
    public Boolean getNegated() {
        return negated;
    }

    @Override
    public Map<String, Integer> getInputs() {
        return inputs;
    }
}
