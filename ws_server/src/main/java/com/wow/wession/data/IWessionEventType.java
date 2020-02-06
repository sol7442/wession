package com.wow.wession.data;

public interface IWessionEventType {
	public static int EVT_SESSION_CREATE 	= 0;
	public static int EVT_SESSION_EXPIRE 	= 1;
	public static int EVT_SESSION_ACCESS 	= 2;

	public static int EVT_LINK_APPEND 		= 10;
	public static int EVT_LINK_REMOVE 		= 11;

	public static int EVT_ATTRIBUTE_SET 	= 20;
	public static int EVT_ATTRIBUTE_DEL 	= 21;
	public static int EVT_ATTRIBUTE_GET 	= 22;
}
