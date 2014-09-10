package com.iris.config;

public class Config {

	public static class NUMBER {

		public final static String ZERO		= "0";
	}

	public static class BOARD {

		public final static String SUB_URL 	= "?id=";
		public static final String BOARD_ID 	= "BoardId";
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

		public static final String FACEBOOK_ID  		= "FACEBOOK_ID";

		public static final String FACEBOOK_BASE_URL  	= "http://graph.facebook.com/";
		public static final String PICTURE_TYPE_LARGE	= "/picture?type=large";
		public static final String PICTURE_TYPE_NOMAL	= "/picture?type=normal";
		public static final String PICTURE_SMALL		= "/picture?type=small";
	}

	public static class API {

		public final static String			DEFAULT_URL 					= "http://182.229.77.152:8080/";

		public final static String			SUB_URL 						= "?id=";
		public final static String 		SUB_URL_BOARD_ID 	 			= "?boardId=";
		public final static String 		SUB_URL_REPLE_ID 	 			= "?repleId=";

		public final static String 		BOARD_FINDALL 					= "board/findAll";
		public final static String 		BOARD_FIND_MY_ALL 				= "board/findMyAll";

		public final static String 		BOARD_SAVE 						= "board/save";
		public final static String 		BOARD_DELETE 		 			= "board/delete";
		public final static String 		BOARD_FIND_ONE		 			= "board/findOne";

		public final static String 		USER_SAVE 						= "user/save";
		public final static String 		USER_SAVE_DEFAULT 				= "user/saveDefault";
		public final static String 		USER_FIND_ONE					= "user/findOne";

		public final static String 		REPLE_SAVE						= "reple/save";
		public final static String 		REPLE_FIND_ONE					= "reple/findOne";
		public final static String 		REPLE_DELETE					= "reple/delete";

		public final static String 		GCM_SEND_REPLE					= "gcm/sendReple";

		public final static String 		FACEBOOK_ALBUM					= "me/photos";

	}

	public static class FLAG {

		public static final 	String OK 				= "ok";
		public final static 	String ERROR 			= "Error";
		public static final 	String TRUE 		 	= "true";
		public static final 	String FALSE 			= "false";
		public static final 	String DATA 		 	= "data";
		public static final  	String MESSAGE 	 		= "message";
		public static final  	String EDIT_STATE 		= "editState";
		public static final  	String FACEBOOK_ID 		= "facebookId";
		public static final  	String FACEBOOK_NAME 	= "facebookName";

		public static final  	String SUMMERNER_NAME 	= "summernerName";
		public static final 	String BOARD_ID 		= "boardId";
		public static final 	String REPLE 			= "reple";	
		public static final 	String USER_NAME 		= "userName";
		public static final 	String IS_LOGIN  		= "isLogin";
		public static final  	String NETWORK_CLEAR 	= "네트워크 상태를 확인해 주세요.";
		public static final 	String BOARD_CONTENT 	= "boardContent";
		public static final 	String NOTIBLOCK 		= "notiblock";
		public static final 	String PICTURE 			= "picture";
		public static final 	String ID 				= "id";
		public static final 	String NAME				= "name";
		public static final 	String LINK				= "link";
		public static final 	String ACCESS_TOKEN  	= "ACCESS_TOKEN";
		public static final	String PROPERTY_REG_ID 	= "registration_id";
		public static final 	String PROPERTY_APP_VERSION = "appVersion";

	}

	public static class GCM {

		public static final 	String TAG 			= "GCM";
		public static final	String SENDER_ID 	= "376992068498";
		public static final 	String PROJECT_ID 	= "450303710235";
	}

	public static class KEY {

		public static final 	String SECRET 		= "5396ad83bfe531ded76139723c747f7b33790b7b";
	}

}
