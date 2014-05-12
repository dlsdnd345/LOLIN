package com.iris.lolin;

import com.iris.entities.Board;
import com.iris.lolin.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class ComposerActivity extends ActionBarActivity {

	private static final String 		EMPRY_CONTENT_MESSAGE  		= "내용을 입력해 주세요.";
	private static final String 		EMPRY_TITLE_MESSAGE  		= "제목을 입력해 주세요.";
	private static final String 		EMPRY_SUMMONERNAME_MESSAGE  = "소환사 명을 입력해 주세요."; 
	private static final String 		UNRANK 						= "언랭크"; 
	private static final int 			RANK_UNRANK 				= 0; 
	
	
	private Board						board;
	private TextView					txtTea;
	private String[] 					rankData,teaData,positionData,timeData;
	private EditText					editSummonerName , editTitle, editContent;
	private Spinner 					rankSpinner,teaSpinner,positionSpinner,timeSpinner;		
	private ArrayAdapter<String> 		rankSpinnerAdapter,teaSpinnerAdapter,positionSpinnerAdapter,timeSpinnerAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_composer);
	    
		init();
		DataInit();
	}

	private void init() {
		txtTea = (TextView)findViewById(R.id.txt_tea);
		editTitle = (EditText)findViewById(R.id.edit_title);
		editContent = (EditText)findViewById(R.id.edit_content);
		rankSpinner = (Spinner)findViewById(R.id.composer_spinner_rank);
		teaSpinner = (Spinner)findViewById(R.id.composer_spinner_tea);
		timeSpinner = (Spinner)findViewById(R.id.composer_spinner_time);
		editSummonerName = (EditText)findViewById(R.id.edit_summoner_name);
		positionSpinner = (Spinner)findViewById(R.id.composer_spinner_position);
	}

	private void DataInit() {
		
		board = new Board();
		
		actionbarInit();
		spinnerInit();
	}

	private void spinnerInit() {
		//spinner init
		rankData = getResources().getStringArray(R.array.composer_rank_array_list);
		rankSpinnerAdapter= new ArrayAdapter<>
		(getApplicationContext(), R.layout.spinner_item,rankData);
		rankSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
		rankSpinner.setAdapter(rankSpinnerAdapter); 
		rankSpinner.setOnItemSelectedListener(rankOnItemSelectedListener);
		
		teaData = getResources().getStringArray(R.array.composer_tea_array_list);
		teaSpinnerAdapter= new ArrayAdapter<>
		(getApplicationContext(), R.layout.spinner_item,teaData);
		teaSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
		teaSpinner.setAdapter(teaSpinnerAdapter); 
		teaSpinner.setOnItemSelectedListener(teaOnItemSelectedListener);
		
		positionData = getResources().getStringArray(R.array.composer_position_array_list);
		positionSpinnerAdapter= new ArrayAdapter<>
		(getApplicationContext(), R.layout.spinner_item,positionData);
		positionSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
		positionSpinner.setAdapter(positionSpinnerAdapter); 
		positionSpinner.setOnItemSelectedListener(positionOnItemSelectedListener);
		
		timeData = getResources().getStringArray(R.array.composer_time_array_list);
		timeSpinnerAdapter= new ArrayAdapter<>
		(getApplicationContext(), R.layout.spinner_item,timeData);
		timeSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
		timeSpinner.setAdapter(timeSpinnerAdapter); 
		timeSpinner.setOnItemSelectedListener(timeOnItemSelectedListener);
	}

	private void actionbarInit() {
		//ActionBar Init
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setTitle(R.string.composer_activity_title);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();

		inflater.inflate(R.menu.write_text_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.ic_action_new:
			
			if(!editSummonerName.getText().toString().equals("")){
				board.setSummonerName(editSummonerName.getText().toString());
			}else{
			    Animation shake = AnimationUtils.loadAnimation(this, R.anim.horizontal_shake);
			    editSummonerName.startAnimation(shake);
				Toast.makeText(getApplicationContext(), EMPRY_SUMMONERNAME_MESSAGE, Toast.LENGTH_LONG).show();
			}
			
			if(!editTitle.getText().toString().equals("")){
				board.setTitle(editTitle.getText().toString());
			}else{
			    Animation shake = AnimationUtils.loadAnimation(this, R.anim.horizontal_shake);
			    editTitle.startAnimation(shake);
				Toast.makeText(getApplicationContext(), EMPRY_TITLE_MESSAGE, Toast.LENGTH_LONG).show();
			}

			if(!editContent.getText().toString().equals("")){
				board.setContent(editContent.getText().toString());
			}else{
			    Animation shake = AnimationUtils.loadAnimation(this, R.anim.horizontal_shake);
			    editContent.startAnimation(shake);
				Toast.makeText(getApplicationContext(), EMPRY_CONTENT_MESSAGE, Toast.LENGTH_LONG).show();
			}
			
			if(!editSummonerName.getText().toString().equals("")
			 ||!editTitle.getText().toString().equals("")
			 ||!editContent.getText().toString().equals("")){
				
				// 서버로 데이터 전송
			}
			
			System.err.println("소환사 명  :  " + board.getSummonerName());
			System.err.println("계급  :  " + board.getRank());
			System.err.println("포지션  :  " + board.getPosition());
			System.err.println("시간  :  " + board.getPlayTime());
			System.err.println("제목  :  " + board.getTitle());
			System.err.println("내용  :  " + board.getContent());
			
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	OnItemSelectedListener rankOnItemSelectedListener = new OnItemSelectedListener(){
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
			
			if(position == RANK_UNRANK){
				txtTea.setVisibility(View.INVISIBLE);
				teaSpinner.setVisibility(View.INVISIBLE);
			}else{
				txtTea.setVisibility(View.VISIBLE);
				teaSpinner.setVisibility(View.VISIBLE);
			}
			
			board.setRank(rankData[position]);
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {}
	};
	
	OnItemSelectedListener teaOnItemSelectedListener = new OnItemSelectedListener(){
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
			if(!board.getRank().equals(UNRANK)){
				board.setRank(board.getRank() + teaData[position]);
			}
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {}
	};
	
	OnItemSelectedListener positionOnItemSelectedListener = new OnItemSelectedListener(){
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
			board.setPosition(positionData[position]);
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {}
	};
	
	OnItemSelectedListener timeOnItemSelectedListener = new OnItemSelectedListener(){
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
			board.setPlayTime(timeData[position]);
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {}
	};

}
