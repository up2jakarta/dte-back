package io.github.up2jakarta.dte.exe;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import io.github.up2jakarta.dte.dsl.BaseScript;
import io.github.up2jakarta.dte.exe.engine.BTree;
import io.github.up2jakarta.dte.exe.engine.Calculation;
import io.github.up2jakarta.dte.exe.engine.Condition;
import io.github.up2jakarta.dte.exe.engine.DTree;
import io.github.up2jakarta.dte.exe.loader.Loader;
import io.github.up2jakarta.dte.exe.loader.LoadingException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static io.github.up2jakarta.dte.exe.engine.TestHelper.mockLoader;

public class DTreeEngineTests {

    private static final Loader<Calculation> C_LOADER = mockLoader(Calculation.class);
    private static final Loader<Condition> D_LOADER = mockLoader(Condition.class);
    private static final StaticEngine ENGINE = new StaticEngine(C_LOADER, D_LOADER);

    private final Map<String, Object> context = new HashMap<>();

    @BeforeClass
    public static void init() {
        Mockito.when(D_LOADER.load(Long.MIN_VALUE)).thenReturn(null);
        Mockito.when(D_LOADER.load(0)).thenThrow(new LoadingException("calculation", 0));
        Mockito.when(D_LOADER.load(1)).thenReturn(Condition.of(BaseScript.class, 1, "currency == 'TND'"));
        {// NOT condition
            final BTree not = new BTree.Builder(55531, "a").not().build("Not a");
            Mockito.when(D_LOADER.load(5553)).thenReturn(Condition.of(not));
        }

        {// EQ condition
            final BTree eq = new BTree.Builder(55541, "a == b").build("a eq b");
            Mockito.when(D_LOADER.load(5554)).thenReturn(Condition.of(eq));
        }

        {// AND condition
            final BTree and = new BTree.Builder(55551, "a")
                    .and(55552, "b")
                    .build("a AND b");
            Mockito.when(D_LOADER.load(5555)).thenReturn(Condition.of(and));
        }

        { // OR condition
            final BTree or = new BTree.Builder(55560, "a")
                    .or(55561, "b")
                    .build("a OR b");
            Mockito.when(D_LOADER.load(5556)).thenReturn(Condition.of(or));
        }
        { // XOR condition
            final BTree xor = new BTree.Builder(55571, "a")
                    .xor(55572, "b")
                    .build("a XOR b");
            Mockito.when(D_LOADER.load(5557)).thenReturn(Condition.of(xor));
        }
        { // XOR complex condition
            final Condition a = Condition.of(BaseScript.class, 55581, "a");
            final Condition b = Condition.of(BaseScript.class, 55582, "b");
            final BTree.Builder first = new BTree.Builder(a).not().and(b);
            final BTree xor = new BTree.Builder(a).andNot(b)
                    .or(first)
                    .build("a XOR b");
            Mockito.when(D_LOADER.load(5558)).thenReturn(Condition.of(xor));
        }

        Mockito.when(C_LOADER.load(Long.MIN_VALUE)).thenReturn(null);
        Mockito.when(C_LOADER.load(0)).thenThrow(new LoadingException("calculation", 0));
        Mockito.when(C_LOADER.load(1)).thenReturn(Calculation.of(DTree.of("Empty")));
        Mockito.when(C_LOADER.load(2)).thenReturn(Calculation.of(BaseScript.class, 2, "country = 'Tunisia'"));

        {//Simple Calculation Tree
            final DTree simple = new DTree.Builder("Simple")
                    .when(31, "currency == 'EUR'", false)
                    .then(311, "country = 'Europe'")
                    .then(312, "country = 'European Union'")
                    .end()
                    .when(ENGINE, 1, false)
                    .then(ENGINE, 2)
                    .end()
                    .otherwise()
                    .then(331, "country = 'Unknown'")
                    .then(332, "log = 'Unknown currency ' + currency")
                    .end()
                    .build();
            Mockito.when(C_LOADER.load(3)).thenReturn(Calculation.of(simple));
        }
    }

    @After
    public void tearDown() {
        context.clear();
    }

    @Test(expected = LoadingException.class)
    public void unknownComputer() {
        // Given
        final long id = 0;

        // when
        ENGINE.resolve(id, context);

        // then BOOM
    }

    @Test(expected = LoadingException.class)
    public void unknownDecider() {
        // Given
        final long id = 0;

        // when
        ENGINE.isFulfilled(id, context);

        // then BOOM
    }

    @Test(expected = NullPointerException.class)
    public void nullComputer() {
        ENGINE.resolve(Long.MIN_VALUE, context);
    }

    @Test(expected = NullPointerException.class)
    public void nullDecider() {
        ENGINE.isFulfilled(Long.MIN_VALUE, context);
    }

    @Test
    public void shouldEmpty() {
        // Given
        final long id = 1;

        // when
        final Map<String, Object> result = ENGINE.resolve(id, context);

        // then
        assertEquals(result, Collections.emptyMap());
    }

    @Test
    public void shouldEurope() {
        // Given
        final long treeId = 3;
        context.put("currency", "EUR");
        context.put("today", LocalDate.now());

        // when
        final Map<String, Object> result = ENGINE.resolve(treeId, context);

        // then
        assertNotNull(result);
        assertEquals("European Union", result.get("country"));
    }

