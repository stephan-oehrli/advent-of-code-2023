package day_5;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import utils.FileUtil;

import java.io.FileNotFoundException;
import java.util.*;

public class DayFive {

    public static void main(String[] args) throws FileNotFoundException {
        long start = System.currentTimeMillis();
        Almanac almanac = InputParser.parseAlmanac(FileUtil.readToList("day_5.txt"));
        long lowestLocationOfSeeds = almanac.findLowestLocationOfSeeds();
        System.out.println("Lowest location of seeds is: " + lowestLocationOfSeeds);
        System.out.println("Duration: " + (System.currentTimeMillis() - start) + "ms");
        start = System.currentTimeMillis();
        long lowestLocationOfSeedRange = almanac.findLowestLocationOfSeedRanges();
        System.out.println("Lowest location of seed ranges is: " + lowestLocationOfSeedRange);
        System.out.println("Duration: " + (System.currentTimeMillis() - start) + "ms");
    }

    protected record Range(long from, long to) {
    }

    @Getter
    @RequiredArgsConstructor
    protected static class RangeMap {
        private final long sourceCategoryStart;
        private final long destinationCategoryStart;
        private final long rangeLength;

        public boolean containsSource(long input) {
            return input >= sourceCategoryStart && input < sourceCategoryStart + rangeLength;
        }

        public long findDestination(long input) {
            long index = input - sourceCategoryStart;
            return destinationCategoryStart + index;
        }
    }

    @Getter
    @RequiredArgsConstructor
    protected static class Almanac {

        private final List<Long> seeds;
        private final List<Range> seedRanges;

        private final List<List<RangeMap>> allMaps;

        private final List<RangeMap> seedToSoil;
        private final List<RangeMap> soilToFertilizer;
        private final List<RangeMap> fertilizerToWater;
        private final List<RangeMap> waterToLight;
        private final List<RangeMap> lightToTemperature;
        private final List<RangeMap> temperatureToHumidity;
        private final List<RangeMap> humidityToLocation;

        private long apply(long input, List<RangeMap> rangeMapList) {
            Optional<RangeMap> inMap = rangeMapList.stream()
                    .filter(rangeMap -> rangeMap.containsSource(input)).findFirst();
            return inMap.map(rangeMap -> rangeMap.findDestination(input)).orElse(input);
        }

        private long findLocation(long seed) {
            long result = seed;
            for (List<RangeMap> map : allMaps) {
                result = apply(result, map);
            }
            return result;
        }

        public long findLowestLocationOfSeeds() {
            return seeds.stream().map(this::findLocation).min(Long::compare)
                    .orElseThrow(() -> new IllegalStateException("Can not find lowest location."));
        }

        public long findLowestLocationOfSeedRanges() {
            Long lowest = null;
            for (Range seedRange : seedRanges) {
                for (long i = seedRange.from(); i <= seedRange.to(); i++) {
                    long seedLocation = findLocation(i);
                    if (lowest == null || lowest > seedLocation) {
                        lowest = seedLocation;
                    }
                }
            }
            if (lowest == null) {
                throw new IllegalStateException("Can not find lowest location.");
            }
            return lowest;
        }
    }

    @UtilityClass
    protected static class InputParser {

        public static Almanac parseAlmanac(List<String> input) {
            List<Long> seeds = parseSeeds(input.get(0));
            List<Range> ranges = parseSeedRanges(input.get(0));
            List<List<RangeMap>> rangeLists = new ArrayList<>();
            List<RangeMap> currentRangeListMap = new ArrayList<>();
            for (int i = 3; i < input.size(); i++) {
                String line = input.get(i);
                if (StringUtils.isNotEmpty(line)) {
                    if (line.contains(":")) {
                        rangeLists.add(currentRangeListMap);
                        currentRangeListMap = new ArrayList<>();
                    } else {
                        currentRangeListMap.add(parseRange(line));
                    }
                }
            }
            rangeLists.add(currentRangeListMap);
            return new Almanac(seeds, ranges, rangeLists, rangeLists.get(0), rangeLists.get(1), rangeLists.get(2),
                    rangeLists.get(3), rangeLists.get(4), rangeLists.get(5), rangeLists.get(6));
        }

        public static List<Long> parseSeeds(String input) {
            return Arrays.stream(input.replace("seeds: ", "").split(" ")).map(Long::parseLong).toList();
        }

        public static List<Range> parseSeedRanges(String input) {
            Queue<Long> seeds = new LinkedList<>(parseSeeds(input));
            List<Range> ranges = new ArrayList<>();
            while (seeds.peek() != null) {
                long from = seeds.poll();
                Long length = seeds.poll();
                assert (length != null);
                long to = from + length - 1;
                ranges.add(new Range(from, to));
            }
            return ranges;
        }

        public static RangeMap parseRange(String input) {
            List<Long> numbers = Arrays.stream(input.split(" ")).map(Long::parseLong).toList();
            return new RangeMap(numbers.get(1), numbers.get(0), numbers.get(2));
        }
    }
}
