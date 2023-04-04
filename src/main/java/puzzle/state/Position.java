package puzzle.state;

import lombok.*;

import java.util.Arrays;

/**
 * Represents a 2D position.
 */
@Builder
@Value
@With
public class Position {
    int row;
    int col;

    /**
     * {@return the position whose vertical and horizontal distances from this
     * position are equal to the coordinate changes of the direction given}
     *
     * @param direction a direction that specifies a change in the coordinates
     */
    public Position getPositionAt(@NonNull final Direction direction) {
        return withRow(this.row + direction.getRowChange())
                .withCol(this.col + direction.getColChange());
    }

    public Position getUp() {
        return getPositionAt(Direction.UP);
    }

    public Position getRight() {
        return getPositionAt(Direction.RIGHT);
    }

    public Position getDown() {
        return getPositionAt(Direction.DOWN);
    }

    public Position getLeft() {
        return getPositionAt(Direction.LEFT);
    }

    @Override
    public String toString() {
        return String.format("(%d,%d)", row, col);
    }

    /**
     * Represents the four main directions.
     */
    @Getter
    @AllArgsConstructor
    public enum Direction {

        UP(-1, 0),
        RIGHT(0, 1),
        DOWN(1, 0),
        LEFT(0, -1);

        private final int rowChange;
        private final int colChange;

        /**
         * Returns the direction that corresponds to the coordinate changes
         * specified.
         *
         * @param rowChange the change in the row coordinate
         * @param colChange the change in the column coordinate
         * @return The direction.
         */
        public static Direction of(
                final int rowChange,
                final int colChange) {

            return Arrays.stream(Direction.values())
                    .filter(d -> d.rowChange == rowChange && d.colChange == colChange)
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }

    }
}
