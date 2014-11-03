package com.iris.lolin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.Session;
import com.iris.adapter.SectionsPagerAdapter;
import com.iris.config.Config;
import com.iris.pagerslidingtab.PagerSlidingTabStrip;
import com.iris.util.SharedpreferencesUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

@SuppressLint("NewApi")
public class MainActivity extends ActionBarActivity  {

	private final static int WRITE_TEXT 		= 1;
	private final static int RECORD_SEARCH 	= 2;
	private final static int SETTING 		= 3;

	private int 						viewPagerPosition;
	
    private ActionBarDrawerToggle 		mDrawerToggle;
    
    private DisplayImageOptions 		options;
    private ImageLoader 				imageLoader;
    private SharedpreferencesUtil 		sharedpreferencesUtil;
    
    private Button						btnLogin;
    private Button						btnLogout;
    private ImageView 					imgProfile;
    private TextView 					textLoginMassage;
    private LinearLayout 				leftDrawer;
    private DrawerLayout 				mDrawerLayout;
    
	private ViewPager 					mViewPager;
	private PagerSlidingTabStrip 		tabs;
	private SectionsPagerAdapter 		mSectionsPagerAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		init();
		dataInit();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//화면이 켜져 있을때 푸시화면을 보여주기 않기 위함.
		sharedpreferencesUtil.put(Config.FLAG.NOTIBLOCK, Config.FLAG.TRUE);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		//화면이 꺼져 있을때 푸시화면을 보여주기 위함.
		sharedpreferencesUtil.put(Config.FLAG.NOTIBLOCK, Config.FLAG.FALSE);
	}
	
	/**
	 * 액션바 아이콘 생성
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		boolean isLogin = sharedpreferencesUtil.getValue(Config.FLAG.IS_LOGIN, false);
		if(isLogin){
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.write_text_menu, menu);
		}
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * 레이아웃 초기화
	 */
	private void init() {
		
		btnLogin 			= (Button) findViewById(R.id.btnLogin);
		btnLogout 			= (Button) findViewById(R.id.btnLogout);
		mViewPager 			= (ViewPager) findViewById(R.id.pager);
		tabs 				= (PagerSlidingTabStrip) findViewById(R.id.tabs);
		leftDrawer 			= (LinearLayout) findViewById(R.id.leftDrawer);
		mDrawerLayout 		= (DrawerLayout) findViewById(R.id.layoutDrawer);
		textLoginMassage 	= (TextView)findViewById(R.id.textLoginMassage);
		imgProfile 			= (ImageView)findViewById(R.id.imgProfile);
	}

	/**
	 * 데이터 초기화
	 */
	private void dataInit() {
		
		sharedpreferencesUtil = new SharedpreferencesUtil(getApplicationContext());
		
		actionvarInit();
		viewPagerConfig();
        drawerLayoutInit();
		imageLodearInit();
		visibleMenuDrawer();
	}

	/**
	 * 로그인시 /로그아웃시 메뉴 드로어
	 */
	private void visibleMenuDrawer() {
		boolean isLogin = sharedpreferencesUtil.getValue(Config.FLAG.IS_LOGIN, false);
		if(isLogin){
			
			textLoginMassage.setText
			(sharedpreferencesUtil.getValue(Config.FLAG.FACEBOOK_NAME, getString(R.string.main_text_login_massege)));
			
			String facebookId = sharedpreferencesUtil.getValue(Config.FACEBOOK.FACEBOOK_ID, "");
			String url = Config.FACEBOOK.FACEBOOK_BASE_URL+ facebookId+Config.FACEBOOK.PICTURE_SMALL;
			imageLoader.displayImage(url, imgProfile, options);
			
			btnLogin.setVisibility(View.GONE);
			btnLogout.setVisibility(View.VISIBLE);
		}else{
			textLoginMassage.setText(getString(R.string.main_text_login_massege));
			btnLogin.setVisibility(View.VISIBLE);
			btnLogout.setVisibility(View.GONE);
			imgProfile.setBackgroundResource(R.drawable.ic_launcher);
		}
	}

	/**
	 * 이미지 다운로더 초기화
	 */
	private void imageLodearInit() {
		options = new DisplayImageOptions.Builder()
		.showImageOnFail(Color.TRANSPARENT) // 에러 났을때 나타나는 이미지
		.cacheInMemory(true)
		.displayer(new RoundedBitmapDisplayer(1000))
		.cacheOnDisc(true)
		.considerExifParams(true)
		.build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
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
	 * 네비게이션 드로워 초기화
	 */
	private void drawerLayoutInit() {
		// set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	private void actionvarInit() {
		//ActionBar Init
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);
        Drawable drawable = getResources().getDrawable(R.drawable.title_bg);
        getActionBar().setBackgroundDrawable(drawable);
		getActionBar().setTitle(R.string.title_section1);
	}


	/**
	 * 액션바 아이템 선택시 이벤트 발생
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		
		//네비게이션 드로어 열림/닫힘
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
		
		switch (item.getItemId()) {
		case R.id.ic_action_new:
			Intent composerActivityintent = new Intent(MainActivity.this, ComposerActivity.class);
			startActivity(composerActivityintent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
	
	/**
	 * Logout From Facebook 
	 */
	public static void callFacebookLogout(Context context) {
	    Session session = Session.getActiveSession();
	    if (session != null) {
	        if (!session.isClosed()) {
	            session.closeAndClearTokenInformation();
	        }
	    } else {
	        session = new Session(context);
	        Session.setActiveSession(session);
	        session.closeAndClearTokenInformation();
	    }
	    Intent intent = new Intent(context,FaceBookLoginActivity.class);
	    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
	    context.startActivity(intent);
	}
	
	/**
	 * 로그아웃시 다이얼로그
	 */
	private void logoutDialog(){
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		alt_bld.setTitle(getString(R.string.dialog_title));
		alt_bld.setMessage(getString(R.string.setting_dialog_logout))
		.setCancelable(false).setPositiveButton(getString(R.string.dialog_clear),
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				sharedpreferencesUtil.put(Config.FLAG.ACCESS_TOKEN, "");
				callFacebookLogout(getApplicationContext());
			}
		}).setNegativeButton(getString(R.string.dialog_cancel),
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		alt_bld.show();
	}
    
	private void viewPagerConfig() {

		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), getApplicationContext());
		mViewPager.setAdapter(mSectionsPagerAdapter);
		tabs.setIndicatorColor(Color.parseColor("#0099cc"));
		tabs.setOnPageChangeListener(mOnPageChangeListener);
		tabs.setViewPager(mViewPager);
		
	}

	/**
	 * 로그아웃 버튼 클릭시
	 * @param view
	 */
	public void logout(View view){
		logoutDialog();
	}
	
	/**
	 * 로그인 선택시
	 * @param view
	 */
	public void login(View view){
		Intent intent = new Intent(MainActivity.this, FaceBookLoginActivity.class);
		startActivity(intent);
	}
	
	/**
	 * 페이지 이동
	 * @param page
	 */
	public void changePage(int page){
		mViewPager.setCurrentItem(page,true);
	}
	
	OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener(){
		@Override
		public void onPageSelected(int position) {

			viewPagerPosition = position;

			if(position == WRITE_TEXT){
				getActionBar().setTitle(R.string.title_section2);
				invalidateOptionsMenu();
			}else if(position == RECORD_SEARCH){
				getActionBar().setTitle(R.string.title_section3);
				invalidateOptionsMenu();
			}else if(position == SETTING){
				getActionBar().setTitle(R.string.title_section4);
				invalidateOptionsMenu();
			}else{
				getActionBar().setTitle(R.string.title_section1);
				invalidateOptionsMenu();
			}
		}
		@Override
		public void onPageScrolled(int position, float positionOffest, int positionOffestPixel) {}
		@Override
		public void onPageScrollStateChanged(int position) {}
	};

}
