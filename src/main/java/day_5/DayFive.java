package day_5;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import utils.FileUtil;

import java.io.FileNotFoundException;
import java.util.*;

import static java.util.Comparator.comparingLong;

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

        public List<Range> splitBy(List<RangeMap> maps) {
            RangeSplitter rangeSplitter = new RangeSplitter(maps, new LinkedList<>(List.of(this)));
            return rangeSplitter.split();
        }
    }

    protected record RangeMap(long sourceCategoryStart, long destinationCategoryStart, long rangeLength) {

        public boolean containsSource(long input) {
            return input >= sourceCategoryStart && input < sourceCategoryStart + rangeLength;
        }

        public long findDestination(long input) {
            long index = input - sourceCategoryStart;
            return destinationCategoryStart + index;
        }

        public boolean sourceRangeHasNoIntersectionWith(Range range) {
            return range.to() < sourceCategoryStart || range.from() >= sourceCategoryStart + rangeLength;
        }

        public boolean sourceRangeContainsFull(Range range) {
            return range.from() >= sourceCategoryStart && range.to() < sourceCategoryStart + rangeLength;
        }
    }

    @RequiredArgsConstructor
    protected static class RangeSplitter {
        private final List<RangeMap> maps;
        private final Queue<Range> rangeQueue;

        private final List<Range> splitRanges = new ArrayList<>();

        public List<Range> split() {
            while (!rangeQueue.isEmpty()) {
                Range currentRange = rangeQueue.poll();
                if (!hasFoundAnotherRangeFragment(currentRange)) {
                    // No fragment found for maps -> remaining range fragment
                    splitRanges.add(currentRange);
                }
            }
            splitRanges.sort(comparingLong(Range::from));
            return splitRanges;
        }

        private boolean hasFoundAnotherRangeFragment(Range currentRange) {
            for (RangeMap map : maps) {
                // no intersection -> ignore this map
                if (map.sourceRangeHasNoIntersectionWith(currentRange)) {
                    continue;
                }
                // map source range contains entire seed range -> reached max split for this range
                if (map.sourceRangeContainsFull(currentRange)) {
                    splitRanges.add(currentRange);
                    return true;
                }
                // map source range contains upper seed range fragment -> split
                else if (currentRange.from() < map.sourceCategoryStart()) {
                    rangeQueue.add(new Range(currentRange.from(), map.sourceCategoryStart() - 1));
                    rangeQueue.add(new Range(map.sourceCategoryStart(), currentRange.to()));
                    return true;
                }
                // map source range contains lower seed range fragment -> split
                else if (currentRange.to() >= map.sourceCategoryStart() + map.rangeLength()) {
                    rangeQueue.add(new Range(currentRange.from(), map.sourceCategoryStart() + map.rangeLength() - 1));
                    rangeQueue.add(new Range(map.sourceCategoryStart() + map.rangeLength(), currentRange.to()));
                    return true;
                }
            }
            return false;
        }
    }

    @Getter
    @RequiredArgsConstructor
    @SuppressWarnings("ClassCanBeRecord")
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

        private List<Range> apply(Range range, List<RangeMap> rangeMapList) {
            return range.splitBy(rangeMapList).stream()
                    .map(r -> new Range(apply(r.from(), rangeMapList), apply(r.to(), rangeMapList))).toList();
        }

        private List<Range> apply(List<Range> ranges, List<RangeMap> rangeMapList) {
            List<Range> newRanges = new ArrayList<>();
            ranges.stream().map(r -> apply(r, rangeMapList)).forEach(newRanges::addAll);
            newRanges.sort(comparingLong(Range::from));
            return newRanges;
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
            List<Long> lowestLocationInRanges = new ArrayList<>();
            for (Range seedRange : seedRanges) {
                List<Range> seedRangesToApply = List.of(seedRange);
                for (List<RangeMap> maps : allMaps) {
                    seedRangesToApply = apply(seedRangesToApply, maps);
                }
                Range lowestLocationRange = seedRangesToApply.get(0);
                lowestLocationInRanges.add(lowestLocationRange.from());
            }
            lowestLocationInRanges.sort(Long::compare);
            return lowestLocationInRanges.get(0);
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
