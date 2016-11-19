package sqlitedatabase.com.lanying.hw3;

import android.test.AndroidTestCase;
import android.util.Log;

import java.util.List;

import sqlitedatabase.com.lanying.hw3.biz.StudentBiz;
import sqlitedatabase.com.lanying.hw3.entity.Student;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends AndroidTestCase {
    private StudentBiz mStudentBiz;

    // 初始化
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mStudentBiz = new StudentBiz(getContext(),"data.db",1);
    }

    //测试用例——方法
    public void testAddStudent(){
        boolean result = mStudentBiz.addStudent(new Student("zhangsan",18,99));
        assertTrue(result);
    }

    public void testUpdateStudent(){
        assertTrue(mStudentBiz.updateStudent(new Student(1,"zhangsansan",28,99.9)));
    }


    public void testDelStudent(){
        //assertTrue(mStudentBiz.delStudent(99));
    }

    public void testQueryAll(){
        List<Student> list = mStudentBiz.queryAll();
        for (Student stu:list) {
            Log.d("lanying",stu.toString());
        }
    }



    // 资源释放
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

    }
}