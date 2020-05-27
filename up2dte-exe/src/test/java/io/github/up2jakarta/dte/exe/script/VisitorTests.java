package io.github.up2jakarta.dte.exe.script;

import org.codehaus.groovy.control.SourceUnit;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertSame;

public class VisitorTests {

    @Test
    public void shouldGetSourceUnit() {
        // Given
        final SourceUnit source = Mockito.mock(SourceUnit.class);
        final ScriptVisitor visitor = new ScriptVisitor(source);

        // when
        final SourceUnit result = visitor.getSourceUnit();

        // then
        assertSame(source, result);
    }
}
