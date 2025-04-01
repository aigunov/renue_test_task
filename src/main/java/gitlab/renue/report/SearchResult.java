package gitlab.renue.report;

import java.util.List;

/**
 * Класс SearchResult представляет результат поиска, включающий поисковый запрос,
 * список найденных идентификаторов и время выполнения поиска.
 */
public class SearchResult implements ReportElement {
    private String search;
    private List<Integer> result;
    private long time;

    public SearchResult(String search, List<Integer> result, long time) {
        this.search = search;
        this.result = result;
        this.time = time;
    }

    public String getSearch() {
        return search;
    }

    public List<Integer> getResult() {
        return result;
    }

    public long getTime() {
        return time;
    }

    @Override
    public void accept(ReportVisitor visitor) {
        visitor.visit(this);
    }
}