package com.iris.lolin;

import com.facebook.Request;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.iris.config.Config;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class PushActivity extends Activity {

	private static final String 		FACEBOOK_BASE_URL  				= "http://graph.facebook.com/";
	private static final String 		PICTURE_TYPE					= "/picture?type=normal";
	
	private String boardId , message , summernerName , facebookId;
	
	private ImageView					imgProfile;
	private TextView 					txtMessage;
	private TextView 					txtSummernerName;
	private DisplayImageOptions 		options;
	private ImageLoader 				imageLoader;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_push);
	
	    init();
	    dataInit();
	    
	}

	/**
	 * 데이터초기화
	 */
	private void dataInit() {
		
		imageLoderInit();
		
		Intent intent = getIntent();
		if(intent != null){
			boardId = intent.getStringExtra(Config.BOARD.BOARD_ID);
			message = intent.getStringExtra(Config.FLAG.MESSAGE);
			summernerName = intent.getStringExtra(Config.FLAG.SUMMERNER_NAME);
			facebookId = intent.getStringExtra(Config.FLAG.FACEBOOK_ID);
		}
		
		txtMessage.setText(message);
		txtSummernerName.setText(summernerName);
		
		String url = FACEBOOK_BASE_URL+ facebookId+PICTURE_TYPE;
		imageLoader.displayImage(url, imgProfile, options);
	}

	/**
	 * imageLoder초기화
	 */
	private void imageLoderInit() {
		options = new DisplayImageOptions.Builder()
		.showImageOnFail(Color.TRANSPARENT) // 에러 났을때 나타나는 이미지
		.cacheInMemory(true)
		.displayer(new RoundedBitmapDisplayer(1000))
		.cacheOnDisc(true)
		.considerExifParams(true)
		.build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
		.threadPriority(Thread.NORM_PRIORITY - 2)
		.denyCacheImageMultipleSizesInMemory()
		.discCacheFileNameGenerator(new Md5FileNameGenerator())
		.tasksProcessingOrder(QueueProcessingType.LIFO)
		.writeDebugLogs()
		.build();
		ImageLoader.getInstance().init(config);
		imageLoader = ImageLoader.getInstance();
	}
	
	/**
	 * 레이아웃 초기화
	 */
	private void init() {
		
		imgProfile = (ImageView)findViewById(R.id.img_profile);
		txtMessage = (TextView)findViewById(R.id.txt_message);
		txtSummernerName = (TextView)findViewById(R.id.txt_summerner_name);
	}

	@Override
	protected void onResume() {
		super.onResume();
		//Wake Up 폰 화면 꺼져있을시에 푸시가 도착하면 화면을 깨워주기 위함.
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED   
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
	}
	
	public void selectClear(View view){
		
		Intent intent = new Intent(PushActivity.this,BoardDetailActivity.class);
		intent.putExtra(Config.BOARD.BOARD_ID, boardId);
		intent.putExtra(Config.FLAG.MESSAGE, message);
		startActivity(intent);
		finish();
	}
	
	public void selectCancel(View view){
		finish();
	}
	
}
