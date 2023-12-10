package day_6;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import utils.FileUtil;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.lang.Math.*;

public class DaySix {

    public static void main(String[] args) throws FileNotFoundException {
        List<String> input = FileUtil.readToList("day_6.txt");
        List<Race> races = InputParser.parseRaces(input);
        Long partOne = races.stream().reduce(1L, (partialResult, race) ->
                partialResult * race.calculateNumberOfWaysToBeatRecord(), Long::sum);
        System.out.println("Result part one: " + partOne);
        Race race = InputParser.parseRaceFromListWithKerning(input);
        System.out.println("Result part two: " + race.calculateNumberOfWaysToBeatRecord());
    }

    protected record Race(long time, long distance) {

        public long calculateNumberOfWaysToBeatRecord() {
            double discriminant = sqrt(pow(time, 2) - 4 * (distance + 1));
            double lowerTime = ceil((time - discriminant) / 2);
            double higherTime = floor((time + discriminant) / 2);
            return (long) (higherTime - lowerTime) + 1;
        }
    }

    @UtilityClass
    protected static class InputParser {

        public static List<Race> parseRaces(List<String> input) {
            List<Race> races = new ArrayList<>();
            List<String> times = Arrays.stream(input.get(0).split(" ")).filter(StringUtils::isNotEmpty).toList();
            List<String> distances = Arrays.stream(input.get(1).split(" ")).filter(StringUtils::isNotEmpty).toList();
            for (int i = 1; i < times.size(); i++) {
                races.add(new Race(parseInt(times.get(i)), parseInt(distances.get(i))));
            }
            return races;
        }
        
        public static Race parseRaceFromListWithKerning(List<String> input) {
            long time = parseLong(input.get(0).replace("Time:", "").replace(" ", ""));
            long distance = parseLong(input.get(1).replace("Distance:", "").replace(" ", ""));
            return new Race(time, distance);
        }
    }
}
