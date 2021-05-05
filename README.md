# configcenter
1. 简介
> 配置中心现在基本上是大型互联网公司的标配，用于管理公司各个系统繁杂的配置，降低配置维护成本。configcenter是一款操作简单、维护成本低的配置中心。提供了统一的管理配置能力、配置发布回滚能力、配置更新秒级推送能力、客户端配置缓存能力、对客户端验权防止生产环境配置泄漏风险的能力、对敏感配置设置访问权限能力、管理员权限管理能力。configcenter具备完善的权限管理和敏感配置标记能力，让你可以放心的将生产环境配置放权给开发人员，让他们自助的管理非敏感配置，提高工作效率。

2. 环境要求
> - 服务端：jdk1.8、MySQL、Redis
> - 客户端：jdk1.8

3. 演示环境
> 地址：http://configcenter.antframework.org:6220 <br/>
> 超级管理员账号：admin 密码：123 <br/>
> 普通管理员账号：normal 密码：123 <br/>

> 获取配置样例：http://configcenter.antframework.org:6220/config/findConfig?mainAppId=customer&queriedAppId=customer&profileId=dev

<img src="https://note.youdao.com/yws/api/personal/file/WEBd9e53ad6cc4303c226ad4cc0fa97beb6?method=download&shareKey=a585867fddafacb944df2d22607479cd" width=700 />

# 特性
configcenter具备统一的管理配置能力、配置发布回滚能力、配置更新秒级推送能力、客户端配置缓存能力、对客户端验权能力、对敏感配置设置访问权限能力、配置灰度发布能力、管理员权限管理能力。
* 统一的管理配置能力：提供配置管理页面，可管理不同应用在不同环境中的配置。
* 配置发布回滚能力：配置可一按照版本进行发布回滚。
* 配置更新秒级推送能力：新配置发布后可秒级的推送到应用。
* 客户端配置缓存能力：如果应用启动时无法连接服务端，可使用预先存储在缓存文件中的配置，保证应用正常启动。
* 对客户端验权能力：服务端具备对客户端验权能力，防止生产环境配置泄漏风险，生产环境配置不再裸奔。
* 对敏感配置设置访问权限能力：可对一些重要或敏感的配置设置普通管理员的访问权限，防止配置被泄漏。
* 配置灰度发布能力：借鉴git分支的思路，提供配置的分支管理，已满足各种配置灰度发布场景（传统部署方式、容器化部署方式、多重配置灰度发布等）。
* 管理员权限管理能力：可对不同管理员设置对应的配置管理权限，让合适的人管理合适的配置。

# Why configcenter
configcenter具备以下特点支持你选择它。
1. **部署简单**--configcenter只依赖MySQL和Redis，部署时只需一个jar包+MySQL+Redis。
1. **极低耦合**--configcenter的客户端对使用方的代码几乎没有入侵，便于你引入或移除configcenter。
1. **配置安全**--configcenter可对客户端验权、对敏感配置设置访问权限能力，配置不再裸奔，防止配置泄漏风险。
1. **配置治理**--configcenter具有完善权限管理并且支持标记敏感配置，让你可以把非敏感配置放权给开发人员，让他们自助的管理配置，提高工作效率。
1. **配置共享**--configcenter支持应用、环境两个纬度的配置共享，且支持多层配置继承。新增一个环境或集群时，你不再需要繁琐的复制配置，通过环境的配置继承特性，你可以一键创建新环境或集群，极其简单。
1. **灰度发布**--configcenter借鉴git分支的思路，提供配置的分支管理，满足各种配置灰度发布场景（传统部署方式、容器化部署方式、多重配置灰度发布等）

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
欢迎加我微信（zhong_xun_）入群交流。<br/>
<img src="https://note.youdao.com/yws/api/personal/file/WEBbca9e0a9a6e1ea2d9ab9def1cc90f839?method=download&shareKey=00e90849ae0d3b5cb8ed7dd12bc6842e" width=200 />

# Who is using
欢迎使用configcenter的组织在[这里](https://github.com/zhongxunking/configcenter/issues/3)进行登记（仅供其他用户参考）。
