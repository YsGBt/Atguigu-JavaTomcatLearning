package com.atguigu.fruit.dao.impl;

import com.atguigu.fruit.bean.Fruit;
import com.atguigu.fruit.dao.FruitDAO;
import com.atguigu.myssm.basedao.BaseDAO;
import java.sql.Connection;
import java.util.List;

public class FruitDAOImpl extends BaseDAO<Fruit> implements FruitDAO {

  @Override
  public List<Fruit> getFruits(Connection conn) {
    String sql = "select fid,fname,price,fcount,remark from t_fruit";
    List<Fruit> beanList = getBeanList(conn, sql);
    return beanList;
  }

  @Override
  public boolean addFruit(Connection conn, Fruit fruit) {
    String sql = "insert into t_fruit(fname,price,fcount,remark) values(?,?,?,?)";
    int count = update(conn, sql, fruit.getFname(), fruit.getPrice(), fruit.getFcount(),
        fruit.getRemark());
    return count == 1;
  }

  @Override
  public boolean deleteFruitByID(Connection conn, int fid) {
    String sql = "delete from t_fruit where fid=?";
    int count = update(conn, sql, fid);
    return count == 1;
  }

  @Override
  public int deleteFruitByName(Connection conn, String name) {
    String sql = "delete from t_fruit where fname=?";
    int count = update(conn, sql, name);
    return count;
  }

  @Override
  public Fruit getFruitByID(Connection conn, int fid) {
    String sql = "select fid,fname,price,fcount,remark from t_fruit where fid=?";
    Fruit fruit = getBean(conn, sql, fid);
    return fruit;
  }

  @Override
  public Fruit getFruitByName(Connection conn, String name) {
    String sql = "select fid,fname,price,fcount,remark from t_fruit where fname=?";
    Fruit fruit = getBean(conn, sql, name);
    return fruit;
  }

  @Override
  public boolean updateFruit(Connection conn, Fruit fruit) {
    String sql = "update t_fruit set fname=?, price=?, fcount=?, remark=? where fid=?";
    int count = update(conn, sql, fruit.getFname(), fruit.getPrice(), fruit.getFcount(),
        fruit.getRemark(), fruit.getFid());
    return count != 0;
  }
}
