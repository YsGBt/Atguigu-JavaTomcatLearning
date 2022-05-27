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
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// 这里已经用mvc的方式实现在FruitServlet中， 所以将@WebServlet("/index")注释掉了
// Servlet从3.0版本开始支持注解方式的注册
//@WebServlet("/index")
public class DemoThymeleafServlet extends ViewBaseServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    // 设置当前页，默认1
    Integer pageNo = 1;
    HttpSession session = req.getSession();

    // 设置编码
    req.setCharacterEncoding("UTF-8");
    // 如果oper!=null 说明 通过表单的查询按钮点击过来的
    // 如果oper==null 说明 不是通过表单的查询按钮点击过来的
    String oper = req.getParameter("oper");
    String keyword = null;
    if (StringUtil.isNotEmpty(oper) && "search".equals(oper)) {
      // 说明是点击表单查询发送过来的请求
      // 此时，pageNo应该还原为1，keyword应该从请求参数中获取
      keyword = req.getParameter("keyword");
      // 如果keyword为null，需要设置为空字符串""，否则查询时会拼接成 %null% ， 我们期望的是 %%
      if (StringUtil.isEmpty(keyword)) {
        keyword = "";
      }
      // 将keyword保存(覆盖)到session中
      session.setAttribute("keyword", keyword);
    } else {
      // 说明此处不是点击表单查询发送过来的请求(比如点击下面的上一页下一页或者直接在地址栏输入网址)
      // 此时keyword应该从session作用域获取
      String pageNoStr = req.getParameter("pageNo");
      if (StringUtil.isNotEmpty(pageNoStr)) {
        pageNo = Integer.parseInt(pageNoStr); // 如果从请求中读取到pageNo，则类型转换，否则pageNo默认为1
      }
      // 如果不是点击的查询按钮，那么查询是基于session中保存的现有的keyword进行查询
      Object keywordObj = session.getAttribute("keyword");
      keyword = keywordObj == null ? "" : (String) keywordObj;
    }

    // 重新更新当前页的值
    session.setAttribute("pageNo", pageNo);
    Connection connection = null;
    try {
      // 获取数据库链接
      connection = JDBCUtil.getConnection();

      // 获取fruit list
      FruitDAO fruitDAO = new FruitDAOImpl();
      // 总记录条数
      int fruitCount = fruitDAO.getFruitCount(connection, keyword);
      // 总页数
      int pageCount = (fruitCount + 4) / 5;
      List<Fruit> fruitList = fruitDAO.getFruitsByPage(connection, 5, pageNo, keyword);

      // 保存到session作用域
      session.setAttribute("pageCount", pageCount);
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

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    doGet(req, resp);
  }
}
