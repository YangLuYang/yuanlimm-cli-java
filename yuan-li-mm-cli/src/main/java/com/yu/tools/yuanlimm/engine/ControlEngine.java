package com.yu.tools.yuanlimm.engine;

import com.yu.tools.yuanlimm.dto.Stock;
import com.yu.tools.yuanlimm.dto.StocksResponse;
import com.yu.tools.yuanlimm.dto.WishResponse;
import com.yu.tools.yuanlimm.enums.SystemMode;
import com.yu.tools.yuanlimm.enums.SystemStatus;
import com.yu.tools.yuanlimm.exception.BusinessException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;

/**
 * Engine - 控制引擎
 */
@SuppressWarnings({"unused", "WeakerAccess", "Duplicates"})
@Slf4j
@Lazy(false)
@Component
public class ControlEngine {
    /**
     * 系统模式
     */
    @Getter
    @Setter
    @Value("${config.systemMode:auto}")
    private SystemMode SYSTEM_MODE;
    /**
     * 请求线程数
     */
    @Getter
    @Setter
    @Value("${config.requestThread:1}")
    private Integer REQUEST_THREAD;
    /**
     * 计算线程数
     */
    @Getter
    @Setter
    @Value("${config.computeThread:}")
    private Integer COMPUTE_THREAD;
    @Getter
    @Setter
    @Value("${config.workerName:}")
    private String WORKER_NAME;
    @Getter
    @Setter
    @Value("${config.workerHost:}")
    private String WORKER_HOST;
    @Getter
    @Setter
    @Value("${config.workerPort:}")
    private String WORKER_PORT;
    /**
     * 股票代码
     */
    @Getter
    @Setter
    @Value("${config.stockCode:}")
    private String STOCK_CODE;
    /**
     * CHEER_WORD
     */
    @Getter
    @Setter
    @Value("${config.cheerWord:}")
    private String CHEER_WORD;
    /**
     * 钱包地址
     */
    @Getter
    @Setter
    @Value("${config.walletAddress:}")
    private String WALLET_ADDRESS;
    /**
     * 系统状态
     */
    @Getter
    private SystemStatus SYSTEM_STATUS = SystemStatus.needConfig;
    /**
     * HASH难度
     */
    @Getter
    @Setter
    private Integer HASH_HARD = 0;
    /**
     * 股票列表
     */
    private List<Stock> stockList = new ArrayList<>();
    /**
     * 股票Map
     */
    private Map<String, Stock> stockMap = new HashMap<>();
    /**
     * RestTemplate
     */
    @Resource(name = "restTemplate")
    private RestTemplate restTemplate;
    /**
     * 计算引擎
     */
    @Resource(name = "computeEngine")
    private ComputeEngine computeEngine;
    /**
     * 请求引擎
     */
    @Resource(name = "requestEngine")
    private RequestEngine requestEngine;
    @Resource(name = "clusterWorkerEngine")
    private ClusterWorkerEngine clusterWorkerEngine;

    /**
     * Init
     */
    public void init() {
        this.refreshStockList();

        Optional.ofNullable(this.getHashHard()).ifPresent(number -> this.HASH_HARD = number);

        switch (this.SYSTEM_MODE) {
            case auto: {
                this.initConfig();
                if (this.SYSTEM_STATUS.equals(SystemStatus.stopped)) {
                    this.startService();
                }
            }
            break;
            case console: {
                this.inputConfig();
                this.initConfig();
                this.startService();
            }
            break;
            case web: {
                this.initConfig();
            }
            break;
            case worker: {
                this.inputConfig();
                this.initConfig();
                this.clusterWorkerEngine.init();
                //Start in clusterWorkerEngine connect success event.
            }
            break;
        }
    }

    /**
     * 启动服务
     */
    public void startService() {
        if (this.SYSTEM_STATUS.equals(SystemStatus.running)) {
            throw new BusinessException("系统已启动.");
        }

        if (this.SYSTEM_STATUS.equals(SystemStatus.needConfig)) {
            throw new BusinessException("系统需要配置.");
        }

        computeEngine.start();
        requestEngine.start();
        this.SYSTEM_STATUS = SystemStatus.running;
        System.out.println("Service has been started.");
    }

    /**
     * 停止服务
     */
    public void stopService() {
        if (this.SYSTEM_STATUS.equals(SystemStatus.stopped)) {
            throw new BusinessException("系统已停止.");
        }

        computeEngine.stop();
        requestEngine.stop();
        this.SYSTEM_STATUS = SystemStatus.stopped;
        System.out.println("Service has been stopped.");
    }

