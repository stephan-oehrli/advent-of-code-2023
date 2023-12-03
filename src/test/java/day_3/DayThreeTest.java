package day_3;

import day_3.DayThree.EngineSchematic;
import day_3.DayThree.GearLocation;
import day_3.DayThree.InputParser;
import day_3.DayThree.NumberLocation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class DayThreeTest {

    private final static List<String> TEST_INPUT = Arrays.asList(
            "467..114..",
            "...*......",
            "..35..633.",
            "......#...",
            "617*......",
            ".....+.58.",
            "..592.....",
            "......755.",
            "...$.*....",
            ".664.598.."
    );

    private final static List<String> TEST_INPUT_2 = Arrays.asList(
            "12.......*..",
            "+.........34",
            ".......-12..",
            "..78........",
            "..*....60...",
            "78.........9",
            ".5.....23..$",
            "8...90*12...",
            "............",
            "2.2......12.",
            ".*.........*",
            "1.1..503+.56"
    );

    private final static List<String> TEST_INPUT_3 = Arrays.asList(
            ".....24.*23.",
            "..10........",
            "..397*.610..",
            ".......50...",
            "1*2........."
    );

    private final static List<String> TEST_INPUT_4 = Arrays.asList(
            "333.3",
            "...*."
    );

    @Test
    void should_parse_to_number_location_list() {
        assertThat(InputParser.parseToNumberLocationList(TEST_INPUT)).containsExactly(
                new NumberLocation(467, 0, 0, 3),
                new NumberLocation(114, 5, 0, 3),
                new NumberLocation(35, 2, 2, 2),
                new NumberLocation(633, 6, 2, 3),
                new NumberLocation(617, 0, 4, 3),
                new NumberLocation(58, 7, 5, 2),
                new NumberLocation(592, 2, 6, 3),
                new NumberLocation(755, 6, 7, 3),
                new NumberLocation(664, 1, 9, 3),
                new NumberLocation(598, 5, 9, 3)
        );
    }

    @Test
    void should_parse_to_gear_location_list() {
        assertThat(InputParser.parseToGearLocationList(TEST_INPUT)).containsExactly(
                new GearLocation(3, 1, new ArrayList<>()),
                new GearLocation(3, 4, new ArrayList<>()),
                new GearLocation(5, 8, new ArrayList<>())
        );
    }

    @ParameterizedTest(name = "TEST_INPUT_{index}")
    @MethodSource("testInputProvider")
    void should_find_symbol_adjacent_numbers(List<String> input, int ignore, List<Integer> expected) {
        assertThat(new EngineSchematic(input).findSymbolAdjacentNumbers()).isEqualTo(expected);
    }

    @Test
    void should_find_gear_ratio() {
        EngineSchematic engineSchematic = new EngineSchematic(TEST_INPUT);
        assertThat(engineSchematic.findGearRatios()).isEqualTo(Arrays.asList(16345, 451490));
    }

    @ParameterizedTest(name = "TEST_INPUT_{index}")
    @MethodSource("testInputProvider")
    void should_sum_parts_numbers(List<String> input, int expected) {
        EngineSchematic engineSchematic = new EngineSchematic(input);
        assertThat(engineSchematic.findSymbolAdjacentNumbers().stream().reduce(0, Integer::sum)).isEqualTo(expected);
    }

    @Test
    void should_sum_gear_ratios() {
        EngineSchematic engineSchematic = new EngineSchematic(TEST_INPUT);
        assertThat(engineSchematic.findGearRatios().stream().reduce(0, Integer::sum)).isEqualTo(467835);
    }

    static Stream<Arguments> testInputProvider() {
        return Stream.of(
                Arguments.of(
                        TEST_INPUT, 4361,
                        Arrays.asList(467, 35, 633, 617, 592, 755, 664, 598)
                ),
                Arguments.of(
                        TEST_INPUT_2, 925,
                        Arrays.asList(12, 34, 12, 78, 78, 9, 23, 90, 12, 2, 2, 12, 1, 1, 503, 56)
                ),
                Arguments.of(
                        TEST_INPUT_3, 423,
                        Arrays.asList(23, 397, 1, 2)
                ),
                Arguments.of(
                        TEST_INPUT_4, 336,
                        Arrays.asList(333, 3)
                )
        );
    }
}