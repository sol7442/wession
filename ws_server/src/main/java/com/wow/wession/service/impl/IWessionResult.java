package com.wow.wession.service.impl;

public interface IWessionResult {
	public static final int FAIL 		= 0;
	public static final int SUCCESS 	= 1;


	public static final int CLIENT_ERROR    				= 300;
	public static final int PARAM_ERROR	  				= 301;
	public static final int TRANCE_ERROR    				= 400;
	public static final int UNDEFINED_TRANCE_OBJECT = 401;
	public static final int SERVER_ERROR 					= 500;
}
