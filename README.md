# 配置中心

1. 简介
> 配置中心现在基本上是大型互联网公司的标配，用于存储管理公司内部各个系统的配置，降低维护成本。本配置中心提供了配置管理基本功能，提供配置更新推送能力，提供客户端配置缓存能力。

2. 环境要求：
> * 服务端：jdk1.8
> * 客户端：jdk1.8

> 注意：本系统还未上传到maven中央库（近期会上传）

### 1. 整体设计
配置就是不同应用在不同环境的一些键值对。本配置中心内的角色有：服务端、客户端、zookeeper。

服务端：管理不同应用在不同环境中的配置，配置数据落地到MySQL数据库。为客户端提供http查询某个应用在某个环境的配置。当一个应用在某个环境中的配置进行了更新（增删改），则服务端会将zookeeper上的对应的节点进行更新（客户端会监听对应节点）。

客户端：客户端刚启动时会通过http请求调用服务端读取当前应用在当前环境中的配置。如果从服务端查询失败，则客户端会尝试从本地缓存文件中读取配置，如果本地无缓存文件，则会抛出异常。客户端启动成功后，外部可以触发客户端向zookeeper上注册监听器，监听当前应用和当前应环境在zookeeper中的节点。当节点有更新，则客户端会通过http请求调用服务端读取最新配置，并且将最新配置和当前客户端中旧配置进行比较，将变化部分通知给当前应用。

zookeeper：仅仅作为通知工具，并不存储任何配置。当配置有变更，服务端会更新zookeeper的对应节点。节点有更新，则zookeeper会通知监听这个节点的监听器（客户端）。客户端收到通知后就会调用服务端查询最新配置。

应用编码为common的应用会作为公共配置，所有应用的配置都会继承公共配置。同时每个应用可以将部分配置设置为公开，这样其他应用可以读取这个应用的公开配置（对于一个应用需要写客户端给其他应用使用时，这个特性特别好用）。

![image](http://note.youdao.com/yws/api/personal/file/1B8BF06B204942A292E2268381A3F54E?method=download&shareKey=e0553eddaa6d5758c4a48c7fe104eeb5)

### 2. 启动服务端
服务端使用的springboot，直接命令启动jar包即可。（等上传到maven中央库后再详细介绍）

### 3. 集成客户端

1. 引入客户端依赖

        <dependency>
            <groupId>org.antframework.configcenter</groupId>
            <artifactId>configcenter-client</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>

2. 使用客户端
客户端就是Java类，直接new就可以，只是需要传给它相应参数。


        // 设置初始化参数
        ConfigContext.InitParams initParams = new ConfigContext.InitParams();
        initParams.setProfileCode("dev");  // 环境编码（在服务端配置）
        initParams.setAppCode("scbfund");  // 应用编码（在服务端配置） 
        initParams.setQueriedAppCode("scbfund");  // 被查询应用编码（在服务端配置。当前应用可能需要查询其他应用的配置，需传对应应用的编码，只能查询到其他应用公开的配置）
        initParams.setServerUrl("http://localhost:8080");  // 服务端地址
        initParams.setCacheFilePath("/var/config/scbfund.properties");  // 配置缓存文件路径
        initParams.setZkUrl("localhost:2181");  // zookeeper地址
        // 启动客户端（启动时会读取配置，读取不成功会抛异常。一个应用可以new多个客户端，各个客户端之间互不影响）
        ConfigContext configContext = new ConfigContext(initParams);
        // 触发客户端向zookeeper注册监听器
        configContext.listenConfigModified();
        // 手动触发客户端刷新配置（异步），可以不用手动触发，在此只是演示下
        configContext.refreshConfig();

        // 客户端启动好了，现在可以获取配置了，调用configContext.getProperties()
        // 比如需要和spring集成的话，可以在spring启动前将客户端包装成Environment的一个属性资源，这样配置中心里的配置就可以应用的spring了

        // 当要关闭当前系统时，调用下面方法会关闭客户端释放相关资源（zookeeper链接，http客户端）。
        // 想省事的话，可以直接将客户端注入到spring容器，spring容器在关闭时会自动调用close方法.
        configContext.close();
        
### 4. 管理配置
后台管理中管理员有两种：超级管理员、普通管理员。超级管理员可以管理所有配置，也可以管理其他管理员；普通管理员只能管理分配给他的应用的配置。

页面挺丑的，但功能是全的。

登录链接：http://localhost:8080/html/login.html
第一次使用时会让你设置一个超级管理员：
![image](http://note.youdao.com/yws/api/personal/file/85F715EF5C574FAC866F327D7D35396E?method=download&shareKey=2dd73f83d6700c3651513834078e5739)

然后进行登陆进入管理页面：
![image](http://note.youdao.com/yws/api/personal/file/BCC71043C36A4B1694DAFD6058652AA1?method=download&shareKey=e14cd88177df559477464d4a71f2c7eb)

点击相应按钮进入进入相应页面，在此仅展示配置管理页面（点击上图中的环境链接）
![image](http://note.youdao.com/yws/api/personal/file/EDEF433FBF2F4F109F44D952B2A43249?method=download&shareKey=ea2c3fc801049b76128c6b6ffc4ec261)
