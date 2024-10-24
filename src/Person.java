import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Person {
  private String name;
  private final Map<String, String> contacts;
  private final LocalDateTime added;

  public Person(String name, String contactType, String contact) {
    this.name = name;
    this.contacts = new HashMap<>();
    this.contacts.put(contactType, contact);
    this.added = LocalDateTime.now();
  }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public String getContactOf(String contactType) {
    return contacts.getOrDefault(contactType, null);
  }

  public void updateContact(String type, String value) {
    contacts.put(type, value);
  }

  public void deleteContact(String type) {
    contacts.remove(type);
  }

  public int contacts() {
    return contacts.size();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("[ " + name + " ]");
    int unresolvedContactCount = contacts.size();
    if (contacts.containsKey("email")) {
      sb.append(String.format(" <%s>", contacts.get("email")));
      unresolvedContactCount -= 1;
    }
    if (contacts.containsKey("phone")) {
      sb.append(String.format(" <phone:%s>", contacts.get("phone")));
      unresolvedContactCount -= 1;
    }
    for (Map.Entry<String, String> entry : contacts.entrySet()) {
      if (sb.length() > 45) break;
      String key = entry.getKey();
      if (key.equals("phone") || key.equals("email")) continue;
      sb.append(String.format(" <%s:%s>", key, entry.getValue()));
    }
    if (unresolvedContactCount > 0) sb.append(String.format(" (+%d more)", unresolvedContactCount));
    return sb.toString();
  }

  public String verbose() {
    StringBuilder builder = new StringBuilder();
    builder.append(String.format("<< %s >>\n", name));
    builder.append(String.format("Added at %s\n", DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(added)));
    for (Map.Entry<String, String> entry : contacts.entrySet()) {
      builder.append(String.format("%8s : %s \n", entry.getKey(), entry.getValue()));
    }
    return builder.toString();
  }

  public Set<String> getContactTypes() { return contacts.keySet(); }

  public Set<ContactSearchResult> findContact(String find) {
    Set<ContactSearchResult> found = new HashSet<>();
    for (Map.Entry<String, String> entry : contacts.entrySet()) {
      if (entry.getValue().contains(find)) {
        found.add(new ContactSearchResult(this, name, entry.getKey(), entry.getValue()));
      }
    }
    return found;
  }

}
