package sqlitedatabase.com.lanying.hw3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lanying on 2016/11/19.
 * 在应用程序中创建一个数据库，数据库名叫data.db,
 * 在数据库中创建一张表，表名叫student，
 * 内部结构有4个字段（
 * 学号(_id-主键、自增长)，
 * 名字（name），
 * age（18到100），
 * 分数（score））。
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper{
    public static final String TABLE_NAME = "student";
    private static final String CREATE_TABLE = "create table " +// 一定要加空格
            TABLE_NAME +"(" +
            "_id integer primary key autoincrement," +
            "name varchar(10) not null," +
            "age integer check(age>0 and age<300)," +
            "score real check(score>=0)"+
            ")";


    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * 没有数据库时，自动创建数据库——e.g.第一次使用该应用时
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    /**
     * 用于升级数据库
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion){
            case 1:
                // 创建第二张表
            case 2:
                // 创建第三张表
            case 3:
                // 给第三张表增加一列 alter table 表名 add column 列名
        }
    }
}
