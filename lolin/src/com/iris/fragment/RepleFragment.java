package com.iris.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.iris.listener.MessageListener;
import com.iris.lolin.BoardDetailActivity;
import com.iris.lolin.R;
import com.iris.lolin.RecordSearchActivity;
import com.iris.service.RepleService;
import com.iris.service.SettingService;
import com.iris.service.UserService;
import com.iris.util.SharedpreferencesUtil;

import java.util.ArrayList;

/**
 * 상세내용 댓글 프래그먼트
 */
public class RepleFragment extends Fragment {

    private ArrayList<Reple> repleList;

    private int mBoardId;
    private String userName;
    private User user;

    private BoardDetailActivity boardDetailActivity;

    private UserService userService;
    private RepleService repleService;
    private RepleAdapter repleAdapter;
    private SettingService settingService;
    private SharedpreferencesUtil sharedpreferencesUtil;

    private LinearLayout bottomRepleBar;
    private ProgressBar prograssBar;
    private EditText editReple;
    private ListView repleListView;
    private TextView textNoRepleMessage;
    private TextView textAddReple;

    public Fragment newInstance(int boardId, ArrayList<Reple> repleList, String userName) {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_reple, container, false);

        init(rootView);


        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        boardDetailActivity = (BoardDetailActivity)getActivity();

