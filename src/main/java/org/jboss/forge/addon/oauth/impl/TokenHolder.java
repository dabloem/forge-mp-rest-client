package org.jboss.forge.addon.oauth.impl;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

/**
 * TokenHolder
 */
@ApplicationScoped
public class TokenHolder {

    private Map<String, String> map = new HashMap<>();

    public void put(String key, String value) {
        map.put(key, value);
    }

    public String get(String key){
        return map.get(key);
    }
    
}