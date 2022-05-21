package com.atguigu.fruit.dao.impl;

import com.atguigu.util.JDBCUtil;
import java.sql.Connection;
import org.junit.jupiter.api.Test;

class FruitDAOImplTest {

  @Test
  public void test() throws Exception {
    Connection connection = JDBCUtil.getConnection();

    System.out.println(connection);

//    Fruit myFruit = new Fruit(3, "刘强瓜", 20, 50, "大棚的西瓜");
//    FruitDAO fruitDAO = new FruitDAOImpl();
//    boolean success = fruitDAO.addFruit(connection, myFruit);
//    System.out.println(success);

  }

}