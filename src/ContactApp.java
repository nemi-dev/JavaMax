import java.io.PrintStream;
import java.util.*;

public final class ContactApp {
  private final ArrayList<Person> contacts = new ArrayList<>();

  private ContactApp() {}

  public static void main(String[] args) {
    new ContactApp().loopMainMenu();
  }

  /**
   * Try parse given string into Whole number(>=0 integer), Returns -1 on parse error.
   * */
  int toWhole(String s) {
    try {
      return Integer.parseUnsignedInt(s);
    } catch (NumberFormatException e) {
      return -1;
    }
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
            "[1] Add Contact\n" +
            "[2] List\n" +
            "[3] Search by name\n" +
            "[4] Search by contact\n" +
            "[5] Pick random\n" +
            "[6] Quit\n";

        choice = Prompt.option(sb, "Select menu (1~6)", mainOptions);
        switch (choice) {
          case "1":
            loopAddContact(false);
            break;
          case "2":
            loopList();
            break;
          case "3":
            loopNameSearch();
            break;
          case "4":
            loopContactSearch();
            break;
          case "5":
            loopPick();
            break;
          case "6":
          default:
            break;
        }
      }
    } while (!choice.equals("6"));

  }

  void loopAddContact(boolean useMain) {
    System.out.println("\n\n=== [ Add Contact ] ===");
    if (useMain) System.out.println("** You have no people in your Contacts. Let's get started from adding one.");
    String name = Prompt.text("Enter the name of person.");
    String contact = Prompt.text("Enter the contact.");
    String contactType = Prompt.textDefault("Contact description", "phone");
    contacts.add(new Person(name, contactType, contact));
  }

  void loopList() {
    while (true) {
      StringBuilder sb = new StringBuilder();
      sb.append("\n\n=== [ Contact List ] ===\n");
      int i = 1;
      for (Person person : contacts) sb.append(String.format("[ %d ] %s\n", i++, person.toString()));

      String choice = Prompt.option(sb.toString(),
          "Select an index of person, or type \"//\" to quit",
          s -> {
            if (s.equals("//")) return true;
            int e = toWhole(s);
            return e >= 1 && e <= contacts.size();
          });
      if (choice.equals("//")) return;
      int choiceIndex = toWhole(choice) - 1;

      inspectPerson(contacts.get(choiceIndex));
    }
  }

  void loopNameSearch() {
    while (true) {
      String q = Prompt.text("\n\n=== [ Search by name ] ===\n"
        + String.format("%d Contacts out there.\n", contacts.size()),
        "Input name to find. Type \"//\" for quit.");

      if (q.equals("//")) return;

      List<Person> result = searchByName(q);
      if (result.isEmpty()) {
        System.out.printf("No contacts found by name contains \"%s\"\n", q);
        continue;
      }
      System.out.printf("%d contact(s) found.\n", result.size());
      for (Person p : result) {
        System.out.println(p);
      }
    }
  }

  void loopContactSearch() {
    while (true) {
      String q = Prompt.text("\n\n=== [ Search by contact ] ===\n"
          + String.format("%d Contacts out there.\n", contacts.size()),
        "Input contact \"value\" to find. Type \"//\" for quit.");

      if (q.equals("//")) return;

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
    System.out.println("\n\n=== [ Pick random ] ===\n");
    Random random = new Random();
    Person pick = contacts.get(random.nextInt(contacts.size()));
    System.out.println(pick);
  }

  final String[] inspectOption = { "1", "2", "3", "4", "//" };
  void inspectPerson(Person person) {
    while (true) {
      String body = "\n=== [ Person Info ] ===\n" + person.verbose() + "\n"
        + "[ 1 ] Change name\n"
        + "[ 2 ] Change Contact by\n"
        + "[ 3 ] Delete one of Contact\n"
        + "[ 4 ] Delete Person info\n"
        + "[ // ] Close\n";
      String choice = Prompt.option(body, "What to do?", inspectOption);
      if (choice.equals("//")) return;
      switch (choice) {
        case "1" -> changeNameScreen(person);
        case "2" -> changeContactScreen(person);
        case "3" -> deleteContact(person);
        case "4" -> deletePerson(person);
      }
    }
  }

  void changeNameScreen(Person person) {
    System.out.println("=== [Change name] ===");
    System.out.printf("Current Person name : %s\n", person.getName());
    System.out.println("Type new name for current person. Leaving blank would have on effect.");
    String input = Prompt.textDefault("New name", "");
    if (!input.isEmpty()) person.setName(input);
  }

  void changeContactScreen(Person person) {

  }

  void deleteContact(Person person) {

  }

  void deletePerson(Person person) {

  }

  List<Person> searchByName(String q) {
//    return contacts.stream().filter(p -> p.getName().contains(q)).toList();
    ArrayList<Person> found = new ArrayList<>();
    for (Person person : contacts) {
      if (person.getName().contains(q)) {
        found.add(person);
      }
    }
    return found;
  }

  List<ContactSearchResult> searchByContactValue(String q) {
    ArrayList<ContactSearchResult> found = new ArrayList<>();
    for (Person person : contacts) {
      Set<ContactSearchResult> m = person.findContact(q);
      found.addAll(m);
    }
    return found;
  }

}
