package gitlab.renue;

public interface ReportVisitor {
    void visit(SearchResult result);
}