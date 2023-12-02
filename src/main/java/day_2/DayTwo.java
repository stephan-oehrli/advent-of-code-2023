package day_2;

import lombok.experimental.UtilityClass;
import utils.FileUtil;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class DayTwo {

    public static void main(String[] args) throws FileNotFoundException {
        List<String> inputs = FileUtil.readToList("day_2.txt");
        Configuration configuration = new Configuration(12, 13, 14);
        System.out.println("Sum of possible game ids: " + GameSummarizer.sumPossibleGameIds(inputs, configuration));
        System.out.println("Sum of game powers: " + GameSummarizer.sumGamePower(inputs));
    }

    @UtilityClass
    protected static class GameSummarizer {

        public static int sumPossibleGameIds(List<String> inputs, Configuration configuration) {
            return InputParser.parseGames(inputs).stream()
                    .reduce(0, (subTotal, game) -> {
                        int add = game.isPossibleWith(configuration) ? game.id() : 0;
                        return subTotal + add;
                    }, Integer::sum);
        }

        public static long sumGamePower(List<String> inputs) {
            return InputParser.parseGames(inputs).stream()
                    .reduce(0L, (subTotal, game) -> subTotal + game.calculateGamePower(), Long::sum);
        }
    }

    @UtilityClass
    protected static class InputParser {

        public static Game parseGame(String input) {
            int gameId = Integer.parseInt(input.split(": ")[0].split(" ")[1]);
            String[] cubeSetsAsString = input.split(": ")[1].split("; ");
            List<CubeSet> cubeSets = new ArrayList<>();
            for (String cubeSetString : cubeSetsAsString) {
                int redCubes = 0;
                int greenCubes = 0;
                int blueCubes = 0;
                for (String numberAndCube : cubeSetString.split(", ")) {
                    int amount = Integer.parseInt(numberAndCube.split(" ")[0]);
                    String color = numberAndCube.split(" ")[1];
                    switch (color) {
                        case "red" -> redCubes = amount;
                        case "green" -> greenCubes = amount;
                        case "blue" -> blueCubes = amount;
                    }
                }
                cubeSets.add(new CubeSet(redCubes, greenCubes, blueCubes));
            }
            return new Game(gameId, cubeSets);
        }

        public static List<Game> parseGames(List<String> inputList) {
            return inputList.stream().map(InputParser::parseGame).toList();
        }
    }

    protected record Configuration(int maxRed, int maxGreen, int maxBlue) {
    }

    protected record Game(int id, List<CubeSet> cubeSets) {

        public boolean isPossibleWith(Configuration configuration) {
            for (CubeSet cubeSet : cubeSets) {
                if (cubeSet.redCubes() > configuration.maxRed() ||
                        cubeSet.greenCubes() > configuration.maxGreen() ||
                        cubeSet.blueCubes() > configuration.maxBlue()) {
                    return false;
                }
            }
            return true;
        }

        public long calculateGamePower() {
            int maxRed = 0;
            int maxGreen = 0;
            int maxBlue = 0;
            for (CubeSet cubeSet : cubeSets) {
                maxRed = Math.max(cubeSet.redCubes(), maxRed);
                maxGreen = Math.max(cubeSet.greenCubes(), maxGreen);
                maxBlue = Math.max(cubeSet.blueCubes(), maxBlue);
            }
            return (long) maxRed * maxGreen * maxBlue;
        }
    }

    protected record CubeSet(int redCubes, int greenCubes, int blueCubes) {
    }
}
