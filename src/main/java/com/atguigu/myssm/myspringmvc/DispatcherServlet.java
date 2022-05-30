package com.atguigu.myssm.myspringmvc;

import com.atguigu.myssm.util.StringUtil;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

// 拦截所有以.do结尾的请求
@WebServlet("*.do")
public class DispatcherServlet extends ViewBaseServlet {

  private Map<String, Object> beanMap = new HashMap<>();

  @Override
  public void init() throws ServletException {
    super.init();
    InputStream inputStream = null;
    try {
      inputStream = getClass().getClassLoader()
          .getResourceAsStream("applicationContext.xml");
      // 1. 创建DocumentBuilderFactory
      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
      // 2. 创建DocumentBuilder对象
      DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
      // 3. 创建Document对象
      Document document = documentBuilder.parse(inputStream);
      // 4. 获取所有的bean节点
      NodeList beanNodeList = document.getElementsByTagName("bean");
      for (int i = 0; i < beanNodeList.getLength(); ++i) {
        Node beanNode = beanNodeList.item(i);
        if (beanNode.getNodeType() == Node.ELEMENT_NODE) {
          Element beanElement = (Element) beanNode;
          String beanId = beanElement.getAttribute("id");
          String className = beanElement.getAttribute("class");
          Class controllerBeanClass = Class.forName(className);
          Object beanObj = controllerBeanClass.newInstance();

//          Method initBean = beanObj.getClass().getSuperclass().getDeclaredMethod("init",
//              ServletContext.class);
//          initBean.invoke(beanObj, this.getServletContext());
          beanMap.put(beanId, beanObj);
        }
      }
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } finally {
      try {
        inputStream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    // 设置编码
    req.setCharacterEncoding("UTF-8");

    // 假设url是: http://localhost:8080/Atguigu_JavaTomcatLearning_Web_exploded/hello.do
    // 那么Servlet是: /hello.do
    // 思路:
    // 1. /hello.do -> hello 或者 /fruit.do -> fruit
    // 2. hello -> HelloController 或者 fruit -> FruitController
    String servletPath = req.getServletPath();
    int lastDotIndex = servletPath.lastIndexOf(".do");
    servletPath = servletPath.substring(1, lastDotIndex);

    Object controllerBeanObj = beanMap.get(servletPath);

    String operate = req.getParameter("operate");
    if (StringUtil.isEmpty(operate)) {
      operate = "index";
    }

    try {
//      Method method = controllerBeanObj.getClass()
//          .getDeclaredMethod(operate, HttpServletRequest.class);
      Method[] methods = controllerBeanObj.getClass().getDeclaredMethods();
      for (Method method : methods) {
        if (operate.equals(method.getName())) {
          // 1. 统一获取请求参数
          // 获取当前方法的参数数组，返回参数数组
          Parameter[] parameters = method.getParameters();
          // parameterValues 用来承载参数的值
          Object[] parameterValues = new Object[parameters.length];
          for (int i = 0; i < parameters.length; ++i) {
            String parameterName = parameters[i].getName();
            // 如果参数名是req, resp, session 那么就不是通过请求中获取参数的方式了
            if ("req".equals(parameterName)) {
              parameterValues[i] = req;
            } else if ("resp".equals(parameterName)) {
              parameterValues[i] = resp;
            } else if ("session".equals(parameterName)) {
              parameterValues[i] = req.getSession();
            } else {
              // 从请求中获取参数值
              Object parameterValue = req.getParameter(parameterName);
              String typeName = parameters[i].getType().getName();

              if (parameterValue != null && "java.lang.Integer".equals(typeName)) {
                parameterValue = Integer.parseInt((String) parameterValue);
              }
              parameterValues[i] = parameterValue;
            }
          }

          // 2. controller组件中的方法调用
          method.setAccessible(true);
          Object methodReturnObj = method.invoke(controllerBeanObj, parameterValues);
          String methodReturnStr = "";
          if (methodReturnObj != null) {
            methodReturnStr = (String) methodReturnObj;
          }

          // 3. 视图处理
          if (methodReturnStr.startsWith("redirect:")) { // 比如: redirect:fruit.do
            String redirectStr = methodReturnStr.substring("redirect:".length());
            resp.sendRedirect(redirectStr);
          } else {
            super.processTemplate(methodReturnStr, req, resp);
          }
        }
      }

    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }
}
