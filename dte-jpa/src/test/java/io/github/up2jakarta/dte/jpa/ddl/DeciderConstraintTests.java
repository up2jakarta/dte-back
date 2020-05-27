package io.github.up2jakarta.dte.jpa.ddl;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import io.github.up2jakarta.dte.jpa.TestConfiguration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class, inheritLocations = false)
@Transactional
@TestPropertySource(properties = {
        "spring.datasource.data=classpath:groups.sql, classpath:deciders.sql",
        "spring.datasource.tomcat.url=jdbc:h2:mem:deciders_check;MODE=PostgreSQL;"
})
public class DeciderConstraintTests {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void shouldCheckType() {
        assertException("UPDATE dte_btn SET BTN_type = 'X'", "BTN_TYPE_CC");
    }

    @Test
    public void shouldCheckOperator() {
        assertException("UPDATE dte_btn SET BTN_operator = '?'", "BTN_OPERATOR_CC");
    }

    @Test
    public void shouldCheckTreeOperator() {
        assertException("UPDATE dte_btn SET BTN_operator = null WHERE BTN_type IN ('C', 'P')", "BTN_OPERATOR_CC");
    }

    @Test
    public void shouldCheckTreeOperatorBis() {
        assertException("UPDATE dte_btn SET BTN_operator = 'A' WHERE BTN_type NOT IN ('C', 'P')", "BTN_OPERATOR_CC");
    }

    @Test
    public void shouldCheckTreeParent() {
        assertException("UPDATE dte_btn SET BTN_parent_id = null WHERE BTN_type IN ('L', 'P')", "BTN_PARENT_CC");
    }

    @Test
    public void shouldCheckTreeParentBis() {
        assertException("UPDATE dte_btn SET BTN_parent_id = 10 WHERE BTN_type NOT IN ('L', 'P')", "BTN_PARENT_CC");
    }

    @Test
    public void shouldCheckTreeRoot() {
        assertException("UPDATE dte_btn SET BTN_root_id = null WHERE BTN_type IN ('L', 'P')", "BTN_ROOT_CC");
    }

    @Test
    public void shouldCheckTreeRootBis() {
        assertException("UPDATE dte_btn SET BTN_root_id = 10 WHERE BTN_type NOT IN ('L', 'P')", "BTN_ROOT_CC");
    }

    @Test
    public void shouldCheckTreeScript() {
        assertException("UPDATE dte_btn SET BTN_script = null WHERE BTN_type = 'S'", "BTN_SCRIPT_CC");
    }

    @Test
    public void shouldCheckTreeScriptBis() {
        assertException("UPDATE dte_btn SET BTN_script = 'X' WHERE BTN_type <> 'S'", "BTN_SCRIPT_CC");
    }

    @Test
    public void shouldCheckTreeGroup() {
        assertException("UPDATE dte_btn SET BTN_GRP_id = null WHERE BTN_type IN ('C', 'S')", "BTN_GRP_CC");
    }

    @Test
    public void shouldCheckTreeGroupBis() {
        assertException("UPDATE dte_btn SET BTN_GRP_id = 1 WHERE BTN_type NOT IN ('C', 'S')", "BTN_GRP_CC");
    }

    @Test
    public void shouldCheckTreeDecider() {
        assertException("UPDATE dte_btn SET BTN_BTN_ID = null WHERE BTN_type = 'L'", "BTN_BTN_CC");
    }

    @Test
    public void shouldCheckTreeDeciderBis() {
        assertException("UPDATE dte_btn SET BTN_BTN_ID = 10 WHERE BTN_type <> 'L'", "BTN_BTN_CC");
    }

    @Test
    public void shouldCheckTreeOrder() {
        assertException("UPDATE dte_btn SET BTN_order = null WHERE BTN_type NOT IN ('C', 'S')", "BTN_ORDER_CC");
    }

    @Test
    public void shouldCheckFinalOrder() {
        assertException("UPDATE dte_btn SET BTN_order = 1000 WHERE BTN_type IN ('C', 'S')", "BTN_ORDER_CC");
    }

    @Test
    public void shouldCheckNegativeOrder() {
        assertException("UPDATE dte_btn SET BTN_order = BTN_order * -1 WHERE BTN_type NOT IN ('S', 'C', 'R')", "BTN_ORDER_CC");
    }

    @Test
    public void shouldCheckTreeDepthSimple() {
        assertException("UPDATE dte_btn SET BTN_depth = 1 WHERE BTN_type = 'S'", "BTN_DEPTH_CC");
    }

    @Test
    public void shouldCheckTreeDepthComplex() {
        assertException("UPDATE dte_btn SET BTN_depth = 1 WHERE BTN_type = 'C'", "BTN_DEPTH_CC");
    }

    @Test
    public void shouldCheckTreeDepthOther() {
        assertException("UPDATE dte_btn SET BTN_depth = BTN_depth * -1 WHERE BTN_type NOT IN ('C', 'S')", "BTN_DEPTH_CC");
    }

    @Test
    public void shouldClearDescription() {
        int n = entityManager.createNativeQuery("UPDATE dte_btn SET BTN_desc = null WHERE BTN_type IN ('C', 'S')")
                .executeUpdate();
        assertThat(n).isNotEqualTo(0);
    }

    @Test
    public void shouldSetDescription() {
        int n = entityManager.createNativeQuery("UPDATE dte_btn SET BTN_desc = BTN_label WHERE BTN_type IN ('C', 'S')")
                .executeUpdate();
        assertThat(n).isNotEqualTo(0);
    }

    private void assertException(String sqlUpdate, String constraintName) {
        Throwable ex = null;
        try {
            entityManager.createNativeQuery(sqlUpdate).executeUpdate();
        } catch (Throwable t) {
            ex = t;
        }
        assertThat(ex)
                .isNotNull()
                .isInstanceOf(PersistenceException.class)
                .extracting(Throwable::getCause)
                .isInstanceOf(ConstraintViolationException.class);
        assertThat(((ConstraintViolationException) ex.getCause()).getConstraintName())
                .containsIgnoringCase(constraintName)
        ;
    }

}
