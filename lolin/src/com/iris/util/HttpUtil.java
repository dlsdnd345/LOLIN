package com.iris.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

public class HttpUtil {

	URL url;
	Context mContext;
	String cookie ;
	public String cookies = "";
	CookieManager cookieManager;
	static PrintWriter printWriter;
	HttpURLConnection http;

	public HttpUtil(Context context){
		mContext = context;
		CookieSyncManager.createInstance(mContext);
		cookieManager = CookieManager.getInstance();
		CookieSyncManager.getInstance().sync();
	}
	/**
	 * sendHttpCon 메서드
	 * 웹 접속을 도와주는 작업
	 * @param address : 서버 URL
	 * @param list : 서버로 전송할 데이터
	 * @param RequestMethod : GET/POST/PUT/DELETE TYPE 지정
	 * @param shopId : 서버로 전송할 가게번호
	 * @return jsonData
	 */
	@SuppressWarnings("rawtypes")
	public String sendHttpCon(String address,HashMap<String, String> list, String requestMethod,String shopId) {

		StringBuilder sb = new StringBuilder(); //서버에 송신할 데이터 변수
		StringBuilder sb2 = new StringBuilder(); // 서버에서 수신할 데이터 변수
		
		try {
			SharedPreferences sharedPreferences = mContext.getSharedPreferences("url", Context.MODE_PRIVATE);
			if(sharedPreferences.getString("apiUrl", "") != null){
				url = new URL(sharedPreferences.getString("apiUrl", "")+address+shopId);
			}
			http = (HttpURLConnection)url.openConnection();
			http.setConnectTimeout(5000);
			http.setReadTimeout(5000);
			http.setUseCaches(false);

			if(requestMethod.equals("POST") ||requestMethod.equals("PUT")){
				http.setDoOutput(true);
			}else{
				http.setDoInput(true);
			}
			http.setRequestMethod(requestMethod); // GET/POST TYPE 지정, REST 웹서비스 : 1.GET 2.POST 3.PUT(insert) 4.DELETE
			http.setInstanceFollowRedirects(false);//세션 사용하기 위해 false

			cookie = cookieManager.getCookie(http.getURL().toString());
			if(cookie!=null){
				//서버로 쿠키 전송
				http.setRequestProperty("Cookie",cookie);
			}
			//넘어온 데이터가 있을시 서버 데이터 전송
			if(list != null)
			{
				Iterator paramsIterator = list.keySet().iterator();
				while(paramsIterator.hasNext()){
					String key = (String)paramsIterator.next();
					String value = (String)list.get(key);
					sb.append("&");
					sb.append(key);
					sb.append("=");
					sb.append(value);
				}
				printWriter=new PrintWriter(new OutputStreamWriter(
						http.getOutputStream(),"UTF-8"));
				printWriter.write(sb.toString());
				printWriter.flush();
			}
			//서버 데이터 수신
			if(http != null) {
				if(http.getResponseCode() == HttpURLConnection.HTTP_OK) {//<--SocketException
					BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream(),"UTF-8"));
					String inputLine;
					while((inputLine=br.readLine()) != null){
						sb2.append(inputLine);
					}
					br.close();
				}else{
					return null;
				}
			}
			// 서버에서 보내온 쿠키 저장
			Map m = http.getHeaderFields(); 
			if(m.containsKey("Set-Cookie")){
				CookieSyncManager.getInstance().stopSync();
				Collection c =(Collection)m.get("Set-Cookie");
				for(Iterator i = c.iterator(); i.hasNext(); ) {
					cookies += (String)i.next() + ", ";
					cookieManager.setCookie(http.getURL().toString(), cookies);
				}
				CookieSyncManager.getInstance().sync();
			}			
			http.disconnect();
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
			return null;
		}
		return sb2.toString().trim();
	}
}
