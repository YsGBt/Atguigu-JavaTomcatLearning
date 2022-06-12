//////////
1. 设置编码
  1) get请求方式：
    // get方式下目前不需要设置编码（基于tomcat8）
    如果是get请求发送的中文数据，转码稍微有点麻烦（tomcat8之前）
    String fname = req.getParameter("fname");
    1. 将字符串打散成字节数组
    byte[] bytes = fname.getBytes("ISO-8859-1");
    2. 将字节数组按照设定的编码重新组装成字符串
    fname = new String(bytes, "UFT-8");
  2) post请求方式
    // post方式下，设置编码，防止中文乱码
    // 需要注意的是，设置编码这一句代码必须在所有的获取参数动作之前
    req.setCharacterEncoding("UTF-8");

//////////
2. Servlet的继承关系 - 重点查看的是服务方法(service(request, response))
  1) 继承关系
    javax.servlet.Servlet接口
      javax.servlet.GenericServlet抽象类
        javax.servlet.http.HttpServlet抽象子类

  2) 相关方法
    javax.servlet.Servlet接口:
      void init(config) - 初始化方法
      void service(request, response) - 服务方法
      void destroy() - 销毁方法

    javax.servlet.GenericServlet抽象类:
      void service(request, response) - 仍然是抽象的

    javax.servlet.http.HttpServlet抽象子类:
      void service(request, response) - 不是抽象的
      1. String method = req.getMethod(); 获取请求的方式 post/get
      2. 各种if判断，根据请求方式不同，决定去调用不同的do方法
        if (method.equals("GET")) {
          this.doGet(req, resp);
        } else if (method.equals("HEAD")) {
          this.doHead(req, resp);
        } else if (method.equals("POST")) {
          this.doPost(req, resp);
        } ...
      3. 在HttpServlet这个抽象类中，do方法都差不多:
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
          String protocol = req.getProtocol();
          String msg = lStrings.getString("http.method_get_not_supported");
          if (protocol.endsWith("1.1")) {
            resp.sendError(405, msg);
          } else {
            resp.sendError(400, msg);
          }
        }

 3) 小结
  1. 继承关系: HttpServlet -> GenericServlet -> Servlet
  2. Servlet中的核心方法: init(), service(), destroy()
  3. 服务方法: 当有请求过来时，service方法会自动响应(其实是tomcat容器调用的)
    在HttpServlet中我们会去分析请求的方式: 到底是get、post、head还是delete等
    然后再决定调用的是哪个do开头的方法
    那么在HttpServlet中这些do方法默认都是405的实现风格 - 要我们子类去实现对应的方法，否则默认会报405错误
  4. 因此，我们在新建Servlet时，我们才回去考虑请求方法，从而决定重写哪个do方法

//////////
3. Servlet的生命周期
  1) 生命周期: 从出生到死亡的过程就是生命周期。对应Servlet中的三个方法: init(), service(), destroy()
  2) 默认情况下:
    第一次接收请求时，这个Servlet会进行实例化(调用构造方法)、初始化(调用init())、然后服务(调用service())
    从第二次请求开始，每一次都是服务(调用service())
    当容器关闭时，其中的所有的Servlet实例会被销毁，调用销毁方法(调用destroy())
  3) - 通过案例我们发现: Servlet实例tomcat只会创建一个，所有的请求都是这个实例去响应。
     - 默认情况下，第一次请求时，tomcat才会去实例化，初始化，然后再服务。
        - 好处: 提高系统的启动速度
        - 坏处: 第一次请求时耗时较长
     - 因此得出结论: 如果需要提高系统的启动速度，使用默认
                   如果需要提高响应速度，应该设置Servlet的初始化时机
  4) Servlet的初始化时机:
     - 默认是第一次接收请求时，实例化、初始化
     - 我们可以通过 <load-on-startup>1</load-on-startup> 来设置Servlet启动的先后顺序，数字越小，启动越靠前，最小值0
  5) Servlet在容器中: 单例的、线程不安全的
    - 单例: 所有的请求都是同一个实例去响应
    - 线程不安全: 一个线程需要根据这个实例中的某个成员变量值去做逻辑判断。但是在中间某个时机，另一个线程改变另这个成员变量的值，
    从而导致第一个线程的执行路径发生了
    - 我们已经知道了Servlet是线程不安全的，给我们的启发是: 尽量不要在Servlet中定义成员变量。
    如果不得不定义成员变量，那么：
      a. 不要去修改成员变量的值
      b. 不要去根据成员变量的值去做逻辑判断
  6) Servlet3.0开始支持注解L @WebServlet("/...")

