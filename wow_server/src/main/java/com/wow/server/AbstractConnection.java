package com.wow.server;

import java.io.Closeable;

public abstract class AbstractConnection implements Closeable {
	private String Id;

	public String getId() {
		return Id;
	}


}
