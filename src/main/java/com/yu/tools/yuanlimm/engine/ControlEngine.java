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
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;

/**
 * Engine - 控制引擎
 */
@SuppressWarnings({"unused", "WeakerAccess"})
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
    @Getter
    private List<Stock> stockList;
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

    /**
     * Init
     */
    public void init() {
        this.getStockList().forEach(stock -> stockMap.put(stock.getCode(), stock));

        Optional.ofNullable(this.getHashHard()).ifPresent(number -> this.HASH_HARD = number);

        switch (this.SYSTEM_MODE) {
            case auto: {
                this.updateConfig();
                if (this.SYSTEM_STATUS.equals(SystemStatus.stopped)) {
                    this.startService();
                }
            }
            break;
            case console: {
                Scanner scanner = new Scanner(System.in);

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

                this.updateConfig();
                this.startService();
            }
            break;
            case web: {
                this.updateConfig();
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
     * 更新系统配置
     */
    public void updateConfig() {
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
    @Scheduled(fixedDelay = 1000 * 30)
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
     * 获取股票列表
     *
     * @return 股票列表
     */
    public List<Stock> getStockList() {
        if (Objects.isNull(this.stockList)) {
            StocksResponse response = restTemplate.getForObject("https://www.yuanlimm.com/api/stocks", StocksResponse.class);
            this.stockList = response.getData();
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
