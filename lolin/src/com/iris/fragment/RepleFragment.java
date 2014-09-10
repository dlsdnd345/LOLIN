package com.iris.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
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

	private ArrayList<Reple> 			repleList;
	
	private int						boardId;
	private String 						userName;
	private RepleService				repleService;
	private RepleAdapter 				repleAdapter;
	private SettingService				settingService;
	private SharedpreferencesUtil 		sharedpreferencesUtil;

	private LinearLayout				bottomRepleBar;
	private ProgressBar					prograssBar;
	private EditText					editReple;
	private ListView 					repleListView;
	private TextView					textNoRepleMessage;
	private TextView					textAddReple;
	
	public Fragment newInstance(int boardId ,ArrayList<Reple> repleList,String userName) {
		
		RepleFragment fragment = new RepleFragment();
		
		Bundle args = new Bundle();
		args.putInt(Config.FLAG.BOARD_ID, boardId);
		args.putSerializable(Config.FLAG.REPLE, repleList);
		args.putString(Config.FLAG.USER_NAME, userName);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_reple, container, false);
		
		init(rootView);
		dataInit();
		visibleReple(repleList);
		visibleLogin();
		
		repleAdapter = new RepleAdapter(getActivity(), R.layout.row_left_reple, repleList,userName);
		repleListView.setOnItemClickListener(mOnItemClickListener);
		repleListView.setAdapter(repleAdapter);
		
		return rootView;
	}

	/**
	 * 레이아웃 초기화
	 * @param rootView
	 */
	private void init(ViewGroup rootView) {
		
		bottomRepleBar		= (LinearLayout)rootView.findViewById(R.id.bottom_reple_bar);
		prograssBar 		= (ProgressBar)rootView.findViewById(R.id.progressBar);
		editReple			= (EditText)rootView.findViewById(R.id.edit_reple);
		textAddReple 		= (TextView)rootView.findViewById(R.id.text_add_reple);
		textNoRepleMessage 	= (TextView)rootView.findViewById(R.id.text_no_reple_message);
		repleListView 		= (ListView)rootView.findViewById(R.id.list_reple);
		textAddReple.setOnClickListener(mClickListener);
	}
	
	/**
	 * 데이터 초기화
	 */
	@SuppressWarnings("unchecked")
	private void dataInit() {
		repleService = new RepleService();
		settingService = new SettingService();
		sharedpreferencesUtil = new SharedpreferencesUtil(getActivity());
		repleList = (ArrayList<Reple>) getArguments().get(Config.FLAG.REPLE);
		userName = getArguments().getString(Config.FLAG.USER_NAME);
		boardId  = getArguments().getInt(Config.FLAG.BOARD_ID);
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
	 * 로그인시 하단바 보임
	 */
	private void visibleLogin() {
		boolean isLogin = sharedpreferencesUtil.getValue(Config.FLAG.IS_LOGIN, false);
		if(isLogin){
			bottomRepleBar.setVisibility(View.VISIBLE);
		}else{
			bottomRepleBar.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 댓글 추가 Api
	 */
	public void saveReple(){
		
		prograssBar.setVisibility(View.VISIBLE);
		
		// 댓글 내용 없을시 쉐이크 애니메이션 및 토스트 진행
		if(editReple.getText().toString().equals("")){
			Toast.makeText(getActivity(), getString(R.string.reple_fragment_text_reple_hint), Toast.LENGTH_LONG).show();
		    Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.horizontal_shake);
		    editReple.startAnimation(shake);
			return;
		}
		
		String faceBookId = sharedpreferencesUtil.getValue(Config.FACEBOOK.FACEBOOK_ID, "");
		RequestQueue request = Volley.newRequestQueue(getActivity());  
		request.add(new StringRequest(Request.Method.GET, Config.API.DEFAULT_URL + Config.API.REPLE_SAVE+
		repleService.getSubUrl(boardId, userName, editReple.getText().toString(),faceBookId),new Response.Listener<String>() {  
			@Override  
			public void onResponse(String response) { 

				String resultOk = repleService.saveReplePasing(response);
				if(resultOk.equals(Config.FLAG.TRUE)){
					findReple();
					sendPush(editReple.getText().toString());
				}
				prograssBar.setVisibility(View.INVISIBLE);
			}  
		}, new Response.ErrorListener() {  
			@Override  
			public void onErrorResponse(VolleyError error) {  
				VolleyLog.d(Config.FLAG.ERROR, error.getMessage());  
				prograssBar.setVisibility(View.INVISIBLE);
				Toast.makeText(getActivity().getApplicationContext(), Config.FLAG.NETWORK_CLEAR, Toast.LENGTH_LONG).show();
			}  
		}));
	}
	
	/**
	 * 댓글 갱신 Api
	 */
	public void findReple(){
		
		prograssBar.setVisibility(View.VISIBLE);
		
		RequestQueue request = null;
		if(Volley.newRequestQueue(getActivity()) != null){
			request = Volley.newRequestQueue(getActivity());  
		}
		request.add(new StringRequest
				(Request.Method.GET, Config.API.DEFAULT_URL + Config.API.REPLE_FIND_ONE + repleService.getFindRepleSubUrl(boardId) ,new Response.Listener<String>() {  
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
				VolleyLog.d(Config.FLAG.ERROR, error.getMessage());  
				prograssBar.setVisibility(View.INVISIBLE);
				Toast.makeText(getActivity().getApplicationContext(), Config.FLAG.NETWORK_CLEAR, Toast.LENGTH_LONG).show();
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
				(Request.Method.GET, Config.API.DEFAULT_URL + Config.API.REPLE_DELETE+ 
						repleService.getDeleteRepleSubUrl(repleList.get(position).getId()),new Response.Listener<String>() {  
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
				prograssBar.setVisibility(View.INVISIBLE);
				Toast.makeText(getActivity().getApplicationContext(), Config.FLAG.NETWORK_CLEAR, Toast.LENGTH_LONG).show();
			}  
		}));

	}
	
	/**
	 * 푸시 전송 Api
	 */
	public void sendPush(String reple){
		
		prograssBar.setVisibility(View.VISIBLE);
		
		RequestQueue request = Volley.newRequestQueue(getActivity());  
		String faceBookId = sharedpreferencesUtil.getValue(Config.FACEBOOK.FACEBOOK_ID, "");
		
		request.add(new StringRequest
				(Request.Method.GET, Config.API.DEFAULT_URL + Config.API.GCM_SEND_REPLE+repleService.getSendPushSubUrl
						("android", String.valueOf(boardId) ,userName,reple,faceBookId),new Response.Listener<String>() {  
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
				prograssBar.setVisibility(View.INVISIBLE);
				Toast.makeText(getActivity().getApplicationContext(), Config.FLAG.NETWORK_CLEAR, Toast.LENGTH_LONG).show();
			}  
		}));
	}
	
	/**
	 * 삭제시 다이얼로그
	 */
	private void deleteDialog(final int position){
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(getActivity());
		alt_bld.setTitle(getString(R.string.dialog_title));
		alt_bld.setMessage(getString(R.string.reple_delete_dialog_content))
		.setCancelable(false).setPositiveButton(getString(R.string.dialog_clear),
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				deleteReple(position);
			}
		}).setNegativeButton(getString(R.string.dialog_cancel),
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		alt_bld.show();
	}
	
	/**
	 * 리플 아이템 선택시
	 */
	OnItemClickListener mOnItemClickListener = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			deleteDialog(position);
		}
	};
	
	/**
	 * 버튼 리스너
	 */
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