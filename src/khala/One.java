package khala;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class One {
  public static void main(String[] args) throws IOException {
    Scanner scanner = new Scanner(System.in);
    System.out.print("host:port");
    String server = scanner.nextLine().trim();
    String[] parts = server.split(":");
    try (
      Socket socket = new Socket(parts[0], Integer.parseInt(parts[1]))
    ) {
      BufferedReader in;
      BufferedWriter out;
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

      while (true) {
        System.out.print(">> ");
        String outgoing = scanner.nextLine();
        if (outgoing.equalsIgnoreCase("bye")) {
          out.write(outgoing + '\n');
          out.flush();
          break;
        }
        out.write(outgoing + '\n');
        out.flush();
        String incoming = in.readLine();
        System.out.println("<< " + incoming);
      }
    } finally {
      scanner.close();
    }

  }
}
