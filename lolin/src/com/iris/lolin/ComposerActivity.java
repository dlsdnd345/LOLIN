package com.iris.lolin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.iris.analytics.GoogleTracker;
import com.iris.config.Config;
import com.iris.entities.Board;
import com.iris.service.BoardDetailService;
import com.iris.service.ComposerService;
import com.iris.util.SharedpreferencesUtil;

@SuppressLint("NewApi")
/**
 * 글쓰기 화면
 * @author 박인웅
 *
 */
public class ComposerActivity extends ActionBarActivity {

    private static final String TAG = ComposerActivity.class.getSimpleName();

	private static final String 		UNRANK 						= "언랭크"; 
	private static final int 			RANK_UNRANK 				= 0; 

	private int 						rankPosition = 0;
	private int 						teaPosition = 0;
	private int 						positionPosition = 0;
	private int 						timePosition = 0;
	
	private String 						boardId ;
	private String[] 					rankData,teaData,positionData,timeData;
	private Board						board;
	private SharedpreferencesUtil 		sharedpreferencesUtil;
	private BoardDetailService 			boardDetailService;
	private ComposerService				composerService;

	private LinearLayout 				layoutTea;
	
	private ProgressBar					prograssBar;
	private TextView					txtTea;
	private EditText					editTitle, editContent;
	private Spinner 					rankSpinner,teaSpinner,positionSpinner,timeSpinner;		
	private ArrayAdapter<String> 		rankSpinnerAdapter,teaSpinnerAdapter,positionSpinnerAdapter,timeSpinnerAdapter;

