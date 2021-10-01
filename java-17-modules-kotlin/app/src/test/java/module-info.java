open module my.app.app.test {
    // JUnit needs to see our test
    exports my.app.application.test;

    requires my.app.app;
    requires kotlin.stdlib;
    requires org.junit.jupiter.api;
}