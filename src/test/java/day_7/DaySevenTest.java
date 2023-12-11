package day_7;

import day_7.DaySeven.Game;
import day_7.DaySeven.Hand;
import day_7.DaySeven.InputParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.List;

import static java.util.Comparator.comparingInt;
import static org.assertj.core.api.Assertions.assertThat;

class DaySevenTest {

    private static final List<String> TEST_INPUT = Arrays.asList(
            "32T3K 765",
            "T55J5 684",
            "KK677 28",
            "KTJJT 220",
            "QQQJA 483"
    );

    @Test
    void should_parse_hands() {
        assertThat(InputParser.parseHands(TEST_INPUT)).map(Hand::getValue)
                .containsExactly("32T3K", "T55J5", "KK677", "KTJJT", "QQQJA");
        assertThat(InputParser.parseHands(TEST_INPUT)).map(Hand::getBid)
                .containsExactly(765, 684, 28, 220, 483);
    }

    @ParameterizedTest
    @CsvSource({
            "AAAAA,true ",
            "T55J5,false",
            "22222,true ",
            "AAAAK,false",
            "AAAKK,false",
            "A2AA2,false",
            "A2KA2,false",
            "AKQAT,false",
            "AKQJT,false"
    })
    void should_test_five_of_a_kind(String value, boolean expected) {
        assertThat(new Hand(value, 0).isFiveOfAKind()).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "AAAAA,false",
            "T55J5,false",
            "22222,false",
            "AAAAK,true ",
            "AAAKK,false",
            "A2AA2,false",
            "A2KA2,false",
            "AKQAT,false",
            "AKQJT,false"
    })
    void should_test_four_of_a_kind(String value, boolean expected) {
        assertThat(new Hand(value, 0).isFourOfAKind()).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "AAAAA,false",
            "T55J5,false",
            "22222,false",
            "AAAAK,false",
            "AAAKK,true ",
            "A2AA2,true ",
            "A2KA2,false",
            "AKQAT,false",
            "AKQJT,false"
    })
    void should_test_full_house(String value, boolean expected) {
        assertThat(new Hand(value, 0).isFullHouse()).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "AAAAA,false",
            "T55J5,true ",
            "22222,false",
            "AAAAK,false",
            "AAAKK,false",
            "A2AA2,false",
            "A2KA2,false",
            "AKQAT,false",
            "AKQJT,false"
    })
    void should_test_three_of_a_kind(String value, boolean expected) {
        assertThat(new Hand(value, 0).isThreeOfAKind()).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "AAAAA,false",
            "T55J5,false",
            "22222,false",
            "AAAAK,false",
            "AAAKK,false",
            "A2AA2,false",
            "A2KA2,true ",
            "AKQAT,false",
            "AKQJT,false"
    })
    void should_test_two_pair(String value, boolean expected) {
        assertThat(new Hand(value, 0).isTwoPair()).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "AAAAA,false",
            "T55J5,false",
            "22222,false",
            "AAAAK,false",
            "AAAKK,false",
            "A2AA2,false",
            "A2KA2,false",
            "AKQAT,true",
            "AKQJT,false"
    })
    void should_test_one_pair(String value, boolean expected) {
        assertThat(new Hand(value, 0).isOnePair()).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "AAAAA,68157439",
            "22222,67318579",
            "AAAAK,34603006",
            "AKQJT,2092491 "
    })
    void should_get_evaluation(String value, Integer expected) {
        assertThat(new Hand(value, 0).getEvaluation()).isEqualTo(expected);
    }

    @Test
    void should_sort_correctly() {
        List<Hand> hands = InputParser.parseHands(TEST_INPUT);
        hands.sort(comparingInt(Hand::getEvaluation));
        assertThat(hands).extracting(Hand::getValue)
                .containsExactly("32T3K", "KTJJT", "KK677", "T55J5", "QQQJA");
    }

    @Test
    void should_sort_correctly_with_joker_rule() {
        List<Hand> hands = InputParser.parseHands(TEST_INPUT);
        hands.sort(comparingInt(Hand::getJokerEvaluation));
        assertThat(hands).extracting(Hand::getValue)
                .containsExactly("32T3K", "KK677", "T55J5", "QQQJA", "KTJJT");
    }

    @Test
    void should_calculate_winnings() {
        List<Hand> hands = InputParser.parseHands(TEST_INPUT);
        assertThat(Game.calculateWinnings(hands, Hand::getEvaluation)).isEqualTo(6440L);
    }

    @Test
    void should_calculate_winnings_with_joker_rule() {
        List<Hand> hands = InputParser.parseHands(TEST_INPUT);
        assertThat(Game.calculateWinnings(hands, Hand::getJokerEvaluation)).isEqualTo(5905L);
    }
}