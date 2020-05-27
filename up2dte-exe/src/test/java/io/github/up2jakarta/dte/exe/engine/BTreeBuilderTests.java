package io.github.up2jakarta.dte.exe.engine;

import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.ObjectAssert;
import org.junit.Test;
import org.mockito.Mockito;
import io.github.up2jakarta.dte.dsl.BaseScript;
import io.github.up2jakarta.dte.exe.StaticEngine;
import io.github.up2jakarta.dte.exe.api.Operator;
import io.github.up2jakarta.dte.exe.engine.btree.Leaf;
import io.github.up2jakarta.dte.exe.engine.btree.Node;
import io.github.up2jakarta.dte.exe.engine.btree.Operand;

import static org.assertj.core.api.Assertions.assertThat;
import static io.github.up2jakarta.dte.exe.engine.Condition.of;

public class BTreeBuilderTests {

    private static final StaticEngine ENGINE = Mockito.mock(StaticEngine.class);

    @Test
    public void shouldCreateScript() {
        // Given
        final String script = "test";
        final String name = "Test";

        // When
        final BTree tree = new BTree.Builder(5000, script).build(name);

        // Then
        assertThat(tree).isNotNull()
                .hasFieldOrPropertyWithValue("name", name)
                .extracting(t -> t.childAt(0))
                .isInstanceOf(Leaf.class)
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("condition.script", script);
    }

    @Test
    public void shouldCreateLazy() {
        // When
        final String name = "Test";
        final BTree tree = new BTree.Builder(ENGINE, 5000).build(name);

        // Then
        tree.negate();
        assertThat(tree).isNotNull()
                .hasFieldOrPropertyWithValue("negated", true)
                .hasFieldOrPropertyWithValue("name", name)
                .extracting(t -> t.childAt(0))
                .isInstanceOf(Leaf.class)
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("condition.calleeId", 5000L)
                .hasFieldOrPropertyWithValue("condition.engine", ENGINE);
    }

    @Test
    public void shouldCreateCondition() {
        // Given
        final String script = "test";

        // When
        final BTree tree = new BTree.Builder(of(BaseScript.class, 5001, script)).build("");

        // Then
        assertThat(tree).isNotNull()
                .extracting(t -> t.childAt(0))
                .isInstanceOf(Leaf.class)
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("condition.script", script);
    }

    // Negation

    @Test
    public void shouldNegate() {
        // Given
        final String script = "test";

        // When
        final BTree tree = new BTree.Builder(5002, script).not().build("");

        // Then
        assertThat(tree).isNotNull()
                .extracting(t -> t.childAt(0))
                .isInstanceOf(Leaf.class)
                .hasFieldOrPropertyWithValue("negated", true)
                .hasFieldOrPropertyWithValue("condition.script", script);
    }

    @Test
    public void shouldNotNegate() {
        // Given
        final String script = "test";

        // When
        final BTree tree = new BTree.Builder(5003, script).not().not().build("");

        // Then
        assertThat(tree).isNotNull()
                .extracting(t -> t.childAt(0))
                .isInstanceOf(Leaf.class)
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("condition.script", script);
    }

    @Test
    public void shouldNegateOperand() {
        // Given
        final BTree tree = new BTree.Builder(5004, "a").and(5005, "b").not().build("");

        // Then
        assertThat(tree).isNotNull()
                .hasFieldOrPropertyWithValue("negated", true)
                .hasFieldOrPropertyWithValue("operator", Operator.AND)
                .hasFieldOrPropertyWithValue("operands.size", 2);
    }

    @Test
    public void shouldNotNegateOperand() {
        // Given
        final BTree tree = new BTree.Builder(5004, "a").or(5005, "b").not().not().build("");

        // Then
        assertThat(tree).isNotNull()
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("operator", Operator.OR)
                .hasFieldOrPropertyWithValue("operands.size", 2);
    }

    // Script joining

    @Test
    public void shouldAndScript() {
        // Given
        final BTree tree = new BTree.Builder(5004, "a").and(5005, "b").build("");

        // Then
        assertThat(tree).isNotNull()
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("operator", Operator.AND)
                .hasFieldOrPropertyWithValue("operands.size", 2);

        assertThat(tree).extracting(n -> n.childAt(0))
                .hasFieldOrPropertyWithValue("negated", false);
        assertThat(tree).extracting(n -> n.childAt(1))
                .hasFieldOrPropertyWithValue("negated", false);
    }

