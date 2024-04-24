package fi.vm.sade.kayttooikeus.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchResultDto<T> implements Serializable {
    private int totalCount = 0;
    private List<T> results = new ArrayList<>();

    public SearchResultDto(int totalCount, List<T> results) {
        this.totalCount = totalCount;
        this.results = results;
    }

    public SearchResultDto(List<T> results) {
        this.results = results;
        this.totalCount = results.size();
    }

    public SearchResultDto() {
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }
}
