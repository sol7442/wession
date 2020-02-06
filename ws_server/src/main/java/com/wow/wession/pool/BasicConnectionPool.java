package com.wow.wession.pool;


import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wow.client.IConnection;
import com.wow.wession.log.IWessionLogger;
public class BasicConnectionPool extends AbstractConnectionPool{

	private Logger syslogger = LoggerFactory.getLogger(IWessionLogger.SYSTEM_LOGGER);

	private ConnectionHandler handler;
	public BasicConnectionPool(String ip,int port){
		this.handler = new ConnectionHandler(ip, port);
	}
	public void setHandler(ConnectionHandler handler){
		this.handler = handler;
	}
	public ConnectionHandler getHandler(){
		return this.handler;
	}
	public IConnection get() throws IOException {
		IConnection conn = this.queue.poll();
		if(conn == null && this.handler != null){
			conn = this.handler.createConnection();
			syslogger.info("new Conneciton : {}",conn);		
		}
		return conn;
	}

	public void release(IConnection conn) {
		if(conn !=null && conn.isConnected()){
			this.queue.offer(conn);
		}
	}
	public void destroy(){
		this.handler = null;
		IConnection con = null;
		while( (con = this.queue.poll()) !=null){
			try {
				if(con.isConnected()){
					con.close();
					syslogger.info("close Conneciton : {}",con);		
				}
			} catch (IOException e) {
				syslogger.info("new Conneciton : {}",e.getMessage());		
			}
		}
		syslogger.info("destroy connenction pool");		
	}
}
