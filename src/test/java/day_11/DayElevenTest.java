package day_11;

import day_11.DayEleven.InputParser;
import day_11.DayEleven.Pair;
import day_11.DayEleven.Position;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DayElevenTest {

    private static final List<String> TEST_INPUT = List.of(
            "...#......",
            ".......#..",
            "#.........",
            "..........",
            "......#...",
            ".#........",
            ".........#",
            "..........",
            ".......#..",
            "#...#....."
    );

    @Test
    void should_retrieve_positions() {
        assertThat(InputParser.findPositions(TEST_INPUT, 1)).isEqualTo(List.of(
                new Position(3, 0),
                new Position(7, 1),
                new Position(0, 2),
                new Position(6, 4),
                new Position(1, 5),
                new Position(9, 6),
                new Position(7, 8),
                new Position(0, 9),
                new Position(4, 9)
        ));
    }

    @Test
    void should_retrieve_expanded_positions() {
        assertThat(InputParser.findPositions(TEST_INPUT, 2)).isEqualTo(List.of(
                new Position(4, 0),
                new Position(9, 1),
                new Position(0, 2),
                new Position(8, 5),
                new Position(1, 6),
                new Position(12, 7),
                new Position(9, 10),
                new Position(0, 11),
                new Position(5, 11)
        ));
    }

    @Test
    void should_create_pairs() {
        List<Position> positions = InputParser.findPositions(TEST_INPUT, 2);
        assertThat(InputParser.createPairs(positions))
                .hasSize(36)
                .contains(
                        new Pair(new Position(4, 0), new Position(9, 1), 6),
                        new Pair(new Position(4, 0), new Position(5, 11), 12),
                        new Pair(new Position(8, 5), new Position(5, 11), 9),
                        new Pair(new Position(0, 11), new Position(5, 11), 5)
                );
    }

    @ParameterizedTest
    @CsvSource({"2,374", "10,1030", "100,8410"})
    void should_sum_shortest_path_of_all_pairs(Integer expansionBy, Long expected) {
        List<Position> positions = InputParser.findPositions(TEST_INPUT, expansionBy);
        List<Pair> pairs = InputParser.createPairs(positions);
        Long sum = pairs.stream().map(Pair::distance).reduce(Long::sum).orElseThrow();
        assertThat(sum).isEqualTo(expected);
    }
}