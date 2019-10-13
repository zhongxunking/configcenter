# configcenter
1. 简介
> 配置中心现在基本上是大型互联网公司的标配，用于管理公司各个系统繁杂的配置，降低配置维护成本。configcenter是一款操作简单、维护成本低的配置中心。提供了统一的管理配置能力、配置发布回滚能力、配置更新秒级推送能力、客户端配置缓存能力、对敏感配置设置访问权限能力、管理员权限管理能力。

2. 环境要求
> - 服务端：jdk1.8、MySQL、Redis
> - 客户端：jdk1.8

> 注意：本系统已经上传到[maven中央库](http://search.maven.org/#search%7Cga%7C1%7Corg.antframework.configcenter)

3. 演示环境
> 地址：<a href="http://configcenter.antframework.org:6220" target="_blank">http://configcenter.antframework.org:6220</a><br/>
> 超级管理员账号：admin 密码：123 <br/>
> 普通管理员账号：normal 密码：123
<img src="https://note.youdao.com/yws/api/personal/file/WEB6292d40805cc0f62dde1b346a508d68d?method=download&shareKey=4cacc6700a80db6c3d43e0f4c935d1ca" width=700 />

# 特性
configcenter具备统一的管理配置能力、配置发布回滚能力、配置更新秒级推送能力、客户端配置缓存能力、对敏感配置设置访问权限能力、配置灰度发布能力、管理员权限管理能力。
* 统一的管理配置能力：提供配置管理页面，可管理不同应用在不同环境中的配置。
* 配置发布回滚能力：配置可一按照版本进行发布回滚。
* 配置更新秒级推送能力：新配置发布后可秒级的推送到应用。
* 客户端配置缓存能力：如果应用启动时无法连接服务端，可使用预先存储在缓存文件中的配置，保证应用正常启动。
* 对敏感配置设置访问权限能力：可对一些重要或敏感的配置设置访问权限，防止配置被泄漏。
* 配置灰度发布能力：满足各种配置灰度发布场景（传统部署方式、容器化部署方式、多重配置灰度发布等）。
* 管理员权限管理能力：可对不同管理员设置对应的配置管理权限，让合适的人管理合适的配置。

# Why configcenter
configcenter具备以下特点支持你选择它。
1. **简约**--configcenter从架构设计、服务端设计、客户端设计、数据库设计到页面设计，都遵从这个原则。
1. **部署简单**--configcenter只依赖MySQL和Redis，部署时只需一个jar包+MySQL+Redis。
1. **配置治理**--configcenter具有完善权限管理并且支持标记敏感配置，让你可以把非敏感配置放权给开发人员，让他们自助的管理配置，提高工作效率。
1. **配置共享**--configcenter支持应用、环境两个纬度的继承关系（配置共享），让你可以去设计你的配置，让它变得优雅易管理。从开发的角度来看，就像从面向过程开发迈入面向对象开发一样。
1. **灰度发布**--满足各种配置灰度发布场景（传统部署方式、容器化部署方式、多重配置灰度发布等）

# 文档
* 设计<br/>
&ensp;&ensp;[整体设计](https://github.com/zhongxunking/configcenter/wiki/%E6%95%B4%E4%BD%93%E8%AE%BE%E8%AE%A1)
* 部署<br/>
&ensp;&ensp;[部署服务端](https://github.com/zhongxunking/configcenter/wiki/%E9%83%A8%E7%BD%B2%E6%9C%8D%E5%8A%A1%E7%AB%AF)
* 使用<br/>
&ensp;&ensp;[管理配置](https://github.com/zhongxunking/configcenter/wiki/%E7%AE%A1%E7%90%86%E9%85%8D%E7%BD%AE)
* 开发<br/>
&ensp;&ensp;[集成Java客户端](https://github.com/zhongxunking/configcenter/wiki/%E9%9B%86%E6%88%90Java%E5%AE%A2%E6%88%B7%E7%AB%AF)<br/>
&ensp;&ensp;[服务端OpenAPI](https://github.com/zhongxunking/configcenter/wiki/%E6%9C%8D%E5%8A%A1%E7%AB%AFOpenAPI)

# 技术支持
欢迎加我微信入群交流。<br/>
<img src="https://note.youdao.com/yws/api/personal/file/WEBbca9e0a9a6e1ea2d9ab9def1cc90f839?method=download&shareKey=00e90849ae0d3b5cb8ed7dd12bc6842e" width=200 />

# Who is using
欢迎使用configcenter的组织在[这里](https://github.com/zhongxunking/configcenter/issues/3)进行登记（仅供其他用户参考）。