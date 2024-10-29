import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.TreeMap;

import static java.util.Map.entry;

public class Minimalist extends JFrame {

//  Lore lore = new Lore();
  Map<String, String> lore = new TreeMap<>(Map.ofEntries(
  entry("Web", "세계의 여러 컴퓨터에서 정보를 주고받을 수 있는 드넓은 바다"),
  entry("HTML", "텍스트의 한계를 한층 넘어선 하이퍼텍스트를 웹에서 교환하기 위한 마크업 언어"),
  entry("CSS", "HTML 문서를 웹 브라우저 등에 표현하는 방법을 기술하는 스타일시트"),
  entry("JavaScript", "웹 브라우저 등의 에이전트에서 문서의 여러 요소들을 조작하기 위해 탄생한 언어\n이제는 서버 등 브라우저 없이도 실행될 수 있다"),
  entry("jQuery", "브라우저에서 요소들을 조작하는 구문을 간결하게 나타내기 위한 JavaScript 라이브러리"),
  entry("Node.js", "크롬 V8 엔진을 기반으로 만들어진 JavaScript 런타임\n서버용으로 사용가능"),
  entry("React.js", "HTML을 스크립트에 넣는 JSX, 재사용 가능한 컴포넌트 설계, 불변 상태를 사용하는 JavaScript 라이브러리 및 프레임워크"),
  entry("Next.js", "Vercel에서 개발한 React 기반 풀스택 프레임워크"),
  entry("Vercel", "미국의 클라우드 플랫폼 서비스 회사로, Next.js를 개발하고 유지/보수한다."),
  entry("Netlify", "코딩, 배포, 끝! 사용하기 쉬운 클라우드 플랫폼 서비스 회사"),
  entry("Java", "기계 종속성을 해결하기 위하여, C/C++을 계승한 프로그래밍 언어"),
  entry("Spring", "웹 서버 애플리케이션을 개발하기 위한 Java 기반의 프레임워크"),
  entry("Python", "사용하기 쉬운 것에 초점을 둔 고수준 인터프리터 언어\n수많은 사용자와 강력한 라이브러리가 강점"),
  entry("Django", "Python 기반 웹 서버 애플리케이션 프레임워크"),
  entry("MySQL", "데이터를 읽고 조작하는 언어인 SQL을 도입한 관계형 데이터베이스 관리 시스템"),
  entry("MongoDB", "\"문서\" 기반 NoSQL 데이터베이스 관리 시스템")
  ));
  JLabel title = new JLabel("Title goes here");
  JList<String> list = new JList<>();
  String[] loreList;
  JTextArea textarea = new JTextArea();

  public Minimalist() {

    this.setTitle("Minimalist");
//    this.setPreferredSize();
    this.setSize(500, 350);
    GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

    this.setLocation((gd.getDisplayMode().getWidth() - 500) / 2, (gd.getDisplayMode().getHeight() - 350) / 2);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    setUI();

    list.setListData(loreList = lore.keySet().toArray(new String[0]));
    this.setVisible(true);
  }

  private void setUI () {
    JPanel mainPanel = new JPanel();
    mainPanel.setPreferredSize(new Dimension(500, 350));
    mainPanel.setLayout(new BorderLayout());

    title.setHorizontalAlignment(JLabel.CENTER);
    mainPanel.add(title, BorderLayout.NORTH);

    list.setMinimumSize(new Dimension(100, 350));
    JScrollPane scrollpane = new JScrollPane();
    scrollpane.getViewport().setView(list);
    mainPanel.add(scrollpane, BorderLayout.WEST);

    list.addListSelectionListener(e -> {
      if (e.getValueIsAdjusting()) return;
      String key = list.getSelectedValue();
      title.setText(key);
      setTitle(key);
      textarea.setText(lore.get(key));
    });

    textarea.setLineWrap(true);
    textarea.setEditable(false);

    mainPanel.add(textarea, BorderLayout.CENTER);
    scrollpane.setSize(100, scrollpane.getHeight());
    scrollpane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

    this.add(mainPanel);
  }


  public static void main(String[] args) {
    JFrame f = new Minimalist();
  }
}