//////////
4. Http协议
  1) Http 称之为 超文本传输协议
  2) Http 是无状态的
  3) Http 请求响应包含两个部分: 请求和响应
    - 请求(包含三个部分):
      a. 请求行(展示当前请求的最基本信息) POST/dynamic/target.jsp HTTP/1.1 请求方式/访问地址URL HTTP协议的版本
      b. 请求头(包含了很多客户端需要告诉服务器的信息，比如: 我的浏览器型号、版本、能接收的类型、发送内容的类型、内容长度等)
      c. 请求体:
        get方式，没有请求体，但是有一个queryString
        post方式，有请求体，form data
        json格式，有请求体，request payload

    - 响应(包含三个部分):
      a. 响应行(包含三个信息) 1.协议(HTTP/1.1) 2.响应状态码(200) 3.响应状态(ok)
      b. 响应头: 包含了服务器的信息；服务器发送给浏览器的信息(内容的媒体类型、编码、内容长度等)
      c. 响应体: 响应的实际内容(比如请求add.html页面时，响应的内容就是<html>...</html>)

//////////
5. 会话
  1) Http 是无状态的: 服务器无法判断两次请求是同一个客户端发过来的，还是不同的客户端发过来的
    - 无状态带来的现实问题: 第一次请求是添加商品到购物车，第二次请求是结账；如果这两次请求服务器无法区分是同一个用户的，那么就会导致混乱
    - 通过会话跟踪技术来解决无状态的问题

  2) 会话跟踪技术
    - 客户端第一次发送请求给服务器，服务器获取session，获取不到，则创建新的，然后响应给客户端
    - 下次客户端给服务器发请求时，会把sessionID带给服务器，那么服务器就饿能获取到了，那么服务器就判断这一次请求和
    上次某次请求是同一个客户端，从而能够区分开客户端
    - 常用的API:
      request.getSession() / request.getSession(true) -> 获取当前的会话，没有则创建一个新的会话
      request.getSession(false) -> 获取当前的会话，没有则返回null，不会创建新的

      session.getId() -> 获取sessionID
      session.isNew() -> 判断当前session是否是新的
      session.getMaxInactiveInterval() -> session的非激活间隔时常，默认1800秒
      session.setMaxInactiveInterval() -> 设置session的非激活间隔时常
      session.invalidate() -> 强制性让会话立即失效

  3) session保存作用域(session.setAttribute(k, v))
    - session保存作用域是和具体的某一个session对应的
    - 常用的API:
      void session.setAttribute(k, v)
      Object session.getAttribute(k)
      void session.removeAttribute(k)

//////////
6. 服务器内部转发以及客户端重定向
  1) 服务器内部转发: request.getRequestDispatcher("...").forward(request, response);
    - 一次请求响应的过程，对于客户端而言，内部经过多少次转发，客户端是不知道的
    - 地址栏没有变化
      客户端    ------request------> Servlet1
        ^                              | request dispatcher
        |                              v
        |----------response-------- Servlet2
      注意: 客户端不知道是Servlet2进行的response，只知道request from Servlet1

  2) 客户端重定向: response.sendRedirect("...");
    - 两次请求响应的过程。客户端肯定知道URL有变化
    - 地址栏有变化
     |-----| ------request1------->  |--------|
     |客户端| <----sendRedirect-----  |Servlet1|
     |-----|                         |--------|
       ^ |
       | |
       | |--------request2--------> |--------|
       |-----------response-------- |Servlet2|
                                    |--------|

//////////
7. Thymeleaf - 视图模版技术
  1) 添加thymeleaf的jar包

  2) 在web.xml文件中添加配置 <context-param>
    - 配置前缀   view-prefix
    - 配置后缀   view-suffix

  3) 新建一个Servlet类ViewBaseServlet

  4) 使得我们的Servlet继承ViewBaseServlet

  5) 根据 逻辑视图名称 得到 物理视图名称
  // 此处的视图名称是 index
  // 那么thymeleaf会将这个 逻辑视图名称 对应到 物理视图名称 上去
  // 逻辑视图名称: index
  // 物理视图名称: view-prefix + 逻辑视图名称 + view-suffix
  // 所以真实的视图名称是: /index.html
  super.processTemplate("index", req, resp);

  6) 使用thymeleaf的标签
  th:if , th:unless , th:each , th:text , th:href="@{}"

