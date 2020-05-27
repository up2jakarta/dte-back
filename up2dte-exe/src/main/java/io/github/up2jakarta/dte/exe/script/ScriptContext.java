package io.github.up2jakarta.dte.exe.script;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Script Context Model representing the inputs and outputs.
 *
 * @author A.ABBESSI
 */
public class ScriptContext extends AbstractMap<String, Object> {

    private final Map<String, Object> in;
    private final Map<String, Object> out = new HashMap<>();

    /**
     * Public constructor.
     *
     * @param ctx the input context
     */
    public ScriptContext(final Map<String, Object> ctx) {
        this.in = ctx;
    }

    /**
     * @return the output context
     */
    public final Map<String, Object> getOut() {
        return this.out;
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        final Map<String, Object> all = new HashMap<>();
        all.putAll(in);
        all.putAll(out);
        return all.entrySet();
    }

    @Override
    public Object put(final String key, final Object v) {
        return out.put(key, v);
    }

    @Override
    public Object get(final Object key) {
        final Object first = in.get(key);
        if (first != null) {
            return first;
        }
        return out.get(key);
    }
}
