package io.github.up2jakarta.dte.jpa.bytecode;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// TODO create gen sql table use DTSchemaFilter with single table name (FK OK and UK)
public class DomainGeneratorTest {

    private static Class<?> dynamicType;

    @BeforeClass
    public static void init() {
        AnnotationDescription annotation = AnnotationDescription.Builder
                .ofType(TestAnnotation.class)
                //.define("message", "Required")
                .build();

        dynamicType = new ByteBuddy()
                .subclass(Object.class)
                .name("io.github.up2jakarta.dte.gen")
                .annotateType(annotation) // class

                .defineField("bar", String.class)
                .annotateField(annotation)

                .make()
                //.de
                .load(DomainGeneratorTest.class.getClassLoader(), ClassLoadingStrategy.Default.INJECTION)
                .getLoaded();
    }

    @Test
    public void test() throws ClassNotFoundException {
        DomainGeneratorTest.class.getClassLoader().loadClass(dynamicType.getName());
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface TestAnnotation {
    }
}
