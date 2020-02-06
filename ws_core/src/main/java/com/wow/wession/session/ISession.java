package com.wow.wession.session;

import java.io.Serializable;

public abstract interface ISession extends Serializable, Comparable<ISession>{
	public abstract String getID();

	public abstract Object getAttribute(final String key);
	public abstract void setAttribute(final String key,final Object value );
	public abstract Object removeAttribute(final String key);
	public abstract long getCreateTime();
	public abstract long getLastAccessTime();
}
