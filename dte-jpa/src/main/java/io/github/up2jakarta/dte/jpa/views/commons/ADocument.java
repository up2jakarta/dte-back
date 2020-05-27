package io.github.up2jakarta.dte.jpa.views.commons;

import org.hibernate.annotations.Type;

import javax.persistence.Basic;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Abstract shareable document view.
 *
 * @param <T> the type of identifier.
 */
@MappedSuperclass
@SuppressWarnings("unused")
public abstract class ADocument<T extends Serializable> implements Serializable {

    private String label;
    private Integer groupId;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Type(type = "org.hibernate.type.TextType")
    private String description;

    /**
     * @return the display label.
     */
    public String getLabel() {
        return label;
    }

    /**
     * @return the description or documentation.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the direct group identifier.
     */
    public Integer getGroupId() {
        return groupId;
    }

    /**
     * Return the unique identifier of the script.
     *
     * @return the unique identifier.
     */
    public abstract T getId();
}
