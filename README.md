# 配置中心

1. 简介
> 配置中心现在基本上是大型互联网公司的标配，用于存储管理公司内部各个系统的配置，降低维护成本。本配置中心提供了配置管理基本功能，提供配置更新推送能力，提供客户端配置缓存能力。

2. 环境要求：
> * 服务端：jdk1.8
> * 客户端：jdk1.8
> * zookeeper

> 注意：本系统已经上传到[maven中央库](http://search.maven.org/#search%7Cga%7C1%7Corg.antframework.configcenter)

## 1. 整体设计
配置就是不同应用在不同环境的一些键值对。本配置中心内的角色有：服务端、客户端、zookeeper。

- 服务端：管理不同应用在不同环境中的配置，配置数据落地到MySQL数据库。为客户端提供http查询应用在指定环境中的配置。当一个应用在指定环境中的配置有了变更（增删改），则服务端会通过zookeeper通知客户端。

- 客户端：客户端刚启动时会通过http请求服务端读取当前应用在当前环境中的最新配置。如果从服务端读取失败，则客户端会尝试从本地缓存文件中读取配置，如果本地无缓存文件，则会抛出异常。客户端启动成功后，外部可以触发客户端监听配置变更事件。当监听到配置有变更时，客户端会再次通过http请求服务端读取最新配置，然后把最新配置保存到缓存文件，最后将最新配置和当前客户端中旧配置进行比较，将变化部分通知给应用。

- zookeeper：仅仅作为通知工具，并不存储任何配置。当配置有变更，服务端会通知zookeeper，zookeeper接收到消息后会把消息分发给客户端，客户端收到消息后就会调用服务端读取最新配置。

整体设计图：<br/>
<div style="align: center">
    <img src="https://note.youdao.com/yws/api/personal/file/WEB1bad1efff9180e0438a1ee662f86cf32?method=download&shareKey=901c4091647b0b35967d8bbb5c92a5a7" width=600 />
</div>

## 2. 部署服务端
[下载服务端](https://repo.maven.apache.org/maven2/org/antframework/configcenter/configcenter-assemble/1.2.0.RELEASE/configcenter-assemble-1.2.0.RELEASE-exec.jar)。说明：
- 服务端使用的springboot，直接命令启动下载好的jar包即可，无需部署tomcat。
- 服务端使用hibernate自动生成表结构，无需导入sql。
- 服务端在启动时会在"/var/apps/"下创建日志文件，请确保服务端对该目录拥有写权限。
- 由于配置中心本身就是用来管理各个环境中的配置，所以大部分公司只需部署两套，一是线下环境配置中心（管理所有非线上环境配置）；二是线上环境配置中心（管理线上环境配置）。
- 线下环境编码：offline，线上环境编码：online（可以根据各公司自己情况自己定义，这里只是根据我个人习惯推荐的两个编码）。
- 服务端http端口为6220。

启动服务端命令模板：
```shell
java -jar configcenter-assemble-1.2.0.RELEASE-exec.jar --spring.profiles.active="环境编码" --spring.datasource.url="数据库连接" --spring.datasource.username="数据库用户名" --spring.datasource.password="数据库密码" --meta.zk-urls="配置中心使用的zookeeper地址,如果存在多个zookeeper以英文逗号分隔"
```
比如我本地开发时启动命令：
```shell
java -jar configcenter-assemble-1.2.0.RELEASE-exec.jar --spring.profiles.active="offline" --spring.datasource.url="jdbc:mysql://localhost:3306/configcenter-dev?useUnicode=true&characterEncoding=utf-8" --spring.datasource.username="root" --spring.datasource.password="root" --meta.zk-urls="localhost:2181"
```

## 3. 集成客户端
> 读者也可以先看后面的“[配置管理介绍](#4-配置管理介绍)”，再来看本部分的客户端介绍。

提供两种方式集成客户端：
- 直接集成客户端（非spring-boot应用）
- 通过starter进行集成（spring-boot应用）

### 3.1 直接集成客户端
客户端提供最核心也是最原子的能力，它不依赖于任何框架。

#### 3.1.1 引入客户端依赖
```xml
<dependency>
  <groupId>org.antframework.configcenter</groupId>
  <artifactId>configcenter-client</artifactId>
  <version>1.2.0.RELEASE</version>
</dependency>
```
#### 3.1.2 使用客户端
客户端就是Java类，直接new就可以，只是需要传给它相应参数。一个应用可以创建多个客户端，每个客户端之间互不影响。
```java
// 准备初始化参数
ConfigContext.InitParams initParams = new ConfigContext.InitParams();
initParams.setServerUrl("http://localhost:6220");   // 服务端地址
initParams.setMainAppId("customer");   // 主体应用id
initParams.setProfileId("dev");   // 环境id
initParams.setCacheDir("/var/config");   // 缓存文件夹路径
// 创建客户端
ConfigContext configContext = new ConfigContext(initParams);

// 获取会员系统的配置
Config customerConfig = configContext.getConfig("customer");
// 现在就可以获取会员系统的所有配置项了（下面获取redis地址配置）
String redisHost = customerConfig.getProperties().getProperty("redis.host");

// 不仅可以获取会员系统的配置，还可以获取其他应用的配置，不过只能获取其他应用的公开配置，
// 因为当前主体应用为会员系统，现在是以会员系统为视角获取其他应用的配置
// 下面获取账务系统的公开配置
Config accountConfig = configContext.getConfig("account");

// 还可以注册配置变更监听器
customerConfig.getListenerRegistrar().register(new ConfigListener() {
    @Override
    public void onChange(List<ChangedProperty> changedProperties) {
        for (ChangedProperty changedProperty : changedProperties) {
            logger.info("监听到会员系统的配置有变更：{}", changedProperty);
        }
    }
});
// 开启配置变更监听功能
configContext.listenConfigChanged();

// 系统正常运行...

// 当系统运行结束时，需关闭客户端释放相关资源
configContext.close();
```

### 3.2 通过starter进行集成
starter本质上还是依赖于上面介绍的客户端的能力，只不过根据spring-boot场景提供了更优雅的集成方式，也提供了更方便的功能。

#### 3.2.1 引入starter依赖
```xml
<dependency>
  <groupId>org.antframework.configcenter</groupId>
  <artifactId>configcenter-spring-boot-starter</artifactId>
  <version>1.2.0.RELEASE</version>
</dependency>
```

#### 3.2.2 配置客户端
在应用的配置文件application.properties或application-xxx.properties中配置：
```properties
# 必填：服务端地址
configcenter.server-url=http://localhost:6220
# 必填：当前应用id
configcenter.app-id=customer

# 选填：缓存文件夹路径（默认为：/var/apps/config）
configcenter.cache-dir=/var/apps/config
# 选填：是否关闭监听配置被修改（默认为开启）
configcenter.listen.disable=false
# 选填：配置刷新周期（单位：秒。默认为每5分钟刷新一次）
configcenter.refresh-period=300
# 选填：配置中心的配置优先于指定的配置源（默认为最低优先级）。可填入：commandLineArgs（命令行）、systemProperties（系统属性）、systemEnvironment（系统环境）、applicationConfigurationProperties（配置文件）等等
configcenter.prior-to=applicationConfigurationProperties
```

#### 3.2.3 使用配置
可以通过spring的@Value注解、environment.getProperty(java.lang.String)获取配置，而不用直接使用客户端。也可以通过ConfigContexts.getConfig(java.lang.String)获取配置。
```java
// 通过@Value获取配置
@Value("redis.host")
private String redisHost;
@Autowired
private Environment environment;

public void doBiz() {
    // 通过environment获取配置
    String redisHostFromEnvironment = environment.getProperty("redis.host");
    // 通过ConfigContexts.getConfig(java.lang.String)获取配置
    Config config = ConfigContexts.getConfig("customer");
    String redisHostFromConfig = config.getProperties().getProperty("redis.host");
}
```

#### 3.2.4 注册配置变更监听器
可以监听当前应用的配置变更事件：
```java
// 监听当前应用的配置变更事件
@ConfigListener
public class MyConfigListener {
    // 监听所有配置
    @ListenConfigChanged(prefix = "")
    public void listenAll(List<ChangedProperty> changedProperties) {
        // TODO 具体业务代码
    }

    // 监听redis配置（prefix表示需要监听的配置前缀。当以“redis.”开头的配置项被修改时，
    // 被修改的配置会作为入参调用本方法。比如redis.host、redis.port等被修改时都会调用本方法）
    @ListenConfigChanged(prefix = "redis")
    public void listenPool(List<ChangedProperty> changedProperties) {
        // TODO 具体业务代码
    }
    
    // 监听具体某一个配置项（注意：入参不再是List<ChangedProperty>，而是ChangedProperty）
    @ListenConfigChanged(prefix = "redis.host")
    public void listenPool(ChangedProperty changedProperty) {
        // TODO 具体业务代码
    }
}
```
也可以监听其他应用的公开配置变更事件：
```java
// 监听账务系统的配置变更事件
@ConfigListener(appId = "account")
public class MyConfigListener {
    // 监听所有配置
    @ListenConfigChanged(prefix = "")
    public void listenAll(List<ChangedProperty> changedProperties) {
        // TODO 具体业务代码
    }
}
```

## 4. 配置管理介绍
进入服务端地址模板：http://IP地址:6220

4.1 第一次进入服务端需设置一个超级管理员<br/>
<img src="https://note.youdao.com/yws/api/personal/file/WEBdf29971c7ad36423982fedfaa31af838?method=download&shareKey=71ab956ca03dad07319d6b83644d0286" width=500 />

4.2 点击确定，进入初始化页面<br/>
<img src="https://note.youdao.com/yws/api/personal/file/WEB92cb894d04f9f41bf6f9850d2520b73b?method=download&shareKey=12f38f49c1f525f1e0b9a045f0cfc34c" width=500 />

4.3 初始化完成后跳转回登录页面，进行登录<br/>
<img src="https://note.youdao.com/yws/api/personal/file/WEB118b26d609090fb3c2dffcc3f786130c?method=download&shareKey=4edee5f19ac0fb96b0e2ffa3d104eb2f" width=500 />

4.4 登录成功后，进入配置管理页面<br/>
<img src="https://note.youdao.com/yws/api/personal/file/WEBfa8a076427105e90dbb3c8fa4904e097?method=download&shareKey=52e74dbdf68d02cf3404e499e9fe71b2" width=700 />

4.5 点击左侧导航栏中的应用一栏，进行应用管理<br/>
<img src="https://note.youdao.com/yws/api/personal/file/WEB8ef4b088c07451869dce01b2ade19c26?method=download&shareKey=78f36137e5be5e9ff54c2aeee3349679" width=700 />

4.6 点击上图中新增按钮，进行创建应用<br/>
<img src="https://note.youdao.com/yws/api/personal/file/WEB1a1a355638f2426f744bddc169a6c978?method=download&shareKey=b12a276cad556b87f0ba6b00597b810c" width=700 />

4.7 应用有继承关系，而且可以多层继承，类似于java类的单继承，这样的设计可以让我们的配置项的暴露控制在合理的范围。比如下图中的customer（会员系统）--> core-domain（核心领域）--> common（公共配置）<br/>
<img src="https://note.youdao.com/yws/api/personal/file/WEB6f58c3b295f9c95da023a4eed1cf2270?method=download&shareKey=dfaf60b0597259c4fa5282f0d2857589" width=700 />

4.8 点击左侧导航栏中的环境一栏，进行环境管理<br/>
<img src="https://note.youdao.com/yws/api/personal/file/WEB3afdba599c0541969c5b5c9976c00f29?method=download&shareKey=a9c315863fc9a16830a9e82c741faf78" width=700 />

4.9 点击上图中新增按钮，进行创建环境<br/>
<img src="https://note.youdao.com/yws/api/personal/file/WEB99126b0ea4f9379e75ce8fe4b2cbcade?method=download&shareKey=68a7df5cb72a4ed7003a46e2c4ed1df8" width=700 />

4.10 环境管理<br/>
<img src="https://note.youdao.com/yws/api/personal/file/WEBb0ac9f801af62b860b67c8531e759734?method=download&shareKey=d1644223b0516671b3d8967ea4a0065f" width=700 />

4.11 点击左侧导航栏中的配置一栏，进行配置管理<br/>
<img src="https://note.youdao.com/yws/api/personal/file/WEB7eef4972bb9343f76827a098ecbf1937?method=download&shareKey=f137f98b62dc1c1013fc9a2ad82b2fd6" width=700 />

4.12 点击上图中的会员系统，进入配置key管理页面<br/>
<img src="https://note.youdao.com/yws/api/personal/file/WEB7efb5631bd4a679380aff0089182ebe3?method=download&shareKey=142f190c1aa4bdb3be22dcf18acf4df8" width=700 />

4.13 点击上图中的新增配置按钮，进行创建配置key。配置key是所有环境共享的，可以指定配置key的作用域：私有、可继承、公开（类似于java类的字段作用域：private、protected、public）<br/>
<img src="https://note.youdao.com/yws/api/personal/file/WEB1abc4434b4caf39971c96ed5268a95ad?method=download&shareKey=6c1c882f8b6e4666849d75846f84c6b9" width=700 />

4.14 应用会继承父应用的可继承和公开作用域的配置key。比如会员系统会继承core-domain（核心领域）、common（公共配置）的配置key<br/>
<img src="https://note.youdao.com/yws/api/personal/file/WEB432c0a5bf5c4e55a9dde085edd9e2b9a?method=download&shareKey=92b3e3063963929c9f5961fa55311acb" width=700 />

4.15 点击上图中的环境，进入对应环境的配置管理，可进行配置修改<br/>
<img src="https://note.youdao.com/yws/api/personal/file/WEB623aa7a707b02c190b9ae151a7af811a?method=download&shareKey=7f6b7b3b6c95294ba2de31ff8a2f19e6" width=700 />

4.16 修改配置后，点击提交修改按钮，进行真正修改（应用的配置会自动修改成最新配置）<br/>
<img src="https://note.youdao.com/yws/api/personal/file/WEB9859bfdff9931bf59acec77a87150401?method=download&shareKey=f0d7b84c6a2e43891feb031631c53d7e" width=700 />

4.17 点击环境下拉框中的环境，进入对应环境的配置管理页面<br/>
<img src="https://note.youdao.com/yws/api/personal/file/WEBe6bcfbad97f20dace1222818fc84b6bd?method=download&shareKey=0eee229a85534056743549b3e8dd0ab4" width=700 />

4.18 点击左侧导航栏管理员，会进入管理员管理页面<br/>
<img src="https://note.youdao.com/yws/api/personal/file/WEBc90e826457e01113a16e40685a74d8d0?method=download&shareKey=3722d9627fb3a2f463616977a5c2ef65" width=700 />

4.19 点击上图的新增按钮，进行创建管理员。管理员分为两种：超级管理员和普通管理员。超级管理员拥有所有权限可以管理所有应用，普通管理员只能管理被分配给他的应用<br/>
<img src="https://note.youdao.com/yws/api/personal/file/WEBab9ca355c165d4872755d34a3caf67f4?method=download&shareKey=d2d3c3e543e0ae614129bdcec47eb783" width=700 />

4.20 点击左侧导航栏权限，会进入权限管理页面<br/>
<img src="https://note.youdao.com/yws/api/personal/file/WEB7af9cc8567530ff0f83e36a686db4046?method=download&shareKey=465a88d8ef824b260d400c2589ef64e3" width=700 />

4.21 点击新增按钮，可以将应用分配置管理员进行管理<br/>
<img src="https://note.youdao.com/yws/api/personal/file/WEB52963da9d8ffa4337864bf9cdb94529c?method=download&shareKey=a272a1da145a026b2cfaf8a77acca6e1" width=700 />

4.22 权限分配后如下：<br/>
<img src="https://note.youdao.com/yws/api/personal/file/WEBb4a1096eff078b09fcd4f511e0c8d614?method=download&shareKey=c785a881c4a1231c48696f3507a1e4f9" width=700 />
