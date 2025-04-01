package gitlab.renue.report;

public interface ReportVisitor {
    void visit(SearchResult result);
}