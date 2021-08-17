package org.example;

public sealed class Message permits Hello, Update, Goodbye {
}

final class Hello extends Message { }
final class Update extends Message { }
final class Goodbye extends Message { }
