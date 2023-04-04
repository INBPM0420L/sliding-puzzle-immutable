package puzzle.state;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PositionTest {

    Position position;

    void assertPosition(int expectedRow, int expectedCol, Position position) {
        assertAll("position",
                () -> assertEquals(expectedRow, position.getRow()),
                () -> assertEquals(expectedCol, position.getCol())
        );
    }

    @BeforeEach
    void init() {
        position = new Position(0, 0);
    }

    @Test
    void getPositionAt() {
        assertPosition(-1, 0, position.getPositionAt(Position.Direction.UP));
        assertPosition(0, 1, position.getPositionAt(Position.Direction.RIGHT));
        assertPosition(1, 0, position.getPositionAt(Position.Direction.DOWN));
        assertPosition(0, -1, position.getPositionAt(Position.Direction.LEFT));
    }

    @Test
    void getUp() {
        assertPosition(-1, 0, position.getUp());
    }

    @Test
    void getRight() {
        assertPosition(0, 1, position.getRight());
    }

    @Test
    void getDown() {
        assertPosition(1, 0, position.getDown());
    }

    @Test
    void getLeft() {
        assertPosition(0, -1, position.getLeft());
    }

    @Test
    void testToString() {
        assertEquals("(0,0)", position.toString());
    }
}
