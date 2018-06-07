# 援力满满 - 许愿工具的Java实现
## 介绍
本项目是 援力满满 二次元虚拟股市 基于官方工具的Java实现版本
本工具具有console和web两种模式:
* web模式通过web页面配置启动参数
* console模式通过启动参数或者控制台配置运行参数

Web端实现了网页的系统运行状态监控, 许愿统计, 许愿日志和配置热更新.  
通过console模式配置的参数也能通过web页面热更新.

## 用法

### Web模式:
```bash
java -jar ./xxxx.jar
```
控制台出现``System Started.``后, 通过浏览器打开``http://127.0.0.1:8080/``即可.  
本系统默认8080端口, 如果有端口被占用的同学, 可以使用以下配置使用不同的端口.
```bash
java -jar ./xxxx.jar --server.port=8081
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

## 支持

官方QQ群：774800449  @Yu