    @Test
    public void shouldAndNotScript() {
        // Given
        final BTree tree = new BTree.Builder(5004, "a").andNot(5005, "b").build("");

        // Then
        assertThat(tree).isNotNull()
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("operator", Operator.AND)
                .hasFieldOrPropertyWithValue("operands.size", 2);

        assertThat(tree).extracting(n -> n.childAt(0))
                .hasFieldOrPropertyWithValue("negated", false);
        assertThat(tree).extracting(n -> n.childAt(1))
                .hasFieldOrPropertyWithValue("negated", true);
    }

    @Test
    public void shouldOrScript() {
        // Given
        final BTree tree = new BTree.Builder(5004, "a").or(5005, "b").build("");

        // Then
        assertThat(tree).isNotNull()
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("operator", Operator.OR)
                .hasFieldOrPropertyWithValue("operands.size", 2);

        assertThat(tree).extracting(n -> n.childAt(0))
                .hasFieldOrPropertyWithValue("negated", false);
        assertThat(tree).extracting(n -> n.childAt(1))
                .hasFieldOrPropertyWithValue("negated", false);
    }

    @Test
    public void shouldOrNotScript() {
        // Given
        final BTree tree = new BTree.Builder(5004, "a").orNot(5005, "b").build("");

        // Then
        assertThat(tree).isNotNull()
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("operator", Operator.OR)
                .hasFieldOrPropertyWithValue("operands.size", 2);

        assertThat(tree).extracting(n -> n.childAt(0))
                .hasFieldOrPropertyWithValue("negated", false);
        assertThat(tree).extracting(n -> n.childAt(1))
                .hasFieldOrPropertyWithValue("negated", true);
    }

    @Test
    public void shouldXorScript() {
        // Given
        final BTree tree = new BTree.Builder(5004, "a").xor(5005, "b").build("");

        // Then
        assertThat(tree).isNotNull()
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("operator", Operator.XOR)
                .hasFieldOrPropertyWithValue("operands.size", 2);

        assertThat(tree).extracting(n -> n.childAt(0))
                .hasFieldOrPropertyWithValue("negated", false);
        assertThat(tree).extracting(n -> n.childAt(1))
                .hasFieldOrPropertyWithValue("negated", false);
    }

    @Test
    public void shouldXorNotScript() {
        // Given
        final BTree tree = new BTree.Builder(5004, "a").xorNot(5005, "b").build("");

        // Then
        assertThat(tree).isNotNull()
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("operator", Operator.XOR)
                .hasFieldOrPropertyWithValue("operands.size", 2);

        assertThat(tree).extracting(n -> n.childAt(0))
                .hasFieldOrPropertyWithValue("negated", false);
        assertThat(tree).extracting(n -> n.childAt(1))
                .hasFieldOrPropertyWithValue("negated", true);
    }

    // Lazy joining

    @Test
    public void shouldAndLazy() {
        // Given
        final BTree tree = new BTree.Builder(ENGINE, 5004).and(ENGINE, 5005).build("");

        // Then
        assertThat(tree).isNotNull()
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("operator", Operator.AND)
                .hasFieldOrPropertyWithValue("operands.size", 2);

        assertThat(tree).extracting(n -> n.childAt(0))
                .hasFieldOrPropertyWithValue("negated", false);
        assertThat(tree).extracting(n -> n.childAt(1))
                .hasFieldOrPropertyWithValue("negated", false);
    }

    @Test
    public void shouldAndNotLazy() {
        // Given
        final BTree tree = new BTree.Builder(ENGINE, 5004).andNot(ENGINE, 5005).build("");

        // Then
        assertThat(tree).isNotNull()
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("operator", Operator.AND)
                .hasFieldOrPropertyWithValue("operands.size", 2);

        assertThat(tree).extracting(n -> n.childAt(0))
                .hasFieldOrPropertyWithValue("negated", false);
        assertThat(tree).extracting(n -> n.childAt(1))
                .hasFieldOrPropertyWithValue("negated", true);
    }

    @Test
    public void shouldOrLazy() {
        // Given
        final BTree tree = new BTree.Builder(ENGINE, 5004).or(ENGINE, 5005).build("");

        // Then
        assertThat(tree).isNotNull()
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("operator", Operator.OR)
                .hasFieldOrPropertyWithValue("operands.size", 2);

        assertThat(tree).extracting(n -> n.childAt(0))
                .hasFieldOrPropertyWithValue("negated", false);
        assertThat(tree).extracting(n -> n.childAt(1))
                .hasFieldOrPropertyWithValue("negated", false);
    }

