package day_8;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import utils.FileUtil;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class DayEight {

    public static void main(String[] args) throws FileNotFoundException {
        NetworkNavigation networkNavigation = InputParser.parseNetworkNavigation(FileUtil.readToList("day_8.txt"));
        long stepsToReachZZZ = networkNavigation.calculateStepsToReachZZZ();
        System.out.println("Steps to reach ZZZ: " + stepsToReachZZZ);
        long ghostSteps = networkNavigation.calculateGhostSteps();
        System.out.println("Ghost steps: " + ghostSteps);
    }

    @Getter(AccessLevel.PACKAGE)
    @RequiredArgsConstructor
    protected static class NetworkNavigation {

        private final List<Character> instructions;
        private final Map<String, Node> networkMap;

        private boolean isGoalFound;
        private long stepsToGoal;

        public long calculateStepsToReachZZZ() {
            return calculateSteps("AAA", nodeKey -> nodeKey.equals("ZZZ"));
        }

        public long calculateGhostSteps() {
            return networkMap.keySet().stream()
                    .filter(nodeKey -> nodeKey.charAt(2) == 'A')
                    .map(startingNode -> calculateSteps(startingNode, nodeKey -> nodeKey.charAt(2) == 'Z'))
                    .reduce(1L, LeastCommonMultipleCalculator::calculateLeastCommonMultiple);
        }

        private long calculateSteps(String startingNode, Function<String, Boolean> goalFinder) {
            stepsToGoal = 0;
            isGoalFound = false;
            Node node = networkMap.get(startingNode);
            for (int i = 0; i < instructions.size() && !isGoalFound; i++) {
                node = instructions.get(i) == 'L' ? networkMap.get(node.left()) : networkMap.get(node.right());
                stepsToGoal++;
                isGoalFound = goalFinder.apply(node.key());
                if (i == instructions.size() - 1 && !isGoalFound) {
                    i = -1;
                }
            }
            return stepsToGoal;
        }
    }

    protected record Node(String key, String left, String right) {
    }

    @UtilityClass
    protected static class LeastCommonMultipleCalculator {

        public long calculateLeastCommonMultiple(long a, long b) {
            return a * (b / calculateGreatestCommonDivisor(a, b));
        }

        private long calculateGreatestCommonDivisor(long a, long b) {
            return b == 0 ? a : calculateGreatestCommonDivisor(b, a % b);
        }
    }

    @UtilityClass
    protected static class InputParser {

        public NetworkNavigation parseNetworkNavigation(List<String> input) {
            List<Character> instructions = input.get(0).chars().mapToObj(c -> (char) c).toList();
            Map<String, Node> networkMap = new HashMap<>();
            for (int i = 2; i < input.size(); i++) {
                String key = input.get(i).split(" = ")[0];
                String[] leftRight = input.get(i).split(" = ")[1].replace("(", "").replace(")", "").split(", ");
                networkMap.put(key, new Node(key, leftRight[0], leftRight[1]));
            }
            return new NetworkNavigation(instructions, networkMap);
        }
    }
}
