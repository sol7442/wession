package com.wow.wession.cluster;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wow.client.IConnection;
import com.wow.wession.log.IWessionLogger;
import com.wow.wession.pool.BasicConnectionPool;
import com.wow.wession.service.impl.WessionRequest;
import com.wow.wession.service.impl.WessionResponse;
import com.wow.wession.util.StopWatch;

public class ClusterNode  {

	public static final int STATE_CREATE 		= 0;
	public static final int STATE_REGISTER 	= 1;
	public static final int STATE_SYN 			= 2;
	public static final int STATE_JOURNA		= 3;
	public static final int STATE_EXIT			= 4;


	private static Logger syslogger = LoggerFactory.getLogger(IWessionLogger.SYSTEM_LOGGER);

	private String receiverName;
	private String senderName;

	private AtomicInteger state = new AtomicInteger(STATE_CREATE) ; //
	private BasicConnectionPool connection_pool;
	public void initialize(String sender, final String receiver, final String ip, final int port) {
		this.senderName   = sender;
		this.receiverName = receiver;
		connection_pool = new BasicConnectionPool(ip,port);
	}
	public String getReceiverName(){
		return this.receiverName;
	}

	public void setState(int state) {
		this.state.set(state);
	}
	public int getState(){
		return this.state.get();
	}

	public void close() {
		this.connection_pool.destroy();
		syslogger.info("{} Cluster Node Closed", this.receiverName);
	}
	public WessionResponse send(ClusterEntity entity) {

		WessionRequest request      = new WessionRequest(this.senderName,this.receiverName);
		WessionResponse response  = null;

		StopWatch watch = new StopWatch(TimeUnit.MICROSECONDS);
		IConnection conn = null;
		try {
			request.setData(entity);
			conn = connection_pool.get();
			if(conn != null){
				conn.writeRequest(request);
				response = (WessionResponse)conn.readResponse();
				if(response != null){
					syslogger.debug("Cluster Send {} : {} - {} - {}", this.receiverName, entity, response.getResultCode(),watch.stop());
				}
			}
		}catch(Exception e){
			syslogger.error("Cluster Node Send Error : {}",e );
		}finally{
			if(conn != null){
				connection_pool.release(conn);
			}
		}
		return response;
	}
}
