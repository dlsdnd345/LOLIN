package com.iris.listener;

/**
 * Created by woong on 2014. 12. 28..
 * 푸시 이벤트 발생시 댓글 실시간 갱신을 위한 리스너
 */
public interface MessageListener {

    public void sendMessage(String boardId , String message , String summernerName ,
                            String facebookId, String repleId , String writeTime);
}
