package cn.itcast.bgmtv_wikieditor_;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SubjectDBHelper extends SQLiteOpenHelper {
    private static SubjectDBHelper sHelper;
    private static final String DB_NAME = "subject.db";
    private static final int DB_VERSION = 1;
    public SubjectDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context,name,factory,version);
    }
    //创建单例，供使用调用该类里面的的增删改查的方法
    public synchronized static SubjectDBHelper getInstance(Context context) {
        if (null == sHelper) {
            sHelper = new SubjectDBHelper(context, DB_NAME, null, DB_VERSION);
        }
        return sHelper;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建user_table表
        db.execSQL("create table subject_table(subject_id integer primary key autoincrement, " +
                "albumtitle text," +      //专辑名
                "key_value text," +       //键名
                "value text" +            //值名
                ")");
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    //获取专辑维基信息
    public List<SubjectInfo> subjectfind(String albumtitle) {
        SQLiteDatabase db = getReadableDatabase();
        List<SubjectInfo> subjectInfos = new ArrayList<>();
        String sql = "select subject_id,albumtitle,key_value,value  from subject_table where key_value=?";
        String[] selectionArgs = {albumtitle};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        while (cursor.moveToNext()) {
            int subjectIdIndex = cursor.getColumnIndex("subject_id");
            int albumTitleIndex = cursor.getColumnIndex("albumtitle");
            int keyValueIndex = cursor.getColumnIndex("key_value");
            int valueIndex = cursor.getColumnIndex("value");
            if (subjectIdIndex != -1 && albumTitleIndex != -1 && keyValueIndex != -1 && valueIndex != -1) {
                int subject_id = cursor.getInt(subjectIdIndex);
                albumtitle = cursor.getString(albumTitleIndex);
                String key_value = cursor.getString(keyValueIndex);
                String value = cursor.getString(valueIndex);
                SubjectInfo subjectInfo = new SubjectInfo(subject_id, albumtitle, key_value, value);
                subjectInfos.add(subjectInfo);
            }
        }
        cursor.close();
        db.close();
        return subjectInfos;
    }

    public List<SubjectInfo> getAllHistory() {
        List<SubjectInfo> history = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM subject_table", null);
        while (cursor.moveToNext()) {
            int subjectIdIndex = cursor.getColumnIndex("subject_id");
            int albumTitleIndex = cursor.getColumnIndex("albumtitle");
            int keyValueIndex = cursor.getColumnIndex("key_value");
            int valueIndex = cursor.getColumnIndex("value");
            if (subjectIdIndex != -1 && albumTitleIndex != -1 && keyValueIndex != -1 && valueIndex != -1) {
                int subject_id = cursor.getInt(subjectIdIndex);
                String albumtitle = cursor.getString(albumTitleIndex);
                String key_value = cursor.getString(keyValueIndex);
                String value = cursor.getString(valueIndex);
                SubjectInfo subjectInfo = new SubjectInfo(subject_id, albumtitle, key_value, value);
                history.add(subjectInfo);
            }
        }
        cursor.close();
        db.close();
        return history;
    }

    //添加专辑维基
    public int subjectadd(String albumtitle,String key_value, String value) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("albumtitle",albumtitle);
        values.put("key_value", key_value);
        values.put("value", value);
        long insert = db.insert("subject_table", null, values);
        db.close();
        return (int) insert;
    }
}