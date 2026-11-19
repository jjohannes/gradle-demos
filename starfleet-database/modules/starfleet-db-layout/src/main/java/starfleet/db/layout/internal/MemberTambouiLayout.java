package starfleet.db.layout.internal;

import module dev.tamboui.toolkit;
import module starfleet.db.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static dev.tamboui.toolkit.Toolkit.*;

class MemberTambouiLayout extends ToolkitApp {
    private final TextInputState inputState = new TextInputState("kirk");
    private final Function<String, List<Member>> query;
    private final List<Member> members = new ArrayList<>();
    private int index = 0;

    MemberTambouiLayout(Function<String, List<Member>> query) {
        this.query = query;
        members.addAll(query.apply(inputState.text()));
        if (members.isEmpty()) {
            members.add(new Member("", "", List.of(), List.of()));
        }
    }

    @Override
    protected Element render() {
        var dock = dock();
        leftSide(dock);
        rightSide(dock);
        return panel(" Starfleet Database ", dock.fill()).rounded().on(KeyTrigger.key(KeyCode.ESCAPE), _ -> quit());
    }

    private void leftSide(DockElement dock) {
        dock.left(column(panel(" Search ", textInput(inputState).yellow().onSubmit(() -> {
                    members.clear();
                    index = 0;
                    members.addAll(query.apply(inputState.text()));
                    if (members.isEmpty()) {
                        members.add(new Member("", "", List.of(), List.of()));
                    }
                })),
                panel(" Results ", list(members.stream().map(Member::name).toList()).selected(index).on(KeyTrigger.key(KeyCode.DOWN), _ -> {
                    if (index < members.size() - 1) {
                        index++;
                    }
                }).on(KeyTrigger.key(KeyCode.UP), _ -> {
                    if (index > 0) {
                        index--;
                    }
                })),
                spacer(),
                text("Press 'ESC' to quit").dim()), Constraint.ratio(1, 4));
    }

    private void rightSide(DockElement dock) {
        var member = members.get(index);
        dock.right(panel(" %s (%s) ".formatted(member.name(), member.title()),
                panel(" Series (%d) ".formatted(member.shows().size()), list(member.shows()).displayOnly()).magenta(),
                panel(" Episodes (%d) ".formatted(member.episodes().size()), list(member.episodes()).displayOnly()).cyan()
        ).yellow(), Constraint.ratio(3, 4));
    }

}
