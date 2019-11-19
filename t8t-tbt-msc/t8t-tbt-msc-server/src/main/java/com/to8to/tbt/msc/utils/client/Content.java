package com.to8to.tbt.msc.utils.client;

import java.io.UnsupportedEncodingException;
import org.apache.http.Header;

/**
 * @author juntao.guo
 */
public class Content {

    private String contentType;
    private String contentCharset;
    private byte[] content;
    private int statusCode;
    private Header[] headers;
    private int localPort;

    public Content() {
    }

    public static boolean isNull(Content content) {
        return content == null;
    }

    public String getContentCharset() {
        return this.contentCharset;
    }

    public void setContentCharset(String contentCharset) {
        this.contentCharset = contentCharset;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Content withContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public byte[] getContent() {
        return this.content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentString() {
        return this.getContentString(this.contentCharset);
    }

    public String getContentString(String charset) {
        if (this.content == null) {
            return null;
        } else {
            try {
                return charset != null ? new String(this.content, charset) : new String(this.content);
            } catch (UnsupportedEncodingException var3) {
                var3.printStackTrace();
                return null;
            }
        }
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return this.getContentString();
    }

    public Header[] getHeaders() {
        return this.headers;
    }

    public void setHeaders(Header[] headers) {
        this.headers = headers;
    }

    public int getLocalPort() {
        return this.localPort;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }
}
