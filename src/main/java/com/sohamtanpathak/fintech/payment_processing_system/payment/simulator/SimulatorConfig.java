package com.sohamtanpathak.fintech.payment_processing_system.payment.simulator;

import com.sohamtanpathak.fintech.payment_processing_system.common.enums.ChaosMode;
import com.sohamtanpathak.fintech.payment_processing_system.common.enums.PaymentMethod;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


/**
 * This class simply holds the simulator's configuration.
 */

@Configuration
@ConfigurationProperties(prefix = "payment.simulator")
@Getter
@Setter
public class SimulatorConfig {

    private Integer pollIntervalMs = 2000; //default
    private ChaosMode chaosMode = ChaosMode.NORMAL;
    private Map<String,MehodSimulatorConfig> methods = new HashMap<>();

    public SimulatorConfig.MehodSimulatorConfig configFor(PaymentMethod method){
        return methods.getOrDefault(method.name(), new MehodSimulatorConfig());
    }

    @Getter
    @Setter
    public static class MehodSimulatorConfig{
        private Integer minDelaySeconds = 1;  // all these values are default values
        private Integer maxDelaySeconds = 5;
        private Integer successRate = 80;
    }

}
