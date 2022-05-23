package com.atguigu.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 演示服务器端内部转发
public class DemoDispatcherServlet extends HttpServlet {

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    System.out.println("request dispatcher...");
    // 服务器内部转发
    req.getRequestDispatcher("demoLifeCycle").forward(req, resp);
  }
}
