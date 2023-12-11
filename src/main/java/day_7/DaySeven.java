package day_7;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import utils.FileUtil;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;
import static java.util.Comparator.comparingInt;
import static org.apache.commons.lang3.StringUtils.containsOnly;
import static org.apache.commons.lang3.StringUtils.countMatches;

public class DaySeven {

    public static void main(String[] args) throws FileNotFoundException {
        List<Hand> hands = InputParser.parseHands(FileUtil.readToList("day_7.txt"));
        Long winnings = Game.calculateWinnings(hands, Hand::getEvaluation);
        System.out.println("Total winnings are: " + winnings);
        Long winningsWithJokerRule = Game.calculateWinnings(hands, Hand::getJokerEvaluation);
        System.out.println("Total winnings with the joker rule are: " + winningsWithJokerRule);
    }

    @UtilityClass
    protected static class Game {

        public Long calculateWinnings(List<Hand> hands, ToIntFunction<Hand> evaluationSupplier) {
            hands.sort(comparingInt(evaluationSupplier));
            long totalWinnings = 0;
            for (int i = 0; i < hands.size(); i++) {
                totalWinnings += (long) hands.get(i).getBid() * (i + 1);
            }
            return totalWinnings;
        }
    }

    @Getter
    @EqualsAndHashCode
    @RequiredArgsConstructor
    protected static class Hand {

        @NonNull
        private String value;
        private final int bid;

        private Integer evaluation;
        private Integer jokerEvaluation;

        private final List<Supplier<String>> typeMapping = Arrays.asList(
                () -> isFiveOfAKind() ? "1000000" : null,
                () -> isFourOfAKind() ? "0100000" : null,
                () -> isFullHouse() ? "0010000" : null,
                () -> isThreeOfAKind() ? "0001000" : null,
                () -> isTwoPair() ? "0000100" : null,
                () -> isOnePair() ? "0000010" : null,
                () -> "0000001"
        );

        private final Map<Character, String> cardMapping = Map.ofEntries(
                Map.entry('A', "1111"),
                Map.entry('K', "1110"),
                Map.entry('Q', "1101"),
                Map.entry('J', "1100"),
                Map.entry('T', "1011"),
                Map.entry('9', "1010"),
                Map.entry('8', "1001"),
                Map.entry('7', "1000"),
                Map.entry('6', "0111"),
                Map.entry('5', "0110"),
                Map.entry('4', "0101"),
                Map.entry('3', "0100"),
                Map.entry('2', "0011")
        );

        public boolean isFiveOfAKind() {
            return containsOnly(value, value.charAt(0));
        }

        public boolean isFourOfAKind() {
            return countMatches(value, value.charAt(0)) == 4 || countMatches(value, value.charAt(1)) == 4;
        }

        public boolean isFullHouse() {
            int firstMatch = countMatches(value, value.charAt(0));
            if (!(firstMatch == 3 || firstMatch == 2)) {
                return false;
            }
            String removedMatches = value.replace(valueOf(value.charAt(0)), "");
            int secondMatch = countMatches(removedMatches, removedMatches.charAt(0));
            return (firstMatch == 3 && secondMatch == 2) || (firstMatch == 2 && secondMatch == 3);
        }

        public boolean isThreeOfAKind() {
            if (isFullHouse()) {
                return false;
            }
            return countMatches(value, value.charAt(0)) == 3 ||
                    countMatches(value, value.charAt(1)) == 3 ||
                    countMatches(value, value.charAt(2)) == 3;
        }

        public boolean isTwoPair() {
            return getNumOfPairs() == 2;
        }

        public boolean isOnePair() {
            return getNumOfPairs() == 1 && !isFullHouse();
        }

        private int getNumOfPairs() {
            Map<Character, Integer> map = new HashMap<>();
            for (Character c : value.toCharArray()) {
                map.put(c, map.getOrDefault(c, 0) + 1);
            }
            List<Integer> pairs = map.values().stream().filter(amount -> amount == 2).toList();
            return pairs.size();
        }

        private String calculateHexValueOfType() {
            return typeMapping.stream()
                    .map(Supplier::get)
                    .filter(StringUtils::isNotEmpty)
                    .findFirst().orElseThrow();
        }

        public int getEvaluation() {
            if (evaluation != null) {
                return evaluation;
            }
            StringBuilder hexEvaluation = new StringBuilder(calculateHexValueOfType());
            for (Character c : value.toCharArray()) {
                hexEvaluation.append(cardMapping.get(c));
            }
            evaluation = Integer.parseUnsignedInt(hexEvaluation.toString(), 2);
            return evaluation;
        }

        public int getJokerEvaluation() {
            if (jokerEvaluation != null) {
                return jokerEvaluation;
            }
            String previousValue = value;
            applyJoker();
            StringBuilder hexEvaluation = new StringBuilder(calculateHexValueOfType());
            value = previousValue;
            Map<Character, String> map = new HashMap<>(cardMapping);
            map.put('J', "0010");
            for (Character c : value.toCharArray()) {
                hexEvaluation.append(map.get(c));
            }
            jokerEvaluation = Integer.parseUnsignedInt(hexEvaluation.toString(), 2);
            return jokerEvaluation;
        }

        private void applyJoker() {
            if (value.equals("JJJJJ")) {
                value = "AAAAA";
            } else if (value.contains("J")) {
                Map<Character, Integer> map = new HashMap<>();
                for (Character c : value.toCharArray()) {
                    map.put(c, map.getOrDefault(c, 0) + 1);
                }
                Character jokerReplacement = map.entrySet().stream()
                        .filter(e -> e.getKey() != 'J')
                        .max(comparingInt(Entry::getValue))
                        .orElseThrow()
                        .getKey();
                value = value.replace('J', jokerReplacement);
            }
        }
    }

    @UtilityClass
    protected class InputParser {

        public List<Hand> parseHands(List<String> input) {
            List<Hand> hands = new ArrayList<>();
            input.forEach(l -> hands.add(new Hand(l.split(" ")[0], parseInt(l.split(" ")[1]))));
            return hands;
        }
    }
}
