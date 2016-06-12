package com.itheima.customview.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.itheima.customview.R;

import java.util.ArrayList;

/**
 * Created by yong on 2016/6/8.
 */
public class MyPopupAdapter extends BaseAdapter {
    private ArrayList<String> list;
    public MyPopupAdapter(ArrayList<String> list){
        this.list = list;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(null == convertView){
            convertView = View.inflate(parent.getContext(), R.layout.item_dropdown,null);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.tv_number);
            holder.imageButton = (ImageButton) convertView.findViewById(R.id.ib_delete);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(list.get(position));
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                notifyDataSetChanged();
                if(list.size()==0){
                    if(callback!=null){
                        callback.doDismiss();
                    }
                }
            }
        });
        return convertView;
    }

    class ViewHolder{
        TextView textView;
        ImageButton imageButton;
    }
    private Callback callback;

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

   public interface Callback{
       void doDismiss();
    }
}
