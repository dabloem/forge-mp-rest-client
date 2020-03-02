package org.jboss.forge.addon;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Optional;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

/**
 * BasicProvider
 */
@Provider
public class BasicProvider implements ClientRequestFilter {

    private static final String USERNAME = "security.basic.user";
    private static final String PASSWORD = "security.basic.pwd";

	@Override
	public void filter(ClientRequestContext ctx) throws IOException {
        Config config = ConfigProvider.getConfig();

        Optional<String> iss = config.getOptionalValue("security.basic.iss", String.class);

        Optional<String> username = config.getOptionalValue(getKey(USERNAME, iss), String.class);
        Optional<String> password = config.getOptionalValue(getKey(PASSWORD, iss), String.class);

        if (username.isPresent() && password.isPresent()) {
            String auth = username.get() + ":" + password.get();
            byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(Charset.defaultCharset()));
            String authHeader = "Basic " + new String( encodedAuth );
            ctx.getHeaders().add(HttpHeaders.AUTHORIZATION, authHeader);
        }		
		
    }
    
    String getKey(String key, Optional<String> iss) {
        if (iss.isPresent()) {
            key = key.replaceFirst("security\\.basic", "security.basic." + iss.get());
        }
        return key;
    }
        
}