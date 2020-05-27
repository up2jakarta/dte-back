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
        "spring.datasource.data=classpath:groups.sql, classpath:computers.sql",
        "spring.datasource.tomcat.url=jdbc:h2:mem:computers_check;MODE=PostgreSQL;"
})
public class ComputerConstraintTests {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void shouldCheckType() {
        assertException("UPDATE dte_dtn SET DTN_type = 'X'", "DTN_TYPE_CC");
    }

    @Test
    public void shouldCheckTreeNegated() {
        assertException("UPDATE dte_dtn SET DTN_negated = null WHERE DTN_type = 'D'", "DTN_NEGATED_CC");
    }

    @Test
    public void shouldCheckTreeNegatedBis() {
        assertException("UPDATE dte_dtn SET DTN_negated = true WHERE DTN_type <> 'D'", "DTN_NEGATED_CC");
    }

    @Test
    public void shouldCheckTreeParent() {
        assertException("UPDATE dte_dtn SET DTN_parent_id = null WHERE DTN_type NOT IN ('S', 'C')", "DTN_PARENT_CC");
    }

    @Test
    public void shouldCheckTreeParentBis() {
        assertException("UPDATE dte_dtn SET DTN_parent_id = 10 WHERE DTN_type IN ('S', 'C')", "DTN_PARENT_CC");
    }

    @Test
    public void shouldCheckTreeRoot() {
        assertException("UPDATE dte_dtn SET DTN_root_id = null WHERE DTN_type NOT IN ('S', 'C')", "DTN_ROOT_CC");
    }

    @Test
    public void shouldCheckTreeRootBis() {
        assertException("UPDATE dte_dtn SET DTN_root_id = 10 WHERE DTN_type IN ('S', 'C')", "DTN_ROOT_CC");
    }

    @Test
    public void shouldCheckTreeScript() {
        assertException("UPDATE dte_dtn SET DTN_script = null WHERE DTN_type = 'S'", "DTN_SCRIPT_CC");
    }

    @Test
    public void shouldCheckTreeScriptBis() {
        assertException("UPDATE dte_dtn SET DTN_script = 'X' WHERE DTN_type <> 'S'", "DTN_SCRIPT_CC");
    }

    @Test
    public void shouldCheckTreeGroup() {
        assertException("UPDATE dte_dtn SET DTN_GRP_id = null WHERE DTN_type IN ('S', 'C')", "DTN_GRP_CC");
    }

    @Test
    public void shouldCheckTreeGroupBis() {
        assertException("UPDATE dte_dtn SET DTN_GRP_id = 1 WHERE DTN_type NOT IN ('S', 'C', 'T', 'B')", "DTN_GRP_CC");
    }

    @Test
    public void shouldCheckTreeDecider() {
        assertException("UPDATE dte_dtn SET DTN_BTN_ID = null WHERE DTN_type = 'D'", "DTN_BTN_CC");
    }

    @Test
    public void shouldCheckTreeDeciderBis() {
        assertException("UPDATE dte_dtn SET DTN_BTN_ID = 102 WHERE DTN_type <> 'D'", "DTN_BTN_CC");
    }

    @Test
    public void shouldCheckTreeComputer() {
        assertException("UPDATE dte_dtn SET DTN_dtn_id = null WHERE DTN_type = 'R'", "DTN_DTN_CC");
    }

    @Test
    public void shouldCheckTreeComputerBis() {
        assertException("UPDATE dte_dtn SET DTN_dtn_id = 10 WHERE DTN_type <> 'R'", "DTN_DTN_CC");
    }

    @Test
    public void shouldCheckRuleOrder() {
        assertException("UPDATE dte_dtn SET DTN_order = null WHERE DTN_type NOT IN ('C', 'S')", "DTN_ORDER_CC");
    }

    @Test
    public void shouldCheckFinalOrder() {
        assertException("UPDATE dte_dtn SET DTN_order = 1200 WHERE DTN_type IN ('C', 'S')", "DTN_ORDER_CC");
    }

    @Test
    public void shouldCheckNegativeOrder() {
        assertException("UPDATE dte_dtn SET DTN_order = DTN_order * -1 WHERE DTN_type NOT IN ('S', 'C', 'R')", "DTN_ORDER_CC");
    }

    @Test
    public void shouldCheckTreeDepthSimple() {
        assertException("UPDATE dte_dtn SET DTN_depth = 1 WHERE DTN_type = 'S'", "DTN_DEPTH_CC");
    }

    @Test
    public void shouldCheckTreeDepthComplex() {
        assertException("UPDATE dte_dtn SET DTN_depth = 1 WHERE DTN_type = 'C'", "DTN_DEPTH_CC");
    }

    @Test
    public void shouldCheckTreeDepthOther() {
        assertException("UPDATE dte_dtn SET DTN_depth = DTN_depth * -1 WHERE DTN_type NOT IN ('C', 'S')", "DTN_DEPTH_CC");
    }

    @Test
    public void shouldClearDescription() {
        int n = entityManager.createNativeQuery("UPDATE dte_dtn SET DTN_desc = null")
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
