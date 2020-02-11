package test.wowsanta.wession.agent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

import com.wow.wession.agent.WessionAgent;
import com.wow.wession.agent.WessionAgentSession;
import com.wow.wession.license.WessionLicense;
import com.wow.wession.session.ISession;
import com.wow.wession.session.SessionFactory;

public class AgentTest {
	
	@Test
	public void test() {
		String url = "http://127.0.0.1:8080/ws_admin/c_service.jsp";
		WessionAgent.getInstance().initialize(url, 1, new WessionLicense(""));
		WessionAgent.getInstance().setSessionFactory(new SessionFactory() {
			@Override
			public ISession create(String id, String user) {
				return new WessionAgentSession("id", "user", "app");
			}
			
			@Override
			public ISession create(Object obj) {
				WessionAgentSession session = new WessionAgentSession("id", "user", "app");
				session.setAppCode("code");
				session.setAttribute("COMMAND", "AGENT_SET_WESSION");
				return session;
			}
		});
		WessionAgent.getInstance().create(null);
		//WessionAgent.getInstance().add(new WessionAgentSession("id","user","app"));
		
		
		String command = "";
		while(!command.equals("exit")) {
			System.out.print("Enter Commmand : ");
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			try {
				command = reader.readLine();
			} catch (IOException e) {
				System.out.println(e.getMessage());
				command = "exit";
			}
		}
				
	}
}
