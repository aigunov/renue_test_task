package gitlab.renue;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class JsonReportVisitor implements ReportVisitor {
    private String outputFilePath;
    private long initTime;
    private List<SearchResult> results;

    public JsonReportVisitor(String outputFilePath) {
        this.outputFilePath = outputFilePath;
        this.results = new ArrayList<>();
    }

    public void setInitTime(long initTime) {
        this.initTime = initTime;
    }

    @Override
    public void visit(SearchResult result) {
        results.add(result);
    }

    public void writeToFile() {
        try (Writer writer = new FileWriter(outputFilePath)) {
            writer.write(toJson());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String toJson() {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"initTime\": ").append(initTime).append(",\n");
        json.append("  \"result\": [\n");

        for (int i = 0; i < results.size(); i++) {
            SearchResult result = results.get(i);
            json.append("    {\n");
            json.append("      \"search\": \"").append(escapeJson(result.getSearch())).append("\",\n");
            json.append("      \"result\": ").append(result.getResult()).append(",\n");
            json.append("      \"time\": ").append(result.getTime()).append("\n");
            json.append("    }");
            if (i < results.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }

        json.append("  ]\n");
        json.append("}");
        return json.toString();
    }

    private String escapeJson(String str) {
        if (str == null) {
            return "null";
        }
        StringBuilder result = new StringBuilder();
        for (char c : str.toCharArray()) {
            switch (c) {
                case '"':
                    result.append("\\\"");
                    break;
                case '\\':
                    result.append("\\\\");
                    break;
                case '\b':
                    result.append("\\b");
                    break;
                case '\f':
                    result.append("\\f");
                    break;
                case '\n':
                    result.append("\\n");
                    break;
                case '\r':
                    result.append("\\r");
                    break;
                case '\t':
                    result.append("\\t");
                    break;
                default:
                    result.append(c);
            }
        }
        return result.toString();
    }
}