package day_4;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import utils.FileUtil;

import java.io.FileNotFoundException;
import java.util.*;

public class DayFour {

    public static void main(String[] args) throws FileNotFoundException {
        List<String> inputs = FileUtil.readToList("day_4.txt");
        Integer points = inputs.stream().map(ParserTool::parseCard).map(Card::calculatePoints).reduce(0, Integer::sum);
        System.out.println("Total worth of points is: " + points);
        ScratchGame scratchGame = new ScratchGame(ParserTool.parseCards(inputs));
        int finalCardsCount = scratchGame.play();
        System.out.println("Final count of cards is: " + finalCardsCount);
    }

    protected static class ScratchGame {

        private final List<Card> allCards;
        private final Queue<Card> cardQueue = new LinkedList<>();
        
        private int amountOfCards;
        
        public ScratchGame(List<Card> cards) {
            allCards = cards;
            cardQueue.addAll(cards);
            amountOfCards = cards.size();
        }
        
        public int play() {
            while (cardQueue.peek() != null) {
                Card card = cardQueue.poll();
                int matchingNumbersSize = card.getMatchingNumbersSize();
                amountOfCards += matchingNumbersSize;
                for (int i = 1; i <= matchingNumbersSize; i++) {
                    cardQueue.offer(allCards.get(card.id + i - 1));
                }
            }
            return amountOfCards;
        }
    }

    @EqualsAndHashCode
    @RequiredArgsConstructor
    protected static class Card {
        private final int id;
        private final List<Integer> winningNumbers;
        private final List<Integer> cardNumbers;

        private List<Integer> matchingNumbers;

        public int calculatePoints() {
            int matchingNumbersSize = getMatchingNumbersSize();
            if (matchingNumbersSize == 0) {
                return 0;
            }
            return (int) Math.pow(2, matchingNumbersSize - 1);
        }

        public int getMatchingNumbersSize() {
            if (matchingNumbers == null) {
                matchingNumbers = new ArrayList<>(cardNumbers);
                matchingNumbers.retainAll(winningNumbers);
            }
            return matchingNumbers.size();
        }
    }

    @UtilityClass
    protected static class ParserTool {
        
        public static List<Card> parseCards(List<String> input) {
            return input.stream().map(ParserTool::parseCard).toList();
        }

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
