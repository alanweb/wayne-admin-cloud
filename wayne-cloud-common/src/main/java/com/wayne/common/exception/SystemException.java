package com.wayne.common.exception;


import com.wayne.common.msg.DictValue;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class SystemException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  private ErrorCode errorCode;
  private final Map<String, Object> props = new HashMap<>();
  private final Map<String, Object> contexts = new TreeMap<>();

  public static SystemException wrap(Throwable exception, ErrorCode errorCode) {
    if (exception instanceof SystemException) {
      SystemException se = (SystemException) exception;
      if (errorCode != null && errorCode != se.getErrorCode()) {
        return new SystemException(exception.getMessage(), exception, errorCode);
      }
      return se;
    } else {

      return new SystemException(exception.getMessage(), exception, errorCode);
    }
  }

  public static SystemException wrap(Throwable exception) {
    return wrap(exception, null);
  }

  public SystemException(String message) {
    this(message, ErrorCode.SERVER_ERROR);
  }

  public SystemException(String message, Throwable cause) {
    this(message, cause, ErrorCode.SERVER_ERROR);
  }

  public SystemException(String message, ErrorCode errorCode) {
    this(message, null, errorCode);
  }

  public SystemException(String message, Throwable cause, ErrorCode errorCode) {
    super(message, cause);
    this.errorCode = errorCode;
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }

  public SystemException setErrorCode(ErrorCode errorCode) {
    this.errorCode = errorCode;
    return this;
  }

  public Map<String, Object> getContexts() {
    return contexts;
  }

  public Map<String, Object> getProps() {
    return props;
  }

  public SystemException set(String name, Object value) {
    props.put(name, value);
    return this;
  }

  public SystemException set(String name, Object value, String dictKey) {
    DictValue dictValue = new DictValue();
    dictValue.setValue(String.valueOf(value));
    dictValue.setDictKey(dictKey);
    props.put(name, dictValue);
    return this;
  }

  public SystemException setNumberPrecision(String name, double value, int precision) {
    return this.setNumber(name, value, null, precision);
  }

  public SystemException setNumberScale(String name, double value, int scale) {
    return this.setNumber(name, value, scale, null);
  }

  public SystemException setNumber(String name, double value) {
    return this.setNumber(name, value, null, null);
  }

  /**
   * 设置数值（含格式化）
   *
   * @param name 变量名
   * @param value 变量值
   * @param scale 缩放倍数
   * @param precision 精度
   * @return
   */
  public SystemException setNumber(String name, double value, Integer scale, Integer precision) {
    String scaleStr = null == scale ? "" : scale.toString();
    String precisionStr = null == precision ? "" : precision.toString();
    // 设置key
    String key = String.format("$number(%s,%s)", scaleStr, precisionStr);

    return this.set(name, value, key);
  }

  public SystemException setContext(String name, Object value) {
    contexts.put(name, value);
    return this;
  }

  public SystemException setContext(Object value) {
    contexts.put(value.getClass().getSimpleName(), value);
    return this;
  }

  @Override
  public String toString() {
    return super.toString();
  }
}