    @Test
    public void shouldOrNotLazy() {
        // Given
        final BTree tree = new BTree.Builder(ENGINE, 5004).orNot(ENGINE, 5005).build("");

        // Then
        assertThat(tree).isNotNull()
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("operator", Operator.OR)
                .hasFieldOrPropertyWithValue("operands.size", 2);

        assertThat(tree).extracting(n -> n.childAt(0))
                .hasFieldOrPropertyWithValue("negated", false);
        assertThat(tree).extracting(n -> n.childAt(1))
                .hasFieldOrPropertyWithValue("negated", true);
    }

    @Test
    public void shouldXorLazy() {
        // Given
        final BTree tree = new BTree.Builder(ENGINE, 5004).xor(ENGINE, 5005).build("");

        // Then
        assertThat(tree).isNotNull()
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("operator", Operator.XOR)
                .hasFieldOrPropertyWithValue("operands.size", 2);

        assertThat(tree).extracting(n -> n.childAt(0))
                .hasFieldOrPropertyWithValue("negated", false);
        assertThat(tree).extracting(n -> n.childAt(1))
                .hasFieldOrPropertyWithValue("negated", false);
    }

    @Test
    public void shouldXorNotLazy() {
        // Given
        final BTree tree = new BTree.Builder(ENGINE, 5004).xorNot(ENGINE, 5005).build("");

        // Then
        assertThat(tree).isNotNull()
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("operator", Operator.XOR)
                .hasFieldOrPropertyWithValue("operands.size", 2);

        assertThat(tree).extracting(n -> n.childAt(0))
                .hasFieldOrPropertyWithValue("negated", false);
        assertThat(tree).extracting(n -> n.childAt(1))
                .hasFieldOrPropertyWithValue("negated", true);
    }

    // Condition joining

    @Test
    public void shouldAndCondition() {
        // Given
        final BTree tree = new BTree.Builder(of(BaseScript.class, 5004, "a"))
                .and(of(BaseScript.class, 5005, "b")).build("");

        // Then
        assertThat(tree).isNotNull()
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("operator", Operator.AND)
                .hasFieldOrPropertyWithValue("operands.size", 2);

        assertThat(tree).extracting(n -> n.childAt(0))
                .hasFieldOrPropertyWithValue("negated", false);
        assertThat(tree).extracting(n -> n.childAt(1))
                .hasFieldOrPropertyWithValue("negated", false);
    }

    @Test
    public void shouldAndNotCondition() {
        // Given
        final BTree tree = new BTree.Builder(of(BaseScript.class, 5004, "a"))
                .andNot(of(BaseScript.class, 5005, "b")).build("");

        // Then
        assertThat(tree).isNotNull()
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("operator", Operator.AND)
                .hasFieldOrPropertyWithValue("operands.size", 2);

        assertThat(tree).extracting(n -> n.childAt(0))
                .hasFieldOrPropertyWithValue("negated", false);
        assertThat(tree).extracting(n -> n.childAt(1))
                .hasFieldOrPropertyWithValue("negated", true);
    }

    @Test
    public void shouldOrCondition() {
        // Given
        final BTree tree = new BTree.Builder(of(BaseScript.class, 5004, "a"))
                .or(of(BaseScript.class, 5005, "b")).build("");

        // Then
        assertThat(tree).isNotNull()
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("operator", Operator.OR)
                .hasFieldOrPropertyWithValue("operands.size", 2);

        assertThat(tree).extracting(n -> n.childAt(0))
                .hasFieldOrPropertyWithValue("negated", false);
        assertThat(tree).extracting(n -> n.childAt(1))
                .hasFieldOrPropertyWithValue("negated", false);
    }

    @Test
    public void shouldOrNotCondition() {
        // Given
        final BTree tree = new BTree.Builder(of(BaseScript.class, 5004, "a"))
                .orNot(of(BaseScript.class, 5005, "b")).build("");

        // Then
        assertThat(tree).isNotNull()
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("operator", Operator.OR)
                .hasFieldOrPropertyWithValue("operands.size", 2);

        assertThat(tree).extracting(n -> n.childAt(0))
                .hasFieldOrPropertyWithValue("negated", false);
        assertThat(tree).extracting(n -> n.childAt(1))
                .hasFieldOrPropertyWithValue("negated", true);
    }

    @Test
    public void shouldXorCondition() {
        // Given
        final BTree tree = new BTree.Builder(of(BaseScript.class, 5004, "a"))
                .xor(of(BaseScript.class, 5005, "b")).build("");

        // Then
        assertThat(tree).isNotNull()
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("operator", Operator.XOR)
                .hasFieldOrPropertyWithValue("operands.size", 2);

        assertThat(tree).extracting(n -> n.childAt(0))
                .hasFieldOrPropertyWithValue("negated", false);
        assertThat(tree).extracting(n -> n.childAt(1))
                .hasFieldOrPropertyWithValue("negated", false);
    }

