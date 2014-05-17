package com.iris.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.iris.adapter.ComposerAdapter;
import com.iris.entities.Board;
import com.iris.lolin.BoardDetailActivity;
import com.iris.lolin.R;
import com.iris.service.ComposerService;
import com.iris.util.SharedpreferencesUtil;

/**
 * A placeholder fragment containing a simple view.
 */
@SuppressLint("NewApi")
public class ComposerFragment extends Fragment {

	//?faceBookId=566784443436995
	private static final String 		FACEBOOK_ID  					= "FACEBOOK_ID";
	private final static String 		BOARD_FIND_MY_ALL 				= "http://192.168.219.6:8080/board/findMyAll";
	private final static String 		ERROR 							= "Error";
	
	
	private SharedpreferencesUtil  		sharedpreferencesUtil;
	private ComposerService 			composerService;
	private ArrayList<Board> 			boardList;
	private ComposerAdapter 			composerAdapter;
	private ListView 					writeTextListView;
	
	public Fragment newInstance() {
		ComposerFragment fragment = new ComposerFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_write_text, container,false);
		
		init(rootView);
		dataInit();
		
		
		return rootView;
	}

	private void dataInit() {
		composerService = new ComposerService();
		sharedpreferencesUtil = new SharedpreferencesUtil(getActivity());
		getBoardFindAll();
	}

	private void getBoardFindAll() {
		
		String sub_url = "?faceBookId="+ sharedpreferencesUtil.getValue(FACEBOOK_ID, "");
	
		RequestQueue request = Volley.newRequestQueue(getActivity());  
		request.add(new StringRequest(Request.Method.GET, BOARD_FIND_MY_ALL+sub_url,new Response.Listener<String>() {  
			@Override  
			public void onResponse(String response) {  
				boardList = composerService.getBoardFindAll(response);
				listViewInit();
			}  
		}, new Response.ErrorListener() {  
			@Override  
			public void onErrorResponse(VolleyError error) {  
				VolleyLog.d(ERROR, error.getMessage());  
			}  
		}));
	}
	
	private void listViewInit() {
		
		composerAdapter = new ComposerAdapter(getActivity(), R.layout.row_write_text_list, boardList);
		writeTextListView.setAdapter(composerAdapter);
		writeTextListView.setOnItemClickListener(mOnItemClickListener);
	}

	private void init(View rootView) {
		writeTextListView = (ListView)rootView.findViewById(R.id.list_write_text);
	}
	
	OnItemClickListener mOnItemClickListener = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			Intent intent = new Intent(getActivity(), BoardDetailActivity.class);
			startActivity(intent);
		}
		
	};
}
