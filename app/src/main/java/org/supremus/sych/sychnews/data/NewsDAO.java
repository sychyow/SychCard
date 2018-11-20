package org.supremus.sych.sychnews.data;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface NewsDAO {
    @Query("SELECT * FROM news")
    List<NewsEntity> getAllNews();

    @Query("SELECT * FROM news WHERE section = :sectionName")
    List<NewsEntity> getNewsBySection(String sectionName);

    @Query("SELECT * FROM news WHERE id = :id")
    NewsEntity getNewsById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(NewsEntity... newsEntities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(NewsEntity newsEntity);

    @Update
    void update(NewsEntity newsEntity);

    @Delete
    void delete(NewsEntity newsEntity);

    @Query("DELETE from news")
    void deleteAll();

    @Query("DELETE from news where section = :sectionName")
    void deleteSection(String sectionName);
}
