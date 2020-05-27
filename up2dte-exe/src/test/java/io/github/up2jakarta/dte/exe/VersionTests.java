package io.github.up2jakarta.dte.exe;

import org.junit.Assert;
import org.junit.Test;
import io.github.up2jakarta.dte.exe.loader.LoadingException;

public class VersionTests {

    @Test
    public void version() {
        // WHEN
        final Version v = StaticEngine.version();

        // THEN BOOM
        Assert.assertNotNull(v);
        Assert.assertNotNull(v.getSpecVersion());
        Assert.assertNotNull(v.getBuildVersion());
        Assert.assertNotNull(v.getBuildTime());
        Assert.assertNotNull(v.getBuildUser());
        Assert.assertNotNull(v.getBuildJavaVersion());

        Assert.assertNotNull(v.getCommitTime());
        Assert.assertNotNull(v.getCommitId());

        Assert.assertNotNull(v.getCreator());
        Assert.assertNotNull(v.getContact());
        Assert.assertNotNull(v.getRole());
    }

    @Test(expected = LoadingException.class)
    public void invalid() {
        new Version("not_found");
    }
}
