package com.iris.vo;

import java.util.List;

import com.iris.entities.Board;

public class BoardResponseVO {

	private int pageTotalCount;
	private List<Board> boardList;
	
	
	public int getPageTotalCount() {
		return pageTotalCount;
	}
	public void setPageTotalCount(int pageTotalCount) {
		this.pageTotalCount = pageTotalCount;
	}
	public List<Board> getBoardList() {
		return boardList;
	}
	public void setBoardList(List<Board> boardList) {
		this.boardList = boardList;
	}
	
}
