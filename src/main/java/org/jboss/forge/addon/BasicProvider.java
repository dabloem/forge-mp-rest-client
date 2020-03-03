package org.jboss.forge.addon;

import java.io.IOException;
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

    private static final String CREDENTIAL = "security.basic.credential";

	@Override
	public void filter(ClientRequestContext ctx) throws IOException {
        Config config = ConfigProvider.getConfig();

        Optional<String> iss = config.getOptionalValue("security.basic.iss", String.class);
        Optional<String> credential = config.getOptionalValue(getKey(CREDENTIAL, iss), String.class);

        if (credential.isPresent()) {
            ctx.getHeaders().add(HttpHeaders.AUTHORIZATION, "Basic " + credential.get());
        }		
    }
    
    String getKey(String key, Optional<String> iss) {
        if (iss.isPresent()) {
            key = key.replaceFirst("security\\.basic", "security.basic." + iss.get());
        }
        return key;
    }
        
}