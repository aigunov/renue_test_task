package gitlab.renue.report;

import java.util.List;

public class ResponseFormatter {
    private final long initTime;
    private final List<SearchResult> results;

    public ResponseFormatter(long initTime, List<SearchResult> results) {
        this.initTime = initTime;
        this.results = results;
    }

    public void format(ReportVisitor visitor) {
        if (visitor instanceof JsonReportVisitor) {
            ((JsonReportVisitor) visitor).setInitTime(initTime);
        }
        for (SearchResult result : results) {
            result.accept(visitor);
        }
        if (visitor instanceof JsonReportVisitor) {
            ((JsonReportVisitor) visitor).writeToFile();
        }
    }
}