package sqlitedatabase.com.lanying.search_music;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 简易音乐搜索播放器【递归】
 1.能够扫描出手机SD卡中的所有mp3文件——动态授权Android6.0
 2.在扫描的过程中，弹出对话框（通知用户正在扫描）——Dialog
 3.扫描完成后，关闭对话框，显示出扫描出的所有歌曲——Handler
 4.想听哪首点哪首。——MediaPlayer

 分析：
 扫描SD卡属于耗时操作，应该开子线程
 扫描：scan
 */
public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 0;
    private static final String TAG = "lanying";
    private static final int SCAN_DONE = 1;// 扫描完成信号
    private List<MyMusic> mMusicList;
    private MyAdapter mAdapter;
    private ListView mListView;
    private MyHandler mHandler;
    private AlertDialog mAlertDialog;
    private MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        init();
        setListener();

    }

    private void setListener() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyMusic music = mMusicList.get(position);
                mPlayer.reset();
                try {
                    mPlayer.setDataSource(music.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mPlayer.prepareAsync();
                mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    // 准备完毕
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mPlayer.start();
                    }
                });
            }
        });
    }

    private void init() {
        mListView = (ListView) findViewById(R.id.lv);
        mMusicList = new ArrayList<>();
        mHandler = new MyHandler(MainActivity.this);
        mAdapter = new MyAdapter(MainActivity.this);
        mListView.setAdapter(mAdapter);
        mAlertDialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("扫描SD卡中的音乐")
                .setMessage("scanning ...")
                .create();
        mPlayer = new MediaPlayer();
    }


    /**
     *  扫描SD卡，并将扫描出的mp3文件存入List
     */
    private void scan(){

        //先判断权限，再开启子线程扫描
        if(Build.VERSION.SDK_INT >= 23){
            int hasReadExternalStoragePermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            if(hasReadExternalStoragePermission != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
            }else{
                // 已经有权限，直接扫描SD卡即可
                doScanSDcard();
            }
        }else{
            // 低版本系统，不需要动态授权
            doScanSDcard();
        }



    }


    /**
     * 动态授权结果
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_REQUEST_CODE:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // 动态授权成功
                    doScanSDcard();
                }else{
                    Toast.makeText(MainActivity.this, "动态授权失败，请不要联系蓝鹰", Toast.LENGTH_SHORT).show();
                }
                
                break;
        }
        
    }


    /**
     * 能走到这一步，说明SD卡读权限已经获取
     */
    private void doScanSDcard(){
        // 清空旧数据
        mMusicList.clear();

        // 显示扫描Dialog
        mAlertDialog.show();

        new Thread(){
            @Override
            public void run() {
                Log.d(TAG,"sd卡是否可用："+(Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED));
                //if(Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED){
                    File rootFile = Environment.getExternalStorageDirectory();
                    iteratorDir(rootFile);// 传说中的耗时操作
                try {
                    Thread.sleep(2000);// 模拟耗时操作
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 扫描完成，通知主线程刷新ListView
                    mHandler.sendEmptyMessage(SCAN_DONE);
                //}

            }
        }.start();
    }


    /**
     * 递归扫描给定目录中的mp3文件
     * @param file
     */
    private void iteratorDir(File file){
        if(file == null || !file.isDirectory()){
            return;
        }
        File[] files = file.listFiles();//return an array of files or null
        if(files != null && files.length != 0){
            // 避免是null或空文件夹
            for (File f : files) {
                Log.d(TAG,f.getAbsolutePath());
                if (f.getName().toLowerCase().endsWith(".mp3")) {
                    // 如果是目标类型的文件，存入List集合
                    mMusicList.add(new MyMusic(f.getName(),f.getAbsolutePath()));
                }else if(f.isDirectory()){
                    // 如果是目录，递归遍历
                    iteratorDir(f);
                }

            }
    }
}

    public void myClick(View view) {
        scan();// 扫描SD卡中的mp3
    }


    /**
     * Created by lanying on 2016/11/19.
     * 为了避免内存泄漏，选择使用静态内部类
     */
    private class MyHandler extends Handler {
        WeakReference<MainActivity> mReference;

        public MyHandler(MainActivity activity){
            mReference = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SCAN_DONE:
                    MainActivity mainActivity = mReference.get();
                    // 刷新数据
                    mainActivity.mAdapter.resetData(mainActivity.mMusicList);

                    // 关闭Dialog
                    mAlertDialog.dismiss();
                    break;
            }

        }
    }
}
