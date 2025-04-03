package gitlab.renue.report;

import java.util.List;

/**
 * Класс ResponseFormatter форматирует результаты поиска, используя посетителя (Visitor).
 * Он устанавливает время инициализации и передает результаты поиска посетителю.
 * В случае использования JsonReportVisitor, он также вызывает метод записи в файл.
 */
public class ResponseFormatter {
    private final long initTime;
    private final List<SearchResult> results;

    public ResponseFormatter(long initTime, List<SearchResult> results) {
        this.initTime = initTime;
        this.results = results;
    }

    public void format(ReportVisitor visitor) {
        for (SearchResult result : results) {
            result.accept(visitor);
        }
        if (visitor instanceof JsonReportVisitor) {
            ((JsonReportVisitor) visitor).writeToFile();
        }
    }
}