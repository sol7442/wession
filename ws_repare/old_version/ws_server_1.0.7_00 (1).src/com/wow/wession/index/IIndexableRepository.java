package com.wow.wession.index;

import com.wow.wession.session.ISession;
import java.util.List;

public abstract interface IIndexableRepository
{
  public abstract IRepositoryIndexHandler getIndexHandler();

  public abstract List<ISession> getBy(String paramString);

  public abstract ISession expireBy(ISession paramISession);
}

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.7_00 (1).jar
 * Qualified Name:     com.wow.wession.index.IIndexableRepository
 * JD-Core Version:    0.5.4
 */