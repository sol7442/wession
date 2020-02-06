package com.wow.sample;

import java.io.IOException;

import com.wowsanta.util.ObjectPool;

public class ConnectionPool extends ObjectPool<SampleConnection>{

	private int port;
	private String address;
	
	@Override
	public SampleConnection clear(SampleConnection object) {
		return object;
	}

	@Override
	public SampleConnection create() {
		return new SampleConnection(address, port);
	}

	@Override
	public void destroy(SampleConnection object) {
		try {
			object.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
