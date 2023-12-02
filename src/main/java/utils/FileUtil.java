package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtil {

    private FileUtil() {
    }

    public static String readToString(String path) throws FileNotFoundException {
        Stream<String> lines = new BufferedReader(new FileReader("src/main/resources/" + path)).lines();
        return lines.collect(Collectors.joining());
    }

    public static List<String> readToList(String path) throws FileNotFoundException {
        Stream<String> lines = new BufferedReader(new FileReader("src/main/resources/" + path)).lines();
        return lines.collect(Collectors.toList());
    }
}
