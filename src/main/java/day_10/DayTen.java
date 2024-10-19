package day_10;

import lombok.*;
import lombok.experimental.UtilityClass;
import utils.FileUtil;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static day_10.DayTen.Position.*;

public class DayTen {

    public static void main(String[] args) throws FileNotFoundException {
        Grid grid = InputParser.parseGrid(FileUtil.readToList("day_10.txt"));
        System.out.println("Part one: " + grid.countStepsToFarthestPosition());
        grid.resetVisited();
        System.out.println("Part two: " + grid.calculateEnclosedPositions());
    }

    @Getter(AccessLevel.PACKAGE)
    @RequiredArgsConstructor
    protected static class Grid {

        private final List<List<Position>> grid;
        private final Position startPosition;

        public int countStepsToFarthestPosition() {
            int stepsTaken = 0;
            NextSteps nextSteps = findStartNeighbours(startPosition);

            while (true) {
                stepsTaken++;
                Position firstNextStep = doNextStep(nextSteps.first());
                Position secondNextStep = doNextStep(nextSteps.second());

                if (firstNextStep.isVisited() || secondNextStep.isVisited()) {
                    break;
                }

                nextSteps = new NextSteps(firstNextStep, secondNextStep);
            }

            return stepsTaken;
        }

        private Position doNextStep(Position position) {
            position.setVisited(true);
            return Stream.of(
                    getConnection(position, -1, 0, 'T'),
                    getConnection(position, 1, 0, 'B'),
                    getConnection(position, 0, -1, 'L'),
                    getConnection(position, 0, 1, 'R')
            ).filter(p -> p != null && !p.isVisited).findFirst().orElse(position);
        }

        private NextSteps findStartNeighbours(Position position) {
            List<Position> connections = Stream.of(
                    getConnection(position, -1, 0, 'T'),
                    getConnection(position, 1, 0, 'B'),
                    getConnection(position, 0, -1, 'L'),
                    getConnection(position, 0, 1, 'R')
            ).filter(Objects::nonNull).toList();
            return new NextSteps(connections.get(0), connections.get(1));
        }

        private Position getConnection(Position position, int deltaY, int deltaX, char direction) {
            try {
                Position possibleConnection = grid.get(position.getY() + deltaY).get(position.getX() + deltaX);
                if (position.matchesFor(possibleConnection, direction)) {
                    return possibleConnection;
                }
            } catch (Exception e) {
                // Ignore the exception
            }
            return null;
        }

        public int calculateEnclosedPositions() {
            // Using shoelace algorithm with pick's theorem. 
            int area = 0;
            int steps = 0;
            startPosition.setVisited(false);
            Position currentPosition = startPosition;
            Position nextPosition;

            while (!currentPosition.isVisited()) {
                steps++;
                nextPosition = doNextStep(currentPosition);
                area += (currentPosition.getX() * nextPosition.getY()) - (nextPosition.getX() * currentPosition.getY());
                currentPosition = nextPosition;
            }
            area += (currentPosition.getX() * startPosition.getY()) - (startPosition.getX() * currentPosition.getY());
            area = Math.abs(area) / 2;

            return area - steps / 2 + 1;
        }
        
        public void resetVisited() {
            for (List<Position> positions : grid) {
                for (Position position : positions) {
                    position.setVisited(false);
                }
            }
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (List<Position> positions : grid) {
                for (Position position : positions) {
                    builder.append(position.getCharacter());
                }
                builder.append("\n");
            }
            return builder.toString();
        }
    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    protected static class Position {

        public static final char START = 'S';
        public static final char VERTICAL_PIPE = '|';
        public static final char HORIZONTAL_PIPE = '-';
        public static final char TOP_LEFT_CORNER = '┌';
        public static final char TOP_RIGHT_CORNER = '┐';
        public static final char BOTTOM_LEFT_CORNER = '└';
        public static final char BOTTOM_RIGHT_CORNER = '┘';

        private static final Map.Entry<Character, List<Character>> TOP_CONNECTIONS =
                Map.entry('T', List.of(TOP_LEFT_CORNER, TOP_RIGHT_CORNER, VERTICAL_PIPE));
        private static final Map.Entry<Character, List<Character>> BOTTOM_CONNECTIONS =
                Map.entry('B', List.of(BOTTOM_LEFT_CORNER, BOTTOM_RIGHT_CORNER, VERTICAL_PIPE));
        private static final Map.Entry<Character, List<Character>> RIGHT_CONNECTIONS =
                Map.entry('R', List.of(TOP_RIGHT_CORNER, BOTTOM_RIGHT_CORNER, HORIZONTAL_PIPE));
        private static final Map.Entry<Character, List<Character>> LEFT_CONNECTIONS =
                Map.entry('L', List.of(TOP_LEFT_CORNER, BOTTOM_LEFT_CORNER, HORIZONTAL_PIPE));

        private static final Map<Character, Map<Character, List<Character>>> matches = Map.of(
                START, Map.ofEntries(TOP_CONNECTIONS, BOTTOM_CONNECTIONS, LEFT_CONNECTIONS, RIGHT_CONNECTIONS),
                VERTICAL_PIPE, Map.ofEntries(TOP_CONNECTIONS, BOTTOM_CONNECTIONS),
                HORIZONTAL_PIPE, Map.ofEntries(RIGHT_CONNECTIONS, LEFT_CONNECTIONS),
                TOP_LEFT_CORNER, Map.ofEntries(RIGHT_CONNECTIONS, BOTTOM_CONNECTIONS),
                TOP_RIGHT_CORNER, Map.ofEntries(LEFT_CONNECTIONS, BOTTOM_CONNECTIONS),
                BOTTOM_LEFT_CORNER, Map.ofEntries(TOP_CONNECTIONS, RIGHT_CONNECTIONS),
                BOTTOM_RIGHT_CORNER, Map.ofEntries(TOP_CONNECTIONS, LEFT_CONNECTIONS)
        );

        private final Character character;
        private final int x;
        private final int y;

        @NonNull
        private boolean isVisited;

        public boolean matchesFor(Position position, Character direction) {
            List<Character> matchingCharacters = matches.get(character).get(direction);
            return matchingCharacters != null && matchingCharacters.contains(position.getCharacter());
        }
    }

    protected record NextSteps(Position first, Position second) {
    }

    @UtilityClass
    protected static class InputParser {

        public Grid parseGrid(List<String> input) {
            List<List<Position>> grid = new ArrayList<>();
            Position startPosition = null;
            for (int y = 0; y < input.size(); y++) {
                grid.add(new ArrayList<>());
                String line = replaceForBetterReadability(input.get(y));
                for (int x = 0; x < input.get(0).length(); x++) {
                    char character = line.charAt(x);
                    if (character == 'S') {
                        startPosition = new Position(character, x, y, true);
                        grid.get(y).add(startPosition);
                    } else {
                        Position position = new Position(character, x, y, false);
                        grid.get(y).add(position);
                    }
                }
            }
            return new Grid(grid, startPosition);
        }

        private String replaceForBetterReadability(String input) {
            return input
                    .replace('L', BOTTOM_LEFT_CORNER)
                    .replace('J', BOTTOM_RIGHT_CORNER)
                    .replace('7', TOP_RIGHT_CORNER)
                    .replace('F', TOP_LEFT_CORNER);
        }
    }
}
