package com.atguigu.filters;

import com.atguigu.myssm.trans.TransactionManager;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

//TODO: 目前还未实现基于ConnUtil的DAO，因此未配置此Filter
//@WebFilter("*.do")
public class OpenSessionInViewFilter implements Filter {

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain filterChain) throws IOException, ServletException {
    try {
      TransactionManager.beginTransaction();
      filterChain.doFilter(servletRequest, servletResponse);
      TransactionManager.commit();
    } catch (SQLException e) {
      e.printStackTrace();
      try {
        TransactionManager.rollback();
      } catch (SQLException ex) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void destroy() {

  }
}
