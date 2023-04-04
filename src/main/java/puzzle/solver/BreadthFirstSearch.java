package puzzle.solver;

import lombok.*;
import puzzle.state.Position;
import puzzle.state.PuzzleState;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BreadthFirstSearch {

    public static void main(String[] args) {
        final var bfs = new BreadthFirstSearch();
        bfs.search(PuzzleState.DEFAULT_STATE)
                .ifPresentOrElse(
                        bfs::printPathTo,
                        () -> System.out.println("No solution found")
                );

    }

    public Optional<Node> search(@NonNull final PuzzleState state) {
        final var queue = new LinkedList<>(List.of(new Node(state)));
        final var seen = new HashSet<>(queue);

        while (!queue.isEmpty()) {
            final var selected = queue.pollFirst();
            if (selected.getState().isGoal()) {
                return Optional.of(selected);
            }

            final var children = selected.getState()
                    .getLegalMoves()
                    .stream()
                    .map(direction -> selected.getState().move(direction)
                            .map(s -> new Node(s, selected, direction))
                            .filter(Predicate.not(seen::contains))
                    )
                    .flatMap(Optional::stream)
                    .collect(Collectors.toUnmodifiableSet());
            seen.addAll(children);
            queue.addAll(children);
        }

        return Optional.empty();
    }

    public void printPathTo(@NonNull final Node node) {
        Optional.ofNullable(node.getParent())
                .ifPresent(this::printPathTo);
        System.out.println(node);
    }

    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class Node {
        @EqualsAndHashCode.Include
        @Getter
        private final PuzzleState state;
        @Getter
        private Node parent;
        private Position.Direction direction;

        @Override
        public String toString() {
            return Optional.ofNullable(direction)
                    .map(value -> String.format("%s %s", value, state))
                    .orElseGet(state::toString);
        }
    }
}
