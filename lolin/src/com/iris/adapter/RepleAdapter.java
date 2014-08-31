package com.iris.adapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
import com.iris.entities.Reple;
import com.iris.lolin.R;
import com.iris.service.RepleService;
import com.iris.util.SharedpreferencesUtil;
import com.iris.util.ViewHolder;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class RepleAdapter extends BaseAdapter{

	private static final String 		FACEBOOK_BASE_URL  	= "http://graph.facebook.com/";
	private static final String 		PICTURE_TYPE		= "/picture?type=small";
	
	private int 								layout;
	private String								userName;
	private Context 							context; 
	private LayoutInflater 						inflater;
	private ArrayList<Reple> 					repleList; 
	private SparseArray<WeakReference<View>> 	viewArray;
	private DisplayImageOptions 				options;
	private ImageLoader 						imageLoader;
	private SharedpreferencesUtil 				sharedpreferencesUtil;
	
	public RepleAdapter(Context context , int layout , ArrayList<Reple> repleList, String userName){

		this.userName = userName;
		this.context=context;
		this.layout = layout;
		this.repleList =repleList;
		
		sharedpreferencesUtil = new SharedpreferencesUtil(context);
		inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.viewArray = new SparseArray<WeakReference<View>>(repleList.size());
		
		options = new DisplayImageOptions.Builder()
		.showImageOnFail(Color.TRANSPARENT) // 에러 났을때 나타나는 이미지
		.cacheInMemory(true)
		.displayer(new RoundedBitmapDisplayer(1000))
		.cacheOnDisc(true)
		.considerExifParams(true)
		.build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
		.threadPriority(Thread.NORM_PRIORITY - 2)
		.denyCacheImageMultipleSizesInMemory()
		.discCacheFileNameGenerator(new Md5FileNameGenerator())
		.tasksProcessingOrder(QueueProcessingType.LIFO)
		.writeDebugLogs()
		.build();
		ImageLoader.getInstance().init(config);
		imageLoader = ImageLoader.getInstance();
		
	}

	public int getCount() {
		return repleList.size();
	}

	@Override
	public String getItem(int position) {		
		return repleList.get(position).getRepleContent();
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
		
		View 		layoutLeftHeadReple 	= ViewHolder.get(convertView ,R.id.layout_left_head_reple);
		View 		layoutLeftContentReple 	= ViewHolder.get(convertView ,R.id.layout_left_content_reple);
		ImageView 	imgPeple 				= ViewHolder.get(convertView ,R.id.img_reple_image);		
		TextView 	textUserName 			= ViewHolder.get(convertView ,R.id.text_reple_user_name);
		TextView 	textWriteTime 			= ViewHolder.get(convertView ,R.id.text_reple_write_time);
		TextView 	textContent 			= ViewHolder.get(convertView ,R.id.text_reple_content);
		ImageView 	imgRepleCancel 			= ViewHolder.get(convertView ,R.id.img_reple_cancel);
		
		visibleCancelReple(position, imgRepleCancel);
		
		String url = FACEBOOK_BASE_URL+ repleList.get(position).getFacebookId()+PICTURE_TYPE;
		
		imageLoader.displayImage(url, imgPeple, options);
		
		textUserName.setText(repleList.get(position).getUserName());
		textWriteTime.setText(repleList.get(position).getWriteTime());
		textContent.setText(repleList.get(position).getRepleContent());

		visibleMyReple(position, layoutLeftHeadReple, layoutLeftContentReple);
		
		return convertView;
	}

	/**
	 * 자신의 리플은 파란색으로 표시 , 상대방 리플 빨간색
	 * @param position
	 * @param layoutLeftHeadReple
	 * @param layoutLeftContentReple
	 */
	private void visibleMyReple(int position, View layoutLeftHeadReple,
			View layoutLeftContentReple) {
		if(userName.equals(repleList.get(position).getUserName())){
			layoutLeftHeadReple.setBackgroundResource(R.color.background_blue);
			layoutLeftContentReple.setBackgroundResource(R.color.background_sky_blue);
		}else{
			layoutLeftHeadReple.setBackgroundResource(R.color.background_red);
			layoutLeftContentReple.setBackgroundResource(R.color.background_sky_red);
		}
	}

	/**
	 * 댓글 삭제 보임 여부 체크
	 * @param position
	 * @param imgRepleCancel
	 */
	private void visibleCancelReple(int position, ImageView imgRepleCancel) {
		String faceBookId = sharedpreferencesUtil.getValue(Config.FACEBOOK.FACEBOOK_ID, "");
		
		if(repleList.get(position).getFacebookId().equals(faceBookId)){
			imgRepleCancel.setVisibility(View.VISIBLE);
		}else{
			imgRepleCancel.setVisibility(View.GONE);
		}
	}
	
}
