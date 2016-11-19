package com.lanying.lesson11;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * 1.实现把编辑框中的字符串内容保存到sdcard的公共目录DCIM中，
 * 文件名为a.txt.再把所对应文件中的内容读到TextView控件中。
 *
 * 补充内容：Android6.0 动态授权
 */
public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 0;

    private TextView mShowTextView;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        mShowTextView = (TextView) findViewById(R.id.tv_show);
        mEditText = (EditText) findViewById(R.id.et);
    }


    public void myClick(View v) {
        switch (v.getId()){
            case R.id.btn_read:
                read();
                break;
            case R.id.btn_write:
                write();
                break;
        }
    }

    // 写入SD卡
    private void write() {

        // 判断系统版本
        if(Build.VERSION.SDK_INT >= 23){
            // 动态授权

            // 1、是否已有权限
            int hasWriteExternalStoragePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(hasWriteExternalStoragePermission != PackageManager.PERMISSION_GRANTED){
                // 未授权，申请权限
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
            }else{
                // 已经授权
                doWrite();
            }
        }else{
            doWrite();
        }


        if(Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED){
            // 没有写SD卡的权限
        }

    }


    /**
     * 读取SD卡DCIM/a.txt中的内容
     */
    private void read() {
        File file = new File(Environment.getExternalStorageDirectory(),"DCIM/a.txt");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

            mShowTextView.setText("");
            String line = "";
            while((line = br.readLine()) != null){
                mShowTextView.append(line+'\n');
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(br != null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    // 申请权限的结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        switch (requestCode){
            case PERMISSION_REQUEST_CODE:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // 授权成功，可以写SD卡
                    doWrite();
                }else{
                    // 授权失败
                    Toast.makeText(MainActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    // 写入SD卡
    private void doWrite() {
        File file = new File(Environment.getExternalStorageDirectory(),"DCIM/a.txt");
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileOutputStream(file),true/*自动刷新*/);

            pw.print(mEditText.getText().toString().trim());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(pw != null) {
                pw.close();
            }
        }

    }
}
