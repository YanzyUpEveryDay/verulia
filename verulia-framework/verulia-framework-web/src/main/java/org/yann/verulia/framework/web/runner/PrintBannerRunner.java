package org.yann.verulia.framework.web.runner;


import cn.hutool.v7.core.net.NetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedHashSet;

/**
 *
 * @author Yann
 * @date 2025/11/10 21:29
 */
@Slf4j
@Component
@Profile("dev")
public class PrintBannerRunner implements ApplicationRunner, Ordered {

    private final ConfigurableEnvironment env;

    public PrintBannerRunner(ConfigurableEnvironment env) {
        this.env = env;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.debug("PrintBannerRunner start...");
        String appName = env.getProperty("spring.application.name", "Verulia");
        String port = env.getProperty("server.port", "8080");
        String contextPath = env.getProperty("server.servlet.context-path", "");

        if (!contextPath.isEmpty() && !contextPath.startsWith("/")) {
            contextPath = "/" + contextPath;
        }

        LinkedHashSet<String> ipv4s = NetUtil.localIpv4s();
        String[] activeProfiles = env.getActiveProfiles();
        if (activeProfiles.length == 0) {
            activeProfiles = env.getDefaultProfiles();
        }
        String profilesStr = Arrays.toString(activeProfiles);

        StringBuilder logMessage = new StringBuilder();

        logMessage.append("\n+---------------------------------------------------------------+\n");
        logMessage.append(String.format("| ✅ Application '%s' is running! Access URLs:\n", appName));
        logMessage.append(String.format("|    Local:      http://localhost:%s%s\n", port, contextPath));

        if (ipv4s.isEmpty()) {
            logMessage.append("|    External:   (No external IP found)\n");
        } else {
            for (String ip : ipv4s) {
                if ("127.0.0.1".equals(ip)) {
                    continue;
                }
                logMessage.append(String.format("|    External:   http://%s:%s%s\n", ip, port, contextPath));
            }
        }

        logMessage.append(String.format("|    Active Profiles: %s\n", profilesStr));
        logMessage.append("+---------------------------------------------------------------+\n");

        System.out.println(logMessage);
    }

    @Override
    public int getOrder() {
        return 9999;
    }
}
