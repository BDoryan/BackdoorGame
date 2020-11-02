package test;

public class Toolkit {
  public static boolean isInteger(String value) {
    try {
      Integer.valueOf(value);
      return true;
    } catch (Exception exception) {
      return false;
    } 
  }
  
  public static boolean isDouble(String value) {
    try {
      Double.valueOf(value);
      return true;
    } catch (Exception exception) {
      return false;
    } 
  }
  
  public static boolean isFloat(String value) {
    try {
      Float.valueOf(value);
      return true;
    } catch (Exception exception) {
      return false;
    } 
  }
  
  public static boolean isLong(String value) {
    try {
      Long.valueOf(value);
      return true;
    } catch (Exception exception) {
      return false;
    } 
  }
  
  public static boolean isShort(String value) {
    try {
      Short.valueOf(value);
      return true;
    } catch (Exception exception) {
      return false;
    } 
  }
  
  public static boolean isBoolean(String value) {
    try {
      Boolean.valueOf(value);
      return true;
    } catch (Exception exception) {
      return false;
    } 
  }
  
  public static Double convertToDouble(String value) {
    try {
      return Double.valueOf(value);
    } catch (NumberFormatException numberFormatException) {
      return null;
    } 
  }
}
