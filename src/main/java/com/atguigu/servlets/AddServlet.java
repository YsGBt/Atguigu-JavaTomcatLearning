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
    /*
        // get方式下目前不需要设置编码（基于tomcat8）
        如果是get请求发送的中文数据，转码稍微有点麻烦（tomcat8之前）
        String fname = req.getParameter("fname");
        1. 将字符串打散成字节数组
        byte[] bytes = fname.getBytes("ISO-8859-1");
        2. 将字节数组按照设定的编码重新组装成字符串
        fname = new String(bytes, "UFT-8");
     */

    // post方式下，设置编码，防止中文乱码
    // 需要注意的是，设置编码这一句代码必须在所有的获取参数动作之前
    req.setCharacterEncoding("UTF-8");
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

    Connection conn = null;
    try {
      conn = JDBCUtil.getConnection();
      FruitDAO fruitDAO = new FruitDAOImpl();
      boolean flag = fruitDAO.addFruit(conn, new Fruit(fname, price, fcount, remark));
      System.out.println(flag ? "添加成功!" : "添加失败!");
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        conn.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
      JDBCUtil.closeDataSource();
    }
  }
}
