package org.jboss.forge.addon.oauth.impl;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;
import org.eclipse.microprofile.rest.client.spi.RestClientListener;
import org.jboss.forge.addon.oauth.api.OAuth;

/**
 * OAuthProvider
 */
public class OAuth2Listener implements RestClientListener {

    @Override
    public void onNewClient(Class<?> serviceInterface, RestClientBuilder builder) {
        OAuth oauth = serviceInterface.getAnnotation(OAuth.class);
        if (oauth != null) {
            builder.register(new OAuth2Filter(oauth.iss()));
            builder.register(new ResponseExceptionMapper() {
                @Override
                public Throwable toThrowable(Response response) {
                    if (response.getStatus() == 401) {
                        return new WebApplicationException("Please login first. (login --iss " + oauth.iss() + ")", 401);
                    }
                    return null;
                }
            });
        }
    }

    
}