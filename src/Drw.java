import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Drw extends JFrame {

  public Drw() {
    this.setSize(1000, 800);

    DrawingBoardYeah board = new DrawingBoardYeah();
    JPanel tools = new DrawingToolsYeah();

    this.setLayout(null);
    this.add(board);
    this.add(tools);
    this.setVisible(true);
    this.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        switch (Character.toUpperCase(e.getKeyChar())) {
          case 'Q': board.setCurrentType(0); break;
          case 'W': board.setCurrentType(1); break;
          case 'E': board.setCurrentType(2); break;
          case 'R': board.setCurrentType(3); break;
        }
      }
    });
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  public static void main(String[] args) {
    new Drw();
  }
}
