package org.jboss.forge.addon.oauth.impl;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;

import com.google.api.client.auth.oauth2.Credential;

/**
 * CredentialHolder
 */
@ApplicationScoped
public class CredentialHolder {

    private Credential credential = null;

    public Optional<Credential> getCredential() {
        return Optional.ofNullable(credential);
    }

	public void setCredential(Credential cred) {
        this.credential = cred;
	}
    
}