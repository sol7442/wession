package com.wow.wession.cluster;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wow.wession.config.WSConfigurationManager;
import com.wow.wession.config.ClusterManagerConfigDocument.ClusterManagerConfig;
import com.wow.wession.config.ClusterServerConfigDocument.ClusterServerConfig;
import com.wow.wession.config.ClusterServerConfigDocument.ClusterServerConfig.ClusterNodeConfig;
import com.wow.wession.journal.JournalFile;
import com.wow.wession.log.IWessionLogger;
import com.wow.wession.repository.IManagerState;
import com.wow.wession.repository.IRepository;
import com.wow.wession.service.impl.IWessionResult;
import com.wow.wession.service.impl.WessionResponse;
import com.wow.wession.session.ISession;
import com.wow.wession.util.StopWatch;


public class ClusterManager implements IRepository , IManagerState{

	private Logger syslogger = LoggerFactory.getLogger(IWessionLogger.SYSTEM_LOGGER) ;
	private Logger weslogger = LoggerFactory.getLogger(IWessionLogger.WESSION_LOGGER);

	private ConcurrentHashMap<String,ClusterNode> clusterNodes = new ConcurrentHashMap<String, ClusterNode>();
	private String serviceName;
	private int checkStartTime   = 1000;
	private int checkPeriodTime = 1000;
	
