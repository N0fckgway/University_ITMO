package ru.suvorov.server.util;

public abstract class Encoder {
    abstract String encode_512(String password);
    /// можно в будущем добавить новые способы хеширования
}
