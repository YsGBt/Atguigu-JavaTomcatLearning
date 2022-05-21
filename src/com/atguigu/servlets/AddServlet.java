package com.atguigu.servlets;

import com.atguigu.fruit.bean.Fruit;
import com.atguigu.fruit.dao.FruitDAO;
import com.atguigu.fruit.dao.impl.FruitDAOImpl;
import com.atguigu.util.JDBCUtil;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String fname = req.getParameter("fname");
    String priceStr = req.getParameter("price");
    Integer price = Integer.parseInt(priceStr);
    String fcountStr = req.getParameter("fcount");
    Integer fcount = Integer.parseInt(fcountStr);
    String remark = req.getParameter("remark");

    System.out.println("fname: " + fname);
    System.out.println("price: " + price);
    System.out.println("fcount: " + fcount);
    System.out.println("remark: " + remark);

    try (Connection conn = JDBCUtil.getConnection();) {
      FruitDAO fruitDAO = new FruitDAOImpl();
      boolean flag = fruitDAO.addFruit(conn, new Fruit(fname, price, fcount, remark));
      System.out.println(flag ? "添加成功!" : "添加失败!");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
