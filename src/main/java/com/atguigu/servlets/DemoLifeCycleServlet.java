package com.atguigu.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 演示Servlet的生命周期
public class DemoLifeCycleServlet extends HttpServlet {

  public DemoLifeCycleServlet() {
    System.out.println("正在实例化......");
  }

  @Override
  public void init() throws ServletException {
    System.out.println("正在初始化......");
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    System.out.println("正在服务......");
  }

  @Override
  public void destroy() {
    System.out.println("正在销毁......");
  }
}
