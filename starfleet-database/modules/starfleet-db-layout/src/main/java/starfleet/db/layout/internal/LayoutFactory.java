package starfleet.db.layout.internal;

import module starfleet.db.model;

import java.util.List;
import java.util.function.Function;

public interface LayoutFactory {

    static void create(Function<String, List<Member>> query) {
        try {
            new MemberTambouiLayout(query).run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
