package org.jboss.forge.addon;

import java.net.ProxySelector;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.spi.RestClientListener;
import org.jboss.forge.addon.configuration.Configuration;

/**
 * ProxyListener
 */
public class ProxyProvider implements RestClientListener {

    private Logger logger = Logger.getLogger(ProxyProvider.class.getName());

    @Inject
    Configuration conf;

	@Override
	public void onNewClient(Class<?> serviceInterface, RestClientBuilder builder) {
        Config config = ConfigProvider.getConfig();
        String proxyHost = config.getOptionalValue("proxy.host", String.class).orElse(null);
        Integer proxyPort = config.getOptionalValue("proxy.port", Integer.class).orElse(null);
        

        if (proxyHost != null && proxyPort != null) {
            builder.property("org.jboss.resteasy.jaxrs.client.proxy.host", proxyHost);
            builder.property("org.jboss.resteasy.jaxrs.client.proxy.port", proxyPort);
            logger.info("proxy set for " + serviceInterface.getSimpleName());
        } else {
            logger.info("proxy not set for " + serviceInterface.getSimpleName());
        }
	}

    
}