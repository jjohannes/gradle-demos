package starfleet.db.layout;

import module starfleet.db.model;

import starfleet.db.layout.internal.LayoutFactory;

import java.util.List;
import java.util.function.Function;

public class MemberLayout {

    public static void use(Function<String, List<Member>> query) {
        LayoutFactory.create(query);
    }
}
