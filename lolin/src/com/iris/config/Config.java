package com.iris.config;

public class Config {

	public static class NUMBER {
		
		public final static String ZERO		= "0";
	}
	
	public static class BOARD {
		
		public final static String SUB_URL 			 = "?id=";
		public static final String BOARD_ID 			 = "BoardId";
		public final static String BOARD_DELETE 		 = "http://192.168.219.6:8080/board/delete";
		public final static String BOARD_FIND_ONE		 = "http://192.168.219.6:8080/board/findOne";
	}
	
	public static class RANK {
		
		public final static String UNRANK 		= "unrank";
		public final static String BRONZE 		= "bronze";
		public final static String SILVER 		= "silver";
		public final static String GOLD 		= "gold";
		public final static String PLATINUM 	= "platinum";
		public final static String DIAMOND 	= "diamond";
		public final static String CHALLENGER = "challenge";
	}
	
	public static class POSITION {
		
		public final static String TOP 		= "탑";
		public final static String MIDE 		= "미드";
		public final static String SUPOT 		= "서폿";
		public final static String JUGGLE 		= "정글";
		public final static String WONDIL 		= "원딜";
	}
	
	public static class TIME {
		
		public final static String MORNING 		= "오전";
		public final static String AFTERNOON 		= "오후";
		public final static String DINNER 			= "저녁";
		public final static String DAWN 			= "새벽";
	}
	
	public static class TEA {
		
		public final static String ONE 	= "1티어";
		public final static String TWO 	= "2티어";
		public final static String THREE 	= "3티어";
		public final static String FOUR 	= "4티어";
		public final static String FIVE 	= "5티어";
	}
	
	public static class FACEBOOK {
		
		public static final String FACEBOOK_ID  	= "FACEBOOK_ID";
	}
	
	public static class API {
		
		public final static String			SUB_URL 						= "?id=";
		public final static String 		SUB_URL_BOARD_ID 	 			= "?boardId=";
		public final static String 		SUB_URL_REPLE_ID 	 			= "?repleId=";
		
		public final static String 		USER_SAVE 						= "http://192.168.219.6:8080/user/save";
		public final static String 		USER_FIND_ONE					= "http://192.168.219.6:8080/user/findOne";
		
		public final static String 		REPLE_SAVE						= "http://192.168.219.6:8080/reple/save";
		public final static String 		REPLE_FIND_ONE					= "http://192.168.219.6:8080/reple/findOne";
		public final static String 		REPLE_DELETE					= "http://192.168.219.6:8080/reple/delete";
		
		public final static String 		GCM_SEND_REPLE					= "http://192.168.219.6:8080/gcm/sendReple";
		
	}
	
	public static class FLAG {
		
		public final static String 	  ERROR 	 = "Error";
		public static final String 	  TRUE 		 = "true";
		public static final String 	  DATA 		 = "data";
		public final static  String	  MESSAGE 	 = "message";
		public final static  String	  EDIT_STATE = "editState";
		public final static  String	  FACEBOOK_ID = "facebookId";
		public final static  String	  SUMMERNER_NAME = "summernerName";
	}

	public static class GCM {
		
	    public static final String PROJECT_ID = "450303710235";
	}
	
	
}
