package gitlab.renue.report;

import gitlab.renue.logic.JsonReportVisitor;

import java.util.List;

public class ResponseFormatter {
    private long initTime;
    private List<SearchResult> results;

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