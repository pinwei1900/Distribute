/*
 * Copyright (c) 2018年11月19日 by XuanWu Wireless Technology Co.Ltd.
 *             All rights reserved
 */
package robin.utils;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;


/**
 * @Description
 * @Author <a href="mailto:haosonglin@wxchina.com">songlin.Hao</a>
 * @Date 2018/11/19
 * @Version 1.0.0
 */
public class HttpUtils {

    public static String sendGet(String url, Map<String, String> parameters) throws IOException {
        HttpGet httpGet = new HttpGet(getParamUrl(url, parameters));
        try (CloseableHttpClient httpclient = HttpClientBuilder.create().build();
                CloseableHttpResponse response = httpclient.execute(httpGet)) {
            return EntityUtils.toString(response.getEntity());
        }
    }

    public static String sendPost(String url, Map<String, Object> parameters) throws IOException {
        HttpPost httppost = new HttpPost(url);
        HttpEntity httpEntity = getMultipartEntity(parameters);
        httppost.setEntity(httpEntity);
        try (CloseableHttpClient httpclient = HttpClientBuilder.create().build();
                CloseableHttpResponse response = httpclient.execute(httppost)) {
            return EntityUtils.toString(response.getEntity());
        }
    }

    private static HttpEntity getMultipartEntity(Map<String, Object> parameters) {
        MultipartEntityBuilder paramsBuilder = MultipartEntityBuilder.create();
        if (parameters != null) {
            for (Entry<String, Object> entry : parameters.entrySet()) {
                Object value = entry.getValue();
                if (value instanceof String) {
                    paramsBuilder.addTextBody(entry.getKey(), (String) value);
                } else if (value instanceof File) {
                    paramsBuilder.addBinaryBody(entry.getKey(), (File) value);
                }
            }
        }
        return paramsBuilder.build();
    }


    private static String getParamUrl(String url, Map<String, String> parameters) {
        StringBuilder pstr = new StringBuilder();
        for (Entry<String, String> p : parameters.entrySet()) {
            pstr.append("&").append(p.getKey() + "=" + p.getValue());
        }
        if (pstr.length() > 0) {
            pstr.replace(0, 1, "?");
        }
        return url + pstr.toString();
    }
}
