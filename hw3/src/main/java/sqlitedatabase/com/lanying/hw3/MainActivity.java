package sqlitedatabase.com.lanying.hw3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * 3、在应用程序中创建一个数据库，数据库名叫data.db,
 * 在数据库中创建一张表，表名叫student，
 * 内部结构有4个字段（
 * 学号(_id-主键、自增长)，
 * 名字（name），
 * age（18到100），
 * 分数（score））。
 *
 * 先要求创建一个业务类。
 * 实现对数据库表操作的相关业务方法（
 * 1、添加一个学生，
 * 2、根据学号删除学生，
 * 3、根据学号更新学员信息，
 * 4、查询所有的学员信息，
 * 5、根据学号查询一个学员信息）。
 *
 * 最后编写一个单元测试类，
 * 来测试添加多个学生和测试查询并log所添加进去的所有学员信息。
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
