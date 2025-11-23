package com.w.callum.pdf_service_data.util;

import java.util.function.Function;

public final class Env {
    public static <T> T getEnvOrDefault(String envVar, Function<String,T> callback, T defaultValue) {
        return System.getenv(envVar) != null ? callback.apply(System.getenv(envVar)) : defaultValue;
    }

    public static <T> T getEnvOrPanic(String envVar, Function<String,T> callback) {
        String value = System.getenv(envVar);
        if (value == null){
            throw new IllegalStateException("Expected environment variable not found" + envVar);
        }

        return callback.apply(System.getenv(envVar));
    }
}