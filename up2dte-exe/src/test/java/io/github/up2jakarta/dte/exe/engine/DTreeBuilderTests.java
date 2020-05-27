package io.github.up2jakarta.dte.exe.engine;

import org.junit.Test;
import org.mockito.Mockito;
import io.github.up2jakarta.dte.dsl.BaseScript;
import io.github.up2jakarta.dte.exe.StaticEngine;
import io.github.up2jakarta.dte.exe.engine.dtree.Rule;

import static org.assertj.core.api.Assertions.assertThat;

public class DTreeBuilderTests {

    private static final StaticEngine ENGINE = Mockito.mock(StaticEngine.class);

    @Test
    public void shouldEmpty() {
        // Given
        final String name = "Test";

        // When
        final DTree calc = new DTree.Builder(name).build();

        // Then
        assertThat(calc)
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", name)
                .hasFieldOrPropertyWithValue("children.size", 0);
    }

    @Test
    public void shouldThenCalculation() {
        // Given
        final String name = "Test";

        // When
        final DTree calc = new DTree.Builder(name)
                .when(1, "D1", false)
                .then(Calculation.of(BaseScript.class, 1, "C1"))
                .end()
                .then(Calculation.of(BaseScript.class, 1, "C1"))
                .build();

        // Then
        assertThat(calc)
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", name)
                .hasFieldOrPropertyWithValue("children.size", 1);
    }

    @Test
    public void shouldThenWhenDecision() {
        // Given
        final String name = "Test";

        // When
        final DTree calc = new DTree.Builder(name)
                .then(Calculation.of(BaseScript.class, 1, "C1"))
                .when(Decision.of(BaseScript.class, 1, "D1"))
                .then(Calculation.of(BaseScript.class, 1, "C1"))
                .when(Decision.of(BaseScript.class, 1, "D1"))
                .end()
                .then(Calculation.of(BaseScript.class, 1, "C1"))
                .build();

        // Then
        assertThat(calc)
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", name)
                .hasFieldOrPropertyWithValue("children.size", 1);
    }

    @Test
    public void shouldEmptyWithEmptyDecisions() {
        // Given
        final String name = "Test";

        // When
        final DTree calc = new DTree.Builder(name)
                .when(1, "D1", false)
                .when(2, "D2", true)
                .end()
                .otherwise()
                .build();

        // Then
        assertThat(calc)
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", name)
                .hasFieldOrPropertyWithValue("children.size", 0);
    }

    @Test
    public void shouldBeHierarchical() {
        // When
        final DTree calc = new DTree.Builder("KEYS")
                .when(1001, "1")
                .when(1002, "2", true)
                .when(1003, "3", true)
                .then(1004, "4").end()
                .when(1005, "3", true)
                .then(1006, "4").end()
                .otherwise()
                .then(1007, "4").end()
                .end()
                .end()
                .when(1008, "3", true)
                .then(1009, "4").end()
                .otherwise()
                .then(1010, "4").end()
                .build();

        // Then
        assertThat(calc)
                .isNotNull()
                .hasFieldOrPropertyWithValue("children.size", 3);

        assertThat(calc)
                .extracting(t -> t.childAt(0))
                .isNotNull()
                .hasFieldOrPropertyWithValue("children.size", 1)
                .extracting(t -> t.childAt(0))
                .isNotNull()
                .hasFieldOrPropertyWithValue("children.size", 3)
                .extracting(t -> t.childAt(0))
                .isNotNull()
                .hasFieldOrPropertyWithValue("children.size", 1)
                .extracting(t -> t.childAt(0))
                .isNotNull()
                .hasFieldOrPropertyWithValue("children.size", 0);

        assertThat(calc)
                .extracting(t -> t.childAt(1))
                .isNotNull()
                .hasFieldOrPropertyWithValue("children.size", 1)
                .extracting(t -> t.childAt(0))
                .isNotNull()
                .hasFieldOrPropertyWithValue("children.size", 0);

        assertThat(calc)
                .extracting(t -> t.childAt(2))
                .isNotNull()
                .hasFieldOrPropertyWithValue("children.size", 1)
                .extracting(t -> t.childAt(0))
                .isNotNull()
                .hasFieldOrPropertyWithValue("children.size", 0);
    }

