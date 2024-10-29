import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Map;

public class Mutalisk extends JFrame {

  private final MutalDict dict = new MutalDict("dict.csv");
  private String currentKey;
  private final JPanel root = new JPanel();
  private final JLabel headLabel = new JLabel("Title goes here");
  private final MutaliskTextField headEdit = new MutaliskTextField("Title goes here");
  private final JList<String> list = new JList<>();
  private final MutaliskTextArea textarea = new MutaliskTextArea();
  private boolean modified = false;
  private JButton addButton;
  private JButton randomButton;
  private JButton editTitleButton;
  private JButton discardEditTitleButton;
  private JButton deleteButton;
  private boolean editTitleMode = false;

  private void updateList() {
    list.setListData(dict.keys());
  }

  private void invokeSelect(String key) {
    if (modified) {
      dict.set(currentKey, textarea.getText());
      modified = false;
    }
    currentKey = key;
    headLabel.setText(key);
    headEdit.setText(key, true);
    this.setTitle(key);
    textarea.setText(dict.get(key), true);
    textarea.setEditable(true);
    editTitleButton.setEnabled(true);
    deleteButton.setEnabled(true);
  }

  private JButton button(String text, ActionListener a) {
    JButton button = new JButton(text);
    button.addActionListener(a);
    return button;
  }

  private boolean askDelete(String key) {
    String title = "Confirm delete " + key;
    String message = String.format("You really really want to delete %s?\nThis CANNOT be undone!!!!!!!!", key);
    int response = JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    if (response == JOptionPane.YES_OPTION) {
      dict.delete(key);
      return true;
    }
    return false;
  }

  private boolean checkValidTitle() {
    String s = headEdit.getText().trim();
    if (s.isEmpty()) {
      JOptionPane.showMessageDialog(this, "Title must not be empty!", "Error", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (s.contains(",")) {
      JOptionPane.showMessageDialog(this, "',' is not allowed for title.", "Error", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (!s.equals(currentKey) && dict.has(s.trim())) {
      JOptionPane.showMessageDialog(this, "Title must be unique!", "Error", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }

  private void startHeaderEdit() {
    root.remove(headLabel);
    root.add(headEdit, BorderLayout.NORTH);
    editTitleButton.setText("Confirm Change");
    list.setEnabled(false);
    randomButton.setEnabled(false);
    addButton.setEnabled(false);
    deleteButton.setEnabled(false);
    discardEditTitleButton.setVisible(true);
    editTitleMode = true;
    root.repaint();
  }

  private void endHeaderEdit(boolean update) {
    if (update) {
      if (!checkValidTitle()) return;

      String s = headEdit.getText().trim();
      if (!s.equals(currentKey)) {
        headLabel.setText(s);
        dict.move(currentKey, s);
        currentKey = s;
        updateList();
        list.setSelectedValue(s, true);
      }
    } else {
      headEdit.setText(headLabel.getText());
    }
    root.remove(headEdit);
    root.add(headLabel, BorderLayout.NORTH);
    editTitleButton.setText("Edit Title");
    list.setEnabled(true);
    randomButton.setEnabled(true);
    addButton.setEnabled(true);
    deleteButton.setEnabled(true);
    discardEditTitleButton.setVisible(false);
    editTitleMode = false;
    root.repaint();
  }

  private void setUI () {
    root.setLayout(new BorderLayout());

    headLabel.setHorizontalAlignment(JLabel.CENTER);
    headLabel.setFont(headLabel.getFont().deriveFont(20f));

    headEdit.setHorizontalAlignment(JTextField.CENTER);
    headEdit.setFont(headEdit.getFont().deriveFont(20f));
    headEdit.onChange(v -> {
      String s = headEdit.getText().trim();
      if (!s.equals(currentKey) && dict.has(s) || s.contains(",")) {
        headEdit.setForeground(Color.RED);
      } else {
        headEdit.setForeground(Color.BLACK);
      }
    });
    headEdit.addActionListener(e -> {
      endHeaderEdit(true);
    });

    root.add(headLabel, BorderLayout.NORTH);

    JScrollPane scrollpane = new JScrollPane();
    scrollpane.getViewport().setView(list);
    root.add(scrollpane, BorderLayout.WEST);

    list.addListSelectionListener(e -> {
      if (e.getValueIsAdjusting()) return;
      String sel = list.getSelectedValue();
      if (sel != null) invokeSelect(sel);
    });

    textarea.setLineWrap(true);
    textarea.setEditable(false);
    textarea.onChange(e -> modified = true);
    root.add(textarea, BorderLayout.CENTER);

    JPanel controller = new JPanel();
    controller.setLayout(new FlowLayout());

    controller.add(addButton = button("Add", e -> {
      String newKey = dict.create();
      currentKey = newKey;
      modified = false;
      updateList();
      list.setSelectedValue(newKey, true);
      startHeaderEdit();
    }));
    controller.add(randomButton = button("Pick", e -> {
      Map.Entry<String, String> choice = dict.pick();
      list.setSelectedValue(choice.getKey(), true);
    }));
    controller.add(editTitleButton = button("Edit Title", e -> {
      if (editTitleMode)  endHeaderEdit(true);
      else startHeaderEdit();
    }));
    controller.add(discardEditTitleButton = button("Cancel", e -> {
      endHeaderEdit(false);
    }));
    discardEditTitleButton.setVisible(false);
    controller.add(deleteButton = button("Delete", e -> {
      if (askDelete(currentKey)) {
        modified = false;
        list.clearSelection();
        updateList();
        textarea.setEditable(false);
        textarea.setText("", true);
        headLabel.setText(currentKey + " has gone...");
        addButton.setEnabled(true);
        randomButton.setEnabled(true);
        editTitleButton.setEnabled(false);
        deleteButton.setEnabled(false);
      }
    }));
    editTitleButton.setEnabled(false);
    deleteButton.setEnabled(false);
    root.add(controller, BorderLayout.SOUTH);

    scrollpane.setSize(100, scrollpane.getHeight());
    scrollpane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

    this.add(root);
  }

  public Mutalisk() {

    this.setTitle("Mutalisk");
    this.setSize(700, 400);
    GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

    this.setLocation((gd.getDisplayMode().getWidth() - 500) / 2, (gd.getDisplayMode().getHeight() - 350) / 2);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    setUI();

    updateList();
    this.setVisible(true);

    Runtime.getRuntime().addShutdownHook(new Thread(() -> dict.set(currentKey, textarea.getText())));
  }

  public static void main(String[] args) {
    JFrame f = new Mutalisk();
  }
}