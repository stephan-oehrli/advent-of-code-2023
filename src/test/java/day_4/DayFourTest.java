package day_4;

import day_4.DayFour.Card;
import day_4.DayFour.ParserTool;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static day_4.DayFour.*;
import static org.assertj.core.api.Assertions.assertThat;

class DayFourTest {

    private final List<String> TEST_INPUT = Arrays.asList(
            "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53",
            "Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19",
            "Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1",
            "Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83",
            "Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36",
            "Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11"
    );

    static Stream<Arguments> cardsArguments() {
        return Stream.of(
                Arguments.of(
                        "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53", 1, 8,
                        Arrays.asList(41, 48, 83, 86, 17),
                        Arrays.asList(83, 86, 6, 31, 17, 9, 48, 53)
                ),
                Arguments.of(
                        "Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19", 2, 2,
                        Arrays.asList(13, 32, 20, 16, 61),
                        Arrays.asList(61, 30, 68, 82, 17, 32, 24, 19)
                ),
                Arguments.of(
                        "Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1", 3, 2,
                        Arrays.asList(1, 21, 53, 59, 44),
                        Arrays.asList(69, 82, 63, 72, 16, 21, 14, 1)
                ),
                Arguments.of(
                        "Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83", 4, 1,
                        Arrays.asList(41, 92, 73, 84, 69),
                        Arrays.asList(59, 84, 76, 51, 58, 5, 54, 83)
                ),
                Arguments.of(
                        "Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36", 5, 0,
                        Arrays.asList(87, 83, 26, 28, 32),
                        Arrays.asList(88, 30, 70, 12, 93, 22, 82, 36)
                ),
                Arguments.of(
                        "Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11", 6, 0,
                        Arrays.asList(31, 18, 13, 56, 72),
                        Arrays.asList(74, 77, 10, 23, 35, 67, 36, 11)
                )
        );
    }

    @ParameterizedTest(name = "Card {index}")
    @MethodSource("cardsArguments")
    void should_parse_card(String input, int cardId, int ignore,
                           List<Integer> expectedWinningNumbers, List<Integer> expectedCardNumbers) {

        Card expected = new Card(cardId, expectedWinningNumbers, expectedCardNumbers);
        assertThat(ParserTool.parseCard(input)).isEqualTo(expected);
    }

    @ParameterizedTest(name = "Card {index}")
    @MethodSource("cardsArguments")
    void should_calculate_points(String input, int ignore, int expectedPoints) {
        Card card = ParserTool.parseCard(input);
        assertThat(card.calculatePoints()).isEqualTo(expectedPoints);
    }
    
    @Test
    void should_count_cards() {
        ScratchGame scratchGame = new ScratchGame(ParserTool.parseCards(TEST_INPUT));
        assertThat(scratchGame.play()).isEqualTo(30);
    }

}