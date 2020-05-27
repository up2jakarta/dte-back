package io.github.up2jakarta.dte.jpa.views.commons;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract Computer view.
 */
@MappedSuperclass
@AttributeOverrides({
        @AttributeOverride(name = "shared", column = @Column(name = "DTN_SHARED")),
        @AttributeOverride(name = "label", column = @Column(name = "DTN_LABEL")),
        @AttributeOverride(name = "description", column = @Column(name = "DTN_DESC")),
        @AttributeOverride(name = "groupId", column = @Column(name = "DTN_GRP_ID"))
})
public abstract class AComputer extends ACalculator<Long> {

    @Id
    @Column(name = "DTN_ID")
    private Long id;

    @ElementCollection
    @CollectionTable(name = "DTE_DIP", joinColumns = @JoinColumn(name = "DIP_DTN_ID"))
    @MapKeyColumn(name = "DIP_NAME")
    @Column(name = "DIP_TPE_ID")
    private Map<String, Integer> inputs = new HashMap<>();

    @ElementCollection
    @CollectionTable(name = "DTE_DOP", joinColumns = @JoinColumn(name = "DOP_DTN_ID"))
    @MapKeyColumn(name = "DOP_NAME")
    @Column(name = "DOP_TPE_ID")
    private Map<String, Integer> outputs = new HashMap<>();

    @ElementCollection
    @CollectionTable(name = "DTE_DIP", joinColumns = @JoinColumn(name = "DIP_DTN_ID"))
    @Column(name = "DIP_NAME")
    @Deprecated
    private List<String> inputsFilter;

    @ElementCollection
    @CollectionTable(name = "DTE_DOP", joinColumns = @JoinColumn(name = "DOP_DTN_ID"))
    @Column(name = "DOP_NAME")
    @Deprecated
    private List<String> outputsFilter;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Map<String, Integer> getInputs() {
        return inputs;
    }

    /**
     * @return the related output names
     */
    public Map<String, Integer> getOutputs() {
        return outputs;
    }
}
