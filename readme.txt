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
    - application(整个应用程序范围) 一次应用程序范围内有效

9. 路径问题
  1) 相对路径
  2) 绝对路径



200: 正常响应
404: 找不到对应的资源
405: 请求方式不支持
500: 服务器内部错误
