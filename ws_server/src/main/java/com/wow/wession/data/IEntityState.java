package com.wow.wession.data;

public interface IEntityState {
	public static final int CREATE 		= 1;		//=	SET MANAGER PROPERTY
	public static final int EXPIRE			= 2;		//=	SET MANAGER PROPERTY
	public static final int ADD			= 10;		//=	SET MANAGER PROPERTY
	public static final int REMOVE 		= 11;		//=	SET MANAGER PROPERTY
	public static final int SET 			= 21;		//=	SET MANAGER PROPERTY
	public static final int DEL 			= 22;		//=	SET MANAGER PROPERTY
	public static final int ALL 			= 30;		//=	SET MANAGER PROPERTY
	
	
	public static final int ACCESS  		= 0;		//=	SET MANAGER PROPERTY
	public static final int CLEAR			= 99;		//=	SET MANAGER PROPERTY
}
