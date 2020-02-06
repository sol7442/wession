package com.wow.wession.service;

import java.io.IOException;
import java.net.Socket;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wow.client.socket.SocketConnection;
import com.wow.server.IAcceptor;
import com.wow.server.IServiceHandler;
import com.wow.server.IWorker;
import com.wow.server.socket.SocketAcceptor;
import com.wow.wession.cluster.ClusterEntity;
import com.wow.wession.config.ClusterServerConfigDocument.ClusterServerConfig;
import com.wow.wession.config.WSConfigurationManager;
import com.wow.wession.data.AbstractEntity;
import com.wow.wession.log.IWessionLogger;
import com.wow.wession.repository.IManagerState;
import com.wow.wession.repository.IRepository;
import com.wow.wession.service.impl.IWessionResult;
import com.wow.wession.service.impl.WessionRequest;
import com.wow.wession.service.impl.WessionResponse;
import com.wow.wession.session.ISession;
import com.wow.wession.session.WessionSessionManager;
import com.wow.wession.util.StopWatch;

public class ClusterService implements IServiceHandler,IRepository, IManagerState {

	private Logger syslogger = LoggerFactory.getLogger(IWessionLogger.SYSTEM_LOGGER) ;

	private String name;
	private IAcceptor acceptor;
	private BlockingQueue<Socket> queue = null;
	private Set<SocketConnection>  connections = new HashSet<SocketConnection>();

	public boolean initialize() {
		boolean bRes = false;
		try {
			ClusterServerConfig cluster_config = WSConfigurationManager.getInstance().get().getClusterServerConfig();
			
			syslogger.info("Cluster Server Configuration \n{}",WSConfigurationManager.getInstance().getText(cluster_config));
			
			this.name = cluster_config.getName() + " Cluster";
			this.acceptor = new SocketAcceptor(
					this,
					cluster_config.getPort(),
					cluster_config.getMinThreadSize(),
					cluster_config.getMaxThreadSize(),
					cluster_config.getIdleMilliTime());
			
			bRes = true;
		} catch (IOException e) {
			syslogger.error("ClusterService initialize fail",e);
		}

		return bRes;
	}

	public void close(){
		try {
			this.acceptor.close();
			Iterator<SocketConnection>iter = this.connections.iterator();
			while(iter.hasNext()){
				iter.next().close();
				iter.remove();
			}

		} catch (IOException e) {
			syslogger.error("Service Close Error : {}",getName());
		}
		syslogger.info("Service Closed : {}",getName());
	}
	public IAcceptor getAcceptor() {
		return this.acceptor;
	}

	public BlockingQueue<Socket> getSocketQueue() {
		if(queue == null){
			queue = new LinkedBlockingQueue<Socket>();//SynchronousQueue<Socket>();
		}
		return queue;
	}

	public IWorker getNewWorker() {
		return new ClusterWorker();
	}

	public String getName() {
		return this.name;
	}


	public class ClusterWorker implements IWorker{
		public String getWorkerName(){
			return "ClusterWorker";
		}
		public void run() {
			SocketConnection connection = null;
			try {
				connection = new SocketConnection(queue.take());
				StopWatch watch = new StopWatch(TimeUnit.NANOSECONDS);

				connections.add(connection)	;
				do{
					WessionRequest request 		= null;
					WessionResponse response 	= null;
					try {
						request = (WessionRequest) connection.readRequest();
						ClusterEntity entity = (ClusterEntity)request.getData();
						watch.reset();

						connection.setKeepAlive(true);
						switch(entity.getCommand()){
							case IRepository.CREATE:
								response = create(entity);
							break;
							case IRepository.EXPIRE:
								response = expire(entity);
							break;
							case IRepository.ADD:
								response = add(entity);
							break;
							case IRepository.REMOVE:
								response = remove(entity);
							break;
							case IRepository.SET:
								response = set(entity);
							break;
							case IRepository.DEL:
								response = delete(entity);
							break;
							case IRepository.ALL:
								response = getAll(entity);
							break;
							case IRepository.CLEAR:
								response = clear(entity);
							break;
							case IManagerState.SET_PROPERTY:
								response = setProperty(entity);
							break;
							case IManagerState.NODE_REGISTRY:
							case IManagerState.NODE_CHECK:
								response = OnNodeRegister(entity);
							break;
//							case IManagerState.NODE_CHECK:
//								response = new WessionResponse(request);
//								response.setResultCode(IWessionResult.SUCCESS);
//								break;
							case IManagerState.NODE_UNREGISTRY:
							break;
						}
					} catch (ClassNotFoundException e) {
						syslogger.error("WessionRequest Read Error : {}",e);
						connection.setKeepAlive(false) ;
					}finally{
						if(request != null){
							syslogger.debug("{} - {} : {}",request.getData(),response,watch.stop());
							connection.writeResponse(response);
						}else{
							connection.close();
						}
					}
				}while(connection.isKeepAlive() && connection.isConnected());

			} catch (IOException e) {
				syslogger.error("ServerSocket Connection Error : {}",e);
			} catch (InterruptedException e) {
				syslogger.error("ServerSocket Connection Error : {}",e);
			}finally{
				syslogger.error("ServerSocket Close : {}",connection);
				if(connection != null){
					try {
						connections.remove(connection);
						connection.close();
					} catch (Exception e) {
						syslogger.error("ServerSocket Connection Error : {}",e);
					}
				}
			}
		}
	}


