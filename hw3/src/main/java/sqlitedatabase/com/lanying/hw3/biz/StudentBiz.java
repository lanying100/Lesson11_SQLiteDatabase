package sqlitedatabase.com.lanying.hw3.biz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import sqlitedatabase.com.lanying.hw3.MySQLiteOpenHelper;
import sqlitedatabase.com.lanying.hw3.entity.Student;

/**
 * Created by lanying on 2016/11/19.
 * 先要求创建一个业务类。
 * 实现对数据库表操作的相关业务方法（
 * 1、添加一个学生，
 * 2、根据学号删除学生，
 * 3、根据学号更新学员信息，
 * 4、查询所有的学员信息，
 * 5、根据学号查询一个学员信息）。
 */
public class StudentBiz {
    private MySQLiteOpenHelper mOpenHelper;// 负责创建、打开、升级数据库、建表
    private SQLiteDatabase mDatabase;// 负责CRUD操作

    public StudentBiz(Context context,String db_name,int version) {
        mOpenHelper = new MySQLiteOpenHelper(context,db_name,null,version);
    }


    /**
     * 1、添加一个学生
     */
    public boolean addStudent(Student stu){
        mDatabase = mOpenHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name",stu.getName());
        values.put("age",stu.getAge());
        values.put("score",stu.getScore());

        return mDatabase.insert(MySQLiteOpenHelper.TABLE_NAME,null,values) != -1;
    }


    /**
     * 2、根据学号删除学生
     */
    public boolean delStudent(int id){
        mDatabase = mOpenHelper.getWritableDatabase();
        return mDatabase.delete(MySQLiteOpenHelper.TABLE_NAME,"_id="+id,null) != 0;
    }


    /**
     * 3、根据学号更新学员信息
     */
    public boolean updateStudent(Student stu){
        mDatabase = mOpenHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name",stu.getName());
        values.put("age",stu.getAge());
        values.put("score",stu.getScore());
        return mDatabase.update(MySQLiteOpenHelper.TABLE_NAME,values,"_id="+stu.getId(),null) > 0;
    }


    /**
     * 4、查询所有的学员信息
     */
    public List<Student> queryAll(){
        mDatabase = mOpenHelper.getReadableDatabase();
        List<Student> list = new ArrayList<>();

        Cursor cursor = mDatabase.query(MySQLiteOpenHelper.TABLE_NAME,null,null,null,null,null,null);
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            // 列名 --> 列的下标 --> 列的值
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            int age = cursor.getInt(cursor.getColumnIndex("age"));
            double score = cursor.getDouble(cursor.getColumnIndex("score"));
            list.add(new Student(id,name,age,score));

        }
        return list;
    }


    /**
     * 5、根据学号查询一个学员信息
     */
    public Student queryStudentById(int id){
        mDatabase = mOpenHelper.getReadableDatabase();
        Cursor cursor = mDatabase.query(MySQLiteOpenHelper.TABLE_NAME,null,"_id="+id,null,null,null,null);

        if(cursor.moveToFirst()) {
            //int _id = cursor.getInt(cursor.getColumnIndex("_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            int age = cursor.getInt(cursor.getColumnIndex("age"));
            double score = cursor.getDouble(cursor.getColumnIndex("score"));
            return new Student(id,name,age,score);
        }else{
            return null;
        }
    }
}
