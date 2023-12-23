package day_9;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import utils.FileUtil;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class DayNine {

    public static void main(String[] args) throws FileNotFoundException {
        List<History> histories = InputParser.parse(FileUtil.readToList("day_9.txt"));
        Long sumOfNextValues = histories.stream().map(History::findNextValue).reduce(0L, Long::sum);
        System.out.println("Sum of next values: " + sumOfNextValues);
        Long sumOfPreviousValues = histories.stream().map(History::findPreviousValue).reduce(0L, Long::sum);
        System.out.println("Sum of previous values: " + sumOfPreviousValues);
    }

    @RequiredArgsConstructor
    protected static class History {

        @Getter(AccessLevel.PACKAGE)
        protected final List<Long> entry;

        public long findNextValue() {
            Stack<List<Long>> stack = getDifferenceStack();
            return stack.stream().mapToLong(row -> row.get(row.size() - 1)).sum();
        }

        public long findPreviousValue() {
            Stack<List<Long>> stack = getDifferenceStack();
            long previousValue = 0;
            while (!stack.isEmpty()) {
                List<Long> row = stack.pop();
                previousValue = row.get(0) - previousValue;
            }
            return previousValue;
        }

        private Stack<List<Long>> getDifferenceStack() {
            Stack<List<Long>> stack = new Stack<>();
            List<Long> currentRow = entry;

            while (!isEachNumberZero(currentRow)) {
                stack.push(currentRow);
                currentRow = calculateDifference(currentRow);
            }
            return stack;
        }

        private boolean isEachNumberZero(List<Long> row) {
            return row.stream().allMatch(number -> number == 0L);
        }

        private List<Long> calculateDifference(List<Long> row) {
            List<Long> result = new ArrayList<>();
            for (int i = 0; i < row.size() - 1; i++) {
                result.add(row.get(i + 1) - row.get(i));
            }
            return result;
        }
    }

    @UtilityClass
    protected static class InputParser {

        public List<History> parse(List<String> input) {
            return input.stream()
                    .map(line -> Arrays.stream(line.split(" ")).map(Long::parseLong).toList())
                    .map(History::new)
                    .toList();
        }
    }
}
