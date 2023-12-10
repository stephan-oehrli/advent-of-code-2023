package day_5;

import day_5.DayFive.Almanac;
import day_5.DayFive.Range;
import day_5.DayFive.RangeMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.List;

import static day_5.DayFive.InputParser.*;
import static org.assertj.core.api.Assertions.assertThat;

class DayFiveTest {

    private static final List<String> TEST_INPUT = Arrays.asList(
            "seeds: 79 14 55 13",
            "",
            "seed-to-soil map:",
            "50 98 2",
            "52 50 48",
            "",
            "soil-to-fertilizer map:",
            "0 15 37",
            "37 52 2",
            "39 0 15",
            "",
            "fertilizer-to-water map:",
            "49 53 8",
            "0 11 42",
            "42 0 7",
            "57 7 4",
            "",
            "water-to-light map:",
            "88 18 7",
            "18 25 70",
            "",
            "light-to-temperature map:",
            "45 77 23",
            "81 45 19",
            "68 64 13",
            "",
            "temperature-to-humidity map:",
            "0 69 1",
            "1 0 69",
            "",
            "humidity-to-location map:",
            "60 56 37",
            "56 93 4"
    );

    @Test
    void should_parse_seeds() {
        assertThat(parseSeeds("seeds: 79 14 55 4173137165")).containsExactly(79L, 14L, 55L, 4173137165L);
    }

    @Test
    void should_parse_seed_ranges() {
        List<Range> ranges = parseSeedRanges("seeds: 79 14 55 13");

        assertThat(ranges).hasSize(2);
        assertThat(ranges.get(0)).isEqualTo(new Range(79L, 92L));
        assertThat(ranges.get(1)).isEqualTo(new Range(55L, 67L));
    }

    @Test
    void should_parse_range() {
        assertThat(parseRange("3266233336 2662846763 101445145")).extracting(
                RangeMap::sourceCategoryStart,
                RangeMap::destinationCategoryStart,
                RangeMap::rangeLength
        ).containsExactly(2662846763L, 3266233336L, 101445145L);
    }

    @Test
    void should_parse_almanac() {
        Almanac almanac = parseAlmanac(TEST_INPUT);

        assertThat(almanac.getSeeds()).hasSize(4);
        assertThat(almanac.getSeedRanges()).hasSize(2);
        assertThat(almanac.getSeedToSoil()).hasSize(2);
        assertThat(almanac.getSoilToFertilizer()).hasSize(3);
        assertThat(almanac.getFertilizerToWater()).hasSize(4);
        assertThat(almanac.getWaterToLight()).hasSize(2);
        assertThat(almanac.getLightToTemperature()).hasSize(3);
        assertThat(almanac.getTemperatureToHumidity()).hasSize(2);
        assertThat(almanac.getHumidityToLocation()).hasSize(2);
        assertThat(almanac.getAllMaps()).hasSize(7);
    }

    @ParameterizedTest
    @CsvSource({
            "97 ,false",
            "98 ,true ",
            "99 ,true ",
            "100,false"
    })
    void should_retrieve_contains_source(long input, boolean expected) {
        RangeMap rangeMap = new RangeMap(98L, 50L, 2);
        assertThat(rangeMap.containsSource(input)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "98, 50",
            "99, 51"
    })
    void should_find_destination(long input, long expected) {
        RangeMap rangeMap = new RangeMap(98L, 50L, 2);
        assertThat(rangeMap.findDestination(input)).isEqualTo(expected);
    }

    @Test
    void should_find_lowest_location_of_seeds() {
        Almanac almanac = parseAlmanac(TEST_INPUT);
        assertThat(almanac.findLowestLocationOfSeeds()).isEqualTo(35L);
    }

    @Test
    void should_find_lowest_location_of_seed_ranges() {
        Almanac almanac = parseAlmanac(TEST_INPUT);
        assertThat(almanac.findLowestLocationOfSeedRanges()).isEqualTo(46L);
    }

    @Test
    void should_split_range() {
        List<RangeMap> rangeMaps = Arrays.asList(
                new RangeMap(50L, 70L, 20L),
                new RangeMap(40L, 30L, 10L)
        );
        Range range = new Range(20L, 69L);

        List<Range> ranges = range.splitBy(rangeMaps);

        assertThat(ranges).hasSize(3);
        assertThat(ranges).containsExactly(
                new Range(20L, 39L),
                new Range(40L, 49L),
                new Range(50L, 69L)
        );
    }
}