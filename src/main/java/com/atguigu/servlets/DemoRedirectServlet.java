package com.atguigu.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 演示客户端重定向
public class DemoRedirectServlet extends HttpServlet {

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    System.out.println("send redirect...");
    // 客户端重定向
    resp.sendRedirect("demoLifeCycle");
  }
}
