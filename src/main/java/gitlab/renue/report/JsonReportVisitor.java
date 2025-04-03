package gitlab.renue.report;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для создания JSON-отчета на основе результатов поиска.
 */
public class JsonReportVisitor implements ReportVisitor {
    private final String outputFilePath;
    private final long initTime;
    private final List<SearchResult> results = new ArrayList<>();

    public JsonReportVisitor(String outputFilePath, long initTime) {
        this.outputFilePath = outputFilePath;
        this.initTime = initTime;
    }

    @Override
    public void visit(SearchResult result) {
        results.add(result);
    }

    public void writeToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            writer.write(toJson());
        } catch (IOException e) {
            System.err.println("Ошибка записи в файл: " + outputFilePath);
        }
    }

    private String toJson() {
        StringBuilder json = new StringBuilder();
        json.append("{\n  \"initTime\": ").append(initTime).append(",\n  \"result\": [\n");

        for (int i = 0; i < results.size(); i++) {
            SearchResult result = results.get(i);
            json.append("    {\"search\": \"").append(escapeJson(result.getSearch())).append("\", ")
                    .append("\"result\": ").append(result.getResult()).append(", ")
                    .append("\"time\": ").append(result.getTime()).append("}");
            if (i < results.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }

        json.append("  ]\n}");
        return json.toString();
    }

    private String escapeJson(String str) {
        if (str == null) return "null";
        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            switch (c) {
                case '\\':
                    sb.append("\\\\");
                    break;
                case '"':
                    sb.append("\\\"");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }
}
