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

  @Override
  public String toString() {
    final int contactsSize = contacts.size();
    String usePlural = contactsSize > 1 ? "s" : "";
    return String.format("%-16s\t<%d contact%s>", name, contactsSize, usePlural);
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

  public Set<ContactSearchResult> findContact(String find) {
    Set<ContactSearchResult> found = new HashSet<>();
    for (Map.Entry<String, String> entry : contacts.entrySet()) {
      if (entry.getValue().contains(find)) {
        found.add(new ContactSearchResult(name, entry.getKey(), entry.getValue()));
      }
    }
    return found;
  }

}
