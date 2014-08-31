package com.iris.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.iris.config.Config;

public class Board implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private int userId;
	private String title;
	private String content;
	private String tea;
	private String summonerName;
	private String position;
	private String rank;
	private String playTime;
	private String writeTime;
	private String repleCount;
	private	 List<Reple> repleList;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSummonerName() {
		return summonerName;
	}
	public void setSummonerName(String summonerName) {
		this.summonerName = summonerName;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	
	public String getWriteTime() {
		return writeTime;
	}
	public void setWriteTime(String writeTime) {
		this.writeTime = writeTime;
	}
	
	public String getPlayTime() {
		return playTime;
	}
	public void setPlayTime(String playTime) {
		this.playTime = playTime;
	}
	public String getTea() {
		return tea;
	}
	public void setTea(String tea) {
		this.tea = tea;
	}
	public String getRepleCount() {
		return repleCount;
	}
	public void setRepleCount(String repleCount) {
		this.repleCount = repleCount;
	}
	public List<Reple> getRepleList() {
		return repleList;
	}
	public void setRepleList(List<Reple> repleList) {
		this.repleList = repleList;
	}
	
	/**
	 * 랭크를 스피너에서 사용할 수 있도록 position 으로 변경
	 * @param rankPosition
	 * @return
	 */
	public int converterRank(String rankPosition){
		
		if(rankPosition.equals(Config.RANK.UNRANK)){
			return 0;
		}
		if(rankPosition.equals(Config.RANK.BRONZE)){
			return 1;
		}
		if(rankPosition.equals(Config.RANK.SILVER)){
			return 2;
		}
		if(rankPosition.equals(Config.RANK.GOLD)){
			return 3;
		}
		if(rankPosition.equals(Config.RANK.PLATINUM)){
			return 4;
		}
		if(rankPosition.equals(Config.RANK.DIAMOND)){
			return 5;
		}
		if(rankPosition.equals(Config.RANK.CHALLENGER)){
			return 6;
		}
		return -1;
	}
	
	/**
	 * 포지션 를 스피너에서 사용할 수 있도록 position 으로 변경
	 * @param position
	 * @return
	 */
	public int converterPosition(String position){
		
		if(position.equals(Config.POSITION.SUPOT)){
			return 0;
		}
		if(position.equals(Config.POSITION.WONDIL)){
			return 1;
		}
		if(position.equals(Config.POSITION.TOP)){
			return 2;
		}
		if(position.equals(Config.POSITION.MIDE)){
			return 3;
		}
		if(position.equals(Config.POSITION.JUGGLE)){
			return 4;
		}
		return -1;
	}
	
	/**
	 * 시간 를 스피너에서 사용할 수 있도록 position 으로 변경
	 * @param position
	 * @return
	 */
	public int converterTime(String time){
		
		if(time.equals(Config.TIME.MORNING)){
			return 0;
		}
		if(time.equals(Config.TIME.AFTERNOON)){
			return 1;
		}
		if(time.equals(Config.TIME.DINNER)){
			return 2;
		}
		if(time.equals(Config.TIME.DAWN)){
			return 3;
		}
		return -1;
	}
	
	/**
	 * 티어 를 스피너에서 사용할 수 있도록 position 으로 변경
	 * @param position
	 * @return
	 */
	public int converterTea(String tea){
		
		if(tea.equals(Config.TEA.ONE)){
			return 0;
		}
		if(tea.equals(Config.TEA.TWO)){
			return 1;
		}
		if(tea.equals(Config.TEA.THREE)){
			return 2;
		}
		if(tea.equals(Config.TEA.FOUR)){
			return 3;
		}
		if(tea.equals(Config.TEA.FIVE)){
			return 4;
		}

		return -1;
	}
	
	public String transformRank(String rank){
		
		if(rank.equals("언랭크")){
			return "unrank";
		}else if(rank.equals("브론즈")){
			return "bronze";
		}else if(rank.equals("실버")){
			return "silver";
		}else if(rank.equals("골드")){
			return "gold";
		}else if(rank.equals("플래티넘")){
			return "platinum";
		}else if(rank.equals("다이아")){
			return "diamond";
		}else if(rank.equals("챌린져")){
			return "challenger";
		}else{
			return "";
		}
	}
	
	public String reverseTransformRank(String rank){
		
		if(rank.equals("unrank")){
			return "언랭크";
		}else if(rank.equals("bronze")){
			return "브론즈";
		}else if(rank.equals("silver")){
			return "실버";
		}else if(rank.equals("gold")){
			return "골드";
		}else if(rank.equals("platinum")){
			return "플래티넘";
		}else if(rank.equals("diamond")){
			return "다이아";
		}else if(rank.equals("challenger")){
			return "챌린져";
		}else{
			return "";
		}
	}
	
}
