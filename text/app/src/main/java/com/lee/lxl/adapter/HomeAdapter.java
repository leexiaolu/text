package com.lee.lxl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lee.lxl.bean.News;
import com.lee.lxl.text.R;

import java.util.List;

/**
 * Created by lee-lxl on 2016/2/2.
 */
public class HomeAdapter extends BaseAdapter{

    private List<News.PostEntity> list;
    private LayoutInflater inflater;
    private Context context;

    public HomeAdapter(Context context,List<News.PostEntity> list){
        inflater=LayoutInflater.from(context);
        this.context=context;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView=inflater.inflate(R.layout.listview_item,null);
            viewHolder.mLayout_item = (RelativeLayout) convertView.findViewById(R.id.mLayout_item);
            viewHolder.title = (TextView) convertView.findViewById(R.id.mListView_title);
            viewHolder.content = (TextView) convertView.findViewById(R.id.mListView_context);
            viewHolder.username = (TextView) convertView.findViewById(R.id.mListView_username);
            viewHolder.ctime = (TextView) convertView.findViewById(R.id.mListView_cTime);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText((CharSequence) list.get(position).getTitle());
        viewHolder.content.setText((CharSequence) list.get(position).getContent());
        viewHolder.username.setText(list.get(position).getUsername());
        viewHolder.ctime.setText(list.get(position).getCtime());
        return convertView;
    }
}

class ViewHolder{
    TextView title,username,content,ctime;
    RelativeLayout mLayout_item;
}