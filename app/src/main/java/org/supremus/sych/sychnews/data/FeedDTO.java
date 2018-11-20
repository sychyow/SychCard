
package org.supremus.sych.sychnews.data;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeedDTO {

    @Expose
    private String copyright;
    @SerializedName("last_updated")
    private String lastUpdated;
    @SerializedName("num_results")
    private Long numResults;
    @Expose
    private List<ResultDTO> results;
    @Expose
    private String section;
    @Expose
    private String status;

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Long getNumResults() {
        return numResults;
    }

    public void setNumResults(Long numResults) {
        this.numResults = numResults;
    }

    public List<ResultDTO> getResults() {
        return results;
    }

    public void setResults(List<ResultDTO> results) {
        this.results = results;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
