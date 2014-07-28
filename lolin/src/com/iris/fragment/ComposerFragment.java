package com.iris.fragment;

import java.util.ArrayList;

import org.w3c.dom.Text;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
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
import com.iris.config.Config;
import com.iris.entities.Board;
import com.iris.entities.User;
import com.iris.lolin.BoardDetailActivity;
import com.iris.lolin.R;
import com.iris.service.ComposerFragmentService;
import com.iris.service.SettingService;
import com.iris.util.SharedpreferencesUtil;

/**
 * A placeholder fragment containing a simple view.
 */
@SuppressLint("NewApi")
public class ComposerFragment extends Fragment {

	private static final String 		FACEBOOK_ID  					= "FACEBOOK_ID";
	private final static String 		BOARD_FIND_MY_ALL 				= "http://192.168.219.6:8080/board/findMyAll";
	private final static String 		ERROR 							= "Error";
	
	private SharedpreferencesUtil  		sharedpreferencesUtil;
	private ComposerFragmentService 	composerService;
	private ArrayList<Board> 			boardList;
	private ComposerAdapter 			composerAdapter;
	
	private TextView					txtEmptyMessage;
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
		
		View rootView = inflater.inflate(R.layout.fragment_composer, container,false);
		
		init(rootView);
		dataInit();
		
		return rootView;
	}

	/**
	 * 데이터 초기화
	 */
	private void dataInit() {
		
		composerService = new ComposerFragmentService();
		sharedpreferencesUtil = new SharedpreferencesUtil(getActivity());
		
		getBoardFindMyAll();
	}

	/**
	 * 내가 쓴 게시판 조회
	 */
	private void getBoardFindMyAll() {
		
		String sub_url = "?faceBookId="+ sharedpreferencesUtil.getValue(FACEBOOK_ID, "");
	
		RequestQueue request = Volley.newRequestQueue(getActivity());  
		request.add(new StringRequest(Request.Method.GET, BOARD_FIND_MY_ALL+sub_url,new Response.Listener<String>() {  
			@Override  
			public void onResponse(String response) {  
				boardList = composerService.getBoardFindAll(response);
				listViewInit();
				visibleComposorEmptyMessage();
				
			}

		}, new Response.ErrorListener() {  
			@Override  
			public void onErrorResponse(VolleyError error) {  
				VolleyLog.d(ERROR, error.getMessage());  
			}  
		}));
	}
	
	/**
	 * 리스트가 없을시 Empty 메세지
	 */
	private void visibleComposorEmptyMessage() {
		if(boardList.size() == 0){
			txtEmptyMessage.setVisibility(View.VISIBLE);
		}else{
			txtEmptyMessage.setVisibility(View.INVISIBLE);
		}
	}  
	
	/**
	 * 내가쓴글 리스트 초기화
	 */
	private void listViewInit() {
		
		composerAdapter = new ComposerAdapter(getActivity(), R.layout.row_write_text_list, boardList);
		writeTextListView.setAdapter(composerAdapter);
		writeTextListView.setOnItemClickListener(mOnItemClickListener);
	}

	/**
	 * 레이아웃 초기화
	 * @param rootView
	 */
	private void init(View rootView) {
		txtEmptyMessage   = (TextView)rootView.findViewById(R.id.txt_empty_message);
		writeTextListView = (ListView)rootView.findViewById(R.id.list_write_text);
	}
	
	/**
	 * 리스트 row 선택시
	 */
	OnItemClickListener mOnItemClickListener = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			
			Intent intent = new Intent(getActivity(), BoardDetailActivity.class);
			intent.putExtra("id", boardList.get(position).getId());
			intent.putExtra("editState", true);
			startActivity(intent);
		}
		
	};
}
