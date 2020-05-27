package io.github.up2jakarta.dte.jpa;

import io.github.up2jakarta.dte.jpa.entities.Template;
import io.github.up2jakarta.dte.jpa.entities.btn.BTreeNode;
import io.github.up2jakarta.dte.jpa.entities.dtn.DTreeNode;
import io.github.up2jakarta.dte.jpa.entities.grp.AbstractGroup;
import io.github.up2jakarta.dte.jpa.entities.grp.MainGroup;
import io.github.up2jakarta.dte.jpa.entities.grp.Workspace;
import io.github.up2jakarta.dte.jpa.entities.tpl.BasicTemplate;
import io.github.up2jakarta.dte.jpa.models.Computer;
import io.github.up2jakarta.dte.jpa.models.Decider;
import io.github.up2jakarta.dte.jpa.validators.BinaryTree;
import io.github.up2jakarta.dte.jpa.validators.DecisionTree;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class TestUtil {

    @SuppressWarnings("deprecation")
    public static final MainGroup MAIN_GROUP = new MainGroup();
    public static final Workspace BTN_SPACE = new Workspace(MAIN_GROUP);
    public static final Workspace DTN_SPACE = new Workspace(MAIN_GROUP);
    public static final BasicTemplate BASE_TEMPLATE = new BasicTemplate(MAIN_GROUP);

    static {
        MAIN_GROUP.setLabel("DTE");
        MAIN_GROUP.setDescription("Decision tree engine");
        MAIN_GROUP.setIcon("logo");
        MAIN_GROUP.setColor("blue");
        setId(MAIN_GROUP, 0);

        BTN_SPACE.setLabel("BTN");
        BTN_SPACE.setDescription("Binary trees");
        BTN_SPACE.setIcon("BTN");
        BTN_SPACE.setColor("green");
        setId(BTN_SPACE, 1);

        DTN_SPACE.setLabel("DTN");
        DTN_SPACE.setDescription("Decision trees");
        DTN_SPACE.setIcon("DTN");
        DTN_SPACE.setColor("red");
        setId(DTN_SPACE, 2);

        setId(BASE_TEMPLATE, 0);
    }

    private TestUtil() {
    }

    public static void setId(final DTreeNode entity, long id) {
        setId0(entity, id);
    }

    public static void setId(final BTreeNode entity, long id) {
        setId0(entity, id);
    }

    public static void setId(final AbstractGroup entity, int id) {
        setId0(entity, id);
    }

    public static void setId(final Template entity, int id) {
        setId0(entity, id);
    }

    public static <T> T last(final Iterable<T> iterable) {
        T last = null;
        for (final T element : iterable) {
            last = element;
        }
        return last;
    }

    private static void setId0(final Object entity, Number id) {
        Field idField = null;
        Class cls = entity.getClass();
        while (cls != null) {
            try {
                final Field field = cls.getDeclaredField("id");
                if (!Modifier.isPublic(field.getModifiers())) {
                    field.setAccessible(true);
                }
                idField = field;
                break;
            } catch (NoSuchFieldException var8) {
                cls = cls.getSuperclass();
            }
        }
        if (idField == null) {
            throw new IllegalStateException("Cannot find the ID field for: " + entity.getClass());
        }
        try {
            idField.set(entity, id);
        } catch (IllegalAccessException var8) {
            throw new IllegalStateException("Cannot set the ID field for: " + entity.getClass());
        }
    }

    public static class BTreeWrapper {

        @BinaryTree
        private final Decider tree;

        public BTreeWrapper(final Decider tree) {
            this.tree = tree;
        }
    }

    public static class DTreeWrapper {

        @DecisionTree
        private final Computer tree;

        public DTreeWrapper(final Computer tree) {
            this.tree = tree;
        }
    }
}
