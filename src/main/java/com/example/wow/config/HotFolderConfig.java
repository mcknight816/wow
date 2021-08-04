package com.example.wow.config;

import com.example.wow.util.hot.HotFolderWatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;

@Slf4j
@Configuration
public class HotFolderConfig {

    @Value("${hot.folder.root.path:/temp/${spring.application.name}/json}")
    String hotFolderRootPath;
    @Value("${hot.folder.poll.interval:500}")
    long hotFolderPollInterval;
    @Value("${hot.folder.quiet.period:300}")
    long hotFolderQuietPeriod;

    @Bean
    public HotFolderWatcher hotFolderWatcher() {
        return new HotFolderWatcher(hotFolderRootPath, hotFolderPollInterval, hotFolderQuietPeriod);
    }

    @PreDestroy
    public void onDestroy() {
        hotFolderWatcher().stop();
    }
}
