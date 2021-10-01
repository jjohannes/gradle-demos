module my.app.app {
    requires my.app.moduletwo;
    requires io.github.classgraph;

    // To allow testing
    exports my.app.application;

    requires kotlin.stdlib;
}