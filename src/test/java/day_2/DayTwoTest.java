package day_2;

import day_2.DayTwo.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DayTwoTest {

    private static final List<String> TEST_INPUTS = Arrays.asList(
            "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green",
            "Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue",
            "Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red",
            "Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red",
            "Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green"
    );

    @ParameterizedTest
    @CsvSource(value = {
            "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green                  #1#3",
            "Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue        #2#3",
            "Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red#3#3",
            "Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red#4#3",
            "Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green                  #5#2",
    }, delimiter = '#')
    void should_parse_game(String input, int id, int cubeSetsSize) {
        Game game = InputParser.parseGame(input);

        assertThat(game.id()).isEqualTo(id);
        assertThat(game.cubeSets()).hasSize(cubeSetsSize);
    }

    @Test
    void should_create_cube_sets() {
        String input = "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green";
        Game game = InputParser.parseGame(input);

        assertThat(game.cubeSets()).containsExactly(
                new CubeSet(4, 0, 3),
                new CubeSet(1, 2, 6),
                new CubeSet(0, 2, 0)
        );
    }

    @Test
    void should_parse_games() {
        assertThat(InputParser.parseGames(TEST_INPUTS)).hasSize(5);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green                  #true ",
            "Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue        #true ",
            "Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red#false",
            "Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red#false",
            "Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green                  #true ",
    }, delimiter = '#')
    void should_check_game_possibility(String input, boolean isPossible) {
        Configuration configuration = new Configuration(12, 13, 14);
        Game game = InputParser.parseGame(input);

        assertThat(game.isPossibleWith(configuration)).isEqualTo(isPossible);
    }

    @Test
    void should_sum_possible_game_ids() {
        Configuration configuration = new Configuration(12, 13, 14);
        assertThat(GameSummarizer.sumPossibleGameIds(TEST_INPUTS, configuration)).isEqualTo(8);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green                  #48  ",
            "Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue        #12  ",
            "Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red#1560",
            "Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red#630 ",
            "Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green                  #36  ",
    }, delimiter = '#')
    void should_calculate_game_power(String input, long power) {
        Game game = InputParser.parseGame(input);

        assertThat(game.calculateGamePower()).isEqualTo(power);
    }

    @Test
    void should_sum_game_power() {
        assertThat(GameSummarizer.sumGamePower(TEST_INPUTS)).isEqualTo(2286L);
    }
}