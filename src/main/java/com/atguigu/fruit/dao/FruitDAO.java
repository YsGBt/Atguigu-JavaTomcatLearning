package com.atguigu.fruit.dao;

import com.atguigu.fruit.bean.Fruit;
import java.sql.Connection;
import java.util.List;

public interface FruitDAO {

  /**
   * 从数据库中查询出所有的记录
   *
   * @param conn
   * @return
   */
  List<Fruit> getFruits(Connection conn);

  /**
   * 从数据库中查询出指定页码上的信息，每页显示数量由fruitsPerPage决定
   *
   * @param conn
   * @param fruitsPerPage
   * @param pageNo Page Number, start form 1
   * @return
   */
  List<Fruit> getFruitsByPage(Connection conn, int fruitsPerPage, int pageNo);

  /**
   *  从数据库中查询出有关keyword的信息，每页显示数量由fruitsPerPage决定
   *
   * @param conn
   * @param fruitsPerPage
   * @param pageNo
   * @param keyword
   * @return
   */
  List<Fruit> getFruitsByPage(Connection conn, int fruitsPerPage, int pageNo, String keyword);

  /**
   * 向数据库中插入一条记录
   *
   * @param conn
   * @param fruit
   * @return
   */
  boolean addFruit(Connection conn, Fruit fruit);

  /**
   * 从数据库中根据 Fruit 的 id 删除一条记录
   *
   * @param conn
   * @param fid
   * @return
   */
  boolean deleteFruitByID(Connection conn, int fid);

  /**
   * 从数据库中根据 Fruit 的 fname 删除所有相关记录
   *
   * @param conn
   * @param name
   * @return
   */
  int deleteFruitByName(Connection conn, String name);

  /**
   * 根据 Fruit 的 id 从数据库中查询出一条记录
   *
   * @param conn
   * @param fid
   * @return
   */
  Fruit getFruitByID(Connection conn, int fid);

  /**
   * 根据 Fruit 的 fname 从数据库中查询出第一条符合的记录
   *
   * @param conn
   * @param name
   * @return
   */
  Fruit getFruitByName(Connection conn, String name);

  /**
   * 根据图书的 id 从数据库中更新一条记录
   *
   * @param conn
   * @param fruit
   * @return
   */
  boolean updateFruit(Connection conn, Fruit fruit);

  /**
   * 查询数据库中总记录水果条数
   *
   * @param conn
   * @return
   */
  int getFruitCount(Connection conn);

  /**
   *  查询数据库中匹配keyword的水果条数
   *
   * @param conn
   * @param keyword
   * @return
   */
  int getFruitCount(Connection conn, String keyword);
}
