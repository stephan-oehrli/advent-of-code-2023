package day_8;

import day_8.DayEight.InputParser;
import day_8.DayEight.NetworkNavigation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class DayEightTest {

    private static final List<String> TEST_INPUT = Arrays.asList(
            "RL",
            "",
            "AAA = (BBB, CCC)",
            "BBB = (DDD, EEE)",
            "CCC = (ZZZ, GGG)",
            "DDD = (DDD, DDD)",
            "EEE = (EEE, EEE)",
            "GGG = (GGG, GGG)",
            "ZZZ = (ZZZ, ZZZ)"
    );

    private static final List<String> TEST_INPUT_2 = Arrays.asList(
            "LLR",
            "",
            "AAA = (BBB, BBB)",
            "BBB = (AAA, ZZZ)",
            "ZZZ = (ZZZ, ZZZ)"
    );

    private static final List<String> TEST_INPUT_3 = Arrays.asList(
            "LR",
            "",
            "11A = (11B, XXX)",
            "11B = (XXX, 11Z)",
            "11Z = (11B, XXX)",
            "22A = (22B, XXX)",
            "22B = (22C, 22C)",
            "22C = (22Z, 22Z)",
            "22Z = (22B, 22B)",
            "XXX = (XXX, XXX)"
    );

    @Test
    void should_parse_network_navigation() {
        NetworkNavigation networkNavigation = InputParser.parseNetworkNavigation(TEST_INPUT);

        assertThat(networkNavigation.getInstructions()).hasSize(2);
        assertThat(networkNavigation.getNetworkMap()).hasSize(7);
    }

    @ParameterizedTest
    @MethodSource("testInputProvider")
    void should_calculate_steps_to_reach_ZZZ(List<String> input, Long expectedSteps) {
        assertThat(InputParser.parseNetworkNavigation(input).calculateStepsToReachZZZ()).isEqualTo(expectedSteps);
    }

    static Stream<Arguments> testInputProvider() {
        return Stream.of(
                Arguments.arguments(TEST_INPUT, 2L),
                Arguments.arguments(TEST_INPUT_2, 6L)
        );
    }

    @Test
    void should_calculate_ghost_steps() {
        NetworkNavigation networkNavigation = InputParser.parseNetworkNavigation(TEST_INPUT_3);
        assertThat(networkNavigation.calculateGhostSteps()).isEqualTo(6);
    }
}