package com.atguigu.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/cookie01")
public class CookieServlet01 extends HttpServlet {

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    // 1. 创建一个Cookie对象
    Cookie cookie = new Cookie("uname", "jim");
    // 2. 将这个Cookie对象保存到浏览器端
    resp.addCookie(cookie);

    // 给Cookie设置过期时间
    // 正数: Cookie的过期时间，以秒为单位
    // 负数: 表示这个Cookie是会话级，浏览器关闭时释放
    // 0: 通知浏览器立即删除这个Cookie
    cookie.setMaxAge(20);

    req.getRequestDispatcher("succ.html").forward(req, resp);
  }
}
