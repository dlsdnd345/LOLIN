package com.iris.adapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iris.config.Config;
import com.iris.entities.Board;
import com.iris.lolin.R;
import com.iris.service.BoardService;
import com.iris.util.ViewHolder;

public class ComposerAdapter extends BaseAdapter{

	private BoardService						boardService;
	
	private int 								layout; 
	private Context 							context; 
	private LayoutInflater 						inflater;
	private ArrayList<Board> 					boardList; 
	private SparseArray<WeakReference<View>> 	viewArray;
	
	public ComposerAdapter(Context context , int layout , ArrayList<Board> boardList){

		this.context=context;
		this.layout = layout;
		this.boardList =boardList;
		boardService = new BoardService(context);
		inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.viewArray = new SparseArray<WeakReference<View>>(boardList.size());
	}

	public int getCount() {
		return boardList.size();
	}

	@Override
	public String getItem(int position) {		
		return boardList.get(position).getTitle();
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
		
		ImageView 	rankImage 			= ViewHolder.get(convertView ,R.id.img_rank);		
		TextView 	txtTitle 			= ViewHolder.get(convertView ,R.id.txt_title);
		TextView 	txtPosition 		= ViewHolder.get(convertView ,R.id.txt_position);
		TextView 	txtSummonerName 	= ViewHolder.get(convertView ,R.id.txt_summonerName);
		TextView 	txtContent 			= ViewHolder.get(convertView ,R.id.txt_content);
		TextView 	textPlayTime 		= ViewHolder.get(convertView ,R.id.text_playTime);
		TextView 	textWriteTime 		= ViewHolder.get(convertView ,R.id.text_write_time);
		TextView 	textRepleCount 		= ViewHolder.get(convertView ,R.id.text_reple_count);
		
		// 이름별 랭크 이미지 삽입
		int resource = convertView.getResources().getIdentifier
		( "img_rank_"+boardList.get(position).getRank(), "drawable", context.getPackageName());
		rankImage.setBackgroundResource(resource);
		
		txtTitle.setText(boardList.get(position).getTitle());
		txtPosition.setText(boardList.get(position).getPosition());
		txtSummonerName.setText(boardList.get(position).getSummonerName());
		txtContent.setText(boardList.get(position).getContent());
		textPlayTime.setText(boardList.get(position).getPlayTime());
		textWriteTime.setText(boardList.get(position).getWriteTime());
		visibleRepleCount(position, textRepleCount);
		return convertView;
	}

	/**
	 * 댓글이 존재 하는 경우만 리플 숫자를 표시
	 * @param position
	 * @param textRepleCount
	 */
	private void visibleRepleCount(int position, TextView textRepleCount) {
		if(boardList.get(position).getRepleCount() != null 
		   && !boardList.get(position).getRepleCount().equals(Config.NUMBER.ZERO)){
			textRepleCount.setText(boardService.transformRepleCount(boardList.get(position).getRepleCount()));
		}else{
			textRepleCount.setText("");
		}
	}
}
