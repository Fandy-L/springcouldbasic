package com.to8to.tbt.msc.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import com.to8to.tbt.msc.utils.client.Content;
import com.to8to.tbt.msc.utils.client.MySocketFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.BasicClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParamBean;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EntityUtils;

/**
 * @author juntao.guo
 */
@Slf4j
public class HttpClientRawUtils {

    public static int SC_NULL = 0;
    protected String userAgentString = "Chrome/22.0.1229.79";
    protected DefaultHttpClient httpClient;
    protected int socketTimeoutMillis = 20000;
    protected int connectionTimeoutMillis = 10000;
    protected int bufferSize = 1048576;
    private MySocketFactory socketFactory;

    public void close() {
        this.httpClient.getConnectionManager().shutdown();
    }

    private byte[] entity2Bytes(HttpEntity entity) throws IOException {
        if (entity == null) {
            throw new IllegalArgumentException("HTTP entity may not be null");
        } else {
            InputStream instream = entity.getContent();
            if (instream == null) {
                return null;
            } else {
                try {
                    if (entity.getContentLength() > 2147483647L) {
                        throw new IllegalArgumentException("HTTP entity too large to be buffered in memory");
                    }

                    int total = (int)entity.getContentLength();
                    int i = total;
                    if (total < 0) {
                        i = this.bufferSize;
                    }

                    ByteArrayBuffer buffer = new ByteArrayBuffer(i);
                    byte[] tmp = new byte[this.bufferSize];
                    int t = 0;

                    int l;
                    while((l = instream.read(tmp)) != -1) {
                        buffer.append(tmp, 0, l);
                        t += l;
                        if (total > 0) {
                            float pct = (float)t / (float)i;
                            log.trace("read: " + t + "/" + i + " bytes(" + pct * 100.0F + "%)");
                        } else {
                            log.trace("read: " + t + " bytes");
                        }
                    }

                    byte[] var15 = buffer.toByteArray();
                    return var15;
                } catch (Exception var13) {
                    log.warn("entity2Bytes exception e:{}", var13);
                } finally {
                    instream.close();
                }

                return null;
            }
        }
    }

    public CookieStore getCookieStore() {
        return this.httpClient.getCookieStore();
    }

    protected int getHttpStatusCode(HttpResponse response) {
        if (response != null && response.getStatusLine() != null) {
            log.trace("http status: " + response.getStatusLine());
            return response.getStatusLine().getStatusCode();
        } else {
            return SC_NULL;
        }
    }

