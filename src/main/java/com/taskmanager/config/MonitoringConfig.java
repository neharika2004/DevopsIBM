package com.taskmanager.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.graphite.GraphiteConfig;
import io.micrometer.graphite.GraphiteMeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class MonitoringConfig {
    
    @Value("${management.metrics.export.graphite.host:localhost}")
    private String graphiteHost;
    
    @Value("${management.metrics.export.graphite.port:2003}")
    private int graphitePort;
    
    @Value("${management.metrics.export.graphite.enabled:true}")
    private boolean graphiteEnabled;
    
    @Bean
    public GraphiteMeterRegistry graphiteMeterRegistry() {
        GraphiteConfig graphiteConfig = new GraphiteConfig() {
            @Override
            public String host() {
                return graphiteHost;
            }
            
            @Override
            public int port() {
                return graphitePort;
            }
            
            @Override
            public Duration step() {
                return Duration.ofSeconds(10);
            }
            
            @Override
            public String get(String key) {
                return null;
            }
            
            @Override
            public boolean enabled() {
                return graphiteEnabled;
            }
        };
        
        return new GraphiteMeterRegistry(graphiteConfig, io.micrometer.core.instrument.Clock.SYSTEM);
    }
    
    @Bean
    public Timer.Sample taskTimer(MeterRegistry meterRegistry) {
        return Timer.start(meterRegistry);
    }
}
