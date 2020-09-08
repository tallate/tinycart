package com.tallate.tinycart;

import java.net.InetAddress;
import java.net.UnknownHostException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableDubbo
@EnableFeignClients
@EnableCircuitBreaker
@Slf4j
@MapperScan("com.tallate.tinycart.dao")
public class OrderCenterApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(OrderCenterApplication.class);
        Environment env = app.run(args).getEnvironment();
        logApplicationStartup(env);
    }

    @SuppressWarnings("all")
    private static void logApplicationStartup(Environment env) {
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        String serverPort = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path");
        if (StringUtils.isBlank(contextPath)) {
            contextPath = "/";
        }
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\t{}://localhost:{}{}\n\t" +
                        "External: \t{}://{}:{}{}\n\t" +
                        "Profile(s): \t{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                protocol,
                serverPort,
                contextPath,
                protocol,
                hostAddress,
                serverPort,
                contextPath,
                env.getActiveProfiles());

        String configServerStatus = env.getProperty("configserver.status");
        if (configServerStatus == null) {
            configServerStatus = "Not found or not setup for this application";
        }
        log.info("\n----------------------------------------------------------\n\t" +
                "Config Server: \t{}\n----------------------------------------------------------", configServerStatus);
        log.info("\n  _____               _   _      __   __   ____     _       ____     _____   \n"
                + " |_ \" _|     ___     | \\ |\"|     \\ \\ / /U /\"___|U  /\"\\  uU |  _\"\\ u |_ \" _|  \n"
                + "   | |      |_\"_|   <|  \\| |>     \\ V / \\| | u   \\/ _ \\/  \\| |_) |/   | |    \n"
                + "  /| |\\      | |    U| |\\  |u    U_|\"|_u | |/__  / ___ \\   |  _ <    /| |\\   \n"
                + " u |_|U    U/| |\\u   |_| \\_|       |_|    \\____|/_/   \\_\\  |_| \\_\\  u |_|U   \n"
                + " _// \\\\_.-,_|___|_,-.||   \\\\,-..-,//|(_  _// \\\\  \\\\    >>  //   \\\\_ _// \\\\_  \n"
                + "(__) (__)\\_)-' '-(_/ (_\")  (_/  \\_) (__)(__)(__)(__)  (__)(__)  (__)__) (__) \n");
    }

}
