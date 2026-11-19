package software.onepiece.gradle.plainc.toolchain.tools;

import org.gradle.api.NonNullApi;
import org.gradle.nativeplatform.toolchain.internal.ArgsTransformer;
import org.gradle.nativeplatform.toolchain.internal.compilespec.AssembleSpec;

import java.util.List;

@NonNullApi
public class AssemblerArgsTransformer implements ArgsTransformer<AssembleSpec> {

    @Override
    public List<String> transform(AssembleSpec assembleSpec) {
        return assembleSpec.getAllArgs();
    }
}