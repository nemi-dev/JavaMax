package handshake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.Serializable;

public class J implements Serializable {
  public static JButton button(String text, ActionListener l) {
    var _button = new JButton(text);
    _button.addActionListener(l);
    return _button;
  }

  public static <T extends JTextField> T textField(T t) {
//    t.setPreferredSize(new Dimension((int) t.getPreferredSize().getWidth(), 20));
    t.setMaximumSize(new Dimension(400, 20)); // Limit height
    return t;
  }
}