//////////
8. 保存作用域
  1) 原始情况下，保存作用域我们可以认为有四个:
    - page(页面级别，现在几乎不用)
    - request(一次请求响应范围)
    - session(一次会话范围)
    - application(一次应用程序范围内有效)  即tomcat启动到tomcat停止

//////////
9. 路径问题
  1) 相对路径
  2) 绝对路径

//////////
10. 实现库存系统的功能
  1) 最初的做法是: 一个请求对应一个Servlet，这样存在的问题是Servlet太多了

  2) 把一系列的请求都对应成一个Servlet， IndexServlet/AddFruitServlet/EditServlet/DeleteServlet/UpdateServlet -> 合并成FruitServlet
     通过一个operate的值来决定调用FruitServlet中的哪一个方法
     使用的是switch-case

  3) 在上一个版本中，Servlet中充斥着大量的switch-case，试想一下，随着我们的项目业务扩大，那么会有很多的Servlet，
     也就意味着会有很多的switch-case，这是一种代码冗余
     因此，我们在Servlet中使用了反射技术，我们规定operate的值和方法名一致，那么接收到operate的值是什么就表明我们需要调用
     对应的方法进行响应，如果找不到对应的方法，则抛出异常

  4) 在上一个版本中我们使用了反射技术，但是其实还是存在一定的问题: 每一个Servlet中都有类似的反射技术的代码。
     因此继续抽取，设计了中央控制器类: DispatcherServlet
     DispatcherServlet这个类的工作分为两大部分:

      1. 根据url定位到能够处理这个请求的controller组件:
          1) 从url中提取ServletPath: /fruit.do -> fruit
          2) 根据fruit找到对应的组件: FruitController，这个对应的依据我们存储在applicationContext.xml中
              <bean id="fruit" class="com.atguigu.controllers.FruitControllerOld"/>
              通过DOM技术我们去解析XML文件，在中央控制器中形成一个beanMap容器，用来存放所有的Controller组件
          3) 根据获取到的operate的值定位到我们FruitController中需要调用的方法

      2. 调用Controller组件中的方法:
          1) 获取参数
              获取即将要调用的方法的参数签名信息: Parameter[] parameters = method.getParameters();
              通过parameter.getName()获取参数的名称
              通过parameter.getType()获取参数的类型 (我们需要考虑参数的类型问题，需要做类型转化的工作)
              准备了Object[] parameterValues 这个数组用来存放对应参数的参数值
          2) 执行方法
              Object methodReturnObj = method.invoke(Object controllerBean, Object... parameterValues);
          3) 视图处理
              String methodReturnStr = (String) returnObj;
              if (methodReturnStr.startsWith("redirect:")) {
                String redirectStr = methodReturnStr.substring("redirect:".length());
                resp.sendRedirect(redirectStr);
              } else {
                super.processTemplate(methodReturnStr, req, resp);
              }

//////////
11. 再次学习Servlet的初始化方法init()
  1) Servlet生命周期: 实例化、初始化、服务、销毁
  2) Servlet中的初始化方法有两个: init(), init(ServletConfig config)
     其中带参数的方法代码如下:
     public void init(ServletConfig config) throes ServletException {
      this.config = config;
      this.init();
     }
     另一个无参的init方法如下:
     public void init() throes ServletException {
     }
     如果我们想要在Servlet初始化时做一些准备工作，那么我们可以去重写init()方法
     我们可以通过如下步骤去获取初始化设置的数据
     - 获取config对象: ServletConfig config = getServletConfig();
     - 获取初始化参数值: config.getInitParameter(key);
  3) 在web.xml文件中配置Servlet
    <servlet>
      <servlet-name>DemoInitServlet</servlet-name>
      <servlet-class>com.atguigu.servlets.DemoInitServlet</servlet-class>
      <init-param>
        <param-name>hello</param-name>
        <param-value>world</param-value>
      </init-param>
      <init-param>
        <param-name>uname</param-name>
        <param-value>Steven</param-value>
      </init-param>
    </servlet>
    <servlet-mapping>
      <servlet-name>DemoInitServlet</servlet-name>
      <url-pattern>/demoInit</url-pattern>
    </servlet-mapping>
  4) 或者也可以通过注解的方式进行配置:
  @WebServlet(urlPatterns = {"/demoInit", "/demoInit2"},
      initParams = {
          @WebInitParam(name = "hello", value = "world"),
          @WebInitParam(name = "uname", value = "Steven")
      })

