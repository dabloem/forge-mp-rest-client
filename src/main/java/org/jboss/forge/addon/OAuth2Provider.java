package org.jboss.forge.addon;

import java.io.IOException;
import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

/**
 * OAuthProvider
 */
@Provider
public class OAuth2Provider implements ClientRequestFilter {

    private static final String OAUTH = "security.oauth2";
    protected static final String TOKEN = OAUTH + ".token";

    @Override
    public void filter(ClientRequestContext ctx) throws IOException {
        Config config = ConfigProvider.getConfig();
        
        Optional<String> iss = config.getOptionalValue("security.oauth2.iss", String.class);
        
        Optional<String> token = config.getOptionalValue(getKey(TOKEN, iss), String.class);
        if (token.isPresent()) {
            // System.out.println("Bearer " + token.get());
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