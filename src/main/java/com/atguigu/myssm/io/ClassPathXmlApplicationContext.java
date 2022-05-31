package com.atguigu.myssm.io;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ClassPathXmlApplicationContext implements BeanFactory {

  private Map<String, Object> beanMap = new HashMap<>();

  public ClassPathXmlApplicationContext() {
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
          Class beanClass = Class.forName(className);
          // 创建bean实例
          Object beanObj = beanClass.newInstance();
          // 将bean实例对象保存到map容器中
          beanMap.put(beanId, beanObj);
          // 到目前为止，此处需要注意的是，bean和bean之前的依赖关系还没有设置
        }
      }
      // 5. 组装bean之间的依赖关系
      for (int i = 0; i < beanNodeList.getLength(); ++i) {
        Node beanNode = beanNodeList.item(i);
        if (beanNode.getNodeType() == Node.ELEMENT_NODE) {
          Element beanElement = (Element) beanNode;
          String beanId = beanElement.getAttribute("id");
          // 根据beanId获取beanMap中的bean
          Object beanObj = beanMap.get(beanId);
          Class beanClazz = beanObj.getClass();
          NodeList beanChildNodeList = beanElement.getChildNodes();
          for (int j = 0; j < beanChildNodeList.getLength(); ++j) {
            Node beanChildNode = beanChildNodeList.item(j);
            if (beanChildNode.getNodeType() == Node.ELEMENT_NODE && "property".equals(
                beanChildNode.getNodeName())) {
              Element propertyElement = (Element) beanChildNode;
              String propertyName = propertyElement.getAttribute("name");
              String propertyRef = propertyElement.getAttribute("ref");
              // 1) 找到propertyRef对应的实例
              Object refObj = beanMap.get(propertyRef);
              // 2) 将refObj设置到当前bean对应的实例的property属性上去
              Field field = beanClazz.getDeclaredField(propertyName);
              field.setAccessible(true);
              field.set(beanObj, refObj);
            }
          }
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
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    } finally {
      try {
        inputStream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public Object getBean(String id) {
    return beanMap.get(id);
  }
}
