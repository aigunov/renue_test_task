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
     */
    public void parseCsv(final String path, Trie trie, final int column) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",", column + 1);
                var value = columns[column - 1].trim().replaceAll("^\"|\"$", "");
                int id = Integer.parseInt(columns[0]);
                trie.insert(value, id);
            }
        } catch (IOException e) {
            System.err.println("Произошла ошибка чтения таблицы: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Ошибка конвертирования строки в число: " + e.getMessage());
        }
    }
}