    public HttpClientRawUtils() {
        HttpParams params = new BasicHttpParams();
        HttpProtocolParamBean paramsBean = new HttpProtocolParamBean(params);
        params.setParameter("http.protocol.cookie-policy", "best-match");
        paramsBean.setVersion(HttpVersion.HTTP_1_1);
        paramsBean.setContentCharset("UTF-8");
        paramsBean.setUseExpectContinue(false);
        params.setParameter("http.useragent", this.userAgentString);
        params.setIntParameter("http.socket.timeout", this.socketTimeoutMillis);
        params.setIntParameter("http.connection.timeout", this.connectionTimeoutMillis);
        params.setBooleanParameter("http.protocol.handle-redirects", false);
        params.setBooleanParameter("http.protocol.reject-relative-redirect", false);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        this.socketFactory = new MySocketFactory();
        schemeRegistry.register(new Scheme("http", 80, this.socketFactory));
        BasicClientConnectionManager connMgr = new BasicClientConnectionManager(schemeRegistry);
        this.httpClient = new DefaultHttpClient(connMgr, params);
        this.httpClient.addRequestInterceptor(new HttpRequestInterceptor() {
            @Override
            public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
                if (!request.containsHeader("Accept-Encoding")) {
                    request.addHeader("Accept-Encoding", "gzip");
                }

            }
        });
        this.httpClient.addResponseInterceptor(new HttpResponseInterceptor() {
            @Override
            public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
                HttpEntity entity = response.getEntity();
                Header contentEncoding = entity.getContentEncoding();
                if (contentEncoding != null) {
                    HeaderElement[] codecs = contentEncoding.getElements();
                    HeaderElement[] var6 = codecs;
                    int var7 = codecs.length;

                    for(int var8 = 0; var8 < var7; ++var8) {
                        HeaderElement codec = var6[var8];
                        if ("gzip".equalsIgnoreCase(codec.getName())) {
                            response.setEntity(new GzipDecompressingEntity(response.getEntity()));
                            return;
                        }
                    }
                }

            }
        });
    }

    public void auth(String user, String password) {
        this.httpClient.getCredentialsProvider().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(user, password));
    }

    public boolean isText(String contentType) {
        if (contentType == null) {
            return false;
        } else if (contentType.equals(ContentType.TEXT_HTML.getMimeType())) {
            return true;
        } else if (contentType.equals(ContentType.TEXT_PLAIN.getMimeType())) {
            return true;
        } else if (contentType.equals(ContentType.TEXT_XML.getMimeType())) {
            return true;
        } else if (contentType.equals(ContentType.APPLICATION_JSON.getMimeType())) {
            return true;
        } else {
            return contentType.equals(ContentType.APPLICATION_XML.getMimeType());
        }
    }

    public Content get(String url, List<BasicHeader> headers) {
        HttpGet request = new HttpGet(url);
        Header header;
        if (headers != null) {
            Iterator var4 = headers.iterator();

            while(var4.hasNext()) {
                header = (Header)var4.next();
                request.addHeader(header);
            }
        }

        HttpResponse response = null;

        Content var6;
        try {
            response = this.httpClient.execute(request);
            if (response == null) {
                header = null;
                return null;
            }

            Content result = this.processResponse(url, response);
            var6 = result;
        } catch (ClientProtocolException var11) {
            log.warn("get exception e:{}", var11);
            return null;
        } catch (Exception var12) {
            log.warn("get exception e:{}", var12);
            return null;
        } finally {
            if (response != null) {
                EntityUtils.consumeQuietly(response.getEntity());
            }

        }

        return var6;
    }

    public Content post(String url, String stringdata) throws Exception {
        return this.post(url, (List)null, (List)null, stringdata, "UTF-8");
    }

    public Content post(String url, List<BasicHeader> headers, List<BasicNameValuePair> params, String stringdata, String paramEncode) throws Exception {
        HttpPost request = new HttpPost(url);
        BasicHeader header;
        if (headers != null) {
            Iterator var7 = headers.iterator();

            while(var7.hasNext()) {
                header = (BasicHeader)var7.next();
                request.addHeader(header);
            }
        }

        if (params != null && params.size() > 0) {
            try {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, paramEncode);
                request.setEntity(entity);
            } catch (Exception var17) {
                log.warn("post exception e:{}", var17);
                throw var17;
            }
        }

        if (StringUtils.isNotEmpty(stringdata)) {
            try {
                StringEntity entity = new StringEntity(stringdata, paramEncode);
                entity.setContentEncoding(paramEncode);
                request.setEntity(entity);
            } catch (Exception var16) {
                log.warn("post exception e:{}", var16);
                throw var16;
            }
        }

        HttpResponse response = null;
        header = null;

        Content result;
        try {
            response = this.httpClient.execute(request);
            if (response == null) {
                Object var9 = null;
                return (Content)var9;
            }

            result = this.processResponse(url, response);
        } catch (Exception var18) {
            String localAddr = this.socketFactory.getSocket() != null && this.socketFactory.getSocket().getLocalSocketAddress() != null ? this.socketFactory.getSocket().getLocalSocketAddress().toString() : null;
            if (localAddr != null) {
                log.warn("post exception e:{}", var18 + "\nLocalSocketAddress:{}", localAddr);
            } else {
                log.warn("post exception e:{}", var18);
            }

            throw var18;
        } finally {
            if (response != null) {
                EntityUtils.consumeQuietly(response.getEntity());
            }

        }

        return result;
    }

    private Content processResponse(String url, HttpResponse res) throws IOException {
        Content result = new Content();
        int statusCode = this.getHttpStatusCode(res);
        result.setStatusCode(statusCode);
        result.setHeaders(res.getAllHeaders());
        HttpEntity entity = res.getEntity();
        if (entity != null) {
            ContentType contentType = ContentType.getOrDefault(entity);
            if (contentType != null) {
                result.setContentType(contentType.getMimeType());
                if (contentType.getCharset() != null) {
                    result.setContentCharset(contentType.getCharset().toString());
                }
            }
        }

        if (entity != null) {
            log.trace("start to read socket ... ");
            byte[] bytes = this.entity2Bytes(entity);
            result.setContent(bytes);
            log.trace("finish read socket!");
        }

        return result;
    }

    public void setConnectionTimeout(int timeoutMillis) {
        this.connectionTimeoutMillis = timeoutMillis;
        this.httpClient.getParams().setIntParameter("http.connection.timeout", timeoutMillis);
    }

    public void setSocketTimeout(int timeoutMillis) {
        this.socketTimeoutMillis = timeoutMillis;
        this.httpClient.getParams().setIntParameter("http.socket.timeout", timeoutMillis);
    }

    public void setTimeout(int timeoutMillis) {
        this.socketTimeoutMillis = timeoutMillis;
        this.connectionTimeoutMillis = timeoutMillis;
        this.httpClient.getParams().setIntParameter("http.connection.timeout", timeoutMillis);
        this.httpClient.getParams().setIntParameter("http.socket.timeout", timeoutMillis);
    }

    public void setUserAgentString(String userAgentString) {
        this.userAgentString = userAgentString;
        this.httpClient.getParams().setParameter("http.useragent", userAgentString);
    }
}