    @Test
    public void shouldXorNotCondition() {
        // Given
        final BTree tree = new BTree.Builder(of(BaseScript.class, 5004, "a"))
                .xorNot(of(BaseScript.class, 5005, "b")).build("");

        // Then
        assertThat(tree).isNotNull()
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("operator", Operator.XOR)
                .hasFieldOrPropertyWithValue("operands.size", 2);

        assertThat(tree).extracting(n -> n.childAt(0))
                .hasFieldOrPropertyWithValue("negated", false);
        assertThat(tree).extracting(n -> n.childAt(1))
                .hasFieldOrPropertyWithValue("negated", true);
    }

    // Leaf joining

    @Test
    public void shouldAndBuilder() {
        // Given
        final BTree.Builder builder = new BTree.Builder(of(BaseScript.class, 5004, "b"));

        // When
        final BTree tree = new BTree.Builder(of(BaseScript.class, 5005, "a")).and(builder).build("");

        // Then
        assertThat(tree).isNotNull()
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("operator", Operator.AND)
                .hasFieldOrPropertyWithValue("operands.size", 2);

        assertThat(tree).extracting(n -> n.childAt(0))
                .hasFieldOrPropertyWithValue("negated", false);
        assertThat(tree).extracting(n -> n.childAt(1))
                .hasFieldOrPropertyWithValue("negated", false);
    }


    @Test
    public void shouldAndNotBuilder() {
        // Given
        final BTree.Builder builder = new BTree.Builder(of(BaseScript.class, 5005, "b"));

        // When
        final BTree tree = new BTree.Builder(of(BaseScript.class, 5004, "a")).andNot(builder).build("");

        // Then
        assertThat(tree).isNotNull()
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("operator", Operator.AND)
                .hasFieldOrPropertyWithValue("operands.size", 2);

        assertThat(tree).extracting(n -> n.childAt(0))
                .hasFieldOrPropertyWithValue("negated", false);
        assertThat(tree).extracting(n -> n.childAt(1))
                .hasFieldOrPropertyWithValue("negated", true);
    }

    @Test
    public void shouldOrBuilder() {
        // Given
        final BTree.Builder builder = new BTree.Builder(of(BaseScript.class, 5004, "b"));

        // When
        final BTree tree = new BTree.Builder(of(BaseScript.class, 5005, "a")).or(builder).build("");

        // Then
        assertThat(tree).isNotNull()
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("operator", Operator.OR)
                .hasFieldOrPropertyWithValue("operands.size", 2);

        assertThat(tree).extracting(n -> n.childAt(0))
                .hasFieldOrPropertyWithValue("negated", false);
        assertThat(tree).extracting(n -> n.childAt(1))
                .hasFieldOrPropertyWithValue("negated", false);
    }

    @Test
    public void shouldOrNotBuilder() {
        // Given
        final BTree.Builder builder = new BTree.Builder(of(BaseScript.class, 5004, "b"));

        // When
        final BTree tree = new BTree.Builder(of(BaseScript.class, 5005, "a")).orNot(builder).build("");

        // Then
        assertThat(tree).isNotNull()
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("operator", Operator.OR)
                .hasFieldOrPropertyWithValue("operands.size", 2);

        assertThat(tree).extracting(n -> n.childAt(0))
                .hasFieldOrPropertyWithValue("negated", false);
        assertThat(tree).extracting(n -> n.childAt(1))
                .hasFieldOrPropertyWithValue("negated", true);
    }

    @Test
    public void shouldXorBuilder() {
        // Given
        final BTree.Builder builder = new BTree.Builder(of(BaseScript.class, 5004, "b"));

        // When
        final BTree tree = new BTree.Builder(of(BaseScript.class, 5005, "a")).xor(builder).build("");

        // Then
        assertThat(tree).isNotNull()
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("operator", Operator.XOR)
                .hasFieldOrPropertyWithValue("operands.size", 2);

        assertThat(tree).extracting(n -> n.childAt(0))
                .hasFieldOrPropertyWithValue("negated", false);
        assertThat(tree).extracting(n -> n.childAt(1))
                .hasFieldOrPropertyWithValue("negated", false);
    }

