package com.wow.wession.journal;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wow.wession.data.IEntityState;
import com.wow.wession.log.IWessionLogger;
import com.wow.wession.server.WessionServerSession;
import com.wow.wession.session.ISession;


public class JournalFile {

	private Logger syslogger = LoggerFactory.getLogger(IWessionLogger.SYSTEM_LOGGER);

	private File A;		// data append file
	private File B;		// data backup file
	private File D;		// data dump file

	private final String dataPath;
	private final long   rollTime ;
	private final long   sessionTime;

	private long fileModifyTime = 0;
	private ObjectOutputStream appendstream;
	private Lock appendLock = new ReentrantLock();

	public JournalFile(String path, long session_time, long roll_time){
		this.dataPath 		= path;
		this.sessionTime 	= session_time;
		this.rollTime 			= roll_time;
	}

	public void initialize(String name){
		createNewPaht();
		A = new File(this.dataPath,name+"_A");
		B = new File(this.dataPath,name+"_B");
		D = new File(this.dataPath,name+"_D");
	}
	public void append(JournalEntity entity){
		appendLock.lock();
		try{
			try {
				ObjectOutputStream outstream = getOutputSteam(entity.getTime()); 
				outstream.writeObject(entity);
				outstream.flush();

				syslogger.debug("Journal Append : {}", entity);

			} catch (IOException e) {
				syslogger.error("Journal File append Error : {}",e );
			}
		}finally{
			appendLock.unlock();
		}
	}
	private ObjectOutputStream getOutputSteam(long time) {
		
		if(this.appendstream == null || isRollOver(time)){
			try{
				fileModifyTime = System.currentTimeMillis();
				this.appendstream = new ObjectOutputStream(new FileOutputStream(A));
			}catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return this.appendstream;
	}

	private boolean isRollOver(long time) {
		boolean rollover = false;
		if( (time - fileModifyTime) > rollTime ){
			rollover = true;
			syslogger.debug("Journal File HandOver {}-{}",new Date(fileModifyTime), new Date(time));
			try {
				this.appendstream.close();
				this.appendstream = null;
				if(B.exists()){
					B.delete();
				}
				A.renameTo(B);
			} catch (IOException e) {
				syslogger.error("Journal File HandOver Error : {} ", e);
			}
		}
		return rollover;
	}
	public int createDumpFile(Collection<ISession> all_session){
		if(D.exists()){
			D.delete();
		}
		try {
			ObjectOutputStream outstream = new ObjectOutputStream(new FileOutputStream(D));
			long alive_time = System.currentTimeMillis() - this.sessionTime;
			for (ISession session : all_session) {
				if(session.getCreateTime() > alive_time ){
					outstream.writeObject(session);
				}
			}
			outstream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	private File createNewPaht(){
		File data_path = new File(dataPath);
		if(!data_path.exists() || !data_path.isDirectory()){
			data_path.mkdir();
			syslogger.info("make journal path {}", data_path);
		}
		return  data_path;
	}

	public void close() {
		appendLock.lock();
		try {
			appendstream.close();
		} catch (IOException e) {
			syslogger.error("Journal File Close Error : {}", e );
		}finally{
			appendLock.unlock();
		}
	}

	public Collection<ISession> getAll() {
		
		long alive_time = System.currentTimeMillis() - this.sessionTime;
		
		ConcurrentHashMap<String,ISession> dump_repository =  readDumpFile(alive_time);
		
		Set<JournalEntity> entity_set = new TreeSet<JournalEntity>( new Comparator<JournalEntity>() {
			public int compare(JournalEntity o1, JournalEntity o2) {
				return o1.getTime() < o2.getTime() ? -1 : 1;
			}
		});
		
		entity_set.addAll(readJournalEntity(B,alive_time));
		entity_set.addAll(readJournalEntity(A,alive_time));
		
		int after_size = dump_repository.size();
		Iterator<JournalEntity> entiry_iter = entity_set.iterator();
		while(entiry_iter.hasNext()){
			JournalEntity entity = entiry_iter.next();
			syslogger.debug("JournalEntity  : {} ", entity);
			WessionServerSession server_session = null;
			switch (entity.getCommand()) {
			case IEntityState.CREATE:
				dump_repository.put(((ISession)entity.getData(0)).getID(), (ISession)entity.getData(0));
				break;
			case IEntityState.EXPIRE:
				dump_repository.remove(entity.getData(0));
				break;
			case IEntityState.ADD:
				server_session = (WessionServerSession)dump_repository.get(entity.getData(0));
				if(server_session != null){
					server_session.addAgentSession((ISession)entity.getData(1),false);
				}
				break;
			case IEntityState.REMOVE:
				server_session = (WessionServerSession)dump_repository.get(entity.getData(0));
				if(server_session != null){
					server_session.removeAgentSession((String)entity.getData(1),false);
				}
				break;
			case IEntityState.SET:
				server_session = (WessionServerSession)dump_repository.get(entity.getData(0));
				if(server_session != null){
					server_session.setAttribute((String)entity.getData(1), entity.getData(2),false);
				}
				break;
			case IEntityState.DEL:
				server_session = (WessionServerSession)dump_repository.get(entity.getData(0));
				if(server_session != null){
					server_session.removeAgentSession((String)entity.getData(1),false);
				}
				break;
			case IEntityState.CLEAR:
				dump_repository.clear();
				break;
			}
			
		}
		syslogger.info("dump repository size : {} - {}", after_size , dump_repository.size());
		return dump_repository.values();
	}
	
	private Set<JournalEntity> readJournalEntity(File read_file,long alive_time) {

		Set<JournalEntity> entity_set = new HashSet<JournalEntity>();
		try {
			ObjectInputStream instream = new ObjectInputStream(new FileInputStream(read_file));
			JournalEntity entity = null;
			try{
				while(( entity = (JournalEntity)instream.readObject()) != null){
					if(entity.getTime() > alive_time){
						entity_set.add(entity);
						syslogger.debug("read file in-time {} - {}",read_file.getName(), entity);
					}else{
						syslogger.debug("read file out-time {} - {}",read_file.getName(), entity);
					}
				}
			}catch(EOFException e){
				syslogger.debug("read file end-line {}", read_file.getName());
			}finally{
				instream.close();
			}
		} catch (FileNotFoundException e) {
			syslogger.warn("read error : {} ", read_file.getName());
		} catch (IOException e) {
			syslogger.error("read error : {} - {}", read_file.getName(), e);
		}catch (ClassNotFoundException e) {
			syslogger.error("read error : {} - {}", read_file.getName(), e);
		}
		return entity_set;
	}

	private ConcurrentHashMap<String,ISession> readDumpFile(long alive_time){
		ConcurrentHashMap<String,ISession> dump_repository = new ConcurrentHashMap<String, ISession>();
		
		File dump_file  = new File(new File(dataPath),"D");
		if(dump_file.exists()){
			try {
				ObjectInputStream instreadm = new ObjectInputStream(new FileInputStream(dump_file));
				try{
					ISession session = null;
					while( (session = (ISession)instreadm.readObject()) !=null){
						if((session.getCreateTime() > alive_time)){
							dump_repository.put(session.getID(), session);
							syslogger.debug("demp file in-time {} - {}",dump_file.getName(), session);
						}else{
							syslogger.debug("demp file out-time {} - {}",dump_file.getName(), session);
						}
					}
				}catch(EOFException e){
					syslogger.debug("demp file end-line {}", dump_file.getName());
				}finally{
					instreadm.close();
				}
			}catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return dump_repository;
	}
	
}
