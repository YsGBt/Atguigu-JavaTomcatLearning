package com.atguigu.controllers;

import com.atguigu.fruit.service.FruitService;
import com.atguigu.fruit.bean.Fruit;
import com.atguigu.myssm.util.StringUtil;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

// DispatcherServlet会对此请求进行响应
//@WebServlet("/fruit.do")
public class FruitController {

  // 之前FruitServlet是一个Servlet组建，那么其中的init方法一定会被调用
  // 之前的init方法内部会出现一句话: super.init();
  // 现在FruitServlet被转换成Controller，所以init方法不会被自动调用
  // 因此，我们需要手动调用init方法
  private FruitService fruitService = null;


  private String index(String oper, String keyword, Integer pageNo, HttpServletRequest req) {
    // 设置当前页，默认1
    if (pageNo == null) {
      pageNo = 1;
    }
    HttpSession session = req.getSession();

    // 如果oper!=null 说明 通过表单的查询按钮点击过来的
    // 如果oper==null 说明 不是通过表单的查询按钮点击过来的
    if (StringUtil.isNotEmpty(oper) && "search".equals(oper)) {
      // 说明是点击表单查询发送过来的请求
      // 此时，pageNo应该还原为1，keyword应该从请求参数中获取
      // 如果keyword为null，需要设置为空字符串""，否则查询时会拼接成 %null% ， 我们期望的是 %%
      if (StringUtil.isEmpty(keyword)) {
        keyword = "";
      }
      // 将keyword保存(覆盖)到session中
      session.setAttribute("keyword", keyword);
    } else {
      // 说明此处不是点击表单查询发送过来的请求(比如点击下面的上一页下一页或者直接在地址栏输入网址)
      // 此时keyword应该从session作用域获取
      // 如果不是点击的查询按钮，那么查询是基于session中保存的现有的keyword进行查询
      Object keywordObj = session.getAttribute("keyword");
      keyword = keywordObj == null ? "" : (String) keywordObj;
    }

    // 重新更新当前页的值
    session.setAttribute("pageNo", pageNo);
    // 总页数
    int pageCount = fruitService.getPageCount(keyword);
    // 获取fruit list
    List<Fruit> fruitList = fruitService.getFruitList(keyword, pageNo);

    // 保存到session作用域
    session.setAttribute("pageCount", pageCount);
    session.setAttribute("fruitList", fruitList);

    // 此处的视图名称是 index
    // 那么thymeleaf会将这个 逻辑视图名称 对应到 物理视图名称 上去
    // 逻辑视图名称: index
    // 物理视图名称: view-prefix + 逻辑视图名称 + view-suffix
    // 所以真实的视图名称是: /index.html
//      super.processTemplate("index", req, resp);
    return "index";
  }

  private String add(String fname, Integer price, Integer fcount, String remark) {
    Fruit fruit = new Fruit(fname, price, fcount, remark);
    fruitService.addFruit(fruit);
//      resp.sendRedirect("fruit.do");
    return "redirect:fruit.do";
  }

  private String redirectAdd() {
//    super.processTemplate("addFruit", req, resp);
    return "addFruit";
  }


  private String delete(Integer fid) {
    if (fid != null) {
      fruitService.delFruit(fid);
//        resp.sendRedirect("fruit.do");
      return "redirect:fruit.do";
    }
    return "error";
  }

  private String edit(Integer fid, HttpServletRequest req) {
    if (fid != null) {
      Fruit fruit = fruitService.getFruitById(fid);
      req.setAttribute("fruit", fruit);
//        super.processTemplate("edit", req, resp);
      return "edit";
    }
    return "error";
  }

  private String update(Integer fid, String fname, Integer price, Integer fcount, String remark) {
    // 获取参数
    Fruit fruit = new Fruit(fid, fname, price, fcount, remark);
    // 执行更新
    fruitService.updateFruit(fruit);
    return "redirect:fruit.do";
  }

}
