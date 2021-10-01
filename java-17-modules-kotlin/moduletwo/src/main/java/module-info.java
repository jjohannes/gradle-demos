module my.app.moduletwo {
    exports my.app.module2;

    requires transitive my.app.moduleone;
    requires com.fasterxml.jackson.databind;

    requires kotlin.stdlib;
}