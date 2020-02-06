package com.wow.wession.agent;

import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.impl.cookie.DefaultCookieSpec;

class EasyCookieSpec extends DefaultCookieSpec
{
  public void validate(Cookie arg0, CookieOrigin arg1)
    throws MalformedCookieException
  {
  }
}

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.11_00.jar
 * Qualified Name:     com.wow.wession.agent.EasyCookieSpec
 * JD-Core Version:    0.5.4
 */