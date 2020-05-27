package io.github.up2jakarta.dte.exe.engine;

import io.github.up2jakarta.dte.exe.loader.CacheEntry;
import io.github.up2jakarta.dte.exe.script.StaticScript;

import java.util.zip.CRC32;

/**
 * Script Cache Entry.
 */
abstract class DynamicScript implements CacheEntry {

    static final char CALC_DIGIT = 'W';
    static final char BOOL_DIGIT = 'X';
    static final char TEMP_DIGIT = 'Y';
    private static final char FILL_DIGIT = 'Z';
    private static final char[] DIGITS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
            'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V'
    };

    private final int checksum;
    private final long id;
    private Class<? extends groovy.lang.Script> base;
    private Object script;

    /**
     * Private constructor for ScriptEntry.
     *
     * @param base   the script base class
     * @param id     the script unique identifier
     * @param script the script source code
     */
    DynamicScript(final Class<? extends groovy.lang.Script> base, final long id, final String script) {
        this.script = script;
        this.base = base;
        this.id = id;
        this.checksum = checksum(script);
    }

    /**
     * Compute the content checksum and return it.
     *
     * @param scriptText the content
     * @return the content checksum.
     */
    private static int checksum(final String scriptText) {
        final CRC32 checksum = new CRC32();
        for (final String line : scriptText.split("\\r?\\n")) {
            checksum.update(line.getBytes());
        }
        return (int) checksum.getValue();
    }

    /**
     * Returns a fixed length string representation of the {@code number} argument in DTE t32 radix.
     *
     * @param prefix a prefix digit
     * @param number a {@code long} to be converted to a string.
     * @return a fixed length string representation of the argument in the specified radix.
     */
    private static String toKey(final char prefix, final long number) {
        int len = 15;
        char[] buf = new char[len];
        long val = number;
        if (number == Long.MIN_VALUE) {
            val = Long.MAX_VALUE;
            buf[1] = 'M';
        } else if (number < 0) {
            val = -number;
            buf[1] = 'N';
        } else {
            buf[1] = 'P';
        }
        do {
            buf[--len] = DIGITS[((int) val) & 31];
            val >>>= 5;
        } while (val != 0 && len > 2);
        while (len > 2) {
            buf[--len] = FILL_DIGIT;
        }
        buf[0] = prefix;
        return new String(buf);
    }

    /**
     * @return compiled script.
     */
    StaticScript getScript() {
        if (script instanceof String) {
            synchronized (this) {
                script = getParser(base).parse(getScripName(), (String) script);
            }
        }
        return (StaticScript) script;
    }

    /**
     * Handle parsing if any change in the script code source or the base class.
     *
     * @param base   the script base class
     * @param script the script source code
     */
    void update(final Class<? extends groovy.lang.Script> base, final String script) {
        if (!(this.script instanceof String) && (this.base != base || this.checksum != checksum(script))) {
            synchronized (this) {
                this.script = script;
                this.base = base;
            }
        }
    }

    /**
     * Return the script parser from specified script {@code base} class.
     *
     * @param base the script base class
     * @return the script parser
     */
    abstract ScriptLoader getParser(Class<? extends groovy.lang.Script> base);

    /**
     * @return the script name prefix
     */
    abstract char getPrefix();

    /**
     * @return the script unique key
     */
    final String getScripName() {
        return toKey(getPrefix(), getId());
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public final String toString() {
        return getPrefix() + "#" + getId();
    }

}
