import javax.swing.*;
import java.awt.*;

public class DrawingToolsYeah extends JPanel {

  DrawingToolsYeah() {
    this.setSize(1000, 160);
    this.setLocation(0, 640);
    this.setLayout(new FlowLayout());
    this.add(new JLabel("Q : 직선"));
    this.add(new JLabel("W : 사각형"));
    this.add(new JLabel("E : 원"));
    this.add(new JLabel("R : 연필"));
    this.add(new JLabel("Ctrl+Z : 마지막 그림 지우기"));
  }

}