    @Test
    public void shouldTunisia() {
        // Given
        final long treeId = 3;
        context.put("currency", "TND");

        // when
        final Map<String, Object> result = ENGINE.resolve(treeId, context);

        // then
        assertNotNull(result);
        assertEquals("Tunisia", result.get("country"));

    }

    @Test
    public void shouldUnknown() {
        // Given
        final long treeId = 3;
        context.put("currency", "USD");

        // when
        final Map<String, Object> result = ENGINE.resolve(treeId, context);

        // then
        assertNotNull(result);
        assertEquals("Unknown", result.get("country"));
        assertEquals("Unknown currency USD", result.get("log"));
    }

    @Test
    public void simpleComputer() {
        // Given
        final long computerId = 2;

        // when
        final Map<String, Object> result = ENGINE.resolve(computerId, context);

        // then
        assertNotNull(result);
        assertEquals("Tunisia", result.get("country"));
    }

    @Test
    public void simpleDecider() {
        // Given
        final long deciderId = 1;
        context.put("currency", "TND");

        // when
        final boolean test = ENGINE.isFulfilled(deciderId, context);

        // then
        assertTrue(test);
    }

    @Test
    public void testNotBTree() {
        // GIVEN
        final long id = 5553;
        final Condition bool = Condition.of(ENGINE, id);

        // WHEN
        final boolean result1 = bool.isFulfilled(Collections.singletonMap("a", true));
        final boolean result2 = bool.isFulfilled(Collections.singletonMap("a", false));

        // THEN
        assertFalse(result1);
        assertTrue(result2);
    }

    @Test
    public void testAndBTree() {
        // GIVEN
        final long id = 5555;
        final Condition bool = Condition.of(ENGINE, id);

        // WHEN
        boolean result = bool.isFulfilled(new HashMap<String, Object>() {{
            put("a", true);
            put("b", true);
        }});
        // THEN
        assertTrue(result);

        // WHEN
        result = bool.isFulfilled(new HashMap<String, Object>() {{
            put("a", false);
            put("b", true);
        }});
        // THEN
        assertFalse(result);

        // WHEN
        result = bool.isFulfilled(new HashMap<String, Object>() {{
            put("a", true);
            put("b", false);
        }});
        // THEN
        assertFalse(result);

        // WHEN
        result = bool.isFulfilled(new HashMap<String, Object>() {{
            put("a", false);
            put("b", false);
        }});
        // THEN
        assertFalse(result);
    }

    @Test
    public void testOrBTree() {
        // GIVEN
        final long id = 5556;
        final Condition bool = Condition.of(ENGINE, id);

        // WHEN
        boolean result = bool.isFulfilled(new HashMap<String, Object>() {{
            put("a", true);
            put("b", true);
        }});
        // THEN
        assertTrue(result);

        // WHEN
        result = bool.isFulfilled(new HashMap<String, Object>() {{
            put("a", false);
            put("b", true);
        }});
        // THEN
        assertTrue(result);

        // WHEN
        result = bool.isFulfilled(new HashMap<String, Object>() {{
            put("a", true);
            put("b", false);
        }});
        // THEN
        assertTrue(result);

        // WHEN
        result = bool.isFulfilled(new HashMap<String, Object>() {{
            put("a", false);
            put("b", false);
        }});
        // THEN
        assertFalse(result);
    }

    @Test
    public void testXorTree() {
        // GIVEN
        final long id = 5557;
        final Condition bool = Condition.of(ENGINE, id);

        // WHEN
        boolean result = bool.isFulfilled(new HashMap<String, Object>() {{
            put("a", true);
            put("b", true);
        }});
        // THEN
        assertFalse(result);

        // WHEN
        result = bool.isFulfilled(new HashMap<String, Object>() {{
            put("a", false);
            put("b", true);
        }});
        // THEN
        assertTrue(result);

        // WHEN
        result = bool.isFulfilled(new HashMap<String, Object>() {{
            put("a", true);
            put("b", false);
        }});
        // THEN
        assertTrue(result);

        // WHEN
        result = bool.isFulfilled(new HashMap<String, Object>() {{
            put("a", false);
            put("b", false);
        }});
        // THEN
        assertFalse(result);
    }

    @Test
    public void testXorBTree() {
        // GIVEN
        final long id = 5558;
        final Condition bool = Condition.of(ENGINE, id);

        // WHEN
        boolean result = bool.isFulfilled(new HashMap<String, Object>() {{
            put("a", true);
            put("b", true);
        }});
        // THEN
        assertFalse(result);

        // WHEN
        result = bool.isFulfilled(new HashMap<String, Object>() {{
            put("a", false);
            put("b", true);
        }});
        // THEN
        assertTrue(result);

        // WHEN
        result = bool.isFulfilled(new HashMap<String, Object>() {{
            put("a", true);
            put("b", false);
        }});
        // THEN
        assertTrue(result);

        // WHEN
        result = bool.isFulfilled(new HashMap<String, Object>() {{
            put("a", false);
            put("b", false);
        }});
        // THEN
        assertFalse(result);
    }

    @Test
    public void testEqualsBTree() {
        // GIVEN
        final long id = 5554;
        final Condition bool = Condition.of(ENGINE, id);

        // WHEN
        boolean result = bool.isFulfilled(new HashMap<String, Object>() {{
            put("a", true);
            put("b", true);
        }});
        // THEN
        assertTrue(result);

        result = bool.isFulfilled(new HashMap<String, Object>() {{
            put("a", "word");
            put("b", "word");
        }});
        // THEN
        assertTrue(result);
    }
}
