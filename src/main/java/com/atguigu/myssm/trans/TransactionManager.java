package com.atguigu.myssm.trans;

import com.atguigu.myssm.basedao.ConnUtil;
import java.sql.SQLException;

public class TransactionManager {

  // 开启事务
  public static void beginTransaction() throws SQLException {
    ConnUtil.getConnection().setAutoCommit(false);
  }

  // 提交事务
  public static void commit() throws SQLException {
    ConnUtil.getConnection().commit();
    ConnUtil.closeConnection();
  }

  // 回滚事务
  public static void rollback() throws SQLException {
    ConnUtil.getConnection().rollback();
    ConnUtil.closeConnection();
  }
}
