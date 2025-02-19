package org.popcraft.bolt.matcher;

import java.util.Optional;

public interface Matcher<T> {
    boolean canMatch(T type);

    Optional<Match> findMatch(T type);
}
