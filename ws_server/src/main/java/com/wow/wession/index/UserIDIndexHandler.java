package com.wow.wession.index;

import com.wow.wession.server.WessionServerSession;
import com.wow.wession.session.ISession;

public class UserIDIndexHandler implements IRepositoryIndexHandler {

	public String getIndexKey(ISession session) {
		if (session instanceof WessionServerSession) {
			WessionServerSession server_session = (WessionServerSession) session;
			return server_session.getUser();
		}
		return null;
	}

}
