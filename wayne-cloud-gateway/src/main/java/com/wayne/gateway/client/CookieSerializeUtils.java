package com.wayne.gateway.client;

import org.springframework.http.HttpCookie;
import org.springframework.util.SerializationUtils;

import java.util.Base64;

public final class CookieSerializeUtils {

  /**
   * @param object
   * @return
   */
  public static String serialize(Object object) {
    return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(object));
  }

  /**
   * @param cookie
   * @param cls
   * @param <T>
   * @return
   */
  public static <T> T deserialize(HttpCookie cookie, Class<T> cls) {
    return cls.cast(
        SerializationUtils.deserialize(Base64.getUrlDecoder().decode(cookie.getValue())));
  }
}
