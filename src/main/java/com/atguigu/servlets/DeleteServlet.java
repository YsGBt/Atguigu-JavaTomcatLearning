package com.atguigu.servlets;

import com.atguigu.fruit.dao.FruitDAO;
import com.atguigu.fruit.dao.impl.FruitDAOImpl;
import com.atguigu.myssm.myspringmvc.ViewBaseServlet;
import com.atguigu.myssm.util.StringUtil;
import com.atguigu.util.JDBCUtil;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@WebServlet("/del.do")
public class DeleteServlet extends ViewBaseServlet {
  private FruitDAO fruitDAO = new FruitDAOImpl();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String fidStr = req.getParameter("fid");
    if (StringUtil.isNotEmpty(fidStr)) {
      Integer fid = Integer.parseInt(fidStr);
      Connection conn = null;

      try {
        conn = JDBCUtil.getConnection();
        boolean success = fruitDAO.deleteFruitByID(conn, fid);
        System.out.println(success ? "删除成功" : "删除失败");
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
}
