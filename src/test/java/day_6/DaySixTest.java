package day_6;

import day_6.DaySix.Race;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.List;

import static day_6.DaySix.InputParser.parseRaceFromListWithKerning;
import static day_6.DaySix.InputParser.parseRaces;
import static org.assertj.core.api.Assertions.assertThat;

class DaySixTest {

    private static final List<String> TEST_INPUT = Arrays.asList(
            "Time:      7  15   30",
            "Distance:  9  40  200"
    );

    @Test
    void should_parse_races() {
        assertThat(parseRaces(TEST_INPUT)).containsExactly(
                new Race(7, 9),
                new Race(15, 40),
                new Race(30, 200)
        );
    }

    @Test
    void should_parse_race_from_list_with_kerning() {
        assertThat(parseRaceFromListWithKerning(TEST_INPUT))
                .isEqualTo(new Race(71530, 940200));
    }

    @ParameterizedTest
    @CsvSource({
            "7 ,  9,4",
            "15, 40,8",
            "30,200,9"
    })
    void should_calculate_number_of_way_to_beat_record(int time, int distance, int expected) {
        Race race = new Race(time, distance);
        assertThat(race.calculateNumberOfWaysToBeatRecord()).isEqualTo(expected);
    }
}