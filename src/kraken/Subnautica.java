package kraken;

import java.sql.DriverManager; //public class DriverManager
import java.sql.Connection; //public interface Connection extends Wrapper, AutoCloseable {
import java.sql.PreparedStatement; // public interface PreparedStatement extends Statement
import java.sql.Statement; //public interface Statement extends Wrapper, AutoCloseable {
import java.sql.ResultSet; //public interface Statement extends Wrapper, AutoCloseable {
import java.sql.SQLException; // public class SQLException extends java.lang.Exception implements Iterable<Throwable> {

public class Subnautica {
  public static void main(String[] args) {
    String driver = "oracle.jdbc.driver.OracleDriver"; // 12행 ~ 15행 데이터베이스 접속을 위한 4가지 정보를 String 변수에 저장.
    String url = "jdbc:oracle:thin:@localhost:1521:xe";
    String userid = "scott";
    String passwd = "tiger";
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      Class.forName(driver);
//명시된 드라이버 클래스를 메모리에 로딩한다.
      con = DriverManager.getConnection(url, userid, passwd);
// DriverManager 클래스의 getConnection() 메소드를 이용해서
// Connection 객체를 얻는다.
      String query = "SELECT deptno,dname,loc FROM dept";
//요청할 SQL 문을 String 변수에 저장한다.
      pstmt =con.prepareStatement(query);
//SQL 문 전송에 필요한 PreparedStatement 객체를
//Connection 객체의 preparedStatement(sql)메소드를 이용해서 얻는다.

      rs = pstmt.executeQuery();
//SELECT 문을 요청하기 때문에 executeQuery()
// 메소드를 사용하며 결과는 ResultSet 객체로 받는다.
      while(rs.next()) {
        int deptno = rs.getInt("deptno");
        String dname = rs.getString("dname");
        String loc = rs.getString("loc");
        System.out.println(deptno + " " + dname + " " + loc);
      }
    }catch(Exception e) {
      e.printStackTrace();
    }finally {
      try {
        if (rs != null) rs.close();
        if (pstmt != null) pstmt.close();
        if ( con != null) con.close();
      }catch(SQLException e){
        e.printStackTrace();
      }
    }
  }
}