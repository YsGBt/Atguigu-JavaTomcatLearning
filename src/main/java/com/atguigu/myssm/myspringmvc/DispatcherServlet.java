package com.atguigu.myssm.myspringmvc;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 拦截所有以.do结尾的请求
//@WebServlet("*.do")
public class DispatcherServlet extends ViewBaseServlet {

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    // 设置编码
    req.setCharacterEncoding("UTF-8");

    // 假设url是: http://localhost:8080/Atguigu_JavaTomcatLearning_Web_exploded/hello.do
    // 那么Servlet是: /hello.do
    // 思路:
    // 1. /hello.do -> hello
    // 2. hello -> HelloController
    String servletPath = req.getServletPath();
    int lastDotIndex = servletPath.lastIndexOf(".do");
    servletPath = servletPath.substring(1, lastDotIndex);
  }
}
