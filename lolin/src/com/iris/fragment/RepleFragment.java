package com.iris.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.iris.adapter.RepleAdapter;
import com.iris.config.Config;
import com.iris.entities.Reple;
import com.iris.entities.User;
import com.iris.lolin.R;
import com.iris.service.RepleService;
import com.iris.service.SettingService;
import com.iris.util.SharedpreferencesUtil;

public class RepleFragment extends Fragment {

	private static final String REPLE_WARNING_MWSSAGE					= "댓글을 입력 하세요.";
	
	private static final String USER_NAME 							= "userName";
	private static final String BOARD_ID 								= "boardId";	
	private static final String REPLE 								= "reple";	
	private final static String ERROR 								= "Error";
	
	private ArrayList<Reple> 			repleList;
	
	private int						boardId;
	private String 						userName;
	private RepleService				repleService;
	private RepleAdapter 				repleAdapter;
	private SettingService				settingService;
	private SharedpreferencesUtil 		sharedpreferencesUtil;

	private ProgressBar					prograssBar;
	private EditText					editReple;
	private ListView 					repleListView;
	private TextView					textNoRepleMessage;
	private TextView					textAddReple;
	
	public Fragment newInstance(int boardId ,ArrayList<Reple> repleList,String userName) {
		
		RepleFragment fragment = new RepleFragment();
		
		Bundle args = new Bundle();
		args.putInt(BOARD_ID, boardId);
		args.putSerializable(REPLE, repleList);
		args.putString(USER_NAME, userName);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_reple, container, false);
		
		repleList = (ArrayList<Reple>) getArguments().get(REPLE);
		userName = getArguments().getString(USER_NAME);
		boardId  = getArguments().getInt(BOARD_ID);
		
		init(rootView);
		dataInit();
		visibleReple(repleList);
		
		repleAdapter = new RepleAdapter(getActivity(), R.layout.row_left_reple, repleList,userName);
		repleListView.setOnItemClickListener(mOnItemClickListener);
		repleListView.setAdapter(repleAdapter);
		
		return rootView;
	}

	/**
	 * 데이터 초기화
	 */
	private void dataInit() {
		repleService = new RepleService();
		settingService = new SettingService();
		sharedpreferencesUtil = new SharedpreferencesUtil(getActivity());
	}

	/**
	 * 리플이 없을 시 메세지 문구
	 * @param repleList
	 */
	private void visibleReple(ArrayList<Reple> repleList) {
		if(repleList.size() == 0){
			textNoRepleMessage.setVisibility(View.VISIBLE);
			repleListView.setVisibility(View.GONE);
		}else{
			textNoRepleMessage.setVisibility(View.GONE);
			repleListView.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 레이아웃 초기화
	 * @param rootView
	 */
	private void init(ViewGroup rootView) {
		
		prograssBar 		= (ProgressBar)rootView.findViewById(R.id.progressBar);
		editReple			= (EditText)rootView.findViewById(R.id.edit_reple);
		textAddReple 		= (TextView)rootView.findViewById(R.id.text_add_reple);
		textNoRepleMessage 	= (TextView)rootView.findViewById(R.id.text_no_reple_message);
		repleListView 		= (ListView)rootView.findViewById(R.id.list_reple);
		textAddReple.setOnClickListener(mClickListener);
	}
	
	/**
	 * 댓글 추가 Api
	 */
	public void saveReple(){
		
		prograssBar.setVisibility(View.VISIBLE);
		
		// 댓글 내용 없을시 쉐이크 애니메이션 및 토스트 진행
		if(editReple.getText().toString().equals("")){
			Toast.makeText(getActivity(), REPLE_WARNING_MWSSAGE, Toast.LENGTH_LONG).show();
		    Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.horizontal_shake);
		    editReple.startAnimation(shake);
			return;
		}
		
		String faceBookId = sharedpreferencesUtil.getValue(Config.FACEBOOK.FACEBOOK_ID, "");
		RequestQueue request = Volley.newRequestQueue(getActivity());  
		request.add(new StringRequest
				(Request.Method.GET, Config.API.REPLE_SAVE+
						repleService.getSubUrl(boardId, userName, editReple.getText().toString(),faceBookId)
				,new Response.Listener<String>() {  
			@Override  
			public void onResponse(String response) {  

				String resultOk = repleService.saveReplePasing(response);
				if(resultOk.equals(Config.FLAG.TRUE)){
					findReple();
				}
				prograssBar.setVisibility(View.INVISIBLE);
			}  
		}, new Response.ErrorListener() {  
			@Override  
			public void onErrorResponse(VolleyError error) {  
				VolleyLog.d(ERROR, error.getMessage());  
			}  
		}));
	}
	
	/**
	 * 댓글 갱신 Api
	 */
	public void findReple(){
		
		prograssBar.setVisibility(View.VISIBLE);
		
		RequestQueue request = Volley.newRequestQueue(getActivity());  
		request.add(new StringRequest
				(Request.Method.GET, Config.API.REPLE_FIND_ONE+Config.API.SUB_URL_BOARD_ID+boardId,new Response.Listener<String>() {  
			@Override  
			public void onResponse(String response) {  

				String resultOk = repleService.saveReplePasing(response);
				if(resultOk.equals(Config.FLAG.TRUE)){
					
					editReple.setText("");
					repleList = repleService.getRepleFindOne(response);
					visibleReple(repleList);
					repleAdapter = new RepleAdapter(getActivity(), R.layout.row_left_reple, repleList,userName);
					repleListView.setAdapter(repleAdapter);
				}
				prograssBar.setVisibility(View.INVISIBLE);
			}  
		}, new Response.ErrorListener() {  
			@Override  
			public void onErrorResponse(VolleyError error) {  
				VolleyLog.d(ERROR, error.getMessage());  
			}  
		}));

	}
	
	/**
	 * 댓글 삭제 Api
	 */
	public void deleteReple(int position){
		
		prograssBar.setVisibility(View.VISIBLE);
		
		RequestQueue request = Volley.newRequestQueue(getActivity());  
		request.add(new StringRequest
				(Request.Method.GET, Config.API.REPLE_DELETE+Config.API.SUB_URL_REPLE_ID+repleList.get(position).getId()
				,new Response.Listener<String>() {  
			@Override  
			public void onResponse(String response) {  

				String resultOk = repleService.deleteReplePasing(response);
				if(resultOk.equals(Config.FLAG.TRUE)){
					findReple();
				}
				prograssBar.setVisibility(View.INVISIBLE);				
			}  
		}, new Response.ErrorListener() {  
			@Override  
			public void onErrorResponse(VolleyError error) {  
				VolleyLog.d(Config.FLAG.ERROR, error.getMessage());  
			}  
		}));

	}
	
	OnItemClickListener mOnItemClickListener = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			deleteReple(position);
		}
	};
	
	Button.OnClickListener mClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.text_add_reple:	
				saveReple();
				break;
			}
		}
	};
	
}