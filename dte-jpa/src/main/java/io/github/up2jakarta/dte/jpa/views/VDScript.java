package io.github.up2jakarta.dte.jpa.views;

import io.github.up2jakarta.dte.jpa.views.commons.ADecider;

import javax.persistence.*;
import java.util.Set;

/**
 * Binary tree script view.
 */
@Entity
@org.hibernate.annotations.Immutable
@org.hibernate.annotations.Subselect("(SELECT * FROM DTE_JPA_BTN WHERE BTN_TYPE IN ('S', 'T', 'B'))")
@org.hibernate.annotations.Synchronize({"DTE_DTN", "DTE_BTN", "DTE_GRP"})
public class VDScript extends ADecider {

    @Column(name = "BTN_TPL_ID")
    private Integer templateId;

    @Lob
    @Column(name = "BTN_SCRIPT")
    @Basic(fetch = FetchType.EAGER)
    @org.hibernate.annotations.Type(type = "org.hibernate.type.TextType")
    private String script;

    @ElementCollection
    @CollectionTable(name = "DTE_JPA_GRP",
            joinColumns = @JoinColumn(name = "GRP_PID", referencedColumnName = "BTN_GRP_ID")
    )
    @Column(name = "GRP_DID")
    @Deprecated
    private Set<Integer> groupsFilter;

    /**
     * @return the script source code.
     */
    public String getScript() {
        return script;
    }

    /**
     * @return the script template identifier
     */
    public Integer getTemplateId() {
        return templateId;
    }

}
