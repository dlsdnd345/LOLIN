package com.iris.lolin;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.RequestAsyncTask;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.model.GraphObject;
import com.iris.adapter.BoardDetailPagerAdapter;
import com.iris.config.Config;
import com.iris.entities.Board;
import com.iris.listener.MessageListener;
import com.iris.pagerslidingtab.PagerSlidingTabStrip;
import com.iris.service.BoardDetailService;
import com.iris.util.ScreenshotUtil;
import com.iris.util.SharedpreferencesUtil;

public class BoardDetailActivity extends ActionBarActivity {

	private static final int CONTENT_FRAGMENT = 0;
	private static final int RECORD_SEARCH_FRAGMENT = 1;

	private Session.StatusCallback 		statusCallback = new SessionStatusCallback();
	private Session 					sessionTemp;

    private BroadcastReceiver           broadcastReceiver;

	private boolean 					editState;
	private int 						viewPagerPosition;
	private String						boardId;
	private RequestQueue 				request;
	private Board						board;
	private BoardDetailService 			boardDetailService;
	private ScreenshotUtil				screenshotUtil;
	private SharedpreferencesUtil 		sharedpreferencesUtil;

	private PagerSlidingTabStrip 		tabs;
	private ViewPager 					mViewPager;
	private PagerAdapter 				mPagerAdapter;
	private ProgressBar					prograssBar;

	private ImageView					imgShare;
	private ImageView					imgRank;

    private TextView                    textContent;
	private	TextView					textPosition;
	private TextView					textPlayTime;
	private TextView					textDetailTitle;
	private TextView					textSummernerName;

	private RelativeLayout 				layoutDelete;
	private RelativeLayout 				layoutUpdate;
	private RelativeLayout 				layoutFacebookSharing;

    private LinearLayout                layoutTopEdit;