    @Test
    public void shouldDistinctKeys() {
        // Given

        // When
        final DTree calc = new DTree.Builder("KEYS")
                .when(2000, "D")
                .when(2001, "B1", false)
                .when(2002, "B2", true)
                .then(2003, "C")
                .end()
                .end()
                //.end()
                .build();

        // Then
        assertThat(calc).isNotNull()
                .hasFieldOrPropertyWithValue("children.size", 1)

                .extracting(t -> t.childAt(0))
                .isNotNull()
                .hasFieldOrProperty("condition.id")
                .hasFieldOrPropertyWithValue("condition.script", "D")
                .hasFieldOrPropertyWithValue("children.size", 1)

                .extracting(t -> t.childAt(0))
                .isNotNull()
                .hasFieldOrProperty("condition.id")
                .hasFieldOrPropertyWithValue("condition.script", "B1")
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("children.size", 1)

                .extracting(t -> t.childAt(0))
                .isNotNull()
                .hasFieldOrProperty("condition.id")
                .hasFieldOrPropertyWithValue("condition.script", "B2")
                .hasFieldOrPropertyWithValue("negated", true)
                .hasFieldOrPropertyWithValue("children.size", 1)

                .extracting(t -> t.childAt(0))
                .isNotNull()
                .hasFieldOrProperty("rule.id")
                .hasFieldOrPropertyWithValue("rule.script", "C")
                .hasFieldOrPropertyWithValue("children.size", 0)

        ;
    }

    @Test
    public void shouldCreateLazy() {
        // When
        final DTree tree = new DTree.Builder("Lazy")
                .then(ENGINE, 5000L)
                .build();

        // Then
        assertThat(tree).isNotNull()
                .hasFieldOrPropertyWithValue("children.size", 1)
                .hasFieldOrPropertyWithValue("name", "Lazy")
                .extracting(t -> t.childAt(0))
                .isInstanceOf(Rule.class)
                .hasFieldOrPropertyWithValue("rule.calleeId", 5000L)
                .hasFieldOrPropertyWithValue("rule.engine", ENGINE);
    }

    @Test
    public void shouldClean() {
        // Given
        final String name = "Test";

        // When
        final DTree calc = new DTree.Builder(name)
                .when(3001, "D1", false)
                .when(3002, "D2", true).end()
                .end()
                .otherwise()
                .then(3003, "T1")
                .then(3004, "T2")
                .end()
                .build();

        // Then
        assertThat(calc)
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", name)
                .hasFieldOrPropertyWithValue("children.size", 2);

        assertThat(calc)
                .extracting(t -> t.childAt(0))
                .isNotNull()
                .hasFieldOrProperty("rule.id")
                .hasFieldOrPropertyWithValue("rule.script", "T1")
                .hasFieldOrPropertyWithValue("children.size", 0);

        assertThat(calc)
                .extracting(t -> t.childAt(1))
                .isNotNull()
                .hasFieldOrProperty("rule.id")
                .hasFieldOrPropertyWithValue("rule.script", "T2")
                .hasFieldOrPropertyWithValue("children.size", 0);
    }

    @Test

    public void shouldRules() {
        //Given
        final String name = "Test";

        // When
        final DTree calc = new DTree.Builder(name)
                .then(4001, "T1")
                .then(4002, "T2")
                .build();

        // Then
        assertThat(calc)
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", name)
                .hasFieldOrPropertyWithValue("children.size", 2);

        assertThat(calc)
                .extracting(t -> t.childAt(0))
                .isNotNull()
                .hasFieldOrProperty("rule.id")
                .hasFieldOrPropertyWithValue("rule.script", "T1")
                .hasFieldOrPropertyWithValue("children.size", 0);

        assertThat(calc)
                .extracting(t -> t.childAt(1))
                .isNotNull()
                .hasFieldOrProperty("rule.id")
                .hasFieldOrPropertyWithValue("rule.script", "T2")
                .hasFieldOrPropertyWithValue("children.size", 0);
    }

}
