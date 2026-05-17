package de.hsbi.lockgame.model;

import java.util.Objects;

public record Position(int x, int y) {

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Position other = (Position) obj;

        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
