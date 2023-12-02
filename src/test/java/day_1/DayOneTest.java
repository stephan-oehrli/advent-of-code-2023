package day_1;

import day_1.DayOne.CalibrationSummarizer;
import day_1.DayOne.NumberInterpreter;
import day_1.DayOne.WordAndNumberInterpreter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DayOneTest {

    @ParameterizedTest
    @CsvSource({
            "1abc2      ,12",
            "pqr3stu8vwx,38",
            "a1b2c3d4e5f,15",
            "treb7uchet ,77",
    })
    void should_find_two_digit_number(String input, int expected) {
        assertThat(new NumberInterpreter().findTwoDigitNumber(input)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "two1nine        ,29",
            "eightwothree    ,83",
            "abcone2threexyz ,13",
            "xtwone3four     ,24",
            "4nineeightseven2,42",
            "zoneight234     ,14",
            "7pqrstsixteen   ,76",
    })
    void should_find_two_digit_word_number(String input, int expected) {
        assertThat(new WordAndNumberInterpreter().findTwoDigitNumber(input)).isEqualTo(expected);
    }

    @Test
    void should_sum_calibration() {
        List<String> calibration = Arrays.asList("1abc2", "pqr3stu8vwx", "a1b2c3d4e5f", "treb7uchet");
        assertThat(CalibrationSummarizer.withNumbers(calibration).summarize()).isEqualTo(142);
    }

    @Test
    void should_sum_word_number_calibration() {
        List<String> calibration = Arrays.asList(
                "two1nine",
                "eightwothree",
                "abcone2threexyz",
                "xtwone3four",
                "4nineeightseven2",
                "zoneight234",
                "7pqrstsixteen"
        );
        assertThat(CalibrationSummarizer.withWordsAndNumbers(calibration).summarize()).isEqualTo(281);
    }
}