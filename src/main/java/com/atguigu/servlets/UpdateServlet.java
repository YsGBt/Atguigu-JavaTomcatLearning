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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@WebServlet("/update.do")
public class UpdateServlet extends ViewBaseServlet {

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

    // 这里的fid是通过隐藏域获取到的
    String fidStr = req.getParameter("fid");
    Integer fid = Integer.parseInt(fidStr);
    Fruit fruit = new Fruit(fid, fname, price, fcount, remark);

    FruitDAO fruitDAO = new FruitDAOImpl();

    Connection conn = null;
    try {
      conn = JDBCUtil.getConnection();
      // 执行更新
      boolean success = fruitDAO.updateFruit(conn, fruit);
      System.out.println(success ? "更改成功" : "更改失败");

      /*
      资源跳转
      如果直接服务器内部转发，则会导致session未更新，所以展示的列表依旧是更改之前的数据
      因此应该使用客户端重定向，调用index并更新session中的fruitList
      super.processTemplate("index", req, resp);
      req.getRequestDispatcher("index.html").forward(req, resp);
      */
      // 此处需要重定向，目的是重新给DemoThymeleafServlet ("/index")发请求，重新获取fruitList，然后覆盖到session中
      // 这样index.html页面上显示的session数据才是最新的
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
