package org.supremus.sych.sychnews.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class NewsItem implements Parcelable {

    private final String title;
    private final String imageUrl;
    private final Category category;
    private final Date publishDate;
    private final String previewText;
    private final String fullText;

    public NewsItem(String title, String imageUrl, Category category, Date publishDate, String previewText, String fullText) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.category = category;
        this.publishDate = publishDate;
        this.previewText = previewText;
        this.fullText = fullText;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Category getCategory() {
        return category;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public String getPreviewText() {
        return previewText;
    }

    public String getFullText() {
        return fullText;
    }

    protected NewsItem(Parcel in) {
        title = in.readString();
        imageUrl = in.readString();
        category = (Category) in.readValue(Category.class.getClassLoader());
        long tmpPublishDate = in.readLong();
        publishDate = tmpPublishDate != -1 ? new Date(tmpPublishDate) : null;
        previewText = in.readString();
        fullText = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(imageUrl);
        dest.writeValue(category);
        dest.writeLong(publishDate != null ? publishDate.getTime() : -1L);
        dest.writeString(previewText);
        dest.writeString(fullText);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<NewsItem> CREATOR = new Parcelable.Creator<NewsItem>() {
        @Override
        public NewsItem createFromParcel(Parcel in) {
            return new NewsItem(in);
        }

        @Override
        public NewsItem[] newArray(int size) {
            return new NewsItem[size];
        }
    };
}