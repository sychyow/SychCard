package org.supremus.sych.sychnews.data;

import org.supremus.sych.sychnews.util.DateConverter;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "news")
public class NewsEntity {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;
    public String title;
    @ColumnInfo(name="image_url")
    public String imageUrl;
    public String section;
    public String category;
    @ColumnInfo(name="publish_date")
    @TypeConverters({DateConverter.class})
    public Date publishDate;
    @ColumnInfo(name="preview_text")
    public String previewText;
    @ColumnInfo(name="full_text")
    public String fullText;


    public NewsEntity(String title, String imageUrl, String category, Date publishDate, String previewText, String fullText) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.category = category;
        this.publishDate = publishDate;
        this.previewText = previewText;
        this.fullText = fullText;
    }

}
