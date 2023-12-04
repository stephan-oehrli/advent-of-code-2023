package day_4;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import utils.FileUtil;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DayFour {

    public static void main(String[] args) throws FileNotFoundException {
        List<String> inputs = FileUtil.readToList("day_4.txt");
        Integer points = inputs.stream().map(ParserTool::parseCard).map(Card::calculatePoints).reduce(0, Integer::sum);
        System.out.println("Total worth of points is: " + points);
    }

    @EqualsAndHashCode
    @RequiredArgsConstructor
    protected static final class Card {
        private final int id;
        private final List<Integer> winningNumbers;
        private final List<Integer> cardNumbers;

        private List<Integer> matchingNumbers;

        public int calculatePoints() {
            if (matchingNumbers == null) {
                matchingNumbers = new ArrayList<>(cardNumbers);
                matchingNumbers.retainAll(winningNumbers);
            }
            if (matchingNumbers.isEmpty()) {
                return 0;
            }
            return (int) Math.pow(2, matchingNumbers.size() - 1);
        }
    }

    @UtilityClass
    protected static class ParserTool {

        public static Card parseCard(String input) {
            List<String> cardIdPart = Arrays.stream(input.split(":")[0].split(" "))
                    .filter(StringUtils::isNotEmpty).toList();
            int id = Integer.parseInt(cardIdPart.get(1));
            String[] numbers = input.split(":")[1].split("\\|");
            List<Integer> winningNumbers = parseNumbers(numbers[0]);
            List<Integer> cardNumbers = parseNumbers(numbers[1]);
            return new Card(id, winningNumbers, cardNumbers);
        }

        private static List<Integer> parseNumbers(String numbers) {
            return Arrays.stream(numbers.split(" ")).filter(StringUtils::isNotEmpty).map(Integer::parseInt).toList();
        }
    }
}
