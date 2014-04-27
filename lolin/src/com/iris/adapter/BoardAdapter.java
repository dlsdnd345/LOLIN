package com.iris.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iris.entities.Board;
import com.iris.lolin.R;

public class BoardAdapter extends BaseAdapter{

	private int layout; 
	private Context context; 
	private LayoutInflater Inflater;
	private ArrayList<Board> boardList; 

	public BoardAdapter(Context context , int layout , ArrayList<Board> boardList){

		this.context=context;
		this.layout = layout;
		this.boardList =boardList;
		Inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

		if(convertView==null){
			convertView = Inflater.inflate(layout, parent,false);
		}

		TextView txtTitle = (TextView)convertView.findViewById(R.id.txt_title);
		txtTitle.setText(boardList.get(position).getTitle());

		TextView txtSummonerName = (TextView)convertView.findViewById(R.id.txt_summonerName);
		txtSummonerName.setText(boardList.get(position).getSummonerName());

		TextView txtPosition = (TextView)convertView.findViewById(R.id.txt_position);
		txtPosition.setText(boardList.get(position).getPosition());

		return convertView;
	}

}
