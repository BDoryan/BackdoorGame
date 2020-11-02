package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import javax.swing.JOptionPane;

public class Lang {
  public static HashMap<String, String> messages = new HashMap<>();
  
  public InputStream inputStream;
  
  public String name;
  
  public Lang(String name, InputStream inputStream) {
    this.name = name;
    this.inputStream = inputStream;
  }
  
  public String getName() {
    return this.name;
  }
  
  public boolean read() {
    messages.clear();
    BufferedReader read = new BufferedReader(new InputStreamReader(this.inputStream, Charset.forName("UTF-8")));
    try {
      String line = null;
      while ((line = read.readLine()) != null) {
        if (!line.startsWith("#") && 
          line.contains("=")) {
          String[] args = line.split("=");
          if (args.length == 2) {
            String key = args[0];
            String value = args[1];
            messages.put(key, (value == "null") ? null : value);
            continue;
          } 
          System.err.println("[Lang] [" + this.name + "] " + "args.length != 2 [line=" + line + "]");
        } 
      } 
      read.close();
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(null, "The language file [" + this.name + "] could not be read!", "Fatal error", 0);
      return false;
    } 
  }
  
  public static String translate(String message, String... replace) {
    String startWith = "lang:";
    if (message.startsWith(startWith))
      return get(message.substring(startWith.length()), replace); 
    return message;
  }
  
  public static String get(String key, String... replace) {
    String message = key;
    if (messages.containsKey(key)) {
      message = messages.get(key);
      if (replace != null && replace.length % 2 == 0) {
        int i = 0;
        while (i < replace.length) {
          message = message.replace(replace[i], replace[i + 1]);
          i += 2;
        } 
      } 
    } 
    return message;
  }
}
