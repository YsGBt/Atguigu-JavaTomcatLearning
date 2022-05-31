package com.atguigu.fruit.service.impl;

import com.atguigu.fruit.bean.Fruit;
import com.atguigu.fruit.dao.FruitDAO;
import com.atguigu.fruit.service.FruitService;
import com.atguigu.util.JDBCUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class FruitServiceImpl implements FruitService {

  private FruitDAO fruitDAO = null;

  @Override
  public List<Fruit> getFruitList(String keyword, Integer pageNo) {
    Connection connection = null;
    try {
      connection = JDBCUtil.getConnection();
      return fruitDAO.getFruitsByPage(connection, 5, pageNo, keyword);
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        connection.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  @Override
  public void addFruit(Fruit fruit) {
    Connection connection = null;
    try {
      connection = JDBCUtil.getConnection();
      fruitDAO.addFruit(connection, fruit);
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        connection.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public Fruit getFruitById(Integer fid) {
    Connection connection = null;
    try {
      connection = JDBCUtil.getConnection();
      return fruitDAO.getFruitByID(connection, fid);
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        connection.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  @Override
  public void delFruit(Integer fid) {
    Connection connection = null;
    try {
      connection = JDBCUtil.getConnection();
      fruitDAO.deleteFruitByID(connection, fid);
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        connection.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public Integer getPageCount(String keyword) {
    Connection connection = null;
    try {
      connection = JDBCUtil.getConnection();
      int count = fruitDAO.getFruitCount(connection, keyword);
      return (count + 4) / 5;
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        connection.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return 0;
  }

  @Override
  public void updateFruit(Fruit fruit) {
    Connection connection = null;
    try {
      connection = JDBCUtil.getConnection();
      fruitDAO.updateFruit(connection, fruit);
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        connection.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }
}
