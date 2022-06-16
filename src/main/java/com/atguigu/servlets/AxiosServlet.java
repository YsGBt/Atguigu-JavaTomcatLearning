package com.atguigu.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/axiosServlet")
public class AxiosServlet extends HttpServlet {

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    req.setCharacterEncoding("UTF-8");
    String uname = req.getParameter("uname");
    String pwd = req.getParameter("pwd");

    System.out.println("uname = " + uname);
    System.out.println("pwd = " + pwd);

    resp.setCharacterEncoding("UTF-8");
    resp.setContentType("text/html;charset=utf-8");

//    throw new NullPointerException("这里故意抛出一个空指针异常...");
    try (PrintWriter out = resp.getWriter();) {
      out.write(uname + "_" + pwd);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
