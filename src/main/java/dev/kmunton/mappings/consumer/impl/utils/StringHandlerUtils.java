package dev.kmunton.mappings.consumer.impl.utils;

public final class StringHandlerUtils {

    public static String handleEmpty(String s) {
        if (s == null) return "";
        return s;
    }
}
