package com.atguigu.servlets;

import com.atguigu.fruit.bean.Fruit;
import com.atguigu.fruit.dao.FruitDAO;
import com.atguigu.fruit.dao.impl.FruitDAOImpl;
import com.atguigu.myssm.myspringmvc.ViewBaseServlet;
import com.atguigu.util.JDBCUtil;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/add.do")
public class AddFruitServlet extends ViewBaseServlet {
  private FruitDAO fruitDAO = new FruitDAOImpl();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    super.processTemplate("addFruit", req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    // 获取参数
    req.setCharacterEncoding("UTF-8");
    String fname = req.getParameter("fname");
    String priceStr = req.getParameter("price");
    Integer price = Integer.parseInt(priceStr);
    String fcountStr = req.getParameter("fcount");
    Integer fcount = Integer.parseInt(fcountStr);
    String remark = req.getParameter("remark");

    Fruit fruit = new Fruit(fname, price, fcount, remark);
    Connection conn = null;
    try {
      conn = JDBCUtil.getConnection();
      boolean success = fruitDAO.addFruit(conn, fruit);
      System.out.println(success ? "添加成功" : "添加失败");
      resp.sendRedirect("index");
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        conn.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

  }
}
