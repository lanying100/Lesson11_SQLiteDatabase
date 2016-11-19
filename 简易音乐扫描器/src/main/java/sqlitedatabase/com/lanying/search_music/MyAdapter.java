package sqlitedatabase.com.lanying.search_music;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lanying on 2016/11/19.
 * 适配器的作用：
 * 1、解析项布局——LayoutInflater
 * 2、以合适的方式提供数据
 */
public class MyAdapter extends BaseAdapter{
    private List<MyMusic> mMusics;
    private LayoutInflater mLayoutInflater;

    public MyAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mMusics = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mMusics.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder = null;


        if(view == null){
            // 解析项布局
            view = mLayoutInflater.inflate(R.layout.item,null);
            holder = new ViewHolder();
            holder.mSongName = (TextView) view.findViewById(R.id.tv_name);
            holder.mPath = (TextView) view.findViewById(R.id.tv_path);

            view.setTag(holder);
        }else{
            // 有可重用布局，直接从view中找到holder
            holder = (ViewHolder) view.getTag();
        }


        MyMusic music = mMusics.get(position);
        holder.mSongName.setText(music.getSongName());
        holder.mPath.setText(music.getPath());

        return view;
    }


    class ViewHolder {
        private TextView mSongName;
        private TextView mPath;
    }


    public void resetData(List<MyMusic> list){
        mMusics.clear();
        mMusics.addAll(list);
        notifyDataSetChanged();
    }
}
