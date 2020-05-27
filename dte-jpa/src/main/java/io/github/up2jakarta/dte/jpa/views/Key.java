package io.github.up2jakarta.dte.jpa.views;


import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

/**
 * Decider view identifier.
 */
@Embeddable
@SuppressWarnings("unused")
public class Key implements Comparable<Key>, Serializable {

    @Column(name = "BTN_ID")
    private Long id;

    @Column(name = "BTN_TYPE")
    private Character type;

    /**
     * JPA default constructor for Key.
     */
    protected Key() {
    }

    /**
     * Public constructor for Key.
     *
     * @param id   the decider identifier
     * @param type the decider type
     */
    public Key(final long id, final char type) {
        this.id = id;
        this.type = type;
    }

    /**
     * @return the decider identifier
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the decider type
     */
    public Character getType() {
        return type;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        final Key key = (Key) other;
        return id.equals(key.id) && type.equals(key.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }

    @Override
    @SuppressWarnings("ALL")
    public int compareTo(final Key other) {
        return Comparator.comparing(Key::getType)
                .thenComparingLong(Key::getId)
                .compare(this, other);
    }
}
