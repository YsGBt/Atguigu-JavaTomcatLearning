package com.atguigu.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// 演示Session
public class DemoSessionServlet extends HttpServlet {

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    // 获取Session，如果获取不到，则创建一个新的
    HttpSession session = req.getSession();
    System.out.println("session ID: " + session.getId());
  }
}
