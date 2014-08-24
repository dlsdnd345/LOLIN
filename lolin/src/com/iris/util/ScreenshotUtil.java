package com.iris.util;

import java.io.ByteArrayOutputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class ScreenshotUtil {

	public Activity activity;
	
	public ScreenshotUtil(Activity activity){
		
		this.activity = activity;
	}
	
	/**
	 * 화면 스크린샷 기능
	 * @param activity
	 * @return
	 */
	@SuppressLint("NewApi")
	public byte[] takeScreenShot()
	{
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap b1 = view.getDrawingCache();
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;

		Display display = ((WindowManager)activity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		Point point = new Point();
		 
		display.getSize(point);
		int height = point.y;
		int width = point.x;
		
		Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height  - statusBarHeight);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		b.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		view.destroyDrawingCache();
		
		return baos.toByteArray();
	}
	
}