//////////
12. 学习Servlet中的ServletContext和<context-param>
  1) 获取ServletContext，有很多方法
     在初始化方法中: ServletContext servletContext = getServletContext();
     在服务方法中也可以通过request对象获取 ServletContext servletContext = req.getServletContext();
     也可以通过session获取 ServletContext servletContext = session.getServletContext();
  2) 获取初始化值:
     servletContext.getInitParameter();

//////////
13. 什么是业务层
  1) Model1 和 Model2
  MVC: Model (模型), View (视图), Controller (控制器)
  视图层: 用于做数据展示以及和用户交互的一个界面
  控制层: 能够接受客户端的请求，具体的业务功能还是需要借助于模型组件来完成
  模型层: 模型分为很多种: 有比较简单的pojo/vo(value object)，有业务模型组件，有数据访问层组件
    1) pojo/vo (Plain Old Java Object): 值对象
    2) DAO (Data Access Object): 数据访问对象
    3) BO (Business Object): 业务对象
    4) DTO (Data Transfer Object): 数据传输对象

  2) 区分业务对象和数据访问对象:
    1. DAO中的方法都是单精度方法(细粒度方法)。什么叫单精度?一个方法只考虑一个操作，比如添加，那就是insert操作，查询那就是select操作...
    2. BO中的方法属于业务方法，而实际的业务是比较负载的，因此业务方法的粒度是比较粗的(粗粒度方法)。
      注册这个功能属于业务功能，也就是说注册这个方法属于业务方法。
      那么这个业务方法中包含了多个DAO方法。也就是说注册这个业务功能需要通过多个DAO方法的组合调用，从而完成注册功能的开发。
      注册:
        1. 检查用户名是否已经被注册 - DAO中的select操作
        2. 向用户表新增一条新用户记录 - DAO中的insert操作
        3. 向用户积分表新增一条记录 (新用户默认初始化积分100分) - DAO中的insert操作
        4. 向系统消息表新增一条记录 (某某某新用户注册了，需要根据通讯录信息向他的联系人推送消息) - DAO中的insert操作
        5. 向系统日志表新增一条记录 (某用户在某IP在某年某月某日注册) - DAO中的insert操作
        6. ...

  3) 在库存系统中添加业务层组件

//////////
14. IOC
  1) 耦合/依赖 (Dependency)
    依赖指的是某某某离不开某某某
    在软件系统中，层与层之间是存在依赖的。我们也称之为耦合。
    我们系统架构或者设计的一个原则是: 高内聚低耦合。
    层内部的组成应该是高度聚合的，而层与层之间的关系应该是低耦合的，最理想的情况是0耦合(就是没有耦合)

  2) IOC - 控制反转 / DI - 依赖注入
     控制反转:
      1. 之前在Servlet中，我们创建Service对象，FruitService fruitService = new FruitServiceImpl();
         这句话如果出现在Servlet中的某个方法内部，那么这个fruitService的作用域(生命周期)应该就是这个方法级别；
         如果这句话出现在Servlet的类中，也就是说fruitService是一个成员变量，那么这个fruitService的作用域(生命周期)应该就是这个Servlet实例级别
      2. 之后我们在applicationContext.xml中定义了这个fruitService。然后通过解析XML，产生fruitService实例，存放在beanMap中，
         这个beanMap在一个BeanFactory中。
         因此，我们转移(改变)了之前的Service实例、DAO实例等等他们的生命周期。控制权从程序员转移到BeanFactory。这个现象我们称之为控制反转

     依赖注入：
      1. 之前我们在控制层出现代码: FruitService fruitService = new FruitServiceImpl();
         那么，控制层Controller和Service层存在耦合
      2. 之后，我们将代码修改成 FruitService fruitService = null;
         然后，在配置文件中配置:
         <bean id="fruit" class="FruitController">
            <property name="fruitService" ref="fruitService"/>
         </bean>

//////////
15. 过滤器Filter
    1) Filter也属于Servlet规范
    2) Filter开发步骤: 新建类实现Filter接口，然后实现其中的三个方法: init(), doFilter(), destroy()
       配置Filter，可以用注解@WebFilter()，也可以使用xml文件 <filter> <filter-mapping>
    3) Filter在配置时，和Servlet一样，也可以配置通配符，例如 @WebFilter("*.do")表示拦截所有以.do结尾的请求
    4) 过滤器链
      1) 执行的顺序依次是 A B C demoFilter C2 B2 A2
      2) 如果采取的是注解的方式进行配置，那么过滤器链的拦截顺序是按照全类名的先后顺序排序的
      3) 如果采取的是xml的方式进行配置，那么按照配置的先后顺序进行排序

