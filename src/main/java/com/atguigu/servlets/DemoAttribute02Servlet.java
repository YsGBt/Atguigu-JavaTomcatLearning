package com.atguigu.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 演示从HttpSession保存作用域中获取数据
public class DemoAttribute02Servlet extends HttpServlet {

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    Object username = req.getSession().getAttribute("username");
    System.out.println(username);
  }
}
