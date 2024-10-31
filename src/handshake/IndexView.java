package handshake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class IndexView extends JFrame {

  private JPanel card;
  private CardLayout cardLayout;
  private final JButton showListButton;
  private final JButton logoutButton;

  public IndexView() {
    this.setSize(600, 360);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLayout(new BorderLayout());

    showListButton = J.button("List", IndexView.this::tryOpenList);
    showListButton.setEnabled(false);

    logoutButton = J.button("Logout", e -> this.dispatchLogout());

    this.add(new JPanel() {{
      this.setLayout(new FlowLayout());
      this.add(J.button("Login", _ -> cardLayout.show(card, "Login")));
      this.add(J.button("Sign Up", _ -> cardLayout.show(card, "Signup")));
      this.add(showListButton);
    }}, BorderLayout.NORTH);

    this.add(card = new JPanel() {{
      this.setLayout(cardLayout = new CardLayout());
      this.add(new LoginView(IndexView.this), "Login");
      this.add(new SignupView(), "Signup");
      this.add(new ListView(), "List");
    }}, BorderLayout.CENTER);

    this.setVisible(true);
  }

  void tryOpenList(ActionEvent e) {
    cardLayout.show(card, "List");
  }

  public static void main(String[] args) {
    new IndexView();
  }

  void dispatchLogin(MemberT member) {
    showListButton.setEnabled(true);
    this.setTitle(String.format("Login as : %s <%s>", member.name, member.email));
    this.add(logoutButton, BorderLayout.SOUTH);
    this.repaint();
  }
  void dispatchLogout() {
    showListButton.setEnabled(false);
    cardLayout.show(card, "Login");
    this.remove(logoutButton);
    this.repaint();
  }
}
