package software.onepiece.gradle.plainc.toolchain.tools;

import org.gradle.api.NonNullApi;
import org.gradle.nativeplatform.internal.StaticLibraryArchiverSpec;
import org.gradle.nativeplatform.toolchain.internal.ArgsTransformer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@NonNullApi
public class StaticLibraryArchiverArgsTransformer implements ArgsTransformer<StaticLibraryArchiverSpec> {

    public List<String> transform(StaticLibraryArchiverSpec spec) {
        List<String> args = new ArrayList<>(spec.getAllArgs());

        args.add("-rv");

        args.add(spec.getOutputFile().getAbsolutePath());

        for (File file : spec.getObjectFiles()) {
            args.add(file.getAbsolutePath());
        }

        return args;
    }
}
