package io.github.up2jakarta.dte.jpa.repositories;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import io.github.up2jakarta.dte.jpa.TestConfiguration;
import io.github.up2jakarta.dte.jpa.entities.Type;
import io.github.up2jakarta.dte.jpa.entities.tpe.BasicType;
import io.github.up2jakarta.dte.jpa.repositories.impl.TypeRepositoryImpl;
import io.github.up2jakarta.dte.jpa.views.VType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class, inheritLocations = false)
@Transactional
@TestPropertySource(properties = {
        "spring.datasource.data=classpath:groups.sql",
        "spring.datasource.tomcat.url=jdbc:h2:mem:types_repo;MODE=PostgreSQL;"
})
public class TypeRepositoryTests {

    @Autowired
    private TypeRepositoryImpl repo;

    @Test
    public void shouldGetDefault() {
        // WHEN
        final Type entity = repo.getDefault();

        // THEN
        assertThat(entity).isNotNull()
                .isInstanceOf(BasicType.class)
                .hasFieldOrPropertyWithValue("id", 0)
                .matches(e -> "DTE/Default".equals(entity.toString()))
                .hasFieldOrPropertyWithValue("javaType", Object.class)
                .hasFieldOrPropertyWithValue("label", "Default")
                .hasFieldOrPropertyWithValue("description", "Default")
                .hasFieldOrPropertyWithValue("group.id", 0)
        ;
    }

    @Test
    public void shouldFindById() {
        // WHEN
        final Type entity = repo.find(1);

        // THEN
        assertThat(entity).isNotNull()
                .isInstanceOf(BasicType.class)
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("javaType", Character.class)
                .hasFieldOrPropertyWithValue("label", "Character")
                .hasFieldOrPropertyWithValue("description", "Character")
                .hasFieldOrPropertyWithValue("group.id", 0)
        ;
    }

    @Test
    public void shouldSearch() {
        // GIVEN
        final int groupId = 0;

        // WHEN
        final List<VType> types = repo.search(groupId);

        // THEN
        assertThat(types).isNotNull()
                .hasSize(28)
                .matches(l -> l.stream().allMatch(t -> t.getGroupId() == groupId))
        ;
    }

    @Test
    public void shouldSearchByGroup() {
        // GIVEN
        final int groupId = 1;

        // WHEN
        final List<VType> types = repo.search(groupId);

        // THEN
        assertThat(types).isNotNull()
                .hasSize(28)
                .matches(l -> l.stream().allMatch(t -> t.getGroupId() == 0))
        ;
    }

    @Test
    public void shouldSerialize() {
        // GIVEN
        final Map<String, Integer> types = new HashMap<>();
        types.put("c", 1);
        types.put("s", 2);
        types.put("b", 3);
        types.put("i", 5);

        // WHEN
        final Map<String, Class<?>> typing = repo.serialize(types);

        // THEN
        assertThat(typing).isNotNull().hasSize(types.size());

        Assert.assertEquals(Character.class, typing.get("c"));
        Assert.assertEquals(String.class, typing.get("s"));
        Assert.assertEquals(Boolean.class, typing.get("b"));
        Assert.assertEquals(Integer.class, typing.get("i"));
    }

    @Test
    public void shouldDeserialize() {
        // GIVEN
        final Map<String, Class<?>> typing = new HashMap<>();
        typing.put("c", Character.class);
        typing.put("s", String.class);
        typing.put("b", Boolean.class);
        typing.put("i", Integer.class);

        // WHEN
        final Map<String, Integer> types = repo.deserialize(typing);

        // THEN
        assertThat(typing).isNotNull().hasSize(types.size());

        Assert.assertEquals(Integer.valueOf(1), types.get("c"));
        Assert.assertEquals(Integer.valueOf(2), types.get("s"));
        Assert.assertEquals(Integer.valueOf(3), types.get("b"));
        Assert.assertEquals(Integer.valueOf(5), types.get("i"));
    }

}
