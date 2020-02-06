package com.wow.wession.agent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.entity.SerializableEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.impl.cookie.DefaultCookieSpec;
import org.apache.http.protocol.HttpContext;

import com.wow.wession.session.ISessionMessage;


class EasyCookieSpec extends DefaultCookieSpec {
    @Override
    public void validate(Cookie arg0, CookieOrigin arg1) throws MalformedCookieException {
        //allow all cookies 
    }
}

class EasySpecProvider implements CookieSpecProvider {
    public CookieSpec create(HttpContext context) {
        return new EasyCookieSpec();
    }
}

public class WessionTranser {
	
	//private HttpClient httpClient;
	private ExecutorService excutors ;
	private BlockingQueue<HttpClient> httpclientpool = null;
	private AtomicBoolean enable = new AtomicBoolean(true);
	
	

    
    
	/**
	 * SEND SESSION MESSAGE
	 * 
	 * @param url
	 * @param message
	 * @return
	 * @throws TransException
	 */
	@SuppressWarnings("deprecation")
	public ISessionMessage trans(String url, ISessionMessage message) throws TransException{
		if(this.enable.get() == false){
			throw new TransException("Object Enable false",TransException.ENABLE_FALSE );
		}
		
		
		Registry<CookieSpecProvider> r = RegistryBuilder.<CookieSpecProvider>create()
	            .register("easy", new EasySpecProvider())
	            .build();

		CookieStore cookieStore = new BasicCookieStore();

		RequestConfig requestConfig = RequestConfig.custom()
	            .setCookieSpec("easy")
	            .build();

		CloseableHttpClient http_client = HttpClients.custom()
	            .setDefaultCookieStore(cookieStore)
	            .setDefaultCookieSpecRegistry(r)
	            .setDefaultRequestConfig(requestConfig)
	            .build();

	    
		WessionAgentMessage res = null;
//		HttpClient http_client = null;
		HttpPost  post = new HttpPost (url);	
		HttpResponse response = null;
		SerializableEntity req_entity;
		try {
			req_entity = new SerializableEntity(message, true);
			req_entity.setChunked(true);
			post.setEntity(req_entity);		
//			http_client = getHttpClient();
			
			response = http_client.execute(post);
			
			// 신규추가 부분 =========================================
			HttpEntity entity = response.getEntity();
			
			byte[] buffer = null;
			long conents_length  = entity.getContentLength();
			int read_length = 0;
			
		    if ( entity != null ) {
		    	ByteArrayOutputStream os = new ByteArrayOutputStream();
		    	try {
		    		entity.writeTo(os);
		    		read_length = os.size();
		    		buffer = os.toByteArray();
		    	} catch (IOException e1) {
		    		e1.printStackTrace();
		    	}
		    }
			
			int read_index  = 0;
			while(buffer[read_index] == '\n'){
				read_index++;
				if(read_index > read_length){
					throw new TransException("RESPONSE CONTENTS ERROR ["+conents_length+"]["+read_index+"]", TransException.PARSER_ERROR);
				}
			}
			
			
			if(read_index > 0){
				System.out.println("ws out : invalid stream header 0a0a0a0a readObject bug fixed ["+conents_length+"]["+read_length+"]");
			}
			
			int close_byte = 0;
			
			int contents_length = read_length - read_index + close_byte;			
			byte[] contents_buffer = new byte[contents_length];
			
			System.arraycopy(buffer, read_index, contents_buffer, 0, contents_length - close_byte);
			
			ByteArrayInputStream bis = new ByteArrayInputStream(contents_buffer);
			ObjectInputStream oi_stream = new ObjectInputStream(bis);
			res = (WessionAgentMessage)oi_stream.readObject();
		} catch (IOException e) {
			TransException ex = new TransException(e,TransException.NETWORK_ERROR);
			ex.setUrl(url,message);
			throw ex;
		} catch (ClassNotFoundException e) {
			TransException ex = new TransException(e,TransException.PARAMETER_MISS);
			ex.setUrl(url,message);
			throw ex;
		}finally{
			if(response !=null){
				try {
					response.getEntity().consumeContent();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(http_client != null){
				setHttpClient(http_client);
			}
			
		}
	    return res;
	}
	
	private void setHttpClient(HttpClient client) {
		this.httpclientpool.offer(client);
	}

	@SuppressWarnings("deprecation")
	private HttpClient getHttpClient() {
		HttpClient client = this.httpclientpool.poll();
		if(client == null){
			client = new DefaultHttpClient();
			client = new DefaultHttpClient( new ThreadSafeClientConnManager( client.getParams(), client.getConnectionManager().getSchemeRegistry()), client.getParams() );
		}
		return client;
	}

	public ISessionMessage trans(final String url, final ISessionMessage message,long timeout) throws TransException, InterruptedException, ExecutionException, TimeoutException{
		if(this.enable.get() == false){
			throw new TransException("Object Enable false",TransException.ENABLE_FALSE );
		}
		Future<ISessionMessage> future = excutors.submit(new Callable<ISessionMessage>() {
			public ISessionMessage call() throws Exception {
				return trans(url,message);
			}
		});
		return future.get(timeout,TimeUnit.MILLISECONDS);
	}

//	/**
//	 * SEND etc Http Post Message
//	 * @param post
//	 * @return
//	 * @throws TransException
//	 */
//	public HttpResponse trans(HttpPost post) throws TransException{
//		if(this.enable.get() == false){
//			throw new TransException("Object Enable false",TransException.ENABLE_FALSE );
//		}
//
//		HttpResponse response = null;
//		HttpClient http_client = null;
//		try {
//			http_client = getHttpClient();
//			response = http_client.execute(post);
//		} catch (IOException e) {
//			TransException ex = new TransException(e,TransException.NETWORK_ERROR);
//			throw ex;
//		}finally{
//			if(http_client != null){
//				setHttpClient(http_client);
//			}
//		}
//	    return response;
//	}
	public void initialize(int connection_max_size) {
		httpclientpool = new ArrayBlockingQueue<HttpClient>(connection_max_size);
		excutors  = new ThreadPoolExecutor(connection_max_size,connection_max_size, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),new TransThreadFactory());
	}
	
	private class TransThreadFactory implements ThreadFactory{
		public Thread newThread(Runnable r) {
			Thread thread = new Thread(r);
			thread.setName("Trans Worker Thread");
			return thread;
		}
	}
	public void stop(){
		excutors.shutdown();
	}
	
	public boolean isEnable(){
		return this.enable.get();
	}
	public void setEnable(boolean enable){
		this.enable.set(enable);
	}
	
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 3];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 3] = hexArray[v >>> 4];
	        hexChars[j * 3 + 1] = hexArray[v & 0x0F];
	        hexChars[j * 3 + 2] = ' ';
	    }
	    return new String(hexChars);
	}

}
