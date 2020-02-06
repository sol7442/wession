package com.wow.wession.agent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wow.wession.session.ISessionMessage;

public class WessionListener  {
	private ObjectInputStream    reader ;
	private ObjectOutputStream   writer ;
	
	public WessionListener(HttpServletRequest request, HttpServletResponse response){
		try {
			reader = new ObjectInputStream(request.getInputStream());
			writer = new ObjectOutputStream (response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public ISessionMessage read() throws IOException, ClassNotFoundException{
		return (ISessionMessage)reader.readObject();
	}
	
	public void write(ISessionMessage session) throws IOException{
		writer.writeObject(session);
		writer.flush();
		writer.reset();
	}
}
