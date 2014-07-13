package com.iris.lolin;

import android.annotation.SuppressLint;
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.iris.config.Config;
import com.iris.entities.Board;
import com.iris.service.BoardDetailService;
import com.iris.service.ComposerService;
import com.iris.util.SharedpreferencesUtil;

@SuppressLint("NewApi")
public class ComposerActivity extends ActionBarActivity {

	private final static String BOARD_SAVE = "http://192.168.219.6:8080/board/save";
	private final static String ERROR = "Error";
	
	private static final String 		FACEBOOK_ID  				= "FACEBOOK_ID";
	private static final String 		EMPRY_CONTENT_MESSAGE  		= "내용을 입력해 주세요.";
	private static final String 		EMPRY_TITLE_MESSAGE  		= "제목을 입력해 주세요.";
	private static final String 		EMPRY_SUMMONERNAME_MESSAGE  = "소환사 명을 입력해 주세요."; 
	private static final String 		UNRANK 						= "언랭크"; 
	private static final int 			RANK_UNRANK 				= 0; 
	
	private int boardId  =-1;
	
	private SharedpreferencesUtil 		sharedpreferencesUtil;
	
	private BoardDetailService 			boardDetailService;
	
	private ComposerService				composerService;
	private Board						board;
	private TextView					txtTea;
	private String[] 					rankData,teaData,positionData,timeData;
	private EditText					editTitle, editContent;
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
		positionSpinner = (Spinner)findViewById(R.id.composer_spinner_position);
	}

	private void DataInit() {
		
		Intent intent = getIntent();
		
		boardId = intent.getIntExtra(Config.BOARD.BOARD_ID, -1);
		RequestQueue request = Volley.newRequestQueue(getApplicationContext());  
		
		if(boardId != -1){
			// 게시판 정보 api 요청
			getFindOne(request);
		}
		
		board = new Board();
		composerService = new ComposerService();
		boardDetailService = new BoardDetailService(getApplicationContext());
		sharedpreferencesUtil = new SharedpreferencesUtil(getApplicationContext());
		actionbarInit();
		spinnerInit();
		board.setTea(teaData[0]);
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
	
	
	/**
	 * 아이디를 통해서 게시판 한개의 데이터를 얻음.
	 * @param request
	 */
	private void getFindOne(RequestQueue request) {
		request.add(new StringRequest(Request.Method.GET
				, Config.BOARD.BOARD_FIND_ONE + Config.BOARD.SUB_URL+boardId ,new Response.Listener<String>() {  
			
			@Override  
			public void onResponse(String response) {  
				
				board = boardDetailService.getBoardFindOne(response);
				editTitle.setText(board.getTitle());
				editContent.setText(board.getContent());
				rankSpinner.setSelection(board.converterRank(board.getRank()));
				positionSpinner.setSelection(board.converterPosition(board.getPosition()));
				timeSpinner.setSelection(board.converterTime(board.getPlayTime()));
				teaSpinner.setSelection(board.converterTea(board.getTea()));
				
			}  
		}, new Response.ErrorListener() {  
			@Override  
			public void onErrorResponse(VolleyError error) {  
				VolleyLog.d(ERROR, error.getMessage());  
			}  
		}));
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.ic_action_new:
			
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
			
			if(!editTitle.getText().toString().equals("")
			 &&!editContent.getText().toString().equals("")){
				
				// 서버로 데이터 전송
				String facebookId = sharedpreferencesUtil.getValue(FACEBOOK_ID, "");
				RequestQueue request = Volley.newRequestQueue(getApplicationContext());  
				String subUrl = composerService.getSubUrl(boardId ,facebookId, board.getTitle(), board.getContent(),
						composerService.transformRank(board.getRank()), board.getPosition(), board.getPlayTime(),board.getTea());
				
				request.add(new StringRequest(Request.Method.GET, BOARD_SAVE+subUrl,new Response.Listener<String>() {  
					@Override  
					public void onResponse(String response) {
						Intent intent = new Intent(ComposerActivity.this , MainActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
					}  
				}, new Response.ErrorListener() {  
					@Override  
					public void onErrorResponse(VolleyError error) {  
						VolleyLog.d(ERROR, error.getMessage());  
					}  
				}));
				
			}
			
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
				//board.setRank(board.getRank());
				board.setTea(teaData[position]);
				System.err.println("@@@@@@@@@  티어 선택시   :  " + teaData[position]);
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
