package com.sohamtanpathak.fintech.payment_processing_system.payment.simulator;

import com.sohamtanpathak.fintech.payment_processing_system.common.enums.ChaosMode;
import com.sohamtanpathak.fintech.payment_processing_system.common.enums.PaymentMethod;
import com.sohamtanpathak.fintech.payment_processing_system.common.enums.PaymentStatus;
import com.sohamtanpathak.fintech.payment_processing_system.common.util.RandomizerUtil;
import com.sohamtanpathak.fintech.payment_processing_system.payment.entity.Payment;
import com.sohamtanpathak.fintech.payment_processing_system.payment.repository.PaymentRepository;
import com.sohamtanpathak.fintech.payment_processing_system.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * this class is simulating how a real bank asynchronously responds to payment authorization requests.
 * Instead of the bank sending a callback, a Spring Scheduler periodically checks whether enough time
 * has passed for each payment and then "pretends" that the bank has responded.
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class BankCallBackSimulator {

    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;
    private final SimulatorConfig simulatorConfig;

    @Scheduled(fixedDelayString = "payment.simulator.poll-interval-ms:5000")
    public void processCallbacks(){

        /*
           Get all payments before this time, so we don't get the payments that were just went for authorizing.
           We will get payments that happened(initiated) before 1 sescond of the current time.
           Basically, it creates a small "buffer"
        */
        LocalDateTime globalWindow = LocalDateTime.now().minusSeconds(1);

        // These payments (candidates) needs to be process in this execution of this scheduler
        List<Payment> candidates = paymentRepository.findByStatusAndCreatedAtBefore(PaymentStatus.AUTHORIZING, globalWindow);

        if(candidates.isEmpty())    return;

        for(Payment payment : candidates){
            simulateCallBack(payment);
        }
    }


    /*
     * Simulates a bank callback for a single payment.
     *
     * Before processing, verifies whether the payment has reached its scheduled
     * callback time (dueAt). If not, the payment remains in AUTHORIZING state.
     *
     * Once due, the simulator behaves according to the configured ChaosMode.
     */
    private void simulateCallBack(Payment payment) {

        SimulatorConfig.MehodSimulatorConfig methodConfig = simulatorConfig.configFor(payment.getMethod());

        LocalDateTime dueAt = dueAt(payment, methodConfig);

        if(LocalDateTime.now().isBefore(dueAt)){
            return;   // payment cannot be processed before the dueAt time.
        }

        ChaosMode chaosMode = simulatorConfig.getChaosMode();

        switch(chaosMode){

            // All payments should be approved by the bank.
            case SUCCESS -> resolve(payment, true);

            // All the payments should not be approved by the bank
            case FAILURE -> resolve(payment, false);

            // We want to simulate when all the payments are getting piled up & you are not processing them/
            // In that case, we don't do anything with the payment.
            case TIMEOUT -> {
                log.debug("BankCallback simulator: Payment Timed out");
            }

            case NORMAL, SLOW -> resolve(payment, shouldApprove(payment, methodConfig));
        }
    }

    /*
     * Finalizes the simulated bank authorization.
     *
     * On success, generates a fake bank reference number.
     * On failure, returns a simulated bank error.
     *
     * It sends the actual status update to PaymentService.
     */
    private void resolve(Payment payment, boolean approve){
        if(approve){
            String bankRef = "SIM_BANK_REF"+ RandomizerUtil.randomBase64(8);
            paymentService.resolveAuthorization(payment.getId(), true, bankRef, null, null);
        } else{
            paymentService.resolveAuthorization(payment.getId(), false, null, "SIM_BANK_ERROR_CODE", "Simulated Bank Declined");

        }
    }

    /*
     * Determines whether the simulated bank approves the payment.
     */
    private boolean shouldApprove(Payment payment, SimulatorConfig.MehodSimulatorConfig methConfig){

        // this bucket will give number between 0 to 99
        int bucket = Math.abs(payment.getId().hashCode()) % 100;

        /*
        * If the bucket falls within the configured successRate percentage, the payment is approved; otherwise it is declined.
        * Eg: successRate is 90%, then 90% times bucket will be less than 90, & will return true.
        * 10% times bucket will be greater than 90, & will return false.
        */
        return bucket < methConfig.getSuccessRate();
    }

    /*
     * It is the time when the payment should be processed.
     * After "due" time, payment can be processed anytime, but not before it.
     *
     * Each payment receives a deterministic delay (between minDelaySeconds and maxDelaySeconds) based on its payment ID hash. This spreads callbacks over
     * time while ensuring the same payment always gets the same delay.
     */
    private LocalDateTime dueAt(Payment payment, SimulatorConfig.MehodSimulatorConfig methodConfig){

        int range = methodConfig.getMaxDelaySeconds() - methodConfig.getMinDelaySeconds();

        int delaySeconds = methodConfig.getMinDelaySeconds() + Math.abs(payment.getId().hashCode()) % (range+1);


        // In SLOW chaos mode, the delay is doubled to simulate slower bank responses.
        if(simulatorConfig.getChaosMode() == ChaosMode.SLOW){
            delaySeconds *= 2;
        }

        return payment.getCreatedAt().plusSeconds(delaySeconds);
    }

}
