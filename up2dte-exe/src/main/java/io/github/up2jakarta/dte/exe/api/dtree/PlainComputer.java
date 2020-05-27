package io.github.up2jakarta.dte.exe.api.dtree;

import io.github.up2jakarta.dte.exe.api.Computer;
import io.github.up2jakarta.dte.exe.api.Template;

/**
 * DTree Plain Computer, based on script code source.
 */
public interface PlainComputer extends Computer {

    /**
     * @return the script source code.
     */
    String getScript();

    /**
     * @return the script template
     */
    Template getTemplate();
}