//////////
16. 事务管理 (TransActionManager, ThreadLocal, OpenSessionInViewFilter)
    1) 涉及到的组件:
       - OpenSessionInViewFilter
       - TransActionManager
       - ThreadLocal
       - ConnUtil
       - BaseDAO

    2) ThreadLocal (本地线程)
       - get(), set(Object obj)
       - 我们可以通过set方法在当前线程上存储数据，通过get方法在当前线程上获取数据
       - set方法源码分析:
       public void set(T value) {
           Thread t = Thread.currentThread(); // 获取当前线程
           ThreadLocalMap map = getMap(t); // 每一个线程都维护各自的一个容器(ThreadLocalMap)
           if (map != null) {
               map.set(this, value); // 这里的key对应的是ThreadLocal，因为我们的组件中需要传输(共享)的对象可能会有多个(不止connection一个)
           } else {
               createMap(t, value); // 默认情况下map是没有初始化的，那么第一次望重添加数据时，会去初始化
           }
       }
       - get方法源码分析:
       public T get() {
           Thread t = Thread.currentThread(); // 获取当前线程
           ThreadLocalMap map = getMap(t); // 获取和这个线程相关的ThreadLocalMap
           if (map != null) {
               ThreadLocalMap.Entry e = map.getEntry(this); // this指的是ThreadLocal对象，通过它才能知道是哪一个工作纽带
               if (e != null) {
                   @SuppressWarnings("unchecked")
                   T result = (T)e.value; // entry.value就可以获取到value
                   return result;
               }
           }
           return setInitialValue();
       }

//////////
17. 监听器 (Listener, ContextLoaderListener)
  1) ServletContextListener - 监听ServletContext对象的创建和销毁的过程
     - 应用: ContextLoaderListener
  2) HttpSessionListener - 监听HttpSession对象的创建和销毁的过程
  3) ServletRequestListener - 监听ServletRequest对象的创建和销毁过程

  4) ServletContextAttributeListener - 监听ServletContext的保存作用域的改动(add,remove,replace)
  5) HttpSessionAttributeListener - 监听HttpSession的保存作用域的改动(add,remove,replace)
  6) ServletRequestAttributeListener - 监听ServletRequest的保存作用域的改动(add,remove,replace)

  7) HttpSessionBinding - 监听某个对象在Session域中的创建与移除
  8) HttpSessionActivationListener - 监听某个对象在Session域中的序列号与反序列化

//////////
18. Cookie
    1) 创建Cookie对象

    2) 在客户端保存Cookie

    3) 设置Cookie的有效时常
       cookie.setMaxAge(60), 设置cookie的有效时常为60秒
      - 会话级Cookie
        服务器端并没有明确指定Cookie的存在时间
        在浏览器端，Cookie数据存在于内存中
        只要浏览器还开着，Cookie数据就一直都在
        浏览器关闭，内存中的Cookie数据就会被释放
      - 持久化Cookie
        服务器端明确设置了Cookie的存在时间
        在浏览器端，Cookie数据会被保存到硬盘上
        Cookie在硬盘上存在的时间根据服务器端限定的时间来管控，不受浏览器关闭的影响
        持久化Cookie到达了预设的时间会被释放

    4) 设置Cookie的domain和path
       cookie.setDomain(pattern);
       cookie.setPath(uri);

    5) Cookie的应用:
       - 记住用户名和密码10天: cookie.setMaxAge(60 * 60 * 24 * 10)
       - 十天免登录

//////////
19. Kaptcha
    1) 为什么需要验证码: 防止恶意请求
    2) Kaptcha如何使用:
       - 添加jar
       - 在web.xml文件中注册KaptchaServlet, 并设置验证码图片的相关属性
       - 在html页面上编写一个img标签，然后设置src等于KaptchaServlet对应的url-pattern
    3) Kaptcha验证码图片的各个属性在常量接口: Constants中
    4) KaptchaServlet在生成验证码图片时，会同时将验证码信息保存到session中
       因此，我们在注册请求时，首先将用户文本框中输入的验证码值和session中保存的值进行比较，相等，则进行注册

//////////
20. JS - Exp

//////////
常见错误:
IllegalArgumentException: argument type mismatch


200: 正常响应
404: 找不到对应的资源
405: 请求方式不支持
500: 服务器内部错误
