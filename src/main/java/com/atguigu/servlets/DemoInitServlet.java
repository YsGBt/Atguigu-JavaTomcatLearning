package com.atguigu.servlets;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//<!--  <servlet>-->
//<!--    <servlet-name>DemoInitServlet</servlet-name>-->
//<!--    <servlet-class>com.atguigu.servlets.DemoInitServlet</servlet-class>-->
//<!--    <init-param>-->
//<!--      <param-name>hello</param-name>-->
//<!--      <param-value>world</param-value>-->
//<!--    </init-param>-->
//<!--    <init-param>-->
//<!--      <param-name>uname</param-name>-->
//<!--      <param-value>Steven</param-value>-->
//<!--    </init-param>-->
//<!--  </servlet>-->
//<!--  <servlet-mapping>-->
//<!--    <servlet-name>DemoInitServlet</servlet-name>-->
//<!--    <url-pattern>/demoInit</url-pattern>-->
//<!--  </servlet-mapping>-->

@WebServlet(urlPatterns = {"/demoInit", "/demoInit2"},
    initParams = {
        @WebInitParam(name = "hello", value = "world"),
        @WebInitParam(name = "uname", value = "Steven")
    })
public class DemoInitServlet extends HttpServlet {

  @Override
  public void init() throws ServletException {
    ServletConfig config = getServletConfig();
    String initValue = config.getInitParameter("hello");
    System.out.println(initValue);

    ServletContext servletContext = getServletContext();
    String contextConfigLocation = servletContext.getInitParameter("contextConfigLocation");
    System.out.println(contextConfigLocation);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    ServletContext servletContext = req.getServletContext();
//    ServletContext servletContext = req.getSession().getServletContext();
  }
}
