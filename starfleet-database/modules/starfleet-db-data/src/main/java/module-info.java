module starfleet.db.data {
    exports starfleet.db.data;

    requires transitive starfleet.db.model;
    requires java.net.http;
    requires tools.jackson.databind;

    opens starfleet.db.data.internal.model
            to tools.jackson.databind;

    // Testing
    exports starfleet.db.data.internal.model
            to starfleet.db.data.test;
}