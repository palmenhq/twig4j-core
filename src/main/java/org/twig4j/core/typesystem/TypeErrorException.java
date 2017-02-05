package org.twig4j.core.typesystem;

import org.jetbrains.annotations.NonNls;

public class TypeErrorException extends RuntimeException {
    public TypeErrorException(@NonNls String message) {
        super(message);
    }

    public TypeErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public static TypeErrorException thingAsType(String thing, String type) {
        return new TypeErrorException(String.format("Cannot convert \"%s\" to %s", thing, type));
    }
}
