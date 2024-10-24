import java.util.*;

public final class ContactApp {
  public static final String[] boolean_options = {"yes", "no"};
  private final Map<String, Person> contacts = new HashMap<>();
  private ContactApp() {}

  public static void main(String[] args) {
    new ContactApp().loopMainMenu();
  }

  void loopMainMenu() {

    final String[] mainOptions = {"1", "2", "3", "4", "5", "6"};
    String choice = "";
    do {
      if (contacts.isEmpty()) {
        loopAddContact(true);
      } else {
        String sb = "=== [ MainMenu ] ===\n" +
            String.format(" %3d Contacts right here.\n", contacts.size()) +
            "[1] Add Contact " +
            "[2] List " +
            "[3] Search by name " +
            "[4] Search by contact " +
            "[5] Pick random " +
            "[6] Quit";
        System.out.println(sb);
        choice = Prompt.choose("Select menu", mainOptions);
        switch (choice) {
          case "1" -> loopAddContact(false);
          case "2" -> loopList();
          case "3" -> loopNameSearch();
          case "4" -> loopContactSearch();
          case "5" -> loopPick();
          default -> {}
        }
      }
    } while (!choice.equals("6"));

  }

  void loopAddContact(boolean useMain) {
    System.out.println("\n\n=== [ Add Contact ] ===");
    if (useMain) System.out.println("** You have no people in your Contacts. Let's get started from adding one.");
    String name = Prompt.require("Enter the name of person.");
    String contact = Prompt.require("Enter the contact.");
    String contactType = Prompt.get("Contact description", "phone");
    // person already exists
    if (contacts.containsKey(name)) {
      Person person = contacts.get(name);
      String contactValueOrNull = person.getContactOf(contactType);
      // person already exists, and the contact also overlaps
      if (contactValueOrNull != null) {
        // person, contact and contact value is all same
        if (contactValueOrNull.equals(contact)) {
          System.out.printf("%s's \"%s\" is already set to %s. Come back again!", name, contactType, contactValueOrNull);
          return;
        }
        String body = String.format("%s's \"%s\" is already set to %s. Would you replace it? (yes/no) ", name, contactType, contactValueOrNull);
        String choice = Prompt.choose(body, "no", boolean_options);
        if (choice.equals("yes")) person.updateContact(contactType, contact);

      } else {
        person.updateContact(contactType, contact);
      }
    } else {
      contacts.put(name, new Person(name, contactType, contact));
    }
  }

  void loopList(final Map<String, Person> v) {
    while (true) {
      StringBuilder sb = new StringBuilder();
      sb.append("\n=== [ Contact List ] ===\n");
      for (Person person : v.values()) {
        sb.append(person).append("\n");
      }
      System.out.println(sb);
      String choice = Prompt.choose(
          "Type the name of person to select, Leave blank to go back.",
          "", v.keySet());
      if (choice.isEmpty()) return;
      select(v.get(choice));
    }
  }

  void loopList() {
    loopList(contacts);
  }

  void loopNameSearch() {
    System.out.println("\n=== [ Search by name ] ===");
    System.out.printf("%d Contacts out there.\n", contacts.size());
    System.out.println("Input name to find, leave blank to go back.");
    String q = Prompt.get("");

    if (q.isEmpty()) return;

    Map<String, Person> result = searchByName(q);
    if (result.isEmpty()) {
      System.out.printf("No contacts found by name contains \"%s\"\n", q);
      return;
    }
    System.out.printf("%d contact(s) found.\n", result.size());
    loopList(result);
  }

  void loopContactSearch() {
    while (true) {
      System.out.println("\n=== [ Search by contact ] ===");
      System.out.printf("%d Contacts out there.\n", contacts.size());
      System.out.println("Input contact \"value\" to find, leave blank to go back.");
      String q = Prompt.get("");

      if (q.isEmpty()) return;

      List<ContactSearchResult> result = searchByContactValue(q);
      if (result.isEmpty()) {
        System.out.printf("No contacts found by \"%s\"\n", q);
        continue;
      }
      System.out.printf("%d contact(s) found.\n", result.size());
      for (ContactSearchResult p : result) {
        System.out.println(p);
      }
    }
  }

