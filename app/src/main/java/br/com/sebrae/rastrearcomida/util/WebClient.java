package br.com.sebrae.rastrearcomida.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by Israel on 05/05/2015.
 */
public class WebClient {

    private String url;

    public WebClient(String url){
        this.url = url;
    }

    public String post(String json){
        try {
            HttpPost post = new HttpPost(url);
            post.setEntity(new StringEntity(json));
            post.setHeader("Content-type", "application/json");
            post.setHeader("Accept", "application/json");

            HttpClient client = new DefaultHttpClient();
            HttpResponse resposta = client.execute(post);

            return EntityUtils.toString(resposta.getEntity());
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
}
