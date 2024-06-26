package cn.itcast.bgmtv_wikieditor_;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class UserDBHelper extends SQLiteOpenHelper {
    private static UserDBHelper sHelper;
    private static final String DB_NAME = "user.db";
    private static final int DB_VERSION = 1;
    public UserDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context,name,factory,version);
    }
    //创建单例，供使用调用该类里面的的增删改查的方法
    public synchronized static UserDBHelper getInstance(Context context) {
        if (null == sHelper) {
            sHelper = new UserDBHelper(context, DB_NAME, null, DB_VERSION);
        }
        return sHelper;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建user_table表
        db.execSQL("create table user_table(user_id integer primary key autoincrement, " +
                "email text," +       //邮件名
                "password text," +      //密码
                "nickname text" +       // 注册类型   0---用户   1---管理员
                ")");
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    //登录
    @SuppressLint("Range")
    public UserInfo login(String email) {
        //获取SQLiteDatabase实例
        SQLiteDatabase db = getReadableDatabase();
        UserInfo userInfo = null;
        String sql = "select user_id,email,password,nickname  from user_table where email=?";
        String[] selectionArgs = {email};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        if (cursor.moveToNext()) {
            int user_id = cursor.getInt(cursor.getColumnIndex("user_id"));
            email = cursor.getString(cursor.getColumnIndex("email"));
            String password = cursor.getString(cursor.getColumnIndex("password"));
            String nickname = cursor.getString(cursor.getColumnIndex("nickname"));
            userInfo = new UserInfo(user_id, email, password, nickname);
        }
        cursor.close();
        db.close();
        return userInfo;
    }
    //注册
    public int register(String email, String password, String nickname) {
        //获取SQLiteDatabase实例
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        //填充占位符
        values.put("email", email);
        values.put("password", password);
        values.put("nickname", nickname);
        String nullColumnHack = "values(null,?,?,?)";
        //执行
        int insert = (int) db.insert("user_table", nullColumnHack, values);
        db.close();
        return insert;
    }
}