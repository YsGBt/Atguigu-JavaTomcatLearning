package com.atguigu.myssm.basedao;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.atguigu.util.JDBCUtil;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;

public class ConnUtil {

  private static ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

  private static DataSource source;

  static {
    InputStream is = null;
    try {
      Properties pros = new Properties();
      // 这里用JDBCUtil.class.getClassLoader()才能保证在服务器端依旧可以找到配置文件
      is = JDBCUtil.class.getClassLoader().getResourceAsStream(
          "druid.properties");
      pros.load(is);
      source = DruidDataSourceFactory.createDataSource(pros);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        is.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * 实现Druid连接池，获取连接
   *
   * @return 连接池中的连接对应的Connection对象
   * @throws SQLException
   */
  public static Connection getConnection() throws SQLException {
    Connection conn = threadLocal.get();
    if (conn == null) {
      conn = source.getConnection();
      threadLocal.set(conn);
    }
    return threadLocal.get();
  }

  public static void closeConnection() throws SQLException {
    Connection conn = threadLocal.get();
    if (conn != null && !conn.isClosed()) {
      conn.close();
//      threadLocal.set(null);
      threadLocal.remove();
    }
  }

  public static void closeDataSource() {
    ((DruidDataSource) source).close();
  }
}
