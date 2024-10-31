package handshake;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

public class SignupView extends JPanel {
  private final JTextField username;
  private final JTextField email;
  private final JTextField password;
  private final JTextField passwordConfirm;
  private final JButton signupButton;

  public SignupView() {
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.setBorder(new EmptyBorder(10, 10, 10, 10));
    this.add(new JLabel("This is Signup"));
    this.add(new JLabel("username"));
    this.add(username = J.textField(new JTextField()));
    this.add(new JLabel("email"));
    this.add(email = J.textField(new JTextField()));
    this.add(new JLabel("password"));
    this.add(password = J.textField(new JPasswordField()));
    this.add(new JLabel("confirm"));
    this.add(passwordConfirm = J.textField(new JPasswordField()));
    this.add(signupButton = J.button("SIGN UPASDGIACVBFDAVON!!!!!", this::doSignup));
  }

  private void setChildrenEnable(boolean b) {
    username.setEnabled(b);
    email.setEnabled(b);
    password.setEnabled(b);
    passwordConfirm.setEnabled(b);
    signupButton.setEnabled(b);
  }

  private void wipe() {
    username.setText("");
    email.setText("");
    password.setText("");
    passwordConfirm.setText("");
  }


  private void alert(String message, int type) {
    JOptionPane.showMessageDialog(this, message, "Signup", type);
  }

  private void doSignup(ActionEvent a) {
    if (!password.getText().equals(passwordConfirm.getText())) {
      JOptionPane.showMessageDialog(this, "Check password again.");
      password.requestFocus();
      return;
    }

    setChildrenEnable(false);

    SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
      @Override
      protected Boolean doInBackground() {
        try {
          MemberA.getInstance().insert(
            username.getText(),
            email.getText(),
            password.getText()
          );
          alert("Signed up successfully!!!!! Welcome, " + username.getText() + "!!!!!!!!!!!!", JOptionPane.INFORMATION_MESSAGE);
          wipe();
          return true;
        } catch (SQLException sex) {
          sex.printStackTrace();
          alert("An error occurred on server. ("+ sex.getErrorCode() + ")", JOptionPane.INFORMATION_MESSAGE);
          return false;
        }
      }

      @Override
      protected void done() {
        setChildrenEnable(true);
      }
    };

    worker.run();
  }

}
