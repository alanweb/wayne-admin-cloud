package com.wayne.common.msg;

public class DictValue {
  // 值
  private String value;
  // 数据字典
  private String dictKey;

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getDictKey() {
    return dictKey;
  }

  public void setDictKey(String dictKey) {
    this.dictKey = dictKey;
  }

  @Override
  public String toString() {
    return "DictValue [value=" + value + ", dictKey=" + dictKey + "]";
  }
}
