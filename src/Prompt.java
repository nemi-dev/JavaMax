import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;

public final class Prompt {
  private static final Scanner scanner = new Scanner(System.in);
  private Prompt() {}

  public static String require(String prompt) {
    String input;
    do {
      System.out.print(prompt + " : ");
      input = scanner.nextLine().trim();
    } while (input.isEmpty());
    return input;
  }

  public static String get(String defaultValue) {
    String input = scanner.nextLine().trim();
    return input.isEmpty() ? defaultValue : input;
  }

  public static String get(String prompt, String defaultValue) {
    System.out.printf("%s [%s]: ", prompt, defaultValue);
    String input = scanner.nextLine().trim();
    return input.isEmpty() ? defaultValue : input;
  }

  public static String choose(String prompt, String defaultValue, final Collection<String> options) {
    String input;
    String useDefault = (defaultValue != null && !defaultValue.isEmpty())? String.format("[%s]", defaultValue) : "";
    do {
      System.out.print(prompt + useDefault + ": ");
      input = scanner.nextLine().trim();
      if (input.isEmpty() && defaultValue != null) {
        input = defaultValue;
        break;
      }
    } while (!options.contains(input));
    return input;
  }

  public static String choose(String prompt, String defaultValue, final String[] options) {
    return choose(prompt, defaultValue, Arrays.asList(options));
  }

  public static String choose(String prompt, final Collection<String> options) {
    return choose(prompt, null, options);
  }

  public static String choose(String prompt, final String[] options) {
    return choose(prompt, null, Arrays.asList(options));
  }

}
