package com.atguigu.servlets;

import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 演示application保存作用域
@WebServlet("/demoApplication")
public class DemoApplicationServlet extends HttpServlet {

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    // 向application保存作用域保存数据
    // ServletContext: Servlet上下文
    ServletContext application = req.getServletContext();
    application.setAttribute("username", "lili");
  }
}