    private MessageListener             messageListener;


    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acticity_board_detail);
		facebookInit(savedInstanceState);
	}

    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    @Override
	protected void onResume() {
		super.onResume();

		init();
		dataInit();
		//화면이 켜져 있을때 푸시화면을 보여주기 않기 위함.
		sharedpreferencesUtil.put(Config.FLAG.NOTIBLOCK, Config.FLAG.TRUE);
	}

	@Override
	protected void onPause() {
		super.onPause();
		//화면이 켜져 있을때 푸시화면을 보여주기 않기 위함.
		sharedpreferencesUtil.put(Config.FLAG.NOTIBLOCK, Config.FLAG.FALSE);

        this.unregisterReceiver(broadcastReceiver);
	}

	/**
	 * 레이아웃 초기화
	 */
	private void init() {

		prograssBar 			= (ProgressBar)findViewById(R.id.progressBar);

		imgRank					= (ImageView)findViewById(R.id.imgRank);
		imgShare				= (ImageView)findViewById(R.id.imgShare);

        textContent        	    = (TextView)findViewById(R.id.textContent);
		textPlayTime        	= (TextView)findViewById(R.id.textPlayTime);
		textPosition 			= (TextView)findViewById(R.id.txtPosition);
		textSummernerName 		= (TextView)findViewById(R.id.textSummonerName);
		textDetailTitle 		= (TextView)findViewById(R.id.textDetailTitle);
		mViewPager 				= (ViewPager) findViewById(R.id.pager);
		tabs 					= (PagerSlidingTabStrip) findViewById(R.id.tabs);

		layoutDelete	= (RelativeLayout)findViewById(R.id.layoutDelete);
		layoutUpdate	= (RelativeLayout)findViewById(R.id.layoutUpdate);
		layoutFacebookSharing	= (RelativeLayout)findViewById(R.id.layoutFacebookSharing);

        layoutTopEdit = (LinearLayout)findViewById(R.id.layoutTopEdit);

		layoutDelete.setOnClickListener(mClickListener);
		layoutUpdate.setOnClickListener(mClickListener);
		layoutFacebookSharing.setOnClickListener(mClickListener);
	}

	/**
	 * 데이터 초기화
	 */
	private void dataInit() {

        /**
         * 리시버 등록
         */
        reciveMessage();

		request = Volley.newRequestQueue(getApplicationContext());  
		screenshotUtil = new ScreenshotUtil(BoardDetailActivity.this);
		boardDetailService = new BoardDetailService(getApplicationContext());
		sharedpreferencesUtil = new SharedpreferencesUtil(getApplicationContext());

		//게시판 번호 받기 위함
		boardId = sharedpreferencesUtil.getValue(Config.BOARD.BOARD_ID, "");

		//자신의 게시판일 경우 수정 가능 여부
		visibleUpdate();
		//공유기능 여부
		visibleSharing();
		//게시판 정보 얻기 위함.
		getFindOne(request);
		//Pager Init
		actionBarInit();

		//UserInfo();
	}

    /**
     * GCM 전달 받을시 이벤트 발생
     */
    public void reciveMessage(){
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String boardId = intent.getStringExtra("boardId");
                String message = intent.getStringExtra("message");
                String summernerName = intent.getStringExtra("summernerName");
                String facebookId = intent.getStringExtra("facebookId");
                String repleId = intent.getStringExtra("repleId");
                String writeTime = intent.getStringExtra("writeTime");

                messageListener.sendMessage(boardId,message,summernerName,facebookId,repleId,writeTime);
            }
        };

        IntentFilter filter = new IntentFilter("BoardDetailActivity");
        this.registerReceiver(broadcastReceiver, filter);
    }

	/**
	 * 자신의 게시판일 경우 수정 항목 보여주기 위함.
	 */
	private void visibleUpdate() {
		Intent intent = getIntent();
		//editState = intent.getBooleanExtra(Config.FLAG.EDIT_STATE , false);
		editState = sharedpreferencesUtil.getValue(Config.FLAG.EDIT_STATE, false);
	}

	/**
	 * 자신의 게시판에서만 공유 버튼 활성화
	 */
	private void visibleSharing() {
		if(editState){
			imgShare.setVisibility(View.VISIBLE);
			layoutFacebookSharing.setVisibility(View.VISIBLE);
		}else{
			imgShare.setVisibility(View.GONE);
            layoutTopEdit.setVisibility(View.GONE);

		}
	}

	/**
	 * 삭제시 다이얼로그
	 */
	private void deleteDialog(){
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		alt_bld.setTitle(getString(R.string.dialog_title));
		alt_bld.setMessage(getString(R.string.baord_detail_delete_dialog_content))
		.setCancelable(false).setPositiveButton(getString(R.string.dialog_clear),
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				delete(request);
			}
		}).setNegativeButton(getString(R.string.dialog_cancel),
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		alt_bld.show();
	}
	/**
	 * 아이디를 통해서 게시판 한개의 데이터를 얻음.
	 * @param request
	 */
	private void getFindOne(RequestQueue request) {

		prograssBar.setVisibility(View.VISIBLE);

		String subUrl = boardDetailService.getFindOneSubUrl(boardId);

		request.add(new StringRequest
				(Request.Method.GET, Config.API.DEFAULT_URL +Config.API.BOARD_FIND_ONE+subUrl ,new Response.Listener<String>() {  

					@Override  
					public void onResponse(String response) {  

						board = boardDetailService.getBoardFindOne(response);

						if(board != null){
							textDetailTitle.setText(board.getTitle());
							textSummernerName.setText(board.getSummonerName());
							textPosition.setText(board.getPosition());
							textPlayTime.setText(board.getPlayTime());
                            textContent.setText(board.getContent());
						}
						// 이름별 랭크 이미지 삽입
						int resource = getResources().getIdentifier
								( "img_rank_"+board.getRank(), "drawable", getApplicationContext().getPackageName());
						imgRank.setBackgroundResource(resource);

						viewPagerInit();

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
	 * 게시물 삭제 Api
	 * @param request
	 */
	private void delete(RequestQueue request) {

		prograssBar.setVisibility(View.VISIBLE);

		String subUrl = boardDetailService.getDeleteSubUrl(boardId);

		request.add(new StringRequest
				(Request.Method.GET, Config.API.DEFAULT_URL + Config.API.BOARD_DELETE +subUrl ,new Response.Listener<String>() {  
					@Override  
					public void onResponse(String response) {
						Intent intent = new Intent(BoardDetailActivity.this, MainActivity.class);
						startActivity(intent);

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
	 * 페이스북 초기화
	 * @param savedInstanceState
	 */
	private void facebookInit(Bundle savedInstanceState) {

		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
		Session session = Session.getActiveSession();

		if (session == null) {
			if (savedInstanceState != null) {
				session = Session.restoreSession(this, null, statusCallback, savedInstanceState);
			}
			if (session == null) {
				session = new Session(this);
			}
			Session.setActiveSession(session);
			if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
				session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
			}
		}
	}

	/**
	 * 페이스북 세션 
	 * @author 박인웅
	 *
	 */
	private class SessionStatusCallback implements Session.StatusCallback {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			sessionTemp = session;
		}
	}

	/**
	 *  facebook 앨범에 이미지 올리기
	 */
	public void publishPhoto() {

		Session session = Session.getActiveSession();
		byte[] data = null;

		if (session != null) {

			data = screenshotUtil.takeScreenShot();

			prograssBar.setVisibility(View.VISIBLE);

			Bundle postParams = new Bundle();
			postParams.putByteArray(Config.FLAG.PICTURE,data);

			com.facebook.Request.Callback callback = new com.facebook.Request.Callback() 
			{
				@Override
				public void onCompleted(com.facebook.Response response) {
					FacebookRequestError error = response.getError();
					if (error != null){ 
						Toast.makeText(getApplicationContext() , Config.FLAG.NETWORK_CLEAR, Toast.LENGTH_SHORT).show();
					}else{ 
						try {
							String pictureId;
							GraphObject graphobject = response.getGraphObject();
							JSONObject  jSONObject =graphobject.getInnerJSONObject();
							pictureId = jSONObject.getString(Config.FLAG.ID);
							publishStory(pictureId);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					prograssBar.setVisibility(View.INVISIBLE);
				}
			};
			com.facebook.Request request = new com.facebook.Request(session, Config.API.FACEBOOK_ALBUM, postParams, HttpMethod.POST, callback);
			RequestAsyncTask task = new RequestAsyncTask(request);
			task.execute();
		}
	}

	// facebook에 포스팅
	private void publishStory(String pictureId) {

		prograssBar.setVisibility(View.VISIBLE);
		Session session = Session.getActiveSession();

		if (session != null) {

			Bundle postParams = new Bundle();
			postParams.putString(Config.FLAG.NAME, getString(R.string.facebook_publish_story_name));
			postParams.putString(Config.FLAG.MESSAGE, getString(R.string.facebook_publish_story_message));
			postParams.putString(Config.FLAG.LINK, getString(R.string.facebook_publish_story_link)+pictureId);

			com.facebook.Request.Callback callback = new com.facebook.Request.Callback() 
			{
				@Override
				public void onCompleted(com.facebook.Response response) {
					FacebookRequestError error = response.getError();
					if (error != null){ 
						Toast.makeText(getApplicationContext() , Config.FLAG.NETWORK_CLEAR, Toast.LENGTH_SHORT).show();
					}else{ 
						Toast.makeText(getApplicationContext() , getString(R.string.facebook_publish_story_complete_message), Toast.LENGTH_SHORT).show();
					}
					prograssBar.setVisibility(View.INVISIBLE);
				}
			};

			com.facebook.Request request = new com.facebook.Request(session, "me/feed", postParams, HttpMethod.POST,callback);
			RequestAsyncTask task = new RequestAsyncTask(request);
			task.execute();
		}
	}

	/**
	 * 액션바 초기화
	 */
	@SuppressLint("NewApi")
	private void actionBarInit() {
		//ActionBar Init
		Drawable drawable = getResources().getDrawable(R.drawable.title_bg);
		getActionBar().setBackgroundDrawable(drawable);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setTitle(R.string.board_detail_activity_title);
	}

	/**
	 * ViewPager 초기화
	 */
	private void viewPagerInit() {
		mPagerAdapter = new BoardDetailPagerAdapter(getApplicationContext(),getSupportFragmentManager(),board);
		mViewPager.setAdapter(mPagerAdapter);

		tabs.setIndicatorColor(Color.parseColor("#0099cc"));
		tabs.setOnPageChangeListener(mOnPageChangeListener);
		tabs.setViewPager(mViewPager);
	}

	/**
	 * 뷰페이저 리스너
	 */
	OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener(){
		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		@Override
		public void onPageSelected(int position) {

			viewPagerPosition = position;
			if(position == CONTENT_FRAGMENT){
				getActionBar().setTitle(R.string.detail_title_section1);
			}else if(position == RECORD_SEARCH_FRAGMENT){
				getActionBar().setTitle(R.string.detail_title_section2);
			}else{
				getActionBar().setTitle(R.string.detail_title_section3);
			}
		}
		@Override
		public void onPageScrolled(int position, float positionOffest, int positionOffestPixel) {}
		@Override
		public void onPageScrollStateChanged(int position) {}
	};

	/**
	 * 수정 버튼 선택시
	 */
	private void selectLayoutUpdate() {
		Intent intent = new Intent(BoardDetailActivity.this,ComposerActivity.class);
		intent.putExtra(Config.BOARD.BOARD_ID, boardId);
		startActivity(intent);
	}
	
	/**
	 * 버튼 리스너
	 */
	Button.OnClickListener mClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			//공유하기 버튼
			case R.id.layoutFacebookSharing:
				publishPhoto();
				break;
				//수정버튼
			case R.id.layoutUpdate:
				selectLayoutUpdate();
				break;
				//삭제버튼
			case R.id.layoutDelete:
				deleteDialog();
				break;
			}
		}

	};

}
