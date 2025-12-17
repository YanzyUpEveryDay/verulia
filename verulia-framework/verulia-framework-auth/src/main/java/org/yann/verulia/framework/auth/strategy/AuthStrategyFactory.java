package org.yann.verulia.framework.auth.strategy;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 认证策略工厂
 *
 * @author Yann
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthStrategyFactory {

    private final List<IAuthStrategy> strategies;
    private Map<String, IAuthStrategy> strategyMap = new ConcurrentHashMap<>();

    /**
     * 初始化策略映射
     */
    @PostConstruct
    public void initMap() {
        strategyMap = strategies.stream()
            .collect(Collectors.toMap(IAuthStrategy::getLoginType, Function.identity()));
        log.info("初始化认证策略工厂，已加载策略: {}", strategyMap.keySet());
    }

    /**
     * 根据登录类型获取策略
     *
     * @param loginType 登录类型标识
     * @return 策略实现
     */
    public IAuthStrategy getStrategy(String loginType) {
        IAuthStrategy strategy = strategyMap.get(loginType);
        if (strategy == null) {
            throw new IllegalArgumentException("不支持的登录类型: " + loginType);
        }
        return strategy;
    }
}
