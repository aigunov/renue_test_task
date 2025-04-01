package gitlab.renue;

import java.io.IOException;

import java.io.BufferedReader;
import java.io.FileReader;

public class CsvParser {
    public void parseCsv(final String path, Trie trie, final int column) throws IOException {
        String line;
        try (var reader = new BufferedReader(new FileReader(path))) {
            while ((line = reader.readLine()) != null ){
                var columns = line.split(",");
                var value = columns[column - 1].replaceAll("^\"|\"$", "");
                int id = Integer.parseInt(columns[0]);
                trie.insert(value, id);
            }
            System.out.println("Завершен парсинг таблицы.");
        }
    }
}