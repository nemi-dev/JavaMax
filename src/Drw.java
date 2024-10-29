import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Drw extends JFrame {

  public Drw() {
    this.setSize(1000, 800);

    DrawingBoardYeah board = new DrawingBoardYeah();

    this.setLayout(null);
    this.add(board);
    this.add(new JPanel(){{
      this.setSize(1000, 160);
      this.setLocation(0, 640);
      this.setLayout(new FlowLayout());
      this.add(new JLabel("Q : 직선"));
      this.add(new JLabel("W : 사각형"));
      this.add(new JLabel("E : 원"));
      this.add(new JLabel("R : 연필"));
      this.add(new JLabel("Ctrl+Z : 마지막 그림 지우기"));
    }});
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
