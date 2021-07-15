package com.wayne.auth.controller;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

/**
 * Create date 2020/8/16.
 *
 * @author evan
 */
@RestController
public class JwkController {

  private final KeyPair keyPair;

  public JwkController(KeyPair keyPair) {
    this.keyPair = keyPair;
  }

  /**
   * https://skryvets.com/blog/2020/04/04/configure-oauth2-spring-authorization-server-with-jwt-support/
   *
   * @return
   */
  @GetMapping(value = {"/.well-known/jwks.json"})
  @ResponseBody
  public Map<String, Object> getKey() {
    RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
    RSAKey key = new RSAKey.Builder(publicKey).build();
    return new JWKSet(key).toJSONObject();
  }
}
