package io.github.up2jakarta.dte.web.models;

/**
 * API Item model.
 *
 * @param <T> the type of identifier
 */
public class ItemModel<T extends Comparable<T>> {

    private final Comparable<T> id;
    private final String label;

    /**
     * Pubmic constructor for ItemModel.
     *
     * @param id    the item identifier
     * @param label the item label
     */
    public ItemModel(final Comparable<T> id, final String label) {
        this.id = id;
        this.label = label;
    }

    /**
     * @return the item identifier.
     */
    public Comparable<T> getId() {
        return id;
    }

    /**
     * @return the item label.
     */
    public String getLabel() {
        return label;
    }
}
