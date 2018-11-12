
package org.supremus.sych.sychnews.data;

import java.util.Date;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.supremus.sych.sychnews.data.MultimediaDTO;

public class ResultDTO {

    @Expose
    @SerializedName("abstract")
    private String text_abstract;
    @Expose
    private String byline;
    @SerializedName("created_date")
    private String createdDate;
    @SerializedName("item_type")
    private String itemType;
    @Expose
    private String kicker;
    @Expose
    private List<MultimediaDTO> multimedia;
    @SerializedName("published_date")
    @Expose
    private Date publishedDate;
    @Expose
    private String section;
    @Expose
    private String subsection;
    @Expose
    private String title;
    @Expose
    private String url;

    public String getAbstract() {
        return text_abstract;
    }

    public void setAbstract(String text_abstract) {
        this.text_abstract = text_abstract;
    }

    public String getByline() {
        return byline;
    }

    public void setByline(String byline) {
        this.byline = byline;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getKicker() {
        return kicker;
    }

    public void setKicker(String kicker) {
        this.kicker = kicker;
    }

    public List<MultimediaDTO> getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(List<MultimediaDTO> multimedia) {
        this.multimedia = multimedia;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getSubsection() {
        return subsection;
    }

    public void setSubsection(String subsection) {
        this.subsection = subsection;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
