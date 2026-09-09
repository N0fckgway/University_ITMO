package ru.itmo.ode;

import java.util.*;
import java.lang.reflect.Array;

public final class Json {
    public static String encode(Object v) {
        if (v == null) return "null";
        if (v instanceof String s) return quote(s);
        if (v instanceof Number n) {
            if (!Double.isFinite(n.doubleValue())) throw new IllegalArgumentException("Non-finite JSON number");
            return n.toString();
        }
        if (v instanceof Boolean) return v.toString();
        StringJoiner join;
        if (v instanceof Map<?, ?> map) {
            join = new StringJoiner(",", "{", "}");
            map.forEach((k, value) -> join.add(quote(k.toString()) + ":" + encode(value)));
        } else if (v instanceof Iterable<?> values) {
            join = new StringJoiner(",", "[", "]");
            values.forEach(value -> join.add(encode(value)));
        } else if (v.getClass().isArray()) {
            join = new StringJoiner(",", "[", "]");
            for (int i = 0; i < Array.getLength(v); i++) join.add(encode(Array.get(v, i)));
        } else throw new IllegalArgumentException("Unsupported JSON value");
        return join.toString();
    }
    private static String quote(String s) {
        StringBuilder out = new StringBuilder("\"");
        for (char c : s.toCharArray()) {
            if (c == '"' || c == '\\') out.append('\\').append(c);
            else if (c < 32) out.append(String.format("\\u%04x", (int)c));
            else out.append(c);
        }
        return out.append('"').toString();
    }
}
