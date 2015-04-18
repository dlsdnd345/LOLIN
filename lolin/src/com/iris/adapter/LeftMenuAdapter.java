package com.iris.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iris.entities.LeftMenu;
import com.iris.lolin.R;
import com.iris.util.ViewHolder;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by woong on 2015. 4. 18..
 */
public class LeftMenuAdapter extends BaseAdapter {


    private int layout;
    private Context context;
    private LayoutInflater inflater;
    private List<LeftMenu> leftMenuList;
    private SparseArray<WeakReference<View>> viewArray;

    public LeftMenuAdapter(Context context, int layout , List<LeftMenu> leftMenuList){

        this.context=context;
        this.leftMenuList =leftMenuList;
        this.layout = layout;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.viewArray = new SparseArray<WeakReference<View>>(leftMenuList.size());

    }

    public int getCount() {
        return leftMenuList.size();
    }

    @Override
    public LeftMenu getItem(int position) {
        return leftMenuList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (viewArray != null && viewArray.get(position) != null) {
            convertView = viewArray.get(position).get();
            if (convertView != null)
                return convertView;
        }

        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }

        ImageView imgMenu = ViewHolder.get(convertView, R.id.imgMenu);
        TextView textMenuTitle = ViewHolder.get(convertView ,R.id.textMenuTitle);

        imgMenu.setBackgroundResource(leftMenuList.get(position).getIcon());
        textMenuTitle.setText(leftMenuList.get(position).getTitle());

        return convertView;
    }

}
