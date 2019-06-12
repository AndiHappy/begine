package begine.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HCUtil {
	
  private final static Logger logger = LoggerFactory.getLogger(HCUtil.class);

	
	private final static int socketTimeout = 10000;
  private final static int connectTimeout = 10000;
  private final static int connectionRequestTimeout = 30000;
  private final static int maxTotal = 150;
  private final static int maxPerRoute = 150;
  
  
  private static CloseableHttpClient hc ;
  
  private static IdleConnectionMonitorThread scanThread;
  
  
  private HCUtil(){
	  init();
  }
  
  private static final class HCHolder{
	  private static HCUtil instance = new HCUtil();
  }
  
  public static HCUtil getInstance(){
	  return HCHolder.instance;
  }

  private void init() {
    try {
    	
      SSLContext sslContext = SSLContexts.createSystemDefault();
      
      Registry<ConnectionSocketFactory> socketFactoryRegistry = 
      		RegistryBuilder.<ConnectionSocketFactory>create()
      		.register("http",PlainConnectionSocketFactory.INSTANCE)
      		.register("https",new SSLConnectionSocketFactory(sslContext)).build();
      PoolingHttpClientConnectionManager poolManager = new PoolingHttpClientConnectionManager(
          socketFactoryRegistry);
      
      hc = HttpClients.custom().setConnectionManager(poolManager).disableCookieManagement().build();
      
      // Create socket configuration
      SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
      poolManager.setDefaultSocketConfig(socketConfig);
      
      
      MessageConstraints messageConstraints = MessageConstraints
      		.custom()
      		.setMaxHeaderCount(200)
          .setMaxLineLength(2000)
          .build();
      
      // Create connection configuration
      ConnectionConfig connectionConfig = ConnectionConfig
      		.custom()
      		.setMalformedInputAction(CodingErrorAction.IGNORE)
      		.setUnmappableInputAction(CodingErrorAction.IGNORE)
      		.setCharset(Consts.UTF_8)
      		.setMessageConstraints(messageConstraints).build();
      
      poolManager.setDefaultConnectionConfig(connectionConfig);
      poolManager.setMaxTotal(maxTotal);
      poolManager.setDefaultMaxPerRoute(maxPerRoute);
      // 扫描线程，关闭无效链接
      scanThread = new IdleConnectionMonitorThread(poolManager);
      scanThread.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 关闭连接池.
   */
  public void close() {
    if (hc != null) {
      try {
        hc.close();
      } catch (IOException ignored) {
      }
    }
    if (scanThread != null && scanThread.isAlive()) {
      scanThread.shutdown();
    }
  }


  private static HttpPost constructPost(String url, List<NameValuePair> paramList,
      int socketTimeout, int connectionRequestTimeout) {
    HttpPost post = new HttpPost(url);
    
    post.setEntity(new UrlEncodedFormEntity(paramList, Consts.UTF_8));

    post.setConfig(
        RequestConfig
            .custom()
            .setSocketTimeout(socketTimeout)
            .setConnectTimeout(connectTimeout)
            .setConnectionRequestTimeout(connectionRequestTimeout)
            .setExpectContinueEnabled(false)
            .build()
    );

    return post;
  }

  private static String executePost(HttpPost post, String encoding) throws IOException {
    CloseableHttpResponse response = null;
    HttpEntity entity = null;
    try {

      response = hc.execute(post);
      entity = response.getEntity();
      if (entity != null) {
        String str = EntityUtils.toString(entity, encoding);
        return str;
      }
    } finally {
      if (entity != null) {
        entity.getContent().close();
      }
      if (response != null) {
        response.close();
      }
      post.releaseConnection();
    }
    return null;
  }

  private static String httpPost(String url, int socketTimeout, int connectionRequestTimeout,
      List<NameValuePair> paramList, Header header, String encoding) throws IOException {
    HttpPost post = constructPost(url, paramList, socketTimeout, connectionRequestTimeout);
    if (null != header) {
      post.setHeader(header);
    }
    return executePost(post, encoding);
  }




  public static String post(String url, Map<String, String> paramMap)
      throws IOException {
    return post(url, paramMap, Consts.UTF_8.name(), null);
  }

  public static String post(String url, Map<String, String> paramMap, String charset,
      Header oHeader) throws IOException {
    Header header = null;
    if (null != oHeader) {
      header = new BasicHeader(oHeader.getName(), oHeader.getValue());
    }
    List<NameValuePair> params = new ArrayList<>();
    if (null != paramMap) {
      for (Map.Entry<String, String> entry : paramMap.entrySet()) {
        if (StringUtils.isNotEmpty(entry.getKey())) {
          params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
      }
    }
    return httpPost(url, socketTimeout, connectionRequestTimeout, params, header, charset);
  }

 

  
  private static String httpGet(String url, List<NameValuePair> paramList,
      int socketTimeout, int connectionRequestTimeout, String encode) throws IOException {
    String responseString = null;
    CloseableHttpResponse response = null;
    HttpEntity entity = null;
    RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout)
        .setConnectTimeout(connectTimeout).setConnectionRequestTimeout(connectionRequestTimeout)
        .build();
    StringBuilder sb = new StringBuilder();
    sb.append(url);
    int i = 0;
    for (NameValuePair nameValuePair : paramList) {
      if (i == 0 && !url.contains("?")) {
        sb.append("?");
      } else {
        sb.append("&");
      }
      sb.append(nameValuePair.getName());
      sb.append("=");
      String value = nameValuePair.getValue();
      try {
        sb.append(URLEncoder.encode(value, "UTF-8"));
      } catch (UnsupportedEncodingException e) {
        logger.warn("encode http get params error, value is " + value, e);
      }
      i++;
    }

    logger.info("invokeGet begin invoke:" + sb.toString());
    HttpGet get = new HttpGet(sb.toString());
    get.setConfig(requestConfig);
    try {
      response = hc.execute(get);
      entity = response.getEntity();
      if (entity != null) {
        responseString = EntityUtils.toString(entity, encode);
        return responseString;
      }
    } finally {
      if (entity != null) {
        entity.getContent().close();
      }
      if (response != null) {
        response.close();
      }
      get.releaseConnection();
    }
    return responseString;
  }


  public String getResponse(String url){
  	return getResponse(url, null, Consts.UTF_8.name());
  }

  public String getResponse(String url, NameValuePair[] arrayParams, String charsets) {
    List<NameValuePair> params = new ArrayList<>();
    if (null != arrayParams) {
      Collections.addAll(params, arrayParams);
    }
    
    try {
    	return httpGet(url, params, socketTimeout, connectionRequestTimeout, charsets);
		} catch (IOException e) {
			logger.error("getResponse:{},error:{}",url,e);
		}
    return null;
  }

  static class IdleConnectionMonitorThread extends Thread {
    private final HttpClientConnectionManager connMgr;
    private volatile boolean shutdown;

    IdleConnectionMonitorThread(HttpClientConnectionManager connMgr) {
      super();
      this.connMgr = connMgr;
    }

    @Override
    public void run() {
      while (!shutdown) {
        synchronized (scanThread) {
          try {
            // 关闭无效连接
            connMgr.closeExpiredConnections();
            // 可选, 关闭空闲超过30秒的
            connMgr.closeIdleConnections(30, TimeUnit.SECONDS);
            scanThread.wait(5000);
          } catch (Exception e) {
            logger.error("IdleConnectionMonitorThread", e);
          }
        }
      }
    }

    void shutdown() {
      synchronized (scanThread) {
        shutdown = true;
        scanThread.notifyAll();
      }
    }
  }
}
