package com.xy124.auth.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.*;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("deprecation")
public class DefaultPasswordEncoderFactories {

    private static class LazyHolder {
        public static final DefaultPasswordEncoderFactories INSTANCE = new DefaultPasswordEncoderFactories();
    }
    public static DefaultPasswordEncoderFactories getInstance() {
        return LazyHolder.INSTANCE;
    }

    private DefaultPasswordEncoderFactories() {
    }

    public DelegatingPasswordEncoder createDelegatingPasswordEncoder() {
        final String encodingId = "SHA-256";
        final Map<String, PasswordEncoder> encoders = new HashMap<>();

        encoders.put("ldap", new LdapShaPasswordEncoder());
        encoders.put("MD4", new Md4PasswordEncoder());
        encoders.put("MD5", new MessageDigestPasswordEncoder("MD5"));
        encoders.put("noop", NoOpPasswordEncoder.getInstance());
        encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
        encoders.put("scrypt", new SCryptPasswordEncoder());
        encoders.put("SHA-1", new MessageDigestPasswordEncoder("SHA-1"));
        encoders.put("SHA-256", new MessageDigestPasswordEncoder("SHA-256"));
        encoders.put("bcrypt", new BCryptPasswordEncoder());

        return new DelegatingPasswordEncoder(encodingId, encoders);
    }

}