        dataInit();
        visibleReple(repleList);
        visibleLogin();


    }

    /**
     * 레이아웃 초기화
     *
     * @param rootView
     */
    private void init(ViewGroup rootView) {

        bottomRepleBar = (LinearLayout) rootView.findViewById(R.id.bottom_reple_bar);
        prograssBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        editReple = (EditText) rootView.findViewById(R.id.edit_reple);
        textAddReple = (TextView) rootView.findViewById(R.id.text_add_reple);
        textNoRepleMessage = (TextView) rootView.findViewById(R.id.text_no_reple_message);
        repleListView = (ListView) rootView.findViewById(R.id.list_reple);

        textAddReple.setOnClickListener(mClickListener);

    }

    /**
     * 데이터 초기화
     */
    @SuppressWarnings("unchecked")
    private void dataInit() {

        userService = new UserService(getActivity().getApplicationContext());
        repleService = new RepleService();
        settingService = new SettingService();

        sharedpreferencesUtil = new SharedpreferencesUtil(getActivity());
        repleList = (ArrayList<Reple>) getArguments().get(Config.FLAG.REPLE);
        userName = getArguments().getString(Config.FLAG.USER_NAME);
        mBoardId = getArguments().getInt(Config.FLAG.BOARD_ID);

        getUser();

        repleAdapter = new RepleAdapter(getActivity(), repleList, userName);
        repleListView.setOnItemClickListener(mOnItemClickListener);
        repleListView.setAdapter(repleAdapter);

        boardDetailActivity.setMessageListener(mMessageListener);

    }

    /**
     * 리플이 없을 시 메세지 문구
     *
     * @param repleList
     */
    private void visibleReple(ArrayList<Reple> repleList) {
        if (repleList.size() == 0) {
            textNoRepleMessage.setVisibility(View.VISIBLE);
            repleListView.setVisibility(View.GONE);
        } else {
            textNoRepleMessage.setVisibility(View.GONE);
            repleListView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 로그인시 하단바 보임
     */
    private void visibleLogin() {
        boolean isLogin = sharedpreferencesUtil.getValue(Config.FLAG.IS_LOGIN, false);
        if (isLogin) {
            bottomRepleBar.setVisibility(View.VISIBLE);
        } else {
            bottomRepleBar.setVisibility(View.GONE);
        }
    }

    /**
     * 댓글 추가 Api
     */
    public void saveReple() {

        prograssBar.setVisibility(View.VISIBLE);

        // 댓글 내용 없을시 쉐이크 애니메이션 및 토스트 진행
        if (editReple.getText().toString().equals("")) {
            Toast.makeText(getActivity(), getString(R.string.reple_fragment_text_reple_hint), Toast.LENGTH_LONG).show();
            Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.horizontal_shake);
            editReple.startAnimation(shake);
            return;
        }

        final String faceBookId = sharedpreferencesUtil.getValue(Config.FACEBOOK.FACEBOOK_ID, "");
        RequestQueue request = Volley.newRequestQueue(getActivity());



        request.add(new StringRequest(Request.Method.GET, Config.API.DEFAULT_URL + Config.API.REPLE_SAVE +
                repleService.getSubUrl(mBoardId, user.getSummonerName(), editReple.getText().toString(), faceBookId), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                String resultOk = repleService.saveReplePasing(response);
                if (resultOk.equals(Config.FLAG.TRUE)) {

                    findReple();
                    //본인이 아닌 사람이 댓글을 추가 시
                    if(!userName.equals(user.getSummonerName())){
                        sendPush(editReple.getText().toString() , faceBookId);
                    }
                    // 본이이 댓글 추가 시
                    else{

                        notOverrapSendPush(editReple.getText().toString() , faceBookId);
                    }
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
    public void findReple() {

        prograssBar.setVisibility(View.VISIBLE);

        RequestQueue request = null;
        if (Volley.newRequestQueue(getActivity()) != null) {
            request = Volley.newRequestQueue(getActivity());
        }
        request.add(new StringRequest
                (Request.Method.GET, Config.API.DEFAULT_URL + Config.API.REPLE_FIND_ONE + repleService.getFindRepleSubUrl(mBoardId), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String resultOk = repleService.saveReplePasing(response);
                        if (resultOk.equals(Config.FLAG.TRUE)) {

                            editReple.setText("");
                            repleList = repleService.getRepleFindOne(response);
                            visibleReple(repleList);
                            repleAdapter.setRepleList(repleList);
                            repleAdapter.notifyDataSetChanged();
                            repleListView.setSelection(repleList.size());
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
    public void deleteReple(int position) {

        prograssBar.setVisibility(View.VISIBLE);

        RequestQueue request = Volley.newRequestQueue(getActivity());
        request.add(new StringRequest
                (Request.Method.GET, Config.API.DEFAULT_URL + Config.API.REPLE_DELETE +
                        repleService.getDeleteRepleSubUrl(repleList.get(position).getId()), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String resultOk = repleService.deleteReplePasing(response);
                        if (resultOk.equals(Config.FLAG.TRUE)) {
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
     * 유저 정보 조회
     */
    public void getUser(){

        prograssBar.setVisibility(View.VISIBLE);

        String sub_url = userService.getUserSubUrl();

        RequestQueue request = Volley.newRequestQueue(getActivity());
        request.add(new StringRequest(Request.Method.GET, Config.API.DEFAULT_URL + Config.API.USER_FIND_ONE +sub_url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                user = userService.getUser(response);
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
    public void sendPush(String reple, String faceBookId) {

        prograssBar.setVisibility(View.VISIBLE);

        RequestQueue request = Volley.newRequestQueue(getActivity());

        request.add(new StringRequest
                (Request.Method.GET, Config.API.DEFAULT_URL + Config.API.GCM_SEND_REPLE + repleService.getSendPushSubUrl
                        ( String.valueOf(mBoardId), user.getSummonerName(), reple, faceBookId), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String resultOk = repleService.deleteReplePasing(response);
                        if (resultOk.equals(Config.FLAG.TRUE)) {
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
     * 나을뺀 나머지 사용자에게 푸시 전송 Api
     */
    public void notOverrapSendPush(String reple, String faceBookId) {

        prograssBar.setVisibility(View.VISIBLE);

        RequestQueue request = Volley.newRequestQueue(getActivity());

        request.add(new StringRequest
                (Request.Method.GET, Config.API.DEFAULT_URL + Config.API.GCM_SEND_ME_REPLE + repleService.getSendPushSubUrl
                        (String.valueOf(mBoardId), user.getSummonerName(), reple, faceBookId), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String resultOk = repleService.deleteReplePasing(response);
                        if (resultOk.equals(Config.FLAG.TRUE)) {
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
    private void deleteDialog(final int position) {
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
    OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            //TODO : 상대방 전적 검색 기능으로대체
            if (userName.equals(repleList.get(position).getUserName()) && userName.equals(user.getSummonerName())) {

                deleteDialog(position);
            } else {

                Intent inetnt = new Intent(getActivity(), RecordSearchActivity.class);
                inetnt.putExtra("summernerName",repleList.get(position).getUserName());
                startActivity(inetnt);
            }
        }
    };

    /**
     * GCM 메세지가 올경우
     */
    MessageListener mMessageListener = new MessageListener(){

        @Override
        public void sendMessage(String boardId, String message, String summernerName,
                                String facebookId, String repleId , String writeTime) {


            Reple reple = new Reple();

            if(Integer.parseInt(boardId) == mBoardId) {

                reple.setBoardId(Integer.parseInt(boardId));
                reple.setFacebookId(facebookId);
                reple.setUserName(summernerName);
                reple.setRepleContent(message);
                reple.setId(Integer.parseInt(repleId));
                reple.setWriteTime(writeTime);

                repleAdapter.getRepleList().add(reple);
                repleAdapter.setRepleList(repleAdapter.getRepleList());

                repleAdapter.notifyDataSetChanged();
                repleListView.setSelection(repleList.size());
            }

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