package software.onepiece.gradle.plainc.toolchain.tools;

import org.gradle.api.NonNullApi;
import org.gradle.nativeplatform.toolchain.internal.ArgsTransformer;
import org.gradle.nativeplatform.toolchain.internal.MacroArgsConverter;
import org.gradle.nativeplatform.toolchain.internal.compilespec.CCompileSpec;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@NonNullApi
public class CCompilerArgsTransformer implements ArgsTransformer<CCompileSpec> {

    @Override
    public List<String> transform(CCompileSpec spec) {
        List<String> args = new ArrayList<>();

        for (File file : spec.getIncludeRoots()) {
            args.add("-I");
            args.add(file.getAbsolutePath());
        }

        for (File file : spec.getSystemIncludeRoots()) {
            args.add("-isystem");
            args.add(file.getAbsolutePath());
        }

        for (String macroArg : new MacroArgsConverter().transform(spec.getMacros())) {
            args.add("-D" + macroArg);
        }
        args.addAll(spec.getAllArgs());

        return args;
    }
}
