package amazing.impl;

import amazing.AmazingService;
import com.google.common.collect.ImmutableList;
import org.apache.commons.text.TextStringBuilder;

public class DefaultAmazingService implements AmazingService {
    @Override
    public void compute() {
        var m = ImmutableList.of("Hello ", "from ", "module ", getClass().getModule().getName());
        var builder = new TextStringBuilder();
        m.forEach(builder::append);
        internalCompute(builder);
    }

    void internalCompute(TextStringBuilder builder) {
        System.out.println(builder);
    }
}
