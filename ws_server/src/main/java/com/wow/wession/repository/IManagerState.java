package com.wow.wession.repository;

import com.wow.wession.data.IEntityState;

public interface IManagerState extends IEntityState{
	public static final int NODE_REGISTRY		= 101;
	public static final int NODE_UNREGISTRY	= 102;
	public static final int NODE_CHECK			= 103;
	public static final int SET_PROPERTY		= 104;



	public void setProperty(final String key, final String value);
}
