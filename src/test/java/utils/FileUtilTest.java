package utils;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilTest {

    @Test
    void should_read_file_to_string() throws FileNotFoundException {
        String textFromFile = FileUtil.readToString("test.txt");

        assertEquals("this is a test", textFromFile);
    }

    @Test
    void should_read_file_to_list() throws FileNotFoundException {
        List<String> listFromFile = FileUtil.readToList("test.txt");

        assertEquals(2, listFromFile.size());
        assertEquals("this is a", listFromFile.get(0));
        assertEquals(" test", listFromFile.get(1));
    }
}