  void loopPick() {
    System.out.println("\n=== [ Pick random ] ===\n");
    Random random = new Random();

    String input;
    do {
      int i = random.nextInt(contacts.size());
      Person pick = contacts.values().stream().toList().get(i);
      System.out.println(pick);
      input = Prompt.choose("Another one? (yes/no) ", "yes", boolean_options);
    } while (input.equals("yes"));

  }

  final String[] selectScreenOption = { "1", "2", "3", "4" };
  void select(Person person) {
    boolean useBreak = false;
    while (!useBreak) {
      System.out.println("\n=== [ Person Info ] ===\n" + person.verbose() + "\n"
        + "[ 1 ] Change name "
        + "[ 2 ] Change Contact by "
        + "[ 3 ] Delete one of Contact "
        + "[ 4 ] Delete Person info ");
      String choice = Prompt.choose("Select menu, leave blank to go back", "", selectScreenOption);
      if (choice.isEmpty()) return;
      switch (choice) {
        case "1" -> changeNameScreen(person);
        case "2" -> updateContactScreen(person);
        case "3" -> deleteContact(person);
        case "4" -> useBreak = deletePerson(person);
      }
    }
  }

  void changeNameScreen(Person person) {
    System.out.println("=== [ Change name ] ===");
    System.out.printf("Current Person name : %s\n", person.getName());
    System.out.println("Type new name for current person. Leaving blank would have on effect.");
    String input = Prompt.get("");
    if (!input.isEmpty()) {
      final String name = person.getName();
      contacts.put(input, contacts.remove(name));
      person.setName(input);
    }
  }

  void updateContactScreen(Person person) {
    System.out.println("=== [ Update Contact ] ===");
    String contactType = Prompt.choose(
      "Type the name of \"contact type\" (e.g. \"phone\"), leave blank to go back.",
      "", person.getContactTypes()
    );
    if (contactType.isEmpty()) return;

    System.out.println("Type the new contact. leave blank to cancel.");
    String newContact = Prompt.get("");
    if (!newContact.isEmpty()) {
      person.updateContact(contactType, newContact);
      System.out.println("Contact updated!");
    }
  }

  void deleteContact(Person person) {
    Set<String> ct = person.getContactTypes();
    System.out.println("=== [ Change Contact ] ===");
    String contactType = Prompt.choose(
      "Type the name of \"contact type\" (e.g. \"phone\"), leave blank to go back.",
      "", ct
    );
    if (contactType.isEmpty()) return;
    String body = String.format(
      "Are you sure to remove %s:%s from %s's profile? (yes/no)",
      contactType, person.getContactOf(contactType), person.getName()
    );
    String choice = Prompt.choose(body, "no", boolean_options);
    if (choice.equals("yes")) {
      person.deleteContact(contactType);
    }
  }

  boolean deletePerson(Person person) {
    String body = String.format("Are you sure to remove %s from contact list? (yes/no)", person.getName());
    String choice = Prompt.choose(body, "no", boolean_options);
    if (choice.equals("yes")) {
      contacts.remove(person.getName());
      return true;
    }
    return false;
  }

  Map<String, Person> searchByName(String q) {
    Map<String, Person> found = new HashMap<>();
    for (Map.Entry<String, Person> entry : contacts.entrySet()) {
      if (entry.getKey().contains(q)) {
        found.put(entry.getKey(), entry.getValue());
      }
    }
    return found;
  }

  List<ContactSearchResult> searchByContactValue(String q) {
    ArrayList<ContactSearchResult> found = new ArrayList<>();
    for (Map.Entry<String, Person> entry : contacts.entrySet()) {
      Set<ContactSearchResult> m = entry.getValue().findContact(q);
      found.addAll(m);
    }
    return found;
  }

}
