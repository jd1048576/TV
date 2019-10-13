package jdr.tvtracker.data.remote.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import jdr.tvtracker.data.entities.Show;

public class TVResultsResponse {
    @SerializedName("page")
    @Expose
    private int page;
    @SerializedName("results")
    @Expose
    private List<Show> results = null;
    @SerializedName("total_pages")
    @Expose
    private int totalPages;
    @SerializedName("total_results")
    @Expose
    private int totalResults;

    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Show> getResults() {
        return this.results;
    }

    public void setResults(List<Show> results) {
        this.results = results;
    }

    public int getTotalResults() {
        return this.totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return this.totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
