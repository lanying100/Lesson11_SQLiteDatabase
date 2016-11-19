package sqlitedatabase.com.lanying.sharedpreferences;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

/**
 * Created by lanying on 2016/11/19.
 * 欢迎页
 * 分析：
 * 读取SharedPreferences，如果不是第一次打开，则跳转到Main界面
 */
public class Welcome extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);


        SharedPreferences sp = getSharedPreferences("data",MODE_PRIVATE);
        boolean isFirst = sp.getBoolean("isFirst",true);
        if(!isFirst){
            // 如果不是第一次打开，则跳转到MainActivity
            startMainActivity();
        }else {
            // 如果是第一次打开，修改SharedPreferences
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("isFirst", false);
            editor.commit();
        }
    }

    public void myClick(View view) {
        startMainActivity();
    }

    private void startMainActivity() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();// 关闭自己
    }


}
