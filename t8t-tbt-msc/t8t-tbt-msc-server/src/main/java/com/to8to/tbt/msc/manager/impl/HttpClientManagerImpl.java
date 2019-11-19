package com.to8to.tbt.msc.manager.impl;

import com.to8to.common.http.DefaultWebClient;
import com.to8to.common.http.RequestBuilder;
import com.to8to.common.http.TypeReference;
import com.to8to.common.http.WebClient;
import com.to8to.tbt.msc.manager.HttpClientManager;
import org.springframework.stereotype.Component;

/**
 * @author pajero.quan
 */
@Component
public class HttpClientManagerImpl implements HttpClientManager {
    private volatile DefaultWebClient webClient;

    @Override
    public <T> T execute(String url, Class<T> clz) {
        return this.getWebClient().execute(url, clz);
    }

    @Override
    public <T> T execute(String url, TypeReference<T> reference) {
        return this.getWebClient().execute(url, reference);
    }

    @Override
    public <T> T execute(String url, Object entity, Class<T> clz) {
        return this.getWebClient().execute(url, entity, clz);
    }

    @Override
    public <T> T execute(String url, Object entity, TypeReference<T> reference) {
        return this.getWebClient().execute(url, entity, reference);
    }

    @Override
    public <T> T execute(RequestBuilder requestBuilder) {
        return this.getWebClient().execute(requestBuilder);
    }

    private synchronized WebClient getWebClient() {
        if (webClient == null) {
            webClient = new DefaultWebClient();
        }
        return webClient;
    }
}
