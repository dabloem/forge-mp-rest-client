package org.jboss.forge.addon.oauth.impl;

import java.io.IOException;
import java.util.Optional;

import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.StatusType;
import javax.ws.rs.ext.Provider;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;

import io.opencensus.trace.Status;

// import org.eclipse.microprofile.config.Config;
// import org.eclipse.microprofile.config.ConfigProvider;

/**
 * OAuthProvider
 */
@Provider
public class OAuth2Filter implements ClientRequestFilter {

    private static final String OAUTH = "security.oauth2";
    protected static final String TOKEN = OAUTH + ".token";

    private String iss;

    public OAuth2Filter() {

    }

    public OAuth2Filter(String iss) {
        this.iss = iss;
    }

    @Override
    public void filter(ClientRequestContext ctx) throws IOException {
        // Config config = ConfigProvider.getConfig();
        // CredentialHolder credentialHolder = CDI.current().select(CredentialHolder.class).get();
        // credentialHolder.getCredential().ifPresent((t) -> {
        //     try {
        //         Long expiresIn = t.getExpiresInSeconds();
        //         if (expiresIn != null  && expiresIn < 0) {
        //             System.out.println("url:"+ t.getTokenServerEncodedUrl());
        //             ctx.abortWith(Response.status(401).header("error_msg", "Token expired").build());
        //         }
        //     } catch (Exception ex) {

        //     }
        // });

        TokenHolder holder = CDI.current().select(TokenHolder.class).get();

        Optional<String> token =  Optional.ofNullable(holder.get(iss));
        // Optional<String> token =  Optional.ofNullable(System.getProperty(OAUTH + "." + iss + ".token")); //config.getOptionalValue(getKey(TOKEN, Optional.of(iss)), String.class);        
        if (token.isPresent()) {
            ctx.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + token.get());
        }
    }

    /**
     * Merges the specific iss into the key.
     * @param key
     * @param iss
     * @return
     */
    static String getKey(String key, Optional<String> iss) {
        if (iss.isPresent()) {
            key = key.replaceFirst("security\\.oauth2", OAUTH + "." + iss.get());
        }
        return key;
    }
    
}