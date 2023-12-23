package day_9;

import day_9.DayNine.History;
import day_9.DayNine.InputParser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DayNineTest {

    private static final List<String> TEST_INPUT = List.of(
            "0 3 6 9 12 15",
            "1 3 6 10 15 21",
            "10 13 16 21 30 45"
    );

    @Test
    void should_parse() {
        assertThat(InputParser.parse(TEST_INPUT))
                .extracting(History::getEntry).containsExactly(
                        List.of(0L, 3L, 6L, 9L, 12L, 15L),
                        List.of(1L, 3L, 6L, 10L, 15L, 21L),
                        List.of(10L, 13L, 16L, 21L, 30L, 45L)
                );
    }

    @Test
    void should_find_next_value() {
        List<History> histories = InputParser.parse(TEST_INPUT);

        assertThat(histories.get(0).findNextValue()).isEqualTo(18L);
        assertThat(histories.get(1).findNextValue()).isEqualTo(28L);
        assertThat(histories.get(2).findNextValue()).isEqualTo(68L);
    }

    @Test
    void should_find_previous_value() {
        List<History> histories = InputParser.parse(TEST_INPUT);

        assertThat(histories.get(0).findPreviousValue()).isEqualTo(-3L);
        assertThat(histories.get(1).findPreviousValue()).isEqualTo(0L);
        assertThat(histories.get(2).findPreviousValue()).isEqualTo(5L);
    }
}