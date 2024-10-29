import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class DrawingBoardYeah extends JPanel {

  interface  Shape {
    void draw(Graphics2D g2);
  }

  record RasterShape (int x0, int y0, int x1, int y1, int type) implements Shape {
    int width() { return Math.abs(x1 - x0); }
    int height() { return Math.abs(y1 - y0); }
    int left() { return Math.min(x0, x1); }
    int top() { return Math.min(y0, y1); }
    public void draw(Graphics2D g2) {
      switch(type) {
        case 0: default: g2.drawLine(x0, y0, x1, y1); break;
        case 1: g2.drawRect(left(), top(), width(), height()); break;
        case 2: g2.drawOval(left(), top(), width(), height()); break;
      }
    }
  }

  record FreeHand (int[] xs, int[] ys) implements Shape {
    public void draw(Graphics2D g2) {
      g2.drawPolyline(xs, ys, xs.length);
    }
  }

  private final Vector<Integer> trailX = new Vector<>();
  private final Vector<Integer> trailY = new Vector<>();

  class MyAdapter extends MouseAdapter {
    @Override
    public void mousePressed(MouseEvent e) {
      super.mousePressed(e);
      DrawingBoardYeah.this.addMouseMotionListener(movingMan);
      press = true;
      startX = e.getX();
      startY = e.getY();
      trailX.add(startX);
      trailY.add(startY);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
      super.mouseReleased(e);
      DrawingBoardYeah.this.removeMouseMotionListener(movingMan);
      press = false;
      endX = e.getX();
      endY = e.getY();
      repaint();
      if (currentType != 3) {
        shapes.add(new RasterShape(startX, startY, endX, endY, currentType));
      } else {
        shapes.add(new FreeHand(trailX.stream().mapToInt(Integer::intValue).toArray(), trailY.stream().mapToInt(Integer::intValue).toArray()));
      }
      trailX.clear();
      trailY.clear();
    }
  }

  class MovingMan extends MouseMotionAdapter {
    @Override
    public void mouseDragged(MouseEvent e) {
      super.mouseMoved(e);
      endX = e.getX();
      endY = e.getY();
      trailX.add(endX);
      trailY.add(endY);
      repaint();
    }
  }

  private int currentType = 0;
  private int startX = 0, startY = 0, endX = 0, endY = 0;

  private final Vector<Shape> shapes = new Vector<>();
  private final MouseMotionAdapter movingMan = new MovingMan();
  private boolean press = false;

  void setCurrentType(int md) {
    this.currentType = md;
    repaint();
  }

  DrawingBoardYeah() {
    this.setSize(1000, 640);
    this.setLocation(0, 0);
    this.addMouseListener(new MyAdapter());
    KeyStroke ctrlZ = KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK);
    this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ctrlZ, "undoAction");
    this.getActionMap().put("undoAction", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (!shapes.isEmpty()) {
          shapes.remove(shapes.size() - 1);
          repaint();
        }
      }
    });
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setColor(Color.BLACK);
    for (Shape shape: shapes) {
      shape.draw(g2);
    }
    if (press) {
      g2.setColor(Color.RED);
      if (currentType != 3) new RasterShape(startX, startY, endX, endY, currentType).draw(g2);
      else g2.drawPolyline( trailX.stream().mapToInt(Integer::intValue).toArray(), trailY.stream().mapToInt(Integer::intValue).toArray(), trailX.size() );
    }
  }
}
