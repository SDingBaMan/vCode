package com.sdingba.vcode.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestUtil.class);
    private static PoolingHttpClientConnectionManager CLIENT_POOL = new PoolingHttpClientConnectionManager();

    // Increase max total connection to 200
    static {
        CLIENT_POOL.setMaxTotal(200);
        // Increase default max connection per route to 20
        CLIENT_POOL.setDefaultMaxPerRoute(20);
    }

    private static CloseableHttpClient getHttpClient() {
        DefaultHttpRequestRetryHandler dhr = new DefaultHttpRequestRetryHandler(3, true);
        return HttpClients.custom().setConnectionManager(CLIENT_POOL).setRetryHandler(dhr).build();

    }

    public static String post(String url, List<NameValuePair> params) {
        CloseableHttpClient httpClient = getHttpClient();
        String responseText = "";
        CloseableHttpResponse httpResponse = null;

        try {
            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
            httpPost.setConfig(requestConfig);
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

            httpResponse = httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                responseText = EntityUtils.toString(httpResponse.getEntity());
            }
        } catch (Exception e) {
            LOGGER.error("http_err", e);
        } finally {
            try {
                if (httpResponse != null) {
                    httpResponse.close();
                }
            } catch (Exception e) {
                LOGGER.error("http_response_err", e);
            }
        }
        return responseText;
    }

    public static String post(String url, Map<String, String> params) {
        String responseText = "";
        CloseableHttpResponse httpResponse;
        httpResponse = postResponse(url, params);
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            try {
                responseText = EntityUtils.toString(httpResponse.getEntity());
            } catch (IOException e) {
                LOGGER.error("io_err", e);
            }
        } else {
            responseText = "bad_request.statsCode=" + httpResponse.getStatusLine().getStatusCode();
        }
        return responseText;
    }

    public static CloseableHttpResponse postXml(String url, String xmlStr) {
        CloseableHttpResponse httpResponse = null;
        CloseableHttpClient httpClient = getHttpClient();
        try {
            URIBuilder builder = new URIBuilder(url);
            HttpPost httpPost = new HttpPost(builder.toString());
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
            httpPost.setConfig(requestConfig);
            httpPost.setHeader("Content-Type", "text/xml");
            httpPost.setHeader("charset", "utf-8");
            httpPost.setEntity(new StringEntity(xmlStr, "utf-8"));
            httpResponse = httpClient.execute(httpPost);
        } catch (URISyntaxException e) {
            LOGGER.error("uri_err", e);
        } catch (ClientProtocolException e) {
            LOGGER.error("http_client_err", e);
        } catch (IOException e) {
            LOGGER.error("io_err", e);
        }
        return httpResponse;
    }

    public static CloseableHttpResponse postResponse(String url, Map<String, String> params) {
        CloseableHttpResponse httpResponse = null;
        CloseableHttpClient httpClient = getHttpClient();
        try {
            URIBuilder builder = new URIBuilder(url);
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.addParameter(entry.getKey(), entry.getValue());
            }
            HttpPost httpPost = new HttpPost(builder.toString());
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
            httpPost.setConfig(requestConfig);

            httpResponse = httpClient.execute(httpPost);
        } catch (URISyntaxException e) {
            LOGGER.error("uri_err", e);
        } catch (ClientProtocolException e) {
            LOGGER.error("http_client_err", e);
        } catch (IOException e) {
            LOGGER.error("io_err", e);
        }
        return httpResponse;
    }

    public static HttpResponse getResponse(String url, Map<String, String> paramMap) {

        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = getHttpResponse(url, paramMap);
        } finally {
            try {
                if (httpResponse != null) {
                    httpResponse.close();
                }
            } catch (Exception e) {
                LOGGER.error("http_response_err", e);
            }
        }
        return httpResponse;
    }

    public static byte[] getBytes(String url, Map<String, String> paramMap) throws Exception {

        CloseableHttpResponse response = getHttpResponse(url, paramMap);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        InputStream inStream = response.getEntity().getContent();
        try {

            // 创建一个Buffer字符串
            byte[] buffer = new byte[1024];
            // 每次读取的字符串长度，如果为-1，代表全部读取完毕
            int len;
            // 使用一个输入流从buffer里把数据读取出来
            while ((len = inStream.read(buffer)) != -1) {
                // 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
                outStream.write(buffer, 0, len);
            }
            // 关闭输入流
        } finally {
            response.close();
            inStream.close();
        }
        // 把outStream里的数据写入内存

        return outStream.toByteArray();
    }

    private static CloseableHttpResponse getHttpResponse(String url, Map<String, String> paramMap) {
        CloseableHttpClient httpClient = getHttpClient();
        CloseableHttpResponse httpResponse = null;
        try {
            URIBuilder builder = new URIBuilder(url);
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                builder.addParameter(entry.getKey(), entry.getValue());
            }
            HttpGet httpGet = new HttpGet(builder.toString());
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
            httpGet.setConfig(requestConfig);
            httpResponse = httpClient.execute(httpGet);
        } catch (Exception e) {
            LOGGER.error("http_err", e);
        }
        return httpResponse;
    }

    public static String get(String url, Map<String, String> paramMap) {

        CloseableHttpClient httpClient = getHttpClient();
        String responseText = "";
        CloseableHttpResponse httpResponse = null;
        try {
            URIBuilder builder = new URIBuilder(url);
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                builder.addParameter(entry.getKey(), entry.getValue());
            }
            HttpGet httpGet = new HttpGet(builder.toString());
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
            httpGet.setConfig(requestConfig);
            httpResponse = httpClient.execute(httpGet);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                responseText = EntityUtils.toString(httpResponse.getEntity());
            } else {
                responseText = "bad_request.statusCode=" + statusCode;
            }
        } catch (Exception e) {
            LOGGER.error("http_err", e);
        } finally {
            try {
                if (httpResponse != null) {
                    httpResponse.close();
                }
            } catch (Exception e) {
                LOGGER.error("http_response_err", e);
            }
        }
        return responseText;
    }
}
