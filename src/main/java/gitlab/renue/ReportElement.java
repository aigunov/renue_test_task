package gitlab.renue;

public interface ReportElement {
    void accept(ReportVisitor visitor);
}