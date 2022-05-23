package com.atguigu.fruit.dao.impl;

import com.atguigu.fruit.bean.Fruit;
import com.atguigu.fruit.dao.FruitDAO;
import com.atguigu.util.JDBCUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.Test;

class FruitDAOImplTest {

  private final FruitDAO fruitDAO = new FruitDAOImpl();

  @Test
  public void testConnection() {
    try (Connection connection1 = JDBCUtil.getConnection();) {
      System.out.println(connection1);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testGetFruit() throws SQLException {
    Connection connection = JDBCUtil.getConnection();

    List<Fruit> list;
    list = fruitDAO.getFruits(connection);
    list.forEach(System.out::println);

    connection.close();
  }

  @Test
  public void testAddFruit() throws SQLException {
    Connection connection = JDBCUtil.getConnection();

    Fruit myFruit = new Fruit("刘强瓜", 20, 50, "大棚的西瓜");
    boolean success = fruitDAO.addFruit(connection, myFruit);
    System.out.println(success);

    connection.close();
  }

  @Test
  public void testDeleteFruitByID() throws SQLException {
    Connection connection = JDBCUtil.getConnection();

    int id = 36;
    boolean success = fruitDAO.deleteFruitByID(connection, id);
    System.out.println(success);

    connection.close();
  }

  @Test
  public void testDeleteFruitByName() throws SQLException {
    Connection connection = JDBCUtil.getConnection();

    String name = "刘强瓜";
    int count = fruitDAO.deleteFruitByName(connection, name);
    System.out.println(count);

    connection.close();
  }

  @Test
  public void testGetFruitByID() throws SQLException {
    Connection connection = JDBCUtil.getConnection();

    int id = 41;
    Fruit fruit = fruitDAO.getFruitByID(connection, id);
    System.out.println(fruit);

    connection.close();
  }

  @Test
  public void testGetFruitByName() throws SQLException {
    Connection connection = JDBCUtil.getConnection();

    String name = "刘强瓜";
    Fruit fruit = fruitDAO.getFruitByName(connection, name);
    System.out.println(fruit);

    connection.close();
  }

  @Test
  public void testUpdateFruit() throws SQLException {
    Connection connection = JDBCUtil.getConnection();

    Fruit fruit = new Fruit(41, "刘强瓜", 30, 20, "你这瓜保熟吗");
    boolean success = fruitDAO.updateFruit(connection, fruit);
    System.out.println(success);

    connection.close();
  }
}