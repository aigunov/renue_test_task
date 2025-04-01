package gitlab.renue.report;

public interface ReportElement {
    void accept(ReportVisitor visitor);
}