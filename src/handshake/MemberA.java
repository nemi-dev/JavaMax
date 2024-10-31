package handshake;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberA {
  public static final String driver = "oracle.jdbc.driver.OracleDriver"; // 10행 ~ 14행 데이터베이스
  // 접속을 위한 4가지 정보를 String 변수에 저장.
  public static final String url = "jdbc:oracle:thin:@localhost:1521:xe";
  // 계정 기본은, 모두 접근 불가. 설정을 통해서, 계정에게 접근 권한, 테이블에 관련 여러가지
  // 권한 , 시스템이 할당해야 가능함.
  public static final String userid = "scott";
  public static final String passwd = "tiger";

  private static MemberA instance = null;

  public MemberA() {
    try {
      Class.forName(driver); // 드라이버를 로딩하는 초기화 작업을 생성자에서 구현한다.
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  public static MemberA getInstance() {
    if (instance == null) instance = new MemberA();
    return instance;
  }


  public List<MemberT> selectAll() throws SQLException {
    String query = "SELECT id, name, email FROM member501";
    ArrayList<MemberT> list = new ArrayList<>();
    try (
      Connection connection = DriverManager.getConnection(url, userid, passwd);
      PreparedStatement statement = connection.prepareStatement(query);
      ResultSet resultSet = statement.executeQuery()
      ) {
      while (resultSet.next()) {
        list.add(new MemberT(
          resultSet.getInt("id"),
          resultSet.getString("name"),
          resultSet.getString("email")
        ));
      }
    }
    return list;
  }

  public MemberT selectByEmailAndPassword(String email, String passwdRaw) throws SQLException {
    String query = "SELECT id, name, email, password FROM member501 WHERE email = ? AND password = ?";
    ResultSet resultSet = null;
    try (
      Connection connection = DriverManager.getConnection(url, userid, passwd);
      PreparedStatement statement = connection.prepareStatement(query);
    ) {
      statement.setString(1, email);
      statement.setString(2, passwdRaw);
      resultSet = statement.executeQuery();
      if (resultSet.next()) {
        return (new MemberT(
          resultSet.getInt("id"),
          resultSet.getString("name"),
          resultSet.getString("email")
//          resultSet.getString("password")
        ));
      }
    } finally {
      if (resultSet != null) resultSet.close();
    }
    return null;
  }

  public int insert(String name, String email, String passwdRaw) throws SQLException {
    String query = "INSERT INTO member501(id, name, email, password) VALUES (member501_seq.NEXTVAL, ?, ?, ?)";
    Connection connection = DriverManager.getConnection(url, userid, passwd);
    PreparedStatement statement = connection.prepareStatement(query);

    statement.setString(1, name);
    statement.setString(2, email);
    statement.setString(3, passwdRaw);

    return statement.executeUpdate();
  }

}
