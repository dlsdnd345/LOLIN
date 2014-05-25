package com.iris.adapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.iris.entities.Board;
import com.iris.entities.Reple;
import com.iris.lolin.R;
import com.iris.util.ViewHolder;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class RepleAdapter extends BaseAdapter{

	private static final String 		FACEBOOK_BASE_URL  				= "http://graph.facebook.com/";
	private static final String 		PICTURE_TYPE					= "/picture?type=small";
	
	private String								userName;
	private int 								layout; 
	private Context 							context; 
	private LayoutInflater 						inflater;
	private ArrayList<Reple> 					repleList; 
	private SparseArray<WeakReference<View>> 	viewArray;
	private DisplayImageOptions 				options;
	private ImageLoader 						imageLoader;
	
	public RepleAdapter(Context context , int layout , ArrayList<Reple> repleList, String userName){

		this.userName = userName;
		this.context=context;
		this.layout = layout;
		this.repleList =repleList;
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

	@SuppressLint("ResourceAsColor")
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
		
		View layoutLeftHeadReple = ViewHolder.get(convertView ,R.id.layout_left_head_reple);
		View layoutLeftContentReple = ViewHolder.get(convertView ,R.id.layout_left_content_reple);
		ImageView imgPeple = ViewHolder.get(convertView ,R.id.img_reple_image);		
		TextView textUserName = ViewHolder.get(convertView ,R.id.text_reple_user_name);
		TextView textWriteTime = ViewHolder.get(convertView ,R.id.text_reple_write_time);
		TextView textContent = ViewHolder.get(convertView ,R.id.text_reple_content);
		
		String url = FACEBOOK_BASE_URL+ repleList.get(position).getFacebookId()+PICTURE_TYPE;
		imageLoader.displayImage(url, imgPeple, options);
		
		
		textUserName.setText(repleList.get(position).getUserName());
		textWriteTime.setText(repleList.get(position).getWriteTime());
		textContent.setText(repleList.get(position).getRepleContent());

		if(userName.equals(repleList.get(position).getUserName())){
			layoutLeftHeadReple.setBackgroundResource(R.color.background_blue);
			layoutLeftContentReple.setBackgroundResource(R.color.background_sky_blue);
		}else{
			layoutLeftHeadReple.setBackgroundResource(R.color.background_red);
			layoutLeftContentReple.setBackgroundResource(R.color.background_sky_red);
		}
		
		return convertView;
	}

}