	private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	
	public boolean initialize(String service_name, ClusterManagerConfig mgr_conf, ClusterServerConfig server_conf) {
		syslogger.info("******** Cluster Manager Node Regist ********");

		boolean bRes = false;
		try{
			this.serviceName = service_name;
	
			ClusterNodeConfig[] cluster_node_array = server_conf.getClusterNodeConfigArray();
			for(int i=0; i<cluster_node_array.length; i++){
				ClusterNode node = new ClusterNode();
				node.initialize(server_conf.getName(),
								cluster_node_array[i].getName(),
								cluster_node_array[i].getAddress(),
								cluster_node_array[i].getPort());
				register(node,true);
			}
			syslogger.info("******** Cluster Manager Node Regist ********");
			
			this.checkPeriodTime =  mgr_conf.getCheckPeriodTime();
			this.checkStartTime   =  mgr_conf.getCheckStartTime();
			bRes = true;
		}catch(Exception e){
			syslogger.error("initialize fail {}",e);
		}finally{
			scheduler = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
				public Thread newThread(Runnable r) {
					return new Thread(r,"Cluster NodeCheck Thread");
				}
			});
		}
		return bRes;
	}

	public boolean register(String node_name){
		return register(findClusterNode(node_name),false);
	}
	public boolean register(ClusterNode node, boolean send){
		boolean result = false;
		if(send == true){
			ClusterEntity entity = new ClusterEntity(NODE_REGISTRY);
			entity.addData(this.serviceName);
			WessionResponse res = node.send(entity);
			if(res != null && res.getResultCode() == IWessionResult.SUCCESS){
				result = true;
			}
		}else{
			result = true;
		}

		if(result == true){
			ClusterNode ref_node = clusterNodes.putIfAbsent(node.getReceiverName(),node);
			if(ref_node == null){
				syslogger.info("cluster node registry success : {}-{}", serviceName, node.getReceiverName());
			}
		}else{
			syslogger.info("cluster node registry failed   : {}-{}", serviceName, node.getReceiverName());
		}

		return result;
	}
	public void unRegister(String name){
		clusterNodes.remove(name);
	}

	private ClusterNode findClusterNode(String nodeName){
		ClusterNode node = null;
		try{
			ClusterServerConfig cluster_config = WSConfigurationManager.getInstance().get().getClusterServerConfig();
			ClusterNodeConfig[] cluster_node_array = cluster_config.getClusterNodeConfigArray();

			for(int i=0; i<cluster_node_array.length; i++){
				if(nodeName.equals(cluster_node_array[i].getName())){
					node = new ClusterNode();
					node.initialize(cluster_config.getName(),
									cluster_node_array[i].getName(),
									cluster_node_array[i].getAddress(),
									cluster_node_array[i].getPort());
					node.setState(ClusterNode.STATE_CREATE);
					break;
				}
			}

		}catch(Exception e){
			syslogger.error("cluster node register Exception : ", e);
		}

		return node;
	}

	public boolean checkNode(ClusterNode node){
		ClusterEntity entity = new ClusterEntity(NODE_CHECK);
		entity.addData(this.serviceName);
		WessionResponse res = node.send(entity);
		if(res != null && res.getResultCode() == IWessionResult.SUCCESS){
			return true;
		}else{
			return false;
		}
	}
	public Collection<ISession> getAll() {
		Collection<ISession> all_session = new TreeSet<ISession>();
		Iterator<String> node_iter = this.clusterNodes.keySet().iterator();
		if(node_iter.hasNext()){
			ClusterNode node = this.clusterNodes.get(node_iter.next());
			ClusterEntity entity = new ClusterEntity(ALL);
			WessionResponse res = node.send(entity);
			if(res != null && res.getResultCode() ==IWessionResult.SUCCESS ){
				ClusterEntity res_entity = (ClusterEntity)res.getData();
				for(int i=0; i<res_entity.getDataSize(); i++){
					all_session.add((ISession)res_entity.getData(i));
				}
				syslogger.info("getAll-{} ", res_entity.getDataSize());
			}
		}
		return all_session;
	}

	public void start() {
		scheduler.scheduleAtFixedRate(new Runnable() {
			public void run() {

				try{
					Iterator<ClusterNode> cluster_node_iter = clusterNodes.values().iterator();
					while(cluster_node_iter.hasNext()){
						ClusterNode node = cluster_node_iter.next();
						if(checkNode(node) == false){
							cluster_node_iter.remove();
							node.close();
							syslogger.info("cluster node unregistry  {} - {}", serviceName, node.getReceiverName());
						}
					}
				}catch (Exception e) {
					syslogger.debug("check {} - {}", serviceName, e);
				}
			}
		}, this.checkStartTime,this.checkPeriodTime,TimeUnit.MILLISECONDS);

		syslogger.info("Cluster Manager Star *************");
	}
	public void stop(){
		this.scheduler.shutdown();
		Iterator<ClusterNode> iter = clusterNodes.values().iterator();
		while(iter.hasNext()){
			ClusterNode node = iter.next();
			node.close();
			iter.remove();
		}
		syslogger.info("Cluster Manager Stop ************");
	}

	private List<ClusterNode> getNodeList(){
		List<ClusterNode> array_list = new ArrayList<ClusterNode>();
		array_list.addAll(clusterNodes.values());
		return array_list;
	}
	//***********************************************************************************************************//
	public void setProperty(final String key,final String value) {
		StopWatch  watch = new StopWatch(TimeUnit.NANOSECONDS);

		ClusterEntity entity = new ClusterEntity(SET_PROPERTY);
		entity.addData(key);
		entity.addData(value);

		List<ClusterNode> cluster_list = getNodeList();
		for (ClusterNode node : cluster_list) {
			node.send(entity);
		}

		weslogger.debug("setProperty : key[{}], value[{}]-{} ({})",key,value,watch.stop(),cluster_list.size());
	}
	public void create(final ISession session){
		StopWatch  watch = new StopWatch(TimeUnit.NANOSECONDS);
		ClusterEntity entity = new ClusterEntity(CREATE);
		entity.addData(session);

		List<ClusterNode> cluster_list = getNodeList();
		for (ClusterNode node : cluster_list) {
			node.send(entity);
		}
		weslogger.debug("create : session[{}]-{} ({}) ",session,watch.stop(),cluster_list.size());
	}
	public void expire(String key){
		StopWatch  watch = new StopWatch(TimeUnit.NANOSECONDS);

		ClusterEntity entity = new ClusterEntity(EXPIRE);
		entity.addData(key);

		List<ClusterNode> cluster_list = getNodeList();
		for (ClusterNode node : cluster_list) {
			node.send(entity);
		}
		weslogger.debug("expire : key[{}]-{} ({})",key,watch.stop(),cluster_list.size());
	}
	public void add(String parent,ISession session) {
		StopWatch  watch = new StopWatch(TimeUnit.NANOSECONDS);

		ClusterEntity entity = new ClusterEntity(ADD);
		entity.addData(parent);
		entity.addData(session);

		List<ClusterNode> cluster_list = getNodeList();
		for (ClusterNode node : cluster_list) {
			node.send(entity);
		}
		weslogger.debug("add : parent[{}],session[{}]-{} ({})",parent,session,watch.stop(),cluster_list.size());
	}
	public void remove(String parent,String key) {
		StopWatch  watch = new StopWatch(TimeUnit.NANOSECONDS);

		ClusterEntity entity = new ClusterEntity(REMOVE);
		entity.addData(parent);
		entity.addData(key);

		List<ClusterNode> cluster_list = getNodeList();
		for (ClusterNode node : cluster_list) {
			node.send(entity);
		}
		weslogger.debug("remove : parent[{}],key[{}]-{} ({})",parent,key,watch.stop(),cluster_list.size());
	}

	public void set(String parent, String key, Object object) {
		StopWatch  watch = new StopWatch(TimeUnit.NANOSECONDS);

		ClusterEntity entity = new ClusterEntity(SET);
		entity.addData(parent);
		entity.addData(key);
		entity.addData(object);

		List<ClusterNode> cluster_list = getNodeList();
		for (ClusterNode node : cluster_list) {
			node.send(entity);
		}
		weslogger.debug("set : parent[{}],key[{}],object[{}]-{} ({})",parent,key,object,watch.stop(),cluster_list.size());
	}

	public void delete(String parent, String key) {
		StopWatch  watch = new StopWatch(TimeUnit.NANOSECONDS);

		ClusterEntity entity = new ClusterEntity(DEL);
		entity.addData(parent);
		entity.addData(key);

		List<ClusterNode> cluster_list = getNodeList();
		for (ClusterNode node : cluster_list) {
			node.send(entity);
		}
		weslogger.debug("delete : parent[{}],key[{}]-{} ({})",parent,key,watch.stop(),cluster_list.size());
	}

	public void clear() {
		StopWatch  watch = new StopWatch(TimeUnit.NANOSECONDS);

		ClusterEntity entity = new ClusterEntity(CLEAR);

		List<ClusterNode> cluster_list = getNodeList();
		for (ClusterNode node : cluster_list) {
			node.send(entity);
		}
		weslogger.debug("clear:-{} ({})",watch.stop(),cluster_list.size());
	}
}
