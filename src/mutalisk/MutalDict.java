package mutalisk;

import java.io.*;
import java.util.*;

public class MutalDict {
  private final Map<String, String> map = new TreeMap<>();
  private final File file;

  MutalDict(String path) {
    file = new File(path);
    try (InputStream g = new FileInputStream(file)) {
      Scanner s = new Scanner(g);
      String row;
      while (s.hasNextLine()) {
        row = s.nextLine().trim();
        String[] parts = row.split(",", 2);
        if (parts.length > 1) {
          map.put(parts[0], parts[1].replaceAll("\\\\n", "\n").replaceAll("\\\\\\\\", "\\"));
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  public void dump() {
    try (Writer out = new FileWriter(file)) {
      for (Map.Entry<String, String> entry : map.entrySet()) {
        out.write(entry.getKey());
        out.write(',');
        out.write(entry.getValue().replaceAll("\\\\", "\\\\").replaceAll("\n", "\\\\n"));
        out.write('\n');
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public String get(String key) { return map.get(key); }

  public boolean has(String key) { return map.containsKey(key); }

  public void set(String key, String value) {
    map.put(key, value);
    dump();
  }

  public String[] keys() {
    return map.keySet().toArray(new String[0]);
  }

  private String findUniqueKey() {
    String v;
    int index = 0;
    while (map.containsKey(v = String.format("NewEntry%d",  index))) {
      index += 1;
    }
    return v;
  }


  public String create() {
    String key = findUniqueKey();
    map.put(key, "");
    dump();
    return key;
  }

  public void delete(String key) {
    if (map.remove(key) != null) dump();
  }

  public void move(String fromKey, String toKey) {
    if (!map.containsKey(fromKey) || map.containsKey(toKey)) return;
    map.put(toKey, map.remove(fromKey));
    dump();
  }


  public Map.Entry<String, String> pick() {
    Map.Entry[] a = map.entrySet().toArray(new Map.Entry[0]);
    Random rand = new Random();
    return a[rand.nextInt(a.length)];
  }

}