    @Test
    public void shouldXorNotBuilder() {
        // Given
        final BTree.Builder builder = new BTree.Builder(of(BaseScript.class, 5005, "b"));

        // When
        final BTree tree = new BTree.Builder(of(BaseScript.class, 5004, "a")).xorNot(builder).build("");

        // Then
        assertThat(tree).isNotNull()
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("operator", Operator.XOR)
                .hasFieldOrPropertyWithValue("operands.size", 2);

        assertThat(tree).extracting(n -> n.childAt(0))
                .hasFieldOrPropertyWithValue("negated", false);
        assertThat(tree).extracting(n -> n.childAt(1))
                .hasFieldOrPropertyWithValue("negated", true);
    }

    // Misc tests

    @Test
    public void shouldBuildXor() {
        // Given
        final Condition a = of(BaseScript.class, 5006, "c");
        final Condition b = of(BaseScript.class, 5007, "d");

        // When
        final BTree xor = new BTree.Builder(a)
                .andNot(b)
                .or(new BTree.Builder(a).not().and(b))
                .build("c XOR d");

        // Then
        final ObjectAssert<BTree> tree = assertThat(xor).isNotNull()
                .hasFieldOrPropertyWithValue("name", "c XOR d")
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("operator", Operator.OR)
                .hasFieldOrPropertyWithValue("operands.size", 2);

        final AbstractObjectAssert<?, Node> op1 = tree.extracting(n -> n.childAt(0))
                .isInstanceOf(Operand.class)
                .hasFieldOrPropertyWithValue("operator", Operator.AND)
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("operands.size", 2);
        op1.extracting(n -> ((Operand) n).childAt(0))
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("condition.script", "c");
        op1.extracting(n -> ((Operand) n).childAt(1))
                .hasFieldOrPropertyWithValue("negated", true)
                .hasFieldOrPropertyWithValue("condition.script", "d");


        final AbstractObjectAssert<?, Node> op2 = tree.extracting(n -> n.childAt(1))
                .isInstanceOf(Operand.class)
                .hasFieldOrPropertyWithValue("operator", Operator.AND)
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("operands.size", 2);
        op2.extracting(n -> ((Operand) n).childAt(0))
                .hasFieldOrPropertyWithValue("negated", true)
                .hasFieldOrPropertyWithValue("condition.script", "c");
        op2.extracting(n -> ((Operand) n).childAt(1))
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("condition.script", "d");
    }

    @Test
    public void shouldOptimise() {
        // Given
        final Condition a = of(BaseScript.class, 5004, "a");
        final Condition b = of(BaseScript.class, 5005, "b");

        // When
        final BTree xor = new BTree.Builder(a)
                .xorNot(new BTree.Builder(b).not())
                .build("a XOR b");

        // Then
        final ObjectAssert<BTree> tree = assertThat(xor).isNotNull()
                .hasFieldOrPropertyWithValue("name", "a XOR b")
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("operator", Operator.XOR)
                .hasFieldOrPropertyWithValue("operands.size", 2);

        tree.extracting(n -> n.childAt(0))
                .isInstanceOf(Leaf.class)
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("condition.script", "a");

        tree.extracting(n -> n.childAt(1))
                .isInstanceOf(Leaf.class)
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("condition.script", "b");
    }

    @Test
    public void shouldOptimiseSingleOperand() {
        // Given
        final Condition a = of(BaseScript.class, 5004, "a");
        final Condition b = of(BaseScript.class, 5005, "b");

        // When
        final BTree xor = new BTree.Builder(a)
                .xorNot(new BTree.Builder(b).not())
                .not()
                .build("a XOR b");

        // Then
        final ObjectAssert<BTree> tree = assertThat(xor).isNotNull()
                .hasFieldOrPropertyWithValue("name", "a XOR b")
                .hasFieldOrPropertyWithValue("negated", true)
                .hasFieldOrPropertyWithValue("operator", Operator.XOR)
                .hasFieldOrPropertyWithValue("operands.size", 2);

        tree.extracting(n -> n.childAt(0))
                .isInstanceOf(Leaf.class)
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("condition.script", "a");

        tree.extracting(n -> n.childAt(1))
                .isInstanceOf(Leaf.class)
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("condition.script", "b");
    }

    @Test
    public void shouldJoinSameOperator() {
        // When
        final String name = "Test";
        final BTree tree = new BTree.Builder(ENGINE, 5000)
                .and(of(BaseScript.class, 5004, "a"))
                .and(of(BaseScript.class, 5005, "8"))
                .build(name);

        // Then
        tree.negate();
        assertThat(tree).isNotNull()
                .hasFieldOrPropertyWithValue("negated", true)
                .hasFieldOrPropertyWithValue("name", name)
                .hasFieldOrPropertyWithValue("operands.size", 3);
    }

}
