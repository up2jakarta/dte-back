package io.github.up2jakarta.dte.web.services;

import io.github.up2jakarta.dte.exe.script.CompilationException;
import io.github.up2jakarta.dte.jpa.views.Key;
import io.github.up2jakarta.dte.jpa.views.VCScript;
import io.github.up2jakarta.dte.jpa.views.VDScript;
import io.github.up2jakarta.dte.web.models.ItemModel;

import java.util.List;
import java.util.Map;

/**
 * Service responsible for scripts management.
 */
public interface ScriptService {

    /**
     * Find an existing script based decider by identifier.
     *
     * @param id the unique identifier
     * @return the found entity, otherwise {@code null}.
     */
    VDScript find(Key id);

    /**
     * Find an existing script based computer by identifier.
     *
     * @param id the unique identifier
     * @return the found entity, otherwise {@code null}.
     */
    VCScript find(long id);

    /**
     * Find script based deciders by the specified {@code groupId} and typing {@code inputs}.
     *
     * @param groupId the group identifier
     * @param inputs  the typing input names
     * @return found scripts.
     */
    List<ItemModel<Key>> search(int groupId, List<String> inputs);

    /**
     * Find script based computers by the specified {@code groupId} and typing {@code inputs} and {@code outputs}.
     *
     * @param groupId the group identifier
     * @param inputs  the typing input names
     * @param outputs the typing output names
     * @return found scripts.
     */
    List<ItemModel<Long>> search(int groupId, List<String> inputs, List<String> outputs);

    /**
     * Check the syntax and types of the given {@code script} within DTE extensions.
     * Extract the output variables typing and return them.
     *
     * @param templateId the script base class
     * @param script     the script source code
     * @param types      the input typing
     * @return the output typing
     * @throws CompilationException if the script does not compile
     */
    Map<String, Integer> compile(int templateId, String script, Map<String, Integer> types) throws CompilationException;
}
