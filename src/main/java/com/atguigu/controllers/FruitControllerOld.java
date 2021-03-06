package com.atguigu.controllers;

import com.atguigu.fruit.bean.Fruit;
import com.atguigu.fruit.dao.FruitDAO;
import com.atguigu.fruit.dao.impl.FruitDAOImpl;
import com.atguigu.myssm.util.StringUtil;
import com.atguigu.util.JDBCUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

// DispatcherServlet会对此请求进行响应
//@WebServlet("/fruit.do")
public class FruitControllerOld {

  // 之前FruitServlet是一个Servlet组建，那么其中的init方法一定会被调用
  // 之前的init方法内部会出现一句话: super.init();
  // 现在FruitServlet被转换成Controller，所以init方法不会被自动调用
  // 因此，我们需要手动调用init方法
  private FruitDAO fruitDAO = new FruitDAOImpl();

  private String index(String oper, String keyword, Integer pageNo, HttpServletRequest req) {
    // 设置当前页，默认1
//    Integer pageNo = 1;
    if (pageNo == null) {
      pageNo = 1;
    }
    HttpSession session = req.getSession();

    // 如果oper!=null 说明 通过表单的查询按钮点击过来的
    // 如果oper==null 说明 不是通过表单的查询按钮点击过来的
//    String oper = req.getParameter("oper");
//    String keyword = null;
    if (StringUtil.isNotEmpty(oper) && "search".equals(oper)) {
      // 说明是点击表单查询发送过来的请求
      // 此时，pageNo应该还原为1，keyword应该从请求参数中获取
//      keyword = req.getParameter("keyword");
      // 如果keyword为null，需要设置为空字符串""，否则查询时会拼接成 %null% ， 我们期望的是 %%
      if (StringUtil.isEmpty(keyword)) {
        keyword = "";
      }
      // 将keyword保存(覆盖)到session中
      session.setAttribute("keyword", keyword);
    } else {
      // 说明此处不是点击表单查询发送过来的请求(比如点击下面的上一页下一页或者直接在地址栏输入网址)
      // 此时keyword应该从session作用域获取
//      String pageNoStr = req.getParameter("pageNo");
//      if (StringUtil.isNotEmpty(pageNoStr)) {
//        pageNo = Integer.parseInt(pageNoStr); // 如果从请求中读取到pageNo，则类型转换，否则pageNo默认为1
//      }
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
//      super.processTemplate("index", req, resp);
      return "index";
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
    return "error";
  }

  private String add(String fname, Integer price, Integer fcount, String remark) {
    // 获取参数
//    req.setCharacterEncoding("UTF-8");
//    String fname = req.getParameter("fname");
//    String priceStr = req.getParameter("price");
//    Integer price = Integer.parseInt(priceStr);
//    String fcountStr = req.getParameter("fcount");
//    Integer fcount = Integer.parseInt(fcountStr);
//    String remark = req.getParameter("remark");

    Fruit fruit = new Fruit(fname, price, fcount, remark);
    Connection conn = null;
    try {
      conn = JDBCUtil.getConnection();
      boolean success = fruitDAO.addFruit(conn, fruit);
      System.out.println(success ? "添加成功" : "添加失败");
//      resp.sendRedirect("fruit.do");
      return "redirect:fruit.do";
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        conn.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return "error";
  }

  private String redirectAdd() {
//    super.processTemplate("addFruit", req, resp);
    return "addFruit";
  }


  private String delete(Integer fid) {
//    String fidStr = req.getParameter("fid");
//    if (StringUtil.isNotEmpty(fidStr)) {
    if (fid != null) {
//      Integer fid = Integer.parseInt(fidStr);

      Connection conn = null;

      try {
        conn = JDBCUtil.getConnection();
        boolean success = fruitDAO.deleteFruitByID(conn, fid);
        System.out.println(success ? "删除成功" : "删除失败");
//        resp.sendRedirect("fruit.do");
        return "redirect:fruit.do";
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
    return "error";
  }

  private String edit(Integer fid, HttpServletRequest req) {
//    String fidStr = req.getParameter("fid");
//    if (StringUtil.isNotEmpty(fidStr)) {
    if (fid != null) {
//      int fid = Integer.parseInt(fidStr);
      Connection conn = null;
      try {
        conn = JDBCUtil.getConnection();
        Fruit fruit = fruitDAO.getFruitByID(conn, fid);

        req.setAttribute("fruit", fruit);
//        super.processTemplate("edit", req, resp);
        return "edit";
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
    return "error";
  }

  private String update(Integer fid, String fname, Integer price, Integer fcount, String remark) {
    // 获取参数
//    req.setCharacterEncoding("UTF-8");
//    String fname = req.getParameter("fname");
//    String priceStr = req.getParameter("price");
//    Integer price = Integer.parseInt(priceStr);
//    String fcountStr = req.getParameter("fcount");
//    Integer fcount = Integer.parseInt(fcountStr);
//    String remark = req.getParameter("remark");
//
//    // 这里的fid是通过隐藏域获取到的
//    String fidStr = req.getParameter("fid");
//    Integer fid = Integer.parseInt(fidStr);
    Fruit fruit = new Fruit(fid, fname, price, fcount, remark);

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
//      resp.sendRedirect("fruit.do");
      return "redirect:fruit.do";
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        conn.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return "error";
  }

}
