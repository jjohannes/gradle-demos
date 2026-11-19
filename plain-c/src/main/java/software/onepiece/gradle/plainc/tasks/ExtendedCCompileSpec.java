package software.onepiece.gradle.plainc.tasks;

import org.gradle.language.c.internal.DefaultCCompileSpec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtendedCCompileSpec extends DefaultCCompileSpec {

    private final Map<String, List<String>> perFileCompilerArgs = new HashMap<>();

    public Map<String, List<String>> getPerFileCompilerArgs() {
        return perFileCompilerArgs;
    }
}
