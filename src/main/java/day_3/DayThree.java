package day_3;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import utils.FileUtil;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Integer.parseInt;

public class DayThree {

    public static void main(String[] args) throws FileNotFoundException {
        List<String> inputs = FileUtil.readToList("day_3.txt");
        EngineSchematic engineSchematic = new EngineSchematic(inputs);
        List<Integer> symbolAdjacentNumbers = engineSchematic.findSymbolAdjacentNumbers();
        System.out.println("Sum of all parts in engine is: " + symbolAdjacentNumbers.stream().reduce(0, Integer::sum));
        List<Integer> gearRatios = engineSchematic.findGearRatios();
        System.out.println("Sum of all gear ratios is: " + gearRatios.stream().reduce(0, Integer::sum));
    }

    protected record NumberLocation(int number, int x, int y, int numberLength) {
    }

    protected record GearLocation(int x, int y, List<Integer> adjacentNumbers) {

        public boolean hasTwoAdjacentNumbers() {
            return adjacentNumbers.size() == 2;
        }
    }

    @UtilityClass
    protected static class InputParser {

        public static List<NumberLocation> parseToNumberLocationList(List<String> input) {
            List<NumberLocation> numberLocationList = new ArrayList<>();
            for (int y = 0; y < input.size(); y++) {
                String line = input.get(y).replaceAll("[^0-9]", ".");
                List<String> numbers = Arrays.stream(line.split("\\."))
                        .filter(StringUtils::isNotEmpty)
                        .filter(StringUtils::isNumeric)
                        .toList();
                for (String number : numbers) {
                    int x = line.indexOf(number);
                    int numberLength = number.length();
                    line = line.replaceFirst(number, ".".repeat(numberLength));
                    numberLocationList.add(new NumberLocation(parseInt(number), x, y, numberLength));
                }
            }
            return numberLocationList;
        }

        public static List<GearLocation> parseToGearLocationList(List<String> input) {
            List<GearLocation> gearLocationList = new ArrayList<>();
            for (int y = 0; y < input.size(); y++) {
                String line = input.get(y);
                for (int x = 0; x < line.length(); x++) {
                    if (line.charAt(x) == '*') {
                        gearLocationList.add(new GearLocation(x, y, new ArrayList<>()));
                    }
                }
            }
            return gearLocationList;
        }
    }

    protected static class EngineSchematic {

        private final List<String> partsPlan;
        private final List<NumberLocation> numberLocationList;

        private List<GearLocation> gearLocationList;

        protected EngineSchematic(List<String> partsPlan) {
            this.partsPlan = partsPlan;
            this.numberLocationList = InputParser.parseToNumberLocationList(partsPlan);
        }

        public List<Integer> findSymbolAdjacentNumbers() {
            ArrayList<Integer> symbolAdjacentNumbers = new ArrayList<>();
            numberLocationList.stream().filter(this::isSymbolAdjacent)
                    .map(NumberLocation::number).forEach(symbolAdjacentNumbers::add);
            return symbolAdjacentNumbers;
        }

        private boolean isSymbolAdjacent(NumberLocation numberLocation) {
            for (int y = numberLocation.y() - 1; y <= numberLocation.y() + 1; y++) {
                for (int x = numberLocation.x() - 1; x < numberLocation.x() + numberLocation.numberLength() + 1; x++) {
                    if (isSymbolField(x, y)) {
                        return true;
                    }
                }
            }
            return false;
        }

        private boolean isSymbolField(int x, int y) {
            try {
                char character = partsPlan.get(y).charAt(x);
                return !Character.isDigit(character) && character != '.';
            } catch (IndexOutOfBoundsException ignore) {
            }
            return false;
        }

        public List<Integer> findGearRatios() {
            if (gearLocationList == null) {
                gearLocationList = InputParser.parseToGearLocationList(partsPlan);
                findAdjacentNumbers();
            }
            return gearLocationList.stream().filter(GearLocation::hasTwoAdjacentNumbers).map(gearLocation ->
                    gearLocation.adjacentNumbers.get(0) * gearLocation.adjacentNumbers.get(1)).toList();
        }

        private void findAdjacentNumbers() {
            for (GearLocation gearLocation : gearLocationList) {
                for (NumberLocation numberLocation : numberLocationList) {
                    int gearX = gearLocation.x();
                    int gearY = gearLocation.y();
                    int numberX = numberLocation.x();
                    int numberY = numberLocation.y();
                    int numberLength = numberLocation.numberLength();
                    if (numberY > gearY - 2 && numberY < gearY + 2 &&
                            numberX >= gearX - numberLength && numberX < gearX + 2) {
                        gearLocation.adjacentNumbers().add(numberLocation.number());
                    }
                }
            }
        }
    }
}
