package day_10;

import day_10.DayTen.InputParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class DayTenTest {

    private static final List<String> TEST_INPUT = Arrays.asList(
            "-L|F7",
            "7S-7|",
            "L|7||",
            "-L-J|",
            "L|-JF"
    );

    private static final List<String> TEST_INPUT_2 = Arrays.asList(
            "...........",
            ".S-------7.",
            ".|F-----7|.",
            ".||.....||.",
            ".||.....||.",
            ".|L-7.F-J|.",
            ".|..|.|..|.",
            ".L--J.L--J.",
            "..........."
    );

    private static final List<String> TEST_INPUT_3 = Arrays.asList(
            "..........",
            ".S------7.",
            ".|F----7|.",
            ".||OOOO||.",
            ".||OOOO||.",
            ".|L-7F-J|.",
            ".|II||II|.",
            ".L--JL--J.",
            ".........."
    );

    @Test
    void should_improve_readability() {
        assertThat(InputParser.parseGrid(TEST_INPUT).toString()).isEqualTo("""
                -└|┌┐
                ┐S-┐|
                └|┐||
                -└-┘|
                └|-┘┌
                """);
    }

    @Test
    void should_count_steps_to_farthest_position() {
        assertThat(InputParser.parseGrid(TEST_INPUT).countStepsToFarthestPosition()).isEqualTo(4);
    }

    @ParameterizedTest
    @MethodSource("areaTestInputProvider")
    void should_calculate_enclosed_positions(List<String> input, int expected) {
        assertThat(InputParser.parseGrid(input).calculateEnclosedPositions()).isEqualTo(expected);
    }
    
    static Stream<Arguments> areaTestInputProvider() {
        return Stream.of(
                arguments(TEST_INPUT, 1),
                arguments(TEST_INPUT_2, 4),
                arguments(TEST_INPUT_3, 4)
        );
    }
}