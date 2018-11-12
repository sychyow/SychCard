package org.supremus.sych.sychnews;

import android.provider.SyncStateContract;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.room.TypeConverter;

public class DateConverter {
    static DateFormat df = new SimpleDateFormat(DataUtils.DATE_FORMAT);

    @TypeConverter
    public static Date fromString(String value) {
        if (value != null) {
            try {
                return df.parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return null;
        }
    }

    @TypeConverter
    public static String toString(Date value) {
        return df.format(value);
    }
}