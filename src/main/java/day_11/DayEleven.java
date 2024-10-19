package day_11;

import lombok.experimental.UtilityClass;
import utils.FileUtil;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static day_11.DayEleven.InputParser.createPairs;
import static day_11.DayEleven.InputParser.findPositions;
import static java.lang.Math.abs;

public class DayEleven {

    public static void main(String[] args) throws FileNotFoundException {
        List<String> input = FileUtil.readToList("day_11.txt");
        List<Pair> pairs = createPairs(findPositions(input, 2));
        Long partOneSum = pairs.stream().map(Pair::distance).reduce(Long::sum).orElseThrow();
        System.out.println("Part one: " + partOneSum);
        pairs = createPairs(findPositions(input, 1000000));
        Long partTwoSum = pairs.stream().map(Pair::distance).reduce(Long::sum).orElseThrow();
        System.out.println("Part two: " + partTwoSum);
    }

    protected record Pair(Position p1, Position p2, long distance) {
    }

    protected record Position(long x, long y) {
    }

    @UtilityClass
    protected static class InputParser {

        public static List<Position> findPositions(List<String> input, int expandBy) {
            List<Integer> expansionColumnIndizes = findExpansionColumnIndizes(input);
            List<Integer> expansionRowIndizes = findExpansionRowIndizes(input);
            List<Position> positions = new ArrayList<>();
            for (int y = 0; y < input.size(); y++) {
                for (int x = 0; x < input.get(y).length(); x++) {
                    if (input.get(y).charAt(x) == '#') {
                        long expandedX = calculateExpansion(x, expansionColumnIndizes, expandBy);
                        long expandedY = calculateExpansion(y, expansionRowIndizes, expandBy);
                        positions.add(new Position(expandedX, expandedY));
                    }
                }
            }
            return positions;
        }

        private static long calculateExpansion(int toBeExpanded, List<Integer> indizes, int expandBy) {
            return toBeExpanded + (indizes.stream().filter(i -> i < toBeExpanded).count() * (expandBy - 1));
        }

        public static List<Pair> createPairs(List<Position> positions) {
            List<Pair> pairs = new ArrayList<>();
            for (int i = 0; i < positions.size(); i++) {
                Position currentPosition = positions.get(i);
                for (int j = i + 1; j < positions.size(); j++) {
                    Position nextPosition = positions.get(j);
                    long distance = abs(currentPosition.x - nextPosition.x) + abs(currentPosition.y - nextPosition.y);
                    pairs.add(new Pair(currentPosition, nextPosition, distance));
                }
            }
            return pairs;
        }

        private static List<Integer> findExpansionColumnIndizes(List<String> input) {
            List<Integer> result = new ArrayList<>();
            for (int x = 0; x < input.get(0).length(); x++) {
                boolean isExpansionColumn = true;
                for (String line : input) {
                    if (line.charAt(x) == '#') {
                        isExpansionColumn = false;
                        break;
                    }
                }
                if (isExpansionColumn) {
                    result.add(x);
                }
            }
            return result;
        }

        private static List<Integer> findExpansionRowIndizes(List<String> input) {
            List<Integer> result = new ArrayList<>();
            for (int y = 0; y < input.size(); y++) {
                if (!input.get(y).contains("#")) {
                    result.add(y);
                }
            }
            return result;
        }
    }
}
