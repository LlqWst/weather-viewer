package dev.lqwd.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.UUID;

@UtilityClass
@Slf4j
public final class Parser {

    public static Optional<UUID> parseUUID(String id) {
        try {
            return Optional.ofNullable(id).map(UUID::fromString);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static Optional<Long> parseLocationId(String id) {
        try {
            return Optional.ofNullable(id).map(Long::parseLong);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

}
