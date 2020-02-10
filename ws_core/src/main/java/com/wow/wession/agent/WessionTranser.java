package com.wow.wession.agent;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.SerializableEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import com.wow.wession.session.ISessionMessage;

public class WessionTranser {
	private HttpClient httpClient;
	private PoolingHttpClientConnectionManager connection_mgr = new PoolingHttpClientConnectionManager();
	private ExecutorService excutors;
	private AtomicBoolean enable = new AtomicBoolean(true);

    
    
	/**
	 * SEND SESSION MESSAGE
	 * 
	 * @param url
	 * @param message
	 * @return
	 * @throws TransException
	 */
	public ISessionMessage trans(String url, ISessionMessage message) throws TransException{
		if(this.enable.get() == false){
			throw new TransException("Object Enable false",TransException.ENABLE_FALSE );
		}
		
		WessionAgentMessage res = null;
		HttpPost  post = new HttpPost (url);
		try {
			SerializableEntity req_entity = new SerializableEntity(message, true);
			post.setEntity(req_entity);		
			HttpResponse response = httpClient.execute(post, HttpClientContext.create());
			
			HttpEntity res_entity = response.getEntity();
			long conents_length = res_entity.getContentLength();
			byte[] buffer = new byte[10240];
			int read_length = res_entity.getContent().read(buffer);
			
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
			
			int contents_length = read_length - read_index ;			
			byte[] contents_buffer = new byte[contents_length];
			
			System.arraycopy(buffer, read_index, contents_buffer, 0, contents_length);
			
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
			post.releaseConnection();
		}
	    return res;
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

	/**
	 * SEND etc Http Post Message
	 * @param post
	 * @return
	 * @throws TransException
	 */
	public HttpResponse trans(HttpPost post) throws TransException{
		if(this.enable.get() == false){
			throw new TransException("Object Enable false",TransException.ENABLE_FALSE );
		}

		HttpResponse response = null;
		try {
			response = httpClient.execute(post,HttpClientContext.create()); 
		} catch (IOException e) {
			TransException ex = new TransException(e,TransException.NETWORK_ERROR);
			throw ex;
		}finally{
			post.releaseConnection();
		}
	    return response;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void initialize(int connection_max_size) {
		
		this.connection_mgr.setMaxTotal(connection_max_size * 2);
		this.httpClient = HttpClients.custom().setConnectionManager(this.connection_mgr).build();
		this.excutors = new ThreadPoolExecutor(connection_max_size, connection_max_size, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(), new TransThreadFactory());
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
}
