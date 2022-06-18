package com.atguigu.servlets;

import com.atguigu.axios.pojo.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/axiosJSON")
public class AxiosJSONServlet extends HttpServlet {

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    StringBuffer stringBuffer = new StringBuffer();
    BufferedReader bufferedReader = req.getReader();
    String str = null;
    while ((str = bufferedReader.readLine()) != null) {
      stringBuffer.append(str);
    }
    bufferedReader.close();
    str = stringBuffer.toString(); // -> {"uname":"lina,"pwd":"ok"}

    // 已知 String
    // 需要转化成 Java Object
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create(); // new Gson();
    // Gson 有两个API
    // 1. fromJson(string, T) 将字符串转化成java object
    // 2. toJson(Java Object) 将 java object转化成json字符串，这样才能响应给客户端
    User user = gson.fromJson(str, User.class);
    user.setUname("鸠摩智");
    user.setPwd("123456");

    // 假设user是从数据库查询出来的，现在需要将其转化成json格式的字符串，然后响应给客户端
    String userJsonStr = gson.toJson(user);

    resp.setCharacterEncoding("UTF-8");
    // MIME-type -> application/json
    resp.setContentType("application/json;charset=utf-8");
    PrintWriter out = resp.getWriter();
    out.write(userJsonStr);
    out.close();
  }
}
