package com.iris.listener;

/**
 * Created by woong on 2014. 12. 28..
 */
public interface MessageListener {

    public void sendMessage(String boardId , String message , String summernerName ,
                            String facebookId, String repleId , String writeTime);
}
