# 援力满满 - 许愿工具的Java实现
## 介绍
本项目是 [援力满满 二次元虚拟股市](https://www.yuanlimm.com/) 基于[官方工具](https://github.com/bydmm/yuanlimm-cli)的Java实现版本  
本工具具有auto,console,web三种模式:
* auto模式自动判断启动参数是否满足运算条件, 满足则会自动开始.
* web模式通过web页面配置启动参数.
* console模式通过启动参数或者控制台配置运行参数.

Web端实现了网页的系统运行状态监控, 许愿统计, 许愿日志和配置热更新.  
通过console模式配置的参数也能通过web页面热更新.

本程序运行需要系统安装好Java环境, 支持JRE和JDK.
强烈推荐安装 **Java10** 或更高版本, 本工具中Java10相较于Java8实际测试具有60%以上的提升.

[官网JRE10下载](http://www.oracle.com/technetwork/java/javase/downloads/jre10-downloads-4417026.html)
[官网JDK10下载](http://www.oracle.com/technetwork/java/javase/downloads/jdk10-downloads-4416644.html)

## 用法

### 自动模式:
自动根据启动参数, 满足启动所需参数则自动启动, 否则需要到Web管理页面配置.
默认为此模式.
```bash
java -jar ./xxxx.jar
```

### Web模式:
```bash
java -jar ./xxxx.jar --config.systemMode=web
```
控制台出现``System Started.``后, 系统会提示管理地址.
> You can use the following url to manage the system. 
>  http://localhost:8080

此时通过浏览器打开``http://localhost:8080``即可.  
本系统默认8080端口, 如果有端口被占用的同学, 可以使用以下配置使用不同的端口.

* 配置为8081端口
```bash
java -jar ./xxxx.jar --config.systemMode=web --server.port=8081
```

* 配置为随机端口
```bash
java -jar ./xxxx.jar --config.systemMode=web --server.port=0
```

### Console交互模式:
```bash
java -jar ./xxxx.jar --config.systemMode=console
```

### Console一键模式:
```bash
java -jar ./xxxx.jar \
--config.systemMode=console \
--config.computeThread=1 \
--config.stockCode=ERIRI \
--config.cheerWord=JAVA \
--config.walletAddress=xxxx
```

--config.stockCode: 钱包地址, https://www.yuanlimm.com/#/profile
--config.computeThread: 计算线程数，建议不超过CPU核心数(超线程的核心不算在内)
--config.cheerWord: 应援词
--config.walletAddress: 股票代码，比如英梨梨的地址：https://www.yuanlimm.com/#/stock/ERIRI，的股票代码是ERIRI，

## 截图
* Web端
![](https://ws1.sinaimg.cn/large/728aabffgy1fs2ywwbntej22i81reh0f.jpg)
![](https://ws1.sinaimg.cn/large/728aabffgy1fs2ywwdpydj22i81re4h2.jpg)
![](https://ws1.sinaimg.cn/large/728aabffgy1fs2ywwb320j22i81reh2w.jpg)
![](https://ws1.sinaimg.cn/large/728aabffgy1fs611b33r6j22j41ns4g2.jpg)
* Console端
![](https://ws1.sinaimg.cn/large/728aabffgy1fs2z1k2ixsj21w80tqqcl.jpg)

## 支持

官方QQ群：774800449  @Yu