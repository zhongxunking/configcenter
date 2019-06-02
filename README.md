# 配置中心

1. 简介
> 配置中心现在基本上是大型互联网公司的标配，用于存储管理公司内部各个系统的配置，降低维护成本。本配置中心提供了：配置管理基本能力、配置发布回滚能力、配置更新推送能力、客户端配置缓存能力、对敏感配置设置访问权限能力。本配置中心的目标是让你能优雅的管理配置。

2. 环境要求
> - 服务端：jdk1.8
> - 客户端：jdk1.8
> - MySQL
> - Redis

> 注意：本系统已经上传到[maven中央库](http://search.maven.org/#search%7Cga%7C1%7Corg.antframework.configcenter)

3. 演示环境
> 地址：<a href="http://configcenter.antframework.org:6220" target="_blank">http://configcenter.antframework.org:6220</a><br/>
> 超级管理员账号：admin 密码：123 <br/>
> 普通管理员账号：normal 密码：123

4. 技术交流和支持
> 欢迎进技术交流群一起讨论（加我微信zhong_xun_）。如果本项目对你有帮助，欢迎Star和Fork。

## 1. 整体设计
配置就是不同应用在不同环境的一些键值对。
整体设计图：<br/>
<img src="https://note.youdao.com/yws/api/personal/file/WEB53a19fbcb9e34bdf6caa544e02172428?method=download&shareKey=9058ff8d692f8f68fb17df69a54d30fd" width=600 /><br/>

##### 本配置中心内的角色有：服务端、客户端、MySQL数据库、Redis、配置管理员

- 服务端：管理不同应用在不同环境中的配置，配置数据落地到MySQL数据库。为客户端提供http查询配置，为客户端提供Http Long Polling（长轮询）监听配置变更通知。当应用的配置有了变更（增删改），则会直接通知客户端。

- 客户端：客户端刚启动时会通过http请求服务端读取应用的最新配置。如果从服务端读取失败，则客户端会尝试从本地缓存文件中读取配置；如果本地无缓存文件，则启动失败。客户端启动成功后，会通过Http Long Polling（长轮询）监听服务端配置变更通知。当监听到配置有变更时，客户端会再次通过http请求服务端读取最新配置，然后把最新配置保存到缓存文件，最后将最新配置和当前客户端中旧配置进行比较，将变化部分通知给应用。

- MySQL：用于存储服务端管理的配置。

- Redis：有三个作用：1、缓存数据库存储的配置，用于提高系统响应效率；2、存储分布式session；3、小型MQ，配置中心服务端之间使用它进行通信。

- 配置管理员：管理配置

##### 配置可以从应用和环境两个纬度进行共享

- 应用树：应用树是各个应用之间的继承关系图，类似Java类的继承关系。应用可以多层继承，比如：customer（会员系统）--> core-domain（核心领域）--> common（公共配置）。应用的继承特性可以使不同应用之间共享配置。

- 环境树：环境树是各个环境之间的继承关系图，类似Java类的继承关系。环境可以多层继承，比如：featureTest1（特性测试环境1）--> test（测试环境）--> offline（线下环境）。环境的继承特性可以使一个应用在不同环境之间共享配置。不同集群在这里可以看成是不同的子环境，而不同集群间往往大部分配置是一样的，只有少数配置是不同的。有了继承特性，新增一个集群只需要进行继承，然后覆盖掉少数的不同配置就可以，而不需要从头开始建配置。
> 环境树存在的另一个原因：我们使用的环境变成树形结构可能更好。假设你在进行一个特性测试时，为了不影响公共测试环境，你需要单独部署一套独立的测试环境。可能你只修改了系统A，但系统A依赖了系统B和系统C，为了让系统A运行起来，你需要把系统B、C都部署起来。而系统B、C可能还会依赖其他系统，那么为了完整的测试你有可能会部署很多系统。如果你只是修改了系统A很小的一个功能点，为了测试再部署那么多套系统，那就显得得不偿失了。所以如果我们的环境是树形结构（有继承特性），那么你只需要在独立测试环境中部署系统A，当系统A需要调用系统B、C的服务时，只需要先判断当前环境中有没有B、C的服务，如果有则调用当前环境的服务；如果没有则调用父环境（公共测试环境）的服务。要实现这点还需要RPC框架的支持，支持也并不复杂：所有环境的服务注册到统一的一个服务注册中心，每个服务标识自己所在的是哪个环境，然后服务消费者在消费服务前，优先消费当前环境的服务，如果当前环境没有服务，则根据环境继承结构，依次往上查找可用的服务，直到找到为止，然后消费。利用RPC框架的扩展机制，可以完成上述功能。环境树怎么获取？可以直接调用配置中心获取。

##### 每个配置项可以有作用域：私有、可继承、公开

- 私有：私有的配置只能被当前应用读取，其他应用无法读取，类似Java类的private访问级别字段。这种作用域可以保护配置不被暴露到其他应用。

- 可继承：可继承的配置既能被当前应用读取，也能被子应用读取，类似Java类的protected访问级别字段。这种作用域方便让同一个继承体系的应用共享配置，也让配置共享不扩大到所有应用。

- 公开：公开的配置能被所有应用读取，类似Java类的public访问级别字段。这种作用域能方便有些配置可以被所有应用读取，但又不适合将这种配置放到公共配置里。比如当前应用需要提供一个客户端给其他应用使用，而且客户端需要读取当前应用的一些配置，那么这些配置就可以作为公开配置。

##### 每个配置项对普通管理员有访问权限：读写、只读、无
> 访问权限只约束普通管理员，不会约束超级管理员。运维人员使用超级管理员账号把敏感配置的权限设置为“只读”或“无”，然后给开发人员创建普通管理员账号，让开发人员也能查看和修改非敏感配置（比如一些业务开关配置）， 而不需要再通过运维人员。这样既提高了开发人员效率，也减轻了运维人员负担。

- 读写：普通管理员既能查看该配置项，也能修改该配置项。

- 只读：普通管理员只能查看该配置项，不能修改该配置项。

- 无：普通管理员既不能查看该配置项，也不能修改该配置项。

## 2. 部署服务端
[下载服务端](https://github.com/zhongxunking/configcenter/releases)

<span style="font-size: large">说明：</span>
- 出于安全考虑，当外网（包括配置管理员）尝试通过http请求访问服务端/config/*下的路径时，nginx应该进行拦截；而客户端通过内网访问/config/*路径时，nginx应该允许访问。
- 服务端使用的springboot进行开发，直接命令启动下载好的jar包即可，无需部署tomcat。
- 有两种方式创建数据库表，根据具体情况选择其中一种方式即可：1、手动执行[建表sql](https://github.com/zhongxunking/configcenter/wiki/v1.4.0.RELEASE%E6%95%B0%E6%8D%AE%E5%BA%93%E5%BB%BA%E8%A1%A8DDL)；2、让服务端拥有向数据库执行ddl语句权限，服务端第一次启动时会自动建表，无需手动执行sql。
- 服务端在启动时会在"/var/apps/"下创建日志文件，请确保服务端对该目录拥有写权限。
- 由于配置中心本身就是用来管理各个环境中的配置，所以大部分公司只需部署两套，一是线下环境配置中心（管理所有开发、测试等环境的配置）；二是线上环境配置中心（管理生产、预发布等环境的配置）。
- 服务端http端口为6220。

启动服务端命令模板：
```shell
nohup java -jar configcenter-1.5.4.RELEASE.jar --spring.profiles.active="online" --spring.datasource.url="数据库url" --spring.datasource.username="数据库用户名" --spring.datasource.password="数据库密码" --spring.redis.host="redis的地址" --spring.redis.port="redis的端口" &
```
比如我本地测试时启动命令：
```shell
nohup java -jar configcenter-1.5.4.RELEASE.jar --spring.profiles.active="online" --spring.datasource.url="jdbc:mysql://localhost:3306/configcenter-dev?useUnicode=true&characterEncoding=utf-8" --spring.datasource.username="root" --spring.datasource.password="root" --spring.redis.host="localhost" --spring.redis.port="6379" &
```
>  以上是最简版的启动命令脚本，真正部署时可自行进行丰富，比如限制内存大小等等。


## 3. 集成客户端
> 读者也可以先看后面的“[配置管理介绍](#4-配置管理介绍)”，再来看本部分的客户端介绍。

提供两种方式集成客户端：
- 直接集成客户端（非spring-boot应用）
- 通过starter进行集成（spring-boot应用）

### 3.1 直接集成客户端
客户端提供最核心也是最原子的能力。

#### 3.1.1 引入客户端依赖
```xml
<dependency>
  <groupId>org.antframework.configcenter</groupId>
  <artifactId>configcenter-client</artifactId>
  <version>1.5.4.RELEASE</version>
</dependency>
```
#### 3.1.2 使用客户端
客户端就是Java类，直接new就可以，只是需要传给它相应参数。一个应用可以创建多个客户端，每个客户端之间互不影响。
```java
// 创建客户端
ConfigsContext configsContext = new ConfigsContext(
        "customer",                 // 主体应用id
        "dev",                      // 环境id
        "http://localhost:6220",    // 服务端地址
        "/var/apps/configcenter");  // 缓存文件夹路径

// 获取会员系统的配置
Config customerConfig = configsContext.getConfig("customer");
// 现在就可以获取会员系统的所有配置项了（下面获取redis地址配置）
String redisHost = customerConfig.getProperties().getProperty("redis.host");

// 不仅可以获取会员系统的配置，还可以获取其他应用的配置，不过只能获取其他应用的公开配置，
// 因为当前主体应用为会员系统，现在是以会员系统为视角获取其他应用的配置
// 下面是获取账务系统的公开配置
Config accountConfig = configsContext.getConfig("account");

// 还可以注册配置变更监听器
customerConfig.getListeners().addListener(new org.antframework.configcenter.client.ConfigListener() {
    @Override
    public void onChange(List<ChangedProperty> changedProperties) {
        for (ChangedProperty changedProperty : changedProperties) {
            logger.info("监听到会员系统的配置有变更：{}", changedProperty);
        }
    }
});
// 开启监听服务端的配置
configsContext.listenServer();

// 系统正常运行...

// 当系统运行结束时，需关闭客户端释放相关资源
configsContext.close();
```

### 3.2 通过starter进行集成
starter本质上还是依赖于上面介绍的客户端的能力，只不过根据spring-boot场景提供了更优雅的集成方式，也提供了更便捷的功能（可自动刷新@Value占位符和@ConfigurationProperties配置类）。
> 注意：本starter既支持SpringBoot2.x，也支持SpringBoot1.x

#### 3.2.1 引入starter依赖
- SpringBoot2.x应用引入：
```xml
<dependency>
  <groupId>org.antframework.configcenter</groupId>
  <artifactId>configcenter-spring-boot-starter</artifactId>
  <version>1.5.4.RELEASE</version>
</dependency>
```
- SpringBoot1.x应用引入：
```xml
<dependency>
    <groupId>org.antframework.configcenter</groupId>
    <artifactId>configcenter-spring-boot-starter</artifactId>
    <version>1.5.4.RELEASE</version>
    <exclusions>
        <exclusion>
            <groupId>org.hibernate.validator</groupId>
            <artifactId>hibernate-validator</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>5.3.6.Final</version>
</dependency>
```

#### 3.2.2 配置客户端
在应用的配置文件application.properties或application-xxx.properties中配置：
```properties
# 必填：应用id（配置key：spring.application.name或者configcenter.app-id）
spring.application.name=customer
# 必填：环境id（配置key：spring.profiles.active或者configcenter.profile-id）
spring.profiles.active=dev
# 必填：configcenter服务端的地址
configcenter.server-url=http://localhost:6220

# 选填：缓存目录（默认为：/var/apps/${appId}/configcenter）
configcenter.home=/tmp/configcenter
# 选填：是否开启定期同步服务端的配置（默认为开启）
configcenter.auto-refresh-configs.enable=true
# 选填：定期同步服务端的配置的周期（单位：毫秒。默认为5分钟同步一次）
configcenter.auto-refresh-configs.period=300000
# 选填：configcenter配置优先于指定的配置源（默认为最低优先级）。可填入：commandLineArgs（命令行）、systemProperties（系统属性）、systemEnvironment（系统环境）、random（随机数。比配置文件优先级高）等等
configcenter.prior-to=random

# 选填：是否开启自动刷新@Value占位符（默认为开启）
ant.env.refresh-placeholders.enable=true
# 选填：是否开启自动刷新@ConfigurationProperties配置类（默认为开启）
ant.env.refresh-properties.enable=true
# 选填：指定需自动刷新的@ConfigurationProperties配置类的全名（如果在配置类上已经打上@Refreshable注解，则可以不用在此配置，也会支持自动刷新）
ant.env.refresh-properties.refreshable-classes=com.demo.AProperties,com.demo.BProperties
```

#### 3.2.3 使用配置
可以通过spring的@Value注解、environment.getProperty(java.lang.String)获取配置，而不用直接使用客户端。也可以通过ConfigsContexts.getConfig(java.lang.String)获取配置。
```java
// 通过@Value获取配置（配置变更后，自动刷新redisHost字段）
@Value("redis.host")
private String redisHost;
@Autowired
private Environment environment;

public void doBiz() {
    // 通过environment获取配置
    String redisHostFromEnvironment = environment.getProperty("redis.host");
    // 通过ConfigsContexts.getConfig(java.lang.String)获取配置
    Config config = ConfigsContexts.getConfig("customer");
    String redisHostFromConfig = config.getProperties().getProperty("redis.host");
}
```

#### 3.2.4 自动刷新
当配置变更后，可以通过environment.getProperty(java.lang.String)和ConfigsContexts.getConfig(java.lang.String)获取到最新配置。同时默认情况下，也会自动刷新对应的@Value占位符和开启了刷新功能的@ConfigurationProperties配置类。
- 自动刷新@Value占位符
```java
// 配置变更后，自动刷新redisHost字段
@Value("redis.host")
private String redisHost;

// 配置变更后，自动重新调用setPort方法
@Value("redis.port")  
public void setPort(int port){
}
```
- 自动刷新@ConfigurationProperties配置类
```java
@ConfigurationProperties("myDemo")
@Refreshable  // 配置变更后，自动刷新（如果没有打上@Refreshable注解，则需要通过“ant.env.refresh-properties.refreshable-classes=com.demo.AProperties”指定本配置类需要被自动刷新）
public class AProperties {
    private String key1;
    private int key2;
    // 省略getter、setter
}
```

#### 3.2.5 配置变更监听器
可以监听当前应用的配置变更事件：
```java
// 监听当前应用的配置变更事件
@ConfigListener
public class MyConfigListener {
    // 监听所有配置
    @ListenConfigChanged(prefix = "")
    public void listenAll(List<org.antframework.boot.env.listener.ChangedProperty> changedProperties) {
        // TODO 具体业务代码
    }

    // 监听redis配置（prefix表示需要监听的配置前缀。当以“redis.”开头的配置项被修改时，
    // 被修改的配置会作为入参调用本方法。比如redis.host、redis.port等被修改时都会调用本方法）
    @ListenConfigChanged(prefix = "redis")
    public void listenRedis(List<org.antframework.boot.env.listener.ChangedProperty> changedProperties) {
        // TODO 具体业务代码
    }
    
    // 监听具体某一个配置项（注意：入参不再是List<ChangedProperty>，而是ChangedProperty）
    @ListenConfigChanged(prefix = "redis.host")
    public void listenRedisHost(org.antframework.boot.env.listener.ChangedProperty changedProperty) {
        // TODO 具体业务代码
    }
}
```
也可以监听其他应用的公开配置变更事件：
```java
// 监听账务系统的公开配置变更事件
@ConfigListener(appId = "account")
public class MyConfigListener {
    // 监听所有配置
    @ListenConfigChanged(prefix = "")
    public void listenAll(List<org.antframework.boot.env.listener.ChangedProperty> changedProperties) {
        // TODO 具体业务代码
    }
}
```

## 4. 配置管理介绍
进入服务端地址模板：http://IP地址:6220

> 以下截图有部分未更新，以实际使用为准。

4.1 第一次进入服务端需初始化一个超级管理员<br/>
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

4.16 修改配置后，点击"发布修改"按钮，进行发布（配置变更消息会自动推送给应用）<br/>
<img src="https://note.youdao.com/yws/api/personal/file/WEB9859bfdff9931bf59acec77a87150401?method=download&shareKey=f0d7b84c6a2e43891feb031631c53d7e" width=700 />

4.17 点击环境下拉框，可以进入其他环境的配置管理页面<br/>
<img src="https://note.youdao.com/yws/api/personal/file/WEBe6bcfbad97f20dace1222818fc84b6bd?method=download&shareKey=0eee229a85534056743549b3e8dd0ab4" width=700 />

4.18 点击发布历史按钮可以进入发布历史页面<br/>
<img src="https://note.youdao.com/yws/api/personal/file/WEB5cd652922609c5af5aacea6f2434d347?method=download&shareKey=30277cf118898877d7592f2d37020c97" width=700 />

4.19 在发布历史页面可看到所有的发布记录、每次发布变更的配置和所有配置<br/>
<img src="https://note.youdao.com/yws/api/personal/file/WEB752da51b5231383e705832bf0965cbfc?method=download&shareKey=194264a02909c72f810c1ed154950291" width=700 /><br/>
<img src="https://note.youdao.com/yws/api/personal/file/WEBc359a64fadc55361b4c29bcb09a102b6?method=download&shareKey=5ca7697f6dc495871c31fd52aacb19bd" width=700 />

4.20 在发布页面点击回滚按钮可进行回滚<br/>
<img src="https://note.youdao.com/yws/api/personal/file/WEB074072a5a66c98cf7c41cf7431f9a023?method=download&shareKey=f1035d61c8979ffa4479014285f583ce" width=700 />

4.21 点击左侧导航栏管理员，会进入管理员管理页面<br/>
<img src="https://note.youdao.com/yws/api/personal/file/WEBc90e826457e01113a16e40685a74d8d0?method=download&shareKey=3722d9627fb3a2f463616977a5c2ef65" width=700 />

4.22 点击上图的新增按钮，进行创建管理员。管理员分为两种：超级管理员和普通管理员。超级管理员拥有所有权限可以管理所有应用，普通管理员只能管理被分配给他的应用<br/>
<img src="https://note.youdao.com/yws/api/personal/file/WEBab9ca355c165d4872755d34a3caf67f4?method=download&shareKey=d2d3c3e543e0ae614129bdcec47eb783" width=700 />

4.22 点击左侧导航栏权限，会进入权限管理页面<br/>
<img src="https://note.youdao.com/yws/api/personal/file/WEB7af9cc8567530ff0f83e36a686db4046?method=download&shareKey=465a88d8ef824b260d400c2589ef64e3" width=700 />

4.24 点击新增按钮，可以将应用分配给管理员进行管理<br/>
<img src="https://note.youdao.com/yws/api/personal/file/WEB52963da9d8ffa4337864bf9cdb94529c?method=download&shareKey=a272a1da145a026b2cfaf8a77acca6e1" width=700 />

4.25 权限分配后如下：<br/>
<img src="https://note.youdao.com/yws/api/personal/file/WEBb4a1096eff078b09fcd4f511e0c8d614?method=download&shareKey=c785a881c4a1231c48696f3507a1e4f9" width=700 />
