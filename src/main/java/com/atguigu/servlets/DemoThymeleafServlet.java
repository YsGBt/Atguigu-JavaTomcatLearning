package com.atguigu.servlets;

import com.atguigu.fruit.bean.Fruit;
import com.atguigu.fruit.dao.FruitDAO;
import com.atguigu.fruit.dao.impl.FruitDAOImpl;
import com.atguigu.myssm.myspringmvc.ViewBaseServlet;
import com.atguigu.util.JDBCUtil;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// Servlet从3.0版本开始支持注解方式的注册
@WebServlet("/index")
public class DemoThymeleafServlet extends ViewBaseServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    Connection connection = null;
    try {
      // 获取数据库链接
      connection = JDBCUtil.getConnection();

      // 获取fruit list
      FruitDAO fruitDAO = new FruitDAOImpl();
      List<Fruit> fruitList = fruitDAO.getFruits(connection);

      // 保存到session作用域
      HttpSession session = req.getSession();
      session.setAttribute("fruitList", fruitList);

      // 此处的视图名称是 index
      // 那么thymeleaf会将这个 逻辑视图名称 对应到 物理视图名称 上去
      // 逻辑视图名称: index
      // 物理视图名称: view-prefix + 逻辑视图名称 + view-suffix
      // 所以真实的视图名称是: /index.html
      super.processTemplate("index", req, resp);
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        connection.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
//      JDBCUtil.closeDataSource();
    }
  }
}
