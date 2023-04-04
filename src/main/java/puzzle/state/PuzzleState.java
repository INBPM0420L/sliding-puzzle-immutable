package puzzle.state;

import lombok.NonNull;
import lombok.Value;
import lombok.With;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Represents the state of the puzzle.
 */
@Value
@With
public class PuzzleState {
    /**
     * The default state.
     */
    public static final PuzzleState DEFAULT_STATE = new PuzzleState(List.of(
            new Position(0, 0),
            new Position(2, 0),
            new Position(1, 1),
            new Position(0, 2)
    ));

    /**
     * The size of the board.
     */
    public static final int BOARD_SIZE = 3;

    /**
     * The index of the block.
     */
    public static final int BLOCK = 0;

    /**
     * The index of the red shoe.
     */
    public static final int RED_SHOE = 1;

    /**
     * The index of the blue shoe.
     */
    public static final int BLUE_SHOE = 2;

    /**
     * The index of the black shoe.
     */
    public static final int BLACK_SHOE = 3;

    List<Position> positions;

    /**
     * Creates a {@code PuzzleState} object initializing the positions of the
     * pieces with the positions specified. The constructor expects an array of
     * four {@code Position} objects or four {@code Position} objects.
     *
     * @param positions the initial positions of the pieces
     */
    public PuzzleState(
            @NonNull final List<Position> positions) {

        if (!checkPositions(positions)) {
            throw new IllegalArgumentException();
        }
        this.positions = Collections.unmodifiableList(positions);
    }

    /**
     * Checks whether the table state is correct or not.
     *
     * @param positions the representation of the state
     * @return the result
     */
    private static boolean checkPositions(
            @NonNull final List<Position> positions) {

        try {
            assert positions.size() == 4;
            assert positions.stream()
                    .noneMatch(Predicate.not(PuzzleState::isOnBoard));
            assert !Objects.equals(positions.get(BLUE_SHOE), positions.get(BLACK_SHOE));
            return true;
        } catch (final AssertionError ignored) {
            return false;
        }
    }

    /**
     * {@return whether the puzzle is solved}
     */
    public boolean isGoal() {
        return haveEqualPositions(RED_SHOE, BLUE_SHOE);
    }

    /**
     * Checks whether a position valid or not.
     *
     * @param position the position
     * @return the result
     */
    private static boolean isOnBoard(@NonNull final Position position) {
        return position.getRow() >= 0 && position.getRow() < BOARD_SIZE &&
                position.getCol() >= 0 && position.getCol() < BOARD_SIZE;
    }

    /**
     * {@return whether the block can be moved to the direction specified}
     *
     * @param direction a direction to which the block is intended to be moved
     */
    public boolean canMove(@NonNull final Position.Direction direction) {
        return switch (direction) {
            case UP -> canMoveUp();
            case RIGHT -> canMoveRight();
            case DOWN -> canMoveDown();
            case LEFT -> canMoveLeft();
        };
    }

    private boolean canMoveUp() {
        return positions.get(BLOCK).getRow() > 0 && isEmpty(positions.get(BLOCK).getUp());
    }

    private boolean canMoveRight() {
        // example to use Predicates and catch ArrayIndexOutOfBoundsException
        try {
            final Predicate<Position> blackShoePredicate = right -> Objects.equals(positions.get(BLACK_SHOE), right) && !haveEqualPositions(BLOCK, BLUE_SHOE);
            return blackShoePredicate.or(this::isEmpty)
                    .test(positions.get(BLOCK).getRight());
        } catch (final ArrayIndexOutOfBoundsException ignored) {
            return false;
        }
    }

    private boolean canMoveDown() {
        // example to use asserts (remember to use the -ea JVM option)
        try {
            final var down = positions.get(BLOCK).getDown();
            assert down.getRow() < BOARD_SIZE;
            assert !Objects.equals(positions.get(BLACK_SHOE), down);
            if (Objects.equals(positions.get(RED_SHOE), down)) {
                assert !haveEqualPositions(BLOCK, BLACK_SHOE);
                assert !haveEqualPositions(BLOCK, BLUE_SHOE);
            }
            assert !Objects.equals(positions.get(BLUE_SHOE), down) || !haveEqualPositions(BLOCK, BLACK_SHOE);
            return true;
        } catch (final AssertionError ignored) {
            return false;
        }
    }

    private boolean canMoveLeft() {
        return positions.get(BLOCK).getCol() > 0 && isEmpty(positions.get(BLOCK).getLeft());
    }

    /**
     * Moves the block to the direction specified.
     *
     * @param direction the direction to which the block is moved
     */
    public Optional<PuzzleState> move(
            @NonNull final Position.Direction direction) {

        final var selectedIndices = switch (direction) {
            case UP -> getIndicesToMoveUp();
            case RIGHT, DOWN -> getIndicesToMoveCommon(RED_SHOE, BLUE_SHOE, BLACK_SHOE);
            case LEFT -> getIndicesToMoveCommon(RED_SHOE, BLUE_SHOE);
        };

        try {
            final var newPositions = IntStream.rangeClosed(0, BOARD_SIZE)
                    .mapToObj(i -> selectedIndices.contains(i) ? positions.get(i).getPositionAt(direction) : positions.get(i))
                    .toList();

            return Optional.of(withPositions(newPositions));
        } catch (final IllegalArgumentException ignored) {
            return Optional.empty();
        }
    }

    /**
     * Returns the set of indices which figures will be moved upwards.
     *
     * @return the indices
     */
    private Set<Integer> getIndicesToMoveUp() {
        if (haveEqualPositions(BLOCK, BLACK_SHOE, RED_SHOE)) {
            return Set.of(BLOCK, BLACK_SHOE, RED_SHOE);
        } else if (haveEqualPositions(BLOCK, BLACK_SHOE)) {
            return Set.of(BLOCK, BLACK_SHOE);
        } else {
            return Set.of(BLOCK);
        }
    }

    /**
     * Moves the block to the direction specified and also any of the shoes
     * specified that are at the same position with the block.
     *
     * @param shoes the shoes that must be moved together with the block
     */
    private Set<Integer> getIndicesToMoveCommon(
            final int... shoes) {

        return Stream.concat(
                        Stream.of(BLOCK),
                        Arrays.stream(shoes)
                                .boxed()
                                .filter(i -> haveEqualPositions(i, BLOCK))
                )
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Returns the set of legal moves.
     *
     * @return the moves
     */
    public EnumSet<Position.Direction> getLegalMoves() {
        return Arrays.stream(Position.Direction.values())
                .filter(this::canMove)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(Position.Direction.class)));
    }

    /**
     * Checks whether two or more figures have the same position or not.
     *
     * @param i      the index of the first figure
     * @param others the indices of the other figures
     * @return the result
     */
    private boolean haveEqualPositions(final int i, final int... others) {
        return Arrays.stream(others)
                .mapToObj(positions::get)
                .allMatch(p -> Objects.equals(p, positions.get(i)));
    }

    /**
     * Checks whether a cell is empty or not.
     *
     * @param position the position
     * @return the result
     */
    private boolean isEmpty(@NonNull final Position position) {
        return positions.stream()
                .noneMatch(p -> Objects.equals(p, position));
    }

    @Override
    public String toString() {
        return positions.stream()
                .map(Position::toString)
                .collect(Collectors.joining(",", "[", "]"));
    }
}
