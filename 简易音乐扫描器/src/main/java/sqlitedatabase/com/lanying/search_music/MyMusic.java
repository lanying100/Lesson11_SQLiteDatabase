package sqlitedatabase.com.lanying.search_music;

/**
 * Created by lanying on 2016/11/19.
 * 自定义的Music类
 *
 */
public class MyMusic {

    private String mSongName;// 歌曲名，用于显示在ListView上
    private String mPath;// 歌曲的绝对路径，用于点击时播放

    public MyMusic() {
    }

    public MyMusic(String songName, String path) {
        mSongName = songName;
        mPath = path;
    }

    public String getSongName() {
        return mSongName;
    }

    public void setSongName(String songName) {
        mSongName = songName;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        mPath = path;
    }
}
