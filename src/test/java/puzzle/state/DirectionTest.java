package puzzle.state;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DirectionTest {

    @Test
    void of() {
        assertSame(Position.Direction.UP, Position.Direction.of(-1, 0));
        assertSame(Position.Direction.RIGHT, Position.Direction.of(0, 1));
        assertSame(Position.Direction.DOWN, Position.Direction.of(1, 0));
        assertSame(Position.Direction.LEFT, Position.Direction.of(0, -1));
    }

    @Test
    void of_shouldThrowIllegalArgumentException() {
        assertThrows(AssertionError.class, () -> Position.Direction.of(0, 0));
    }

}
