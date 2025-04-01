package gitlab.renue.report;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для создания JSON-отчета на основе результатов поиска.
 */
public class JsonReportVisitor implements ReportVisitor {
    private static final String INDENT = "  ";
    private static final String NEWLINE = "\n";
    private static final String QUOTE = "\"";
    private static final String ESCAPED_QUOTE = "\\\"";
    private static final String ESCAPED_BACKSLASH = "\\\\";

    private final String outputFilePath;
    private long initTime;
    private final List<SearchResult> results;

    /**
     * Конструктор класса.
     *
     * @param outputFilePath Путь к файлу для записи отчета.
     */
    public JsonReportVisitor(String outputFilePath) {
        this.outputFilePath = outputFilePath;
        this.results = new ArrayList<>();
    }

    /**
     * Устанавливает время инициализации.
     *
     * @param initTime Время инициализации.
     */
    public void setInitTime(long initTime) {
        this.initTime = initTime;
    }

    /**
     * Добавляет результат поиска в отчет.
     *
     * @param result Результат поиска.
     */
    @Override
    public void visit(SearchResult result) {
        results.add(result);
    }

    /**
     * Записывает отчет в файл в формате JSON.
     */
    public void writeToFile() {
        try (Writer writer = new FileWriter(outputFilePath)) {
            writer.write(toJson());
        } catch (IOException e) {
            System.err.println("Не удалось записать результат в файл: " + outputFilePath);
        }
    }

    /**
     * Преобразует отчет в JSON-строку.
     *
     * @return JSON-строка.
     */
    private String toJson() {
        StringBuilder json = new StringBuilder();
        json.append("{").append(NEWLINE);
        json.append(INDENT).append(QUOTE).append("initTime").append(QUOTE)
                .append(": ").append(initTime).append(",").append(NEWLINE);
        json.append(INDENT).append(QUOTE).append("result").append(QUOTE).append(": [").append(NEWLINE);

        for (int i = 0; i < results.size(); i++) {
            SearchResult result = results.get(i);
            json.append(INDENT).append(INDENT).append("{").append(NEWLINE);
            json.append(INDENT).append(INDENT).append(INDENT).append(QUOTE).append("search").append(QUOTE)
                    .append(": ").append(QUOTE).append(escapeJson(result.getSearch())).append(QUOTE).append(",").append(NEWLINE);
            json.append(INDENT).append(INDENT).append(INDENT).append(QUOTE).append("result").append(QUOTE)
                    .append(": ").append(result.getResult()).append(",").append(NEWLINE);
            json.append(INDENT).append(INDENT).append(INDENT).append(QUOTE).append("time").append(QUOTE)
                    .append(": ").append(result.getTime()).append(NEWLINE);
            json.append(INDENT).append(INDENT).append("}");
            if (i < results.size() - 1) {
                json.append(",");
            }
            json.append(NEWLINE);
        }

        json.append(INDENT).append("]").append(NEWLINE);
        json.append("}");
        return json.toString();
    }

    /**
     * Экранирует специальные символы в JSON-строке.
     *
     * @param str Строка для экранирования.
     * @return Экранированная строка.
     */
    private String escapeJson(String str) {
        if (str == null) {
            return "null";
        }
        return str.replace("\\", ESCAPED_BACKSLASH)
                .replace("\"", ESCAPED_QUOTE)
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}