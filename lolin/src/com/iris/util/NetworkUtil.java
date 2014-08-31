package com.iris.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {

	private static final String TITLE = "경고";
	private static final String MESSAGE = "네트워크를 활성화 시켜 주세요.";
	private static final String CLEAR = "확인";
	
	private boolean isOk ;
	private boolean isWifiConn,isMobileConn;
	
	private ConnectivityManager connectivityManager;
	
	private Activity activity;
	
	
	public NetworkUtil(Activity activity){
		this.activity = activity;
	}

	/**
	 * networkCheck 메서드<br>
	 * 어플 실행시 와이파이나 ,3G , 4G 활성화 상태를 체그<br>
	 * 와이파이나 ,3G , 4G 활성화 되있으면 진행<br>
	 * 와이파이나 ,3G , 4G 활성화 되있지 않으면 어플리케이션 중지
	 */
	public Boolean introNetworkCheck(){

		connectivityManager = (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);// WIFI 정보
		isWifiConn = networkInfo.isConnected();// WIFI 연결 여부

		networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); // 3G ,4G 정보
		isMobileConn = networkInfo.isConnected(); //3G ,4G 연결 여부

		if(isWifiConn==false && isMobileConn==false)
		{
			isOk = false;
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
			alertDialog.setTitle(TITLE);
			alertDialog.setMessage(MESSAGE).setCancelable(false).
			setPositiveButton(CLEAR,new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {						
					((Activity) activity).finish();
				}
			});
			alertDialog.show();
		}else{
			isOk = true;
		}
		return isOk;
	}
	
	public Boolean httpNetworkCheck(){
		
		connectivityManager = (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);// WIFI 정보
		isWifiConn = networkInfo.isConnected();// WIFI 연결 여부

		networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); // 3G ,4G 정보
		isMobileConn = networkInfo.isConnected(); //3G ,4G 연결 여부
		
		if(isWifiConn==false && isMobileConn==false)
		{
			isOk = false;
		}else{
			isOk = true;
		}
		return isOk;
		
	}

}