    private GoogleTracker googleTracker;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_composer);

		init();
		DataInit();
	}

    @Override
    protected void onStart() {
        super.onStart();

        googleTracker.actionActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        googleTracker.actionActivityStop(this);
    }

	/**
	 * 레이아웃 초기화
	 */
	private void init() {
		
		layoutTea 		= (LinearLayout)findViewById(R.id.layoutTea);
		prograssBar 	= (ProgressBar)findViewById(R.id.progressBar);
		editTitle 		= (EditText)findViewById(R.id.editTitle);
		editContent 	= (EditText)findViewById(R.id.editContent);
		rankSpinner 	= (Spinner)findViewById(R.id.composerSpinnerRank);
		teaSpinner 		= (Spinner)findViewById(R.id.composerSpinnerTea);
		timeSpinner 	= (Spinner)findViewById(R.id.composerSpinnerTime);
		positionSpinner = (Spinner)findViewById(R.id.composerSpinnerPosition);
	}

	/**
	 * 데이터 초기화
	 */
	private void DataInit() {

        googleTracker = GoogleTracker.getInstance(this);
        googleTracker.sendScreenView(TAG);


        Intent intent = getIntent();

		boardId = intent.getStringExtra(Config.BOARD.BOARD_ID);
		RequestQueue request = Volley.newRequestQueue(getApplicationContext());  

		board = new Board();
		composerService = new ComposerService();
		boardDetailService = new BoardDetailService(getApplicationContext());
		sharedpreferencesUtil = new SharedpreferencesUtil(getApplicationContext());
		actionbarInit();
		spinnerInit();
		board.setTea(teaData[0]);

		if(boardId != null){
			// 게시판 정보 api 요청
			getFindOne(request);
		}else{
			
			editTitle.setText(rankData[0]+" "+positionData[0]+" "+getString(R.string.composor_activity_edit_title_liner)
					+" "+timeData[0]+" "+getString(R.string.composor_activity_edit_title_message));
		}

	}

	/**
	 * 스피너 초기화
	 */
	private void spinnerInit() {
		//spinner init
		rankData = getResources().getStringArray(R.array.composer_rank_array_list);
		rankSpinnerAdapter= new ArrayAdapter<>
		(getApplicationContext(), R.layout.spinner_white_item,rankData);
		rankSpinnerAdapter.setDropDownViewResource(R.layout.spinner_white_item);
		rankSpinner.setAdapter(rankSpinnerAdapter); 
		rankSpinner.setOnItemSelectedListener(rankOnItemSelectedListener);

		teaData = getResources().getStringArray(R.array.composer_tea_array_list);
		teaSpinnerAdapter= new ArrayAdapter<>
		(getApplicationContext(), R.layout.spinner_white_item,teaData);
		teaSpinnerAdapter.setDropDownViewResource(R.layout.spinner_white_item);
		teaSpinner.setAdapter(teaSpinnerAdapter); 
		teaSpinner.setOnItemSelectedListener(teaOnItemSelectedListener);

		positionData = getResources().getStringArray(R.array.composer_position_array_list);
		positionSpinnerAdapter= new ArrayAdapter<>
		(getApplicationContext(), R.layout.spinner_white_item,positionData);
		positionSpinnerAdapter.setDropDownViewResource(R.layout.spinner_white_item);
		positionSpinner.setAdapter(positionSpinnerAdapter); 
		positionSpinner.setOnItemSelectedListener(positionOnItemSelectedListener);

		timeData = getResources().getStringArray(R.array.composer_time_array_list);
		timeSpinnerAdapter= new ArrayAdapter<>
		(getApplicationContext(), R.layout.spinner_white_item,timeData);
		timeSpinnerAdapter.setDropDownViewResource(R.layout.spinner_white_item);
		timeSpinner.setAdapter(timeSpinnerAdapter); 
		timeSpinner.setOnItemSelectedListener(timeOnItemSelectedListener);
	}

	/**
	 * 액션바 초기화 
	 */
	private void actionbarInit() {
		//ActionBar Init
		Drawable drawable = getResources().getDrawable(R.drawable.title_bg);
		getActionBar().setBackgroundDrawable(drawable);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setTitle(R.string.composer_activity_title);
	}

	/**
	 * 액션바 버튼 초기화
	 */
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

		prograssBar.setVisibility(View.VISIBLE);

		String subUrl = composerService.getFindOneSubUrl(boardId);

		request.add(new StringRequest(Request.Method.GET
				, Config.API.DEFAULT_URL + Config.API.BOARD_FIND_ONE+subUrl ,new Response.Listener<String>() {  

			@Override  
			public void onResponse(String response) {  

				board = boardDetailService.getBoardFindOne(response);
				editTitle.setText(board.getTitle());
				editContent.setText(board.getContent());
				rankSpinner.setSelection(board.converterRank(board.getRank()));
				positionSpinner.setSelection(board.converterPosition(board.getPosition()));
				timeSpinner.setSelection(board.converterTime(board.getPlayTime()));
				teaSpinner.setSelection(board.converterTea(board.getTea()));

				prograssBar.setVisibility(View.INVISIBLE);
			}  
		}, new Response.ErrorListener() {  
			@Override  
			public void onErrorResponse(VolleyError error) {  
				VolleyLog.d(Config.FLAG.ERROR, error.getMessage());  
				prograssBar.setVisibility(View.INVISIBLE);
				Toast.makeText(getApplicationContext(), Config.FLAG.NETWORK_CLEAR, Toast.LENGTH_LONG).show();
			}  
		}));
	}

	/**
	 * 게시판 내용 저장 및 생성
	 * @param request
	 */
	private void saveBoard(RequestQueue request) {

		prograssBar.setVisibility(View.VISIBLE);

		// 서버로 데이터 전송
		String facebookId = sharedpreferencesUtil.getValue(Config.FACEBOOK.FACEBOOK_ID, "");

		String subUrl = composerService.getSubUrl(boardId ,facebookId, board.getTitle(), board.getContent(),
				board.transformRank(rankData[rankPosition]), board.getPosition(), board.getPlayTime(),board.getTea());

		request.add(new StringRequest(Request.Method.GET, Config.API.DEFAULT_URL + Config.API.BOARD_SAVE+subUrl,new Response.Listener<String>() {
			@Override  
			public void onResponse(String response) {
				Intent intent = new Intent(ComposerActivity.this , MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				prograssBar.setVisibility(View.INVISIBLE);

                /**
                 * 메인화면 게시판 초기화 될수있도록 하기 위함.
                 */
                sharedpreferencesUtil.put("isLoad",true);

			}
		}, new Response.ErrorListener() {  
			@Override  
			public void onErrorResponse(VolleyError error) {  
				VolleyLog.d(Config.FLAG.ERROR, error.getMessage());  
				prograssBar.setVisibility(View.INVISIBLE);
				Toast.makeText(getApplicationContext(), Config.FLAG.NETWORK_CLEAR, Toast.LENGTH_LONG).show();
			}  
		}));
	}

	/**
	 * 서버 저장 내용 전송
	 */
	private void requestSaveBoard() {
		if(!editTitle.getText().toString().equals("")
				&&!editContent.getText().toString().equals("")){
			RequestQueue request = Volley.newRequestQueue(getApplicationContext());  
			saveBoard(request);
		}
	}

	/**
	 * 내용 입력 예외 처리
	 */
	private void emptyCheckContent() {
		if(!editContent.getText().toString().equals("")){
			board.setContent(editContent.getText().toString());
		}else{
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.horizontal_shake);
			editContent.startAnimation(shake);
			Toast.makeText(getApplicationContext(), getString(R.string.composor_activity_edit_content_empty_message), Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Title 입력 예외처리
	 */
	private void emptyCheckTitle() {
		if(!editTitle.getText().toString().equals("")){
			board.setTitle(editTitle.getText().toString());
		}else{
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.horizontal_shake);
			editTitle.startAnimation(shake);
			Toast.makeText(getApplicationContext(), getString(R.string.composor_activity_edit_title_empty_message), Toast.LENGTH_LONG).show();
		}
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.ic_action_new:

			emptyCheckTitle();
			emptyCheckContent();
			requestSaveBoard();

            googleTracker.sendEventView("작성","버튼","추가");

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * 제목 작성
	 */
	private void setTextTitle() {
		if(rankPosition == 0){
			editTitle.setText(rankData[rankPosition]+" "+positionData[positionPosition]+" "+getString(R.string.composor_activity_edit_title_liner)
					+" "+timeData[timePosition]+" "+getString(R.string.composor_activity_edit_title_message));
		}else{
			editTitle.setText(rankData[rankPosition]+" "+teaData[teaPosition]+" "+positionData[positionPosition]
					+" "+getString(R.string.composor_activity_edit_title_liner)
					+" "+timeData[timePosition]+" "+getString(R.string.composor_activity_edit_title_message));
		}
	}
	
	/**
	 * 랭크 스피너 선택시
	 */
	OnItemSelectedListener rankOnItemSelectedListener = new OnItemSelectedListener(){
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {

			if(position == RANK_UNRANK){
				layoutTea.setVisibility(View.INVISIBLE);
			}else{
				layoutTea.setVisibility(View.VISIBLE);
			}
			
			rankPosition = position;
			setTextTitle();
			
			board.setRank(rankData[position]);

		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {}
	};

	/**
	 * 티어 스피너 선택시
	 */
	OnItemSelectedListener teaOnItemSelectedListener = new OnItemSelectedListener(){
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
			if(!board.getRank().equals(UNRANK)){
				
				teaPosition = position;
				board.setTea(teaData[position]);
			}
			
			setTextTitle();
			
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {}
	};

	/**
	 * 포지션 스피너 선택시
	 */
	OnItemSelectedListener positionOnItemSelectedListener = new OnItemSelectedListener(){
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
			
			positionPosition = position;
			board.setPosition(positionData[position]);
			
			setTextTitle();
			
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {}
	};

	/**
	 * 시간 스피너 선택시
	 */
	OnItemSelectedListener timeOnItemSelectedListener = new OnItemSelectedListener(){
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
			
			timePosition = position;
			board.setPlayTime(timeData[position]);
			
			setTextTitle();
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {}
	};

}
