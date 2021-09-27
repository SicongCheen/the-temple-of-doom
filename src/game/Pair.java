package game;

import java.util.Objects;

/**
 * A Pair (X,Y) represents an immutable ordered pair of two Objects of types X and Y respectively.
 */
public record Pair<X, Y>(X first, Y second) {
    /**
     * Return true iff ob is an instance of Pair with the same values.
     */
    public boolean equals(Object ob) {
        if (!(ob instanceof Pair<?, ?>)) {
            return false;
        }
        Pair<?, ?> p = (Pair<?, ?>) ob;
        return first.equals(p.first) && second.equals(p.second);
    }

    /**
     * Return a hash code based on both values in this obbject.
     */
    public int hashCode() {
        return Objects.hash(first, second);
    }

}
