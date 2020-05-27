package io.github.up2jakarta.dte.exe.engine;

import groovy.transform.Generated;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import io.github.up2jakarta.dte.dsl.BaseScript;
import io.github.up2jakarta.dte.exe.StaticEngine;
import io.github.up2jakarta.dte.exe.script.CompilationException;

import java.util.Collections;

public class SecurityTests {

    @Test
    public void shouldNotUseSystem() {
        //GIVEN
        final String msg = "Method calls not allowed on [java.lang.System]";
        //WHEN
        CompilationException error = null;
        try {
            StaticEngine.compile(BaseScript.class, "System.exit(1);", Collections.emptyMap());
        } catch (CompilationException ex) {
            error = ex;
        }

        // then
        Assertions.assertThat(error)
                .isNotNull()
                .matches(e -> e.getMessage().contains(msg));
    }

    @Test
    public void shouldNotUseSystem2() {
        //GIVEN
        final String msg = "Method calls not allowed on [java.lang.System]";
        //WHEN
        CompilationException error = null;
        try {
            StaticEngine.compile(BaseScript.class, "def b = System; b.exit(1)", Collections.emptyMap());
        } catch (CompilationException ex) {
            error = ex;
        }

        // then
        Assertions.assertThat(error)
                .isNotNull()
                .matches(e -> e.getMessage().contains(msg));
    }

    @Test
    public void shouldNotUseSystem3() {
        //GIVEN
        final String msg = "Method calls not allowed on [groovy.util.Eval]";
        //WHEN
        CompilationException error = null;
        try {
            StaticEngine.compile(BaseScript.class, "Eval.me('System.exit(-1)')", Collections.emptyMap());
        } catch (CompilationException ex) {
            error = ex;
        }

        // then
        Assertions.assertThat(error)
                .isNotNull()
                .matches(e -> e.getMessage().contains(msg));
    }

    @Test
    public void shouldNotUseSystem4() {
        //GIVEN
        final String msg = "Cannot find matching method java.lang.Object#exit(java.lang.Integer)";
        //WHEN
        CompilationException error = null;
        try {
            StaticEngine.compile(BaseScript.class, "((Object)System).exit(-1)", Collections.emptyMap());
        } catch (CompilationException ex) {
            error = ex;
        }

        // then
        Assertions.assertThat(error)
                .isNotNull()
                .matches(e -> e.getMessage().contains(msg));
    }

    @Test
    public void shouldNotUseSystem5() {
        //GIVEN
        final String msg = "Method calls not allowed on [java.lang.System]";
        final String script =
                "void foo() {\n" +
                "    def c = System\n" +
                "    c.exit(-1)\n" +
                "}\n" +
                "foo()";
        //WHEN
        CompilationException error = null;
        try {
            StaticEngine.compile(BaseScript.class, script, Collections.emptyMap());
        } catch (CompilationException ex) {
            error = ex;
        }

        // then
        Assertions.assertThat(error)
                .isNotNull()
                .matches(e -> e.getMessage().contains(msg));
    }

    @Test
    public void shouldNotUseSystem6() {
        //GIVEN
        final String msg = "Method calls not allowed on [java.lang.System]";
        final String script =
                "void foo(System c) {\n" +
                "    c.exit(-1)\n" +
                "}\n" +
                "foo(c)";
        //WHEN
        CompilationException error = null;
        try {
            StaticEngine.compile(BaseScript.class, script, Collections.emptyMap());
        } catch (CompilationException ex) {
            error = ex;
        }

        // then
        Assertions.assertThat(error)
                .isNotNull()
                .matches(e -> e.getMessage().contains(msg));
    }

    @Test
    public void shouldNotUseThread() {
        //GIVEN
        final String msg = "Method calls not allowed on [java.lang.Thread]";
        final String script = "Thread.currentThread().interrupt()";
        //WHEN
        CompilationException error = null;
        try {
            StaticEngine.compile(BaseScript.class, script, Collections.emptyMap());
        } catch (CompilationException ex) {
            error = ex;
        }

        // then
        Assertions.assertThat(error)
                .isNotNull()
                .matches(e -> e.getMessage().contains(msg));
    }

    @Test
    public void shouldNotUseRuntime() {
        //GIVEN
        final String msg = "Method calls not allowed on [java.lang.Runtime]";
        final String script = "Runtime.getRuntime().exec(\"exit 0\")";
        //WHEN
        CompilationException error = null;
        try {
            StaticEngine.compile(BaseScript.class, script, Collections.emptyMap());
        } catch (CompilationException ex) {
            error = ex;
        }

        // then
        Assertions.assertThat(error)
                .isNotNull()
                .matches(e -> e.getMessage().contains(msg));
    }

    @Test
    public void shouldNotUseProcess() {
        //GIVEN
        final String msg = "Method calls not allowed on [java.lang.ProcessBuilder]";
        final String script = "new ProcessBuilder().command(\"exit 0\").start().waitFor()";
        //WHEN
        CompilationException error = null;
        try {
            StaticEngine.compile(BaseScript.class, script, Collections.emptyMap());
        } catch (CompilationException ex) {
            error = ex;
        }

        // then
        Assertions.assertThat(error)
                .isNotNull()
                .matches(e -> e.getMessage().contains(msg));
    }

    @Test
    public void shouldNotUseScript() {
        //GIVEN
        final String msg = "Method calls not allowed on [groovy.lang.Script]";
        final String script = "print 'Hello'";
        //WHEN
        CompilationException error = null;
        try {
            StaticEngine.compile(BaseScript.class, script, Collections.emptyMap());
        } catch (CompilationException ex) {
            error = ex;
        }

        // then
        Assertions.assertThat(error)
                .isNotNull()
                .matches(e -> e.getMessage().contains(msg));
    }

    @Test
    public void shouldNotAccessProperty() {
        //GIVEN
        final String msg = "Property accesses not allowed on [java.lang.System]";
        //WHEN
        CompilationException error = null;
        try {
            StaticEngine.compile(BaseScript.class, "def c = System.out", Collections.emptyMap());
        } catch (CompilationException ex) {
            error = ex;
        }

        // then
        Assertions.assertThat(error)
                .isNotNull()
                .matches(e -> e.getMessage().contains(msg));
    }

    @Test
    public void shouldNotAccessProperty2() {
        //GIVEN
        final String msg = "Property accesses not allowed on [java.lang.System]";
        //WHEN
        CompilationException error = null;
        try {
            StaticEngine.compile(BaseScript.class, "def c = System; def o = c.out", Collections.emptyMap());
        } catch (CompilationException ex) {
            error = ex;
        }

        // then
        Assertions.assertThat(error)
                .isNotNull()
                .matches(e -> e.getMessage().contains(msg));
    }

    @Test
    public void shouldAccessGeneratedProperty() {
        StaticEngine.compile(BaseScript.class, "g.a", Collections.singletonMap("g", Model.class));
    }

    @Generated
    private static class Model {
        public String a;
    }
}
