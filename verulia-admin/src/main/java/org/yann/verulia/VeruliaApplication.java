package org.yann.verulia;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 *
 * @author Yann
 * @date 2025/12/12 17:03
 */
@SpringBootApplication
public class VeruliaApplication {

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext context = SpringApplication.run(VeruliaApplication.class, args);
        ConfigurableEnvironment env = context.getEnvironment();

        String appName = env.getProperty("spring.application.name", "Verulia");
        String port = env.getProperty("server.port", "8080");
        String contextPath = env.getProperty("server.servlet.context-path", "");
        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        String[] activeProfiles = env.getActiveProfiles();
        if (activeProfiles.length == 0) {
            activeProfiles = env.getDefaultProfiles();
        }
        String logMessage = String.format("""
                        
                        +---------------------------------------------------------------+
                        | ✅ Application '%s' is running! Access URLs:
                        |    Local:      http://localhost:%s%s
                        |    External:   http://%s:%s%s
                        |    Active Profiles: %s
                        +---------------------------------------------------------------+
                        """,
                appName,
                port,
                contextPath,
                hostAddress,
                port,
                contextPath,
                Arrays.toString(activeProfiles)
        );
        System.out.println(logMessage);
    }
}
