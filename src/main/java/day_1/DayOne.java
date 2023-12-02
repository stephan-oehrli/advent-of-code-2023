package day_1;

import lombok.RequiredArgsConstructor;
import utils.FileUtil;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import static java.lang.Character.getNumericValue;
import static java.lang.Character.isDigit;
import static org.apache.commons.lang3.StringUtils.reverse;

public class DayOne {

    public static void main(String[] args) throws FileNotFoundException {
        List<String> calibration = FileUtil.readToList("day_1.txt");
        int partOneSum = CalibrationSummarizer.withNumbers(calibration).summarize();
        System.out.println("Sum of part one is: " + partOneSum);
        int partTwoSum = CalibrationSummarizer.withWordsAndNumbers(calibration).summarize();
        System.out.println("Sum of part two is: " + partTwoSum);
    }

    @RequiredArgsConstructor
    protected static class CalibrationSummarizer {
        private final List<String> calibration;
        private final Interpreter interpreter;

        public int summarize() {
            int sum = 0;
            for (String input : calibration) {
                sum += interpreter.findTwoDigitNumber(input);
            }
            return sum;
        }
        
        public static CalibrationSummarizer withNumbers(List<String> calibration) {
            return new CalibrationSummarizer(calibration, new NumberInterpreter());
        }

        public static CalibrationSummarizer withWordsAndNumbers(List<String> calibration) {
            return new CalibrationSummarizer(calibration, new WordAndNumberInterpreter());
        }
    }

    protected interface Interpreter {
        int findTwoDigitNumber(String input);
    }

    protected static class NumberInterpreter implements Interpreter {
        
        @Override
        public int findTwoDigitNumber(String input) {
            input = input.replaceAll("[^0-9]", "");
            char firstDigit = input.charAt(0);
            char secondDigit = input.length() > 1 ? input.charAt(input.length() - 1) : firstDigit;
            return Integer.parseInt(firstDigit + "" + secondDigit);
        }
    }

    protected static class WordAndNumberInterpreter implements Interpreter {
        
        private record WordNumber(String word, int number) {
        }
        
        private final List<WordNumber> mapping = Arrays.asList(
                new WordNumber("one", 1),
                new WordNumber("two", 2),
                new WordNumber("three", 3),
                new WordNumber("four", 4),
                new WordNumber("five", 5),
                new WordNumber("six", 6),
                new WordNumber("seven", 7),
                new WordNumber("eight", 8),
                new WordNumber("nine", 9)
        );

        @Override
        public int findTwoDigitNumber(String input) {
            return Integer.parseInt(findFirst(input) + "" + findLast(input));
        }

        private int findFirst(String input) {
            for (int i = 0; i < input.length(); i++) {
                if (isDigit(input.charAt(i))) {
                    return getNumericValue(input.charAt(i));
                }
                for (WordNumber wordNumber : mapping) {
                    if (input.substring(i).startsWith(wordNumber.word())) {
                        return wordNumber.number();
                    }
                }
            }
            throw new IllegalStateException("No word or number found for input: " + input);
        }

        private int findLast(String input) {
            String reversedInput = reverse(input);
            for (int i = 0; i < reversedInput.length(); i++) {
                if (isDigit(reversedInput.charAt(i))) {
                    return getNumericValue(reversedInput.charAt(i));
                }
                for (WordNumber wordNumber : mapping) {
                    if (reversedInput.substring(i).startsWith(reverse(wordNumber.word()))) {
                        return wordNumber.number();
                    }
                }
            }
            throw new IllegalStateException("No word or number found for input: " + input);
        }
    }
}