	private WessionResponse OnNodeRegister(AbstractEntity entity){
		WessionResponse response = new WessionResponse();
		try{
			if(entity.getDataSize() == 1){
				String node_name = (String)entity.getData(0);
				WessionSessionManager.getInstance().getClusterManager().register(node_name);
				response.setResultCode(IWessionResult.SUCCESS);
			}else{
				response.setResultCode(IWessionResult.PARAM_ERROR);
			}
		}catch(Exception e){
			response.setResultCode(IWessionResult.SERVER_ERROR);
		}
		return response;
	}


////////////////////////////////////////////////////////////////////////////////
	private WessionResponse setProperty(ClusterEntity entity) {
		WessionResponse response = new WessionResponse();
		if(entity != null && entity.getDataSize() > 1){
			String key   = (String)entity.getData(0);
			String value = (String)entity.getData(1);
			setProperty(key,value);
			response.setResultCode(IWessionResult.SUCCESS);
		}else{
			response.setResultCode(IWessionResult.PARAM_ERROR);
		}
		return response;
	}
	public void setProperty(final String key, final String value){
		WessionSessionManager.getInstance().setProperty(key, value,false);
	}


	private WessionResponse create(ClusterEntity entity) {
		WessionResponse response = new WessionResponse();
		if(entity != null && entity.getDataSize() == 1){
			create((ISession)entity.getData(0));
			response.setResultCode(IWessionResult.SUCCESS);
		}else{
			response.setResultCode(IWessionResult.PARAM_ERROR);
		}
		return response;
	}
	public void create(ISession session) {
		WessionSessionManager.getInstance().create(session,false);
	}


	private WessionResponse expire(ClusterEntity entity) {
		WessionResponse response = new WessionResponse();
		if(entity != null && entity.getDataSize() == 1){
			expire((String)entity.getData(0));
			response.setResultCode(IWessionResult.SUCCESS);
		}else{
			response.setResultCode(IWessionResult.PARAM_ERROR);
		}
		return response;
	}
	public void expire(String key) {
		WessionSessionManager.getInstance().expire(key,false);
	}

	private WessionResponse add(ClusterEntity entity) {
		WessionResponse response = new WessionResponse();
		if(entity != null && entity.getDataSize() == 2){
			String parent     = (String)entity.getData(0);
			ISession session = (ISession)entity.getData(1);
			add(parent,session);
			response.setResultCode(IWessionResult.SUCCESS);
		}else{
			response.setResultCode(IWessionResult.PARAM_ERROR);
		}
		return response;
	}
	public void add(String parent,ISession session) {
		WessionSessionManager.getInstance().add(parent, session);
	}
	private WessionResponse remove(ClusterEntity entity) {
		WessionResponse response = new WessionResponse();
		if(entity != null && entity.getDataSize() == 2){
			String parent    = (String)entity.getData(0);
			String key        = (String)entity.getData(1);
			remove(parent,key);
			response.setResultCode(IWessionResult.SUCCESS);
		}else{
			response.setResultCode(IWessionResult.PARAM_ERROR);
		}
		return response;
	}
	public void remove(final String parent,final String key) {
		WessionSessionManager.getInstance().remove(parent, key);
	}

	private WessionResponse set(ClusterEntity entity) {
		WessionResponse response = new WessionResponse();
		if(entity != null && entity.getDataSize() == 3){
			String parent    = (String)entity.getData(0);
			String key        = (String)entity.getData(1);
			Object object    = entity.getData(2);
			set(parent,key,object);
			response.setResultCode(IWessionResult.SUCCESS);
		}else{
			response.setResultCode(IWessionResult.PARAM_ERROR);
		}
		return response;
	}

	public void set(String parent,String key,Object object){
		WessionSessionManager.getInstance().set(parent, key,object);
	}

	private WessionResponse delete(ClusterEntity entity) {
		WessionResponse response = new WessionResponse();
		if(entity != null && entity.getDataSize() == 2){
			String parent    = (String)entity.getData(0);
			String key        = (String)entity.getData(1);
			delete(parent,key);
			response.setResultCode(IWessionResult.SUCCESS);
		}else{
			response.setResultCode(IWessionResult.PARAM_ERROR);
		}
		return response;
	}
	public void  delete(String parent, String key) {
		WessionSessionManager.getInstance().delete(parent, key);
	}
	private WessionResponse getAll(ClusterEntity entity) {
		WessionResponse response = new WessionResponse();
		if(entity != null && entity.getDataSize() == 0){
			Collection<ISession> all_session = WessionSessionManager.getInstance().getAll();
			ClusterEntity res_entiry = new ClusterEntity(entity.getCommand());
			for (ISession session : all_session) {
				res_entiry.addData(session);
			}
			syslogger.debug("getall : {}", res_entiry.getDataSize());
			response.setData(res_entiry);
			response.setResultCode(IWessionResult.SUCCESS);
		}else{
			response.setResultCode(IWessionResult.PARAM_ERROR);
		}
		return response;
	}

	private WessionResponse clear(ClusterEntity entity) {
		WessionResponse response = new WessionResponse();
		if(entity != null && entity.getDataSize() == 0){
			clear();
			response.setResultCode(IWessionResult.SUCCESS);
		}else{
			response.setResultCode(IWessionResult.PARAM_ERROR);
		}
		return response;
	}

	public void clear() {
		WessionSessionManager.getInstance().clear(false);
	}
}
