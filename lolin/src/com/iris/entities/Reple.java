package com.iris.entities;

import java.io.Serializable;

/**
 * 댓글 데이터
 */
public class Reple implements Serializable{

	private static final long serialVersionUID = 7928329363203940132L;
	
	
	private int 	id;
	private int 	boardId;
	private String repleContent;
	private String writeTime;
	private String userName;
	private String facebookId;
    private String os;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getBoardId() {
		return boardId;
	}
	public void setBoardId(int boardId) {
		this.boardId = boardId;
	}
	public String getRepleContent() {
		return repleContent;
	}
	public void setRepleContent(String repleContent) {
		this.repleContent = repleContent;
	}
	public String getWriteTime() {
		return writeTime;
	}
	public void setWriteTime(String writeTime) {
		this.writeTime = writeTime;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFacebookId() {
		return facebookId;
	}
	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}
    public String getOs() {
        return os;
    }
    public void setOs(String os) {
        this.os = os;
    }
}
