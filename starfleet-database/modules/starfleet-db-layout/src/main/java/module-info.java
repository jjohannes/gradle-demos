module starfleet.db.layout {
    exports starfleet.db.layout;

    requires transitive starfleet.db.model;
    requires dev.tamboui.core;
    requires dev.tamboui.toolkit;
    requires dev.tamboui.tui;
    requires dev.tamboui.widgets;

    requires /*runtime*/ dev.tamboui.jline_three.backend;
}