    /**
     * 输入配置
     */
    public void inputConfig() {
        Scanner scanner = new Scanner(System.in);

        if (this.SYSTEM_MODE.equals(SystemMode.worker)) {
            if (StringUtils.isBlank(this.WORKER_NAME)) {
                this.WORKER_NAME = inputWorkerName(scanner);
            }

            if (StringUtils.isBlank(this.WORKER_HOST)) {
                this.WORKER_HOST = inputWorkerHost(scanner);
            }

            if (StringUtils.isBlank(this.WORKER_PORT)) {
                this.WORKER_PORT = inputWorkerPort(scanner);
            }
        }

        if (StringUtils.isBlank(this.WALLET_ADDRESS)) {
            this.WALLET_ADDRESS = inputWalletAddress(scanner);
        }

        if (Objects.isNull(this.COMPUTE_THREAD)) {
            this.COMPUTE_THREAD = inputComputeThread(scanner);
        }

        if (StringUtils.isBlank(this.STOCK_CODE)) {
            Stock stock = stockChoice(scanner);
            this.STOCK_CODE = stock.getCode();
        }

        scanner.close();
    }

    /**
     * 更新系统配置
     */
    public void initConfig() {
        this.updateConfig(this.COMPUTE_THREAD, this.WALLET_ADDRESS, this.STOCK_CODE, this.CHEER_WORD);
    }

    /**
     * 更新系统配置
     *
     * @param computeThread 计算线程
     * @param walletAddress 钱包地址
     * @param stockCode     股票代码
     * @param cheerWord     许愿词
     */
    public void updateConfig(Integer computeThread, String walletAddress, String stockCode, String cheerWord) {
        SystemStatus originalStatus = this.getSYSTEM_STATUS();

        if (originalStatus.equals(SystemStatus.running)) {
            this.stopService();
        }

        this.COMPUTE_THREAD = Optional.ofNullable(computeThread)
                .map(number -> Optional.of(number)
                        .filter(num -> num > 0)
                        .orElseThrow(() -> new BusinessException("输入的计算线程数不合法.")))
                .map(number -> Optional.of(number)
                        .filter(num -> num <= 16)
                        .orElse(16))
                .orElse(null);

        this.WALLET_ADDRESS = walletAddress;
        this.STOCK_CODE = stockCode;
        this.CHEER_WORD = cheerWord;

        this.REQUEST_THREAD = Optional.ofNullable(this.REQUEST_THREAD)
                .filter(number -> number <= 4)
                .orElse(4);

        if (this.COMPUTE_THREAD != null
                && StringUtils.isNotBlank(this.WALLET_ADDRESS)
                && StringUtils.isNotBlank(this.STOCK_CODE)) {
            this.SYSTEM_STATUS = SystemStatus.stopped;
        } else {
            this.SYSTEM_STATUS = SystemStatus.needConfig;
        }

        System.out.println("System config has been updated.");

        if (originalStatus.equals(SystemStatus.running) && this.SYSTEM_STATUS.equals(SystemStatus.stopped)) {
            this.startService();
        }
    }

    /**
     * 每30秒重置难度
     */
    @Scheduled(fixedDelay = 1000 * 20)
    public void updateHashHard() {
        Optional.ofNullable(this.getHashHard()).ifPresent(number -> this.HASH_HARD = number);
    }

    /**
     * 获取难度
     *
     * @return 难度
     */
    private Integer getHashHard() {
        WishResponse response = restTemplate.getForObject("https://www.yuanlimm.com/api/super_wishs", WishResponse.class);
        if (response.getHard() != null) {
            return response.getHard();
        }
        return null;
    }

    /**
     * 刷新股票列表
     */
    @Scheduled(cron = "0 0 * * * *")
    public void refreshStockList() {
        String api = "https://www.yuanlimm.com/api/stocks?page=%d";
        Integer pageIndex = 1;

        this.stockList.clear();

        try {
            StocksResponse response;
            do {
                response = restTemplate.getForObject(String.format(api, pageIndex), StocksResponse.class);
                this.stockList.addAll(response.getData());
                pageIndex++;
            } while (response.getData().size() != 0);
        } catch (HttpClientErrorException e) {
            if (e.getMessage().contains("429")) {
                log.info("请求服务器频率超过限制", e);
            } else if (e.getMessage().contains("503")) {
                log.info("请求服务器失败", e);
            } else if (e.getMessage().contains("504")) {
                log.info("请求服务器失败", e);
            } else {
                log.warn("请求服务器失败", e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        this.stockList.forEach(stock -> stockMap.put(stock.getCode(), stock));
    }

    /**
     * 获取股票列表
     *
     * @return 股票列表
     */
    public List<Stock> getStockList() {
        if (Objects.isNull(this.stockList)) {
            this.refreshStockList();
        }
        return this.stockList;
    }

    /**
     * 根据Code获取股票
     *
     * @param code 股票代码
     * @return 股票
     */
    public Stock getStockByCode(String code) {
        return stockMap.get(code);
    }

    /**
     * 股票选择
     *
     * @param scanner Scanner
     * @return 股票
     */
    private Stock stockChoice(Scanner scanner) {
        List<Stock> stockList = this.getStockList();

        System.out.println("股票列表:");

        for (int i = 0; i < stockList.size(); i++) {
            System.out.println(String.format("%d.%s", i, stockList.get(i).getName()));
        }

        System.out.println("请输入股票序号, 按回车确认.");

        Stock stock = null;

        do {
            if (scanner.hasNextInt()) {
                Integer index = scanner.nextInt();

                if (index < 0 || index > stockList.size()) {
                    System.out.println("您输入的股票序号无效, 请重新输入.");
                    continue;
                }

                stock = stockList.get(index);
            } else {
                scanner.nextLine();
                System.out.println("您输入的股票序号无效, 请重新输入.");
            }
        } while (Objects.isNull(stock));

        System.out.println(String.format("您选择股票的是[%s], 下面开始为TA许愿.", stock.getName()));

        return stock;
    }

    /**
     * 输入钱包地址
     *
     * @param scanner Scanner
     * @return 钱包地址
     */
    private String inputWalletAddress(Scanner scanner) {
        System.out.println("请输入钱包地址, 按回车确认.");

        String address = null;

        do {
            if (scanner.hasNext()) {
                address = scanner.nextLine();
            } else {
                scanner.nextLine();
                System.out.println("您输入的钱包地址无效, 请重新输入.");
            }
        } while (StringUtils.isBlank(address));

        return address;
    }

    /**
     * 输入Worker名称
     *
     * @param scanner Scanner
     * @return Worker名称
     */
    private String inputWorkerName(Scanner scanner) {
        System.out.println("请输入Worker名称, 输入后按回车确认. 输入 * 将随机生成名称." +
                "\n注意: 该Worker名称在单个集群内唯一, 重复的名称将被拒绝接入集群.");

        String input = null;

        do {
            if (scanner.hasNext()) {
                input = scanner.nextLine();
            } else {
                scanner.nextLine();
                System.out.println("您输入的Worker名称无效, 请重新输入.");
            }
        } while (StringUtils.isBlank(input));

        if ("*".equals(input)) {
            input = UUID.randomUUID().toString();
        }

        return input;
    }

    /**
     * 输入WorkerHost
     *
     * @param scanner Scanner
     * @return Worker名称
     */
    private String inputWorkerHost(Scanner scanner) {
        System.out.println("请输入Worker主机地址(IP/域名), 输入后按回车确认. 输入 * 将设置为localhost.");

        String input = null;

        do {
            if (scanner.hasNext()) {
                input = scanner.nextLine();
            } else {
                scanner.nextLine();
                System.out.println("您输入的Worker主机地址无效, 请重新输入.");
            }
        } while (StringUtils.isBlank(input));

        if ("*".equals(input)) {
            input = "localhost";
        }

        return input;
    }

    /**
     * 输入WorkerPort
     *
     * @param scanner Scanner
     * @return Worker端口
     */
    private String inputWorkerPort(Scanner scanner) {
        System.out.println("请输入Worker端口, 输入后按回车确认. 输入 * 将设置为8080.");

        String input = null;

        do {
            if (scanner.hasNext()) {
                input = scanner.nextLine();
            } else {
                scanner.nextLine();
                System.out.println("您输入的Worker端口无效, 请重新输入.");
            }
        } while (StringUtils.isBlank(input));

        if ("*".equals(input)) {
            input = "8080";
        }

        return input;
    }

    /**
     * 输入计算线程数
     *
     * @param scanner Scanner
     * @return 计算线程数
     */
    private Integer inputComputeThread(Scanner scanner) {
        System.out.println("请输入计数线程数, 建议不超过物理核心数, 按回车确认.");

        Integer computeThread = null;

        do {
            if (scanner.hasNextInt()) {
                computeThread = scanner.nextInt();
            } else {
                scanner.nextLine();
                System.out.println("您输入的计数线程数无效, 请重新输入.");
            }
        } while (Objects.isNull(computeThread));

        return computeThread;
    }
}
