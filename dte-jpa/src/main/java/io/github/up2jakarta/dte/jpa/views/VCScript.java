package io.github.up2jakarta.dte.jpa.views;

import io.github.up2jakarta.dte.jpa.views.commons.AComputer;

import javax.persistence.*;
import java.util.Set;

/**
 * Computer script view.
 */
@Entity
@org.hibernate.annotations.Immutable
@org.hibernate.annotations.Subselect("(SELECT * FROM DTE_DTN WHERE DTN_TYPE IN ('S', 'T'))")
@org.hibernate.annotations.Synchronize({"DTE_DTN", "DTE_GRP"})
public class VCScript extends AComputer {

    @Column(name = "DTN_TPL_ID")
    private Integer templateId;

    @Lob
    @Column(name = "DTN_SCRIPT")
    @Basic(fetch = FetchType.EAGER)
    @org.hibernate.annotations.Type(type = "org.hibernate.type.TextType")
    private String script;

    @ElementCollection
    @CollectionTable(name = "DTE_JPA_GRP",
            joinColumns = @JoinColumn(name = "GRP_PID", referencedColumnName = "DTN_GRP_ID")
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
