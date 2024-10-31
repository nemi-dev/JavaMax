package mutalisk;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.util.Arrays;
import java.util.Vector;

public class MutalTextWrapper<T extends JTextComponent> {

  public interface CB {
    void call(DocumentEvent e);
  }

  private boolean forcing = false;
  private final Vector<CB> callbacks = new Vector<>();
  public final T jtc;
  public MutalTextWrapper(T component) {
    this.jtc = component;
    this.jtc.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) { invokeCallbacks(e); }

      @Override
      public void removeUpdate(DocumentEvent e) { invokeCallbacks(e); }

      @Override
      public void changedUpdate(DocumentEvent e) { /* noop */ }
    });
  }

  public void onChange(CB...callbacks) {
    this.callbacks.addAll(Arrays.asList(callbacks));
  }

  private void invokeCallbacks(DocumentEvent e) {
    if (!forcing) for (var callback : callbacks) callback.call(e);
  }

  public void setText(String t, boolean force) {
    forcing = force;
    this.jtc.setText(t);
    forcing = false;
  }

  public void setText(String t) { this.setText(t, false); }


}
