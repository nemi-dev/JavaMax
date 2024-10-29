import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.Vector;



public class MutaliskTextArea extends JTextArea {

  private boolean _forcing = false;

  private final Vector<DocumentCallback> cb = new Vector<>();

  public MutaliskTextArea() {
    super();
    this.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) { if (!_forcing) for (var c : cb) c.call(e); }

      @Override
      public void removeUpdate(DocumentEvent e) { if (!_forcing) for (var c : cb) c.call(e);}

      @Override
      public void changedUpdate(DocumentEvent e) { /* noop */ }
    });
  }

  public void setText(String t, boolean force) {
    if (force) _forcing = true;
    super.setText(t);
    if (force) _forcing = false;

  }

  public void onChange(DocumentCallback v) {
    cb.add(v);
  }

  public void offChange(DocumentCallback v) {
    cb.remove(v);
  }
}
