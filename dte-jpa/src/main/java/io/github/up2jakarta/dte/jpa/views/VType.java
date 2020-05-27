package io.github.up2jakarta.dte.jpa.views;

import io.github.up2jakarta.dte.jpa.views.commons.ADocument;

import javax.persistence.*;
import java.util.Set;

/**
 * Type script view.
 */
@Entity
@org.hibernate.annotations.Immutable
@org.hibernate.annotations.Subselect("(SELECT * FROM DTE_TPE)")
@org.hibernate.annotations.Synchronize({"DTE_TPE", "DTE_GRP"})
@AttributeOverrides({
        @AttributeOverride(name = "label", column = @Column(name = "TPE_LABEL")),
        @AttributeOverride(name = "description", column = @Column(name = "TPE_DESC")),
        @AttributeOverride(name = "groupId", column = @Column(name = "TPE_GRP_ID"))
})
public class VType extends ADocument<Integer> {

    @Id
    @Column(name = "TPE_ID", nullable = false)
    private Integer id;

    @Column(name = "TPE_NAME", length = 127, nullable = false)
    private String name;

    @ElementCollection
    @CollectionTable(name = "DTE_JPA_GRP",
            joinColumns = @JoinColumn(name = "GRP_DID", referencedColumnName = "TPE_GRP_ID")
    )
    @Column(name = "GRP_PID")
    @Deprecated
    private Set<Integer> groupsFilter;

    /**
     * @return the java class name.
     */
    public String getName() {
        return name;
    }

    @Override
    public Integer getId() {
        return id;
    }
}
