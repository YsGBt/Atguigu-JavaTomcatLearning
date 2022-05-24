package com.atguigu.servlets;

import com.atguigu.fruit.bean.Fruit;
import com.atguigu.fruit.dao.FruitDAO;
import com.atguigu.fruit.dao.impl.FruitDAOImpl;
import com.atguigu.myssm.myspringmvc.ViewBaseServlet;
import com.atguigu.myssm.util.StringUtil;
import com.atguigu.util.JDBCUtil;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/edit.do")
public class EditServlet extends ViewBaseServlet {

  private FruitDAO fruitDAO = new FruitDAOImpl();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String fidStr = req.getParameter("fid");
    if (StringUtil.isNotEmpty(fidStr)) {
      int fid = Integer.parseInt(fidStr);
      Connection conn = null;
      try {
        conn = JDBCUtil.getConnection();
        Fruit fruit = fruitDAO.getFruitByID(conn, fid);

        req.setAttribute("fruit", fruit);
        super.processTemplate("edit", req, resp);
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
}
