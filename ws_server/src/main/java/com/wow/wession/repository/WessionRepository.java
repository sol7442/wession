package com.wow.wession.repository;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import com.wow.wession.index.IndexRepository;
import com.wow.wession.session.ISession;

public class WessionRepository{

	//private Logger weslogger = LoggerFactory.getLogger(IWessionLogger.WESSION_LOGGER);

	private ConcurrentHashMap<String, ISession> 	memoryRepository = new ConcurrentHashMap<String, ISession>();
	private IndexRepository								indexRepository = IndexRepository.UserIDIndexRepository;

	public void create(final ISession session) {
		//StopWatch  watch = new StopWatch(TimeUnit.NANOSECONDS);
		ISession ref_session = memoryRepository.putIfAbsent(session.getID(),session);
		if(ref_session == null){
			indexRepository.create(session);
		}
		//weslogger.debug("create : session[{}]-{}",session,watch.stop());
	}
	
	public ISession expire(final String key) {
		ISession ref_session = memoryRepository.remove(key);
		if(ref_session != null){
			indexRepository.expireBy(ref_session);
		}
		return ref_session;
	}

	public void clear() {
		memoryRepository.clear();
		indexRepository.clear();
	}

	public int getSize() {
		return this.memoryRepository.size();
	}

	public ISession get(String key) {
		return this.memoryRepository.get(key);
	}

	public List<ISession> getBy(String handle, String key) {
		return indexRepository.getBy(key);
	}
	public Iterator<ISession> iterator(){
		return getAll().iterator();
	}
	public Collection<ISession> getAll() {
		Set<ISession> sort_set = new TreeSet<ISession>( new Comparator<ISession>() {
			public int compare(ISession o1, ISession o2) {
				return o1.getCreateTime() > o2.getCreateTime() ? -1 : 1;
			}
		});
		sort_set.addAll(this.memoryRepository.values());
		return sort_set;
	}
//
//	public ISession[] toArray(){
//		return getAll().toArray(new ISession[0]);
//	}
}

