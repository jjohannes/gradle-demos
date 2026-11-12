package software.onepiece.gradle.plainc.toolchain.tools;

import org.gradle.api.NonNullApi;
import org.gradle.nativeplatform.internal.LinkerSpec;
import org.gradle.nativeplatform.toolchain.internal.ArgsTransformer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@NonNullApi
public class LinkerArgsTransformer implements ArgsTransformer<LinkerSpec> {

    @Override
    public List<String> transform(LinkerSpec spec) {

        List<String> args = new ArrayList<>(spec.getSystemArgs());

        args.addAll(spec.getArgs());

        for (File file : spec.getObjectFiles()) {
            args.add(file.getAbsolutePath());
        }
        for (File file : spec.getLibraries()) {
            args.add(file.getAbsolutePath());
        }

        args.add("-o");
        args.add(spec.getOutputFile().getAbsolutePath());

        if (!spec.getLibraryPath().isEmpty()) {
            throw new UnsupportedOperationException("Library Path not yet supported");
        }

        return args;
    }
}
