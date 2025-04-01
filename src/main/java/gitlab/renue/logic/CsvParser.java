package gitlab.renue.logic;

import java.io.IOException;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Класс CsvParser предназначен для парсинга CSV-файлов и вставки данных в Trie.
 */
public class CsvParser {

    /**
     * Парсит CSV-файл и вставляет данные в Trie.
     *
     * @param path   Путь к CSV-файлу.
     * @param trie   Trie, в который будут вставлены данные.
     * @param column Индекс столбца, данные из которого будут вставлены в Trie.
     * @throws IOException Если произошла ошибка ввода-вывода при чтении файла.
     * @throws NumberFormatException Если значение в первом столбце не может быть преобразовано в целое число.
     * @throws ArrayIndexOutOfBoundsException Если строка CSV не содержит достаточно столбцов.
     */
    public void parseCsv(final String path, Trie trie, final int column) throws IOException, NumberFormatException, ArrayIndexOutOfBoundsException {
        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                String value = columns[column - 1].replaceAll("^\"|\"$", ""); // Удаление кавычек
                int id = Integer.parseInt(columns[0]);
                trie.insert(value, id);
            }
        }
    }
}