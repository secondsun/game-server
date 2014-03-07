/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.saga.sync.gameserver.portal;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.keycloak.SkeletonKeySession;
import org.keycloak.adapters.HttpClientBuilder;
import org.keycloak.util.JsonSerialization;

/**
 *
 * @author summers
 */
public class CreatePlayer {
 
    public static Player createPlayer(HttpServletRequest req) throws UnsupportedEncodingException {
    SkeletonKeySession session = (SkeletonKeySession) req.getAttribute(SkeletonKeySession.class.getName());

        HttpClient client = new HttpClientBuilder()
                .trustStore(session.getMetadata().getTruststore())
                .hostnameVerification(HttpClientBuilder.HostnameVerificationPolicy.ANY).build();
        try {
            HttpPost post = new HttpPost("https://localhost:8443/gameserver-database/player");
            post.addHeader("Authorization", "Bearer " + session.getTokenString());
            post.setEntity(new StringEntity("{}"));
            try {
                HttpResponse response = client.execute(post);
                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new RuntimeException("" + response.getStatusLine().getStatusCode());
                }
                HttpEntity entity = response.getEntity();
                InputStream is = entity.getContent();
                try {
                    return JsonSerialization.readValue(is, Player.class);
                } finally {
                    is.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } finally {
            client.getConnectionManager().shutdown();
        }
    }
    
}
