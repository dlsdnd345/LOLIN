//package com.iris.adapter;
//
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.iris.entities.LeftMenu;
//import com.iris.listener.ReftMenuOnTapListener;
//import com.iris.lolin.R;
//
//import java.util.List;
//
///**
// * Created by evahpirazzi on 2015-04-08.
// */
//public class ReftMenuListAdapter extends RecyclerView.Adapter<ReftMenuListAdapter.ViewHolder>{
//
//    public static final String TAG = ReftMenuListAdapter.class.getSimpleName();
//
//    private List<LeftMenu> mDataList = null;
//
//    private ReftMenuOnTapListener mReftMenuOnTapListener;
//
//    public void setOnTapListener(ReftMenuOnTapListener onTapListener)
//    {
//        mReftMenuOnTapListener = onTapListener;
//    }
//
//    public ReftMenuListAdapter(List<LeftMenu> dataList) {
//
//        mDataList = dataList;
//
//    }
//
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_left_menu, null);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
//
//        viewHolder.getTextCategoryTitle().setText(mDataList.get(i).getTitle());
//
//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                if (mReftMenuOnTapListener != null) {
//                    mReftMenuOnTapListener.onTapView(i);
//                }
//
//            }
//        });
//
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return mDataList.size();
//    }
//
//
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//
//        private TextView textCategoryTitle = null;
//
//        public ViewHolder(View view){
//            super(view);
//
//            textCategoryTitle = (TextView) view.findViewById(R.id.textCategoryTitle);
//        }
//
//        public TextView getTextCategoryTitle() {
//            return textCategoryTitle;
//        }
//
//    }
//
//}
