package puzzle.state;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ParameterizedPositionTest {

    void assertPosition(int expectedRow, int expectedCol, Position position) {
        assertAll("position",
                () -> assertEquals(expectedRow, position.getRow()),
                () -> assertEquals(expectedCol, position.getCol())
        );
    }

    static Stream<Position> positionProvider() {
        return Stream.of(new Position(0, 0),
                new Position(0, 2),
                new Position(2, 0),
                new Position(2, 2));
    }

    @ParameterizedTest
    @MethodSource("positionProvider")
    void getPositionAt(Position position) {
        assertPosition(position.getRow() - 1, position.getCol(), position.getPositionAt(Position.Direction.UP));
        assertPosition(position.getRow(), position.getCol() + 1, position.getPositionAt(Position.Direction.RIGHT));
        assertPosition(position.getRow() + 1, position.getCol(), position.getPositionAt(Position.Direction.DOWN));
        assertPosition(position.getRow(), position.getCol() - 1, position.getPositionAt(Position.Direction.LEFT));
    }

    @ParameterizedTest
    @MethodSource("positionProvider")
    void getUp(Position position) {
        assertPosition(position.getRow() - 1, position.getCol(), position.getUp());
    }

    @ParameterizedTest
    @MethodSource("positionProvider")
    void getRight(Position position) {
        assertPosition(position.getRow(), position.getCol() + 1, position.getRight());
    }

    @ParameterizedTest
    @MethodSource("positionProvider")
    void getDown(Position position) {
        assertPosition(position.getRow() + 1, position.getCol(), position.getDown());
    }

    @ParameterizedTest
    @MethodSource("positionProvider")
    void getLeft(Position position) {
        assertPosition(position.getRow(), position.getCol() - 1, position.getLeft());
    }

    @ParameterizedTest
    @MethodSource("positionProvider")
    void testToString(Position position) {
        assertEquals(String.format("(%d,%d)", position.getRow(), position.getCol()), position.toString());
    }

}
