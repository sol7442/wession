package com.wow.wession.session;

public interface ISessionState {
	public static final int	CREATED 			= 0;
	
	public static final int	VALID_SUCEESS 		= 10;	// 검증성공
	public static final int	VALID_FAIL		 	= 11;	// 검증실패
	public static final int	DUMMY_DUP 		= 12;	// 검증 필요없음
	public static final int	DUMMY_FAIL 		= 13;	// 검증 할수없음
	
	public static final int	EXPIRED 				= 99;
}
