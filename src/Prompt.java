import java.util.Arrays;
import java.util.Scanner;
import java.util.function.Predicate;

public final class Prompt {
  private static final Scanner scanner = new Scanner(System.in);
  private Prompt() {}

  public static String text(String body, String repeat) {
    String input;
    System.out.println(body);
    do {
      System.out.print(repeat + " : ");
      input = scanner.nextLine().trim();
    } while (input.isEmpty());
    return input;
  }

  public static String text(String repeat) {
    return text("", repeat);
  }

  public static String textDefault(String body, String defaultValue) {
    System.out.printf("%s (default: %s) : ", body, defaultValue);
    String input = scanner.nextLine().trim();
    return input.isEmpty() ? defaultValue : input;
  }

  public static String option(String body, String repeat, Predicate<String> predicate) {
    System.out.println(body);
    String input;
    do {
      System.out.print(repeat + " : ");
      input = scanner.nextLine();
    } while (input.isEmpty() || !predicate.test(input));
    return input;
  }

  public static String option(String body, String repeat, final String[] options) {
    return option(body, repeat, s -> Arrays.asList(options).contains(s));
  }
}
