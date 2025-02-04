package com.finapp.backend.kyc.config;

import com.finapp.backend.kyc.enums.KYCVerificationEvent;
import com.finapp.backend.kyc.enums.KYCVerificationStatus;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

@Configuration
@EnableStateMachineFactory
public class KYCStateMachineConfig extends StateMachineConfigurerAdapter<KYCVerificationStatus, KYCVerificationEvent> {

    @Override
    public void configure(StateMachineStateConfigurer<KYCVerificationStatus, KYCVerificationEvent> states) throws Exception {
        states
                .withStates()
                .initial(KYCVerificationStatus.INITIATED)
                .state(KYCVerificationStatus.DOCUMENTS_REQUIRED)
                .state(KYCVerificationStatus.DOCUMENTS_SUBMITTED)
                .state(KYCVerificationStatus.AUTOMATION_CHECK_IN_PROGRESS)
                .state(KYCVerificationStatus.MANUAL_REVIEW_REQUIRED)
                .state(KYCVerificationStatus.APPROVED)
                .state(KYCVerificationStatus.REJECTED)
                .state(KYCVerificationStatus.SUSPENDED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<KYCVerificationStatus, KYCVerificationEvent> transitions) throws Exception {
        transitions
                // From INITIATED to DOCUMENTS_REQUIRED
                .withExternal()
                .source(KYCVerificationStatus.INITIATED)
                .target(KYCVerificationStatus.DOCUMENTS_REQUIRED)
                .event(KYCVerificationEvent.DOCUMENTS_REQUIRED)
                .and()

                // From DOCUMENTS_REQUIRED to DOCUMENTS_SUBMITTED
                .withExternal()
                .source(KYCVerificationStatus.DOCUMENTS_REQUIRED)
                .target(KYCVerificationStatus.DOCUMENTS_SUBMITTED)
                .event(KYCVerificationEvent.DOCUMENTS_UPLOADED)
                .and()

                // From DOCUMENTS_SUBMITTED to AUTOMATION_CHECK_IN_PROGRESS
                .withExternal()
                .source(KYCVerificationStatus.DOCUMENTS_SUBMITTED)
                .target(KYCVerificationStatus.AUTOMATION_CHECK_IN_PROGRESS)
                .event(KYCVerificationEvent.AUTOMATION_STARTED)
                .and()

                // From AUTOMATION_CHECK_IN_PROGRESS to MANUAL_REVIEW_REQUIRED
                .withExternal()
                .source(KYCVerificationStatus.AUTOMATION_CHECK_IN_PROGRESS)
                .target(KYCVerificationStatus.MANUAL_REVIEW_REQUIRED)
                .event(KYCVerificationEvent.MANUAL_REVIEW_REQUESTED)
                .and()

                // From AUTOMATION_CHECK_IN_PROGRESS to APPROVED
                .withExternal()
                .source(KYCVerificationStatus.AUTOMATION_CHECK_IN_PROGRESS)
                .target(KYCVerificationStatus.APPROVED)
                .event(KYCVerificationEvent.APPROVAL)
                .and()

                // From MANUAL_REVIEW_REQUIRED to APPROVED
                .withExternal()
                .source(KYCVerificationStatus.MANUAL_REVIEW_REQUIRED)
                .target(KYCVerificationStatus.APPROVED)
                .event(KYCVerificationEvent.MANUAL_REVIEW_COMPLETED)
                .and()

                // From MANUAL_REVIEW_REQUIRED to REJECTED
                .withExternal()
                .source(KYCVerificationStatus.MANUAL_REVIEW_REQUIRED)
                .target(KYCVerificationStatus.REJECTED)
                .event(KYCVerificationEvent.REJECTION)
                .and()

                // From any state to SUSPENDED
                .withExternal()
                .source(KYCVerificationStatus.INITIATED)
                .target(KYCVerificationStatus.SUSPENDED)
                .event(KYCVerificationEvent.SUSPEND)
                .and()
                .withExternal()
                .source(KYCVerificationStatus.DOCUMENTS_REQUIRED)
                .target(KYCVerificationStatus.SUSPENDED)
                .event(KYCVerificationEvent.SUSPEND)
                .and()
                .withExternal()
                .source(KYCVerificationStatus.DOCUMENTS_SUBMITTED)
                .target(KYCVerificationStatus.SUSPENDED)
                .event(KYCVerificationEvent.SUSPEND)
                .and()
                .withExternal()
                .source(KYCVerificationStatus.AUTOMATION_CHECK_IN_PROGRESS)
                .target(KYCVerificationStatus.SUSPENDED)
                .event(KYCVerificationEvent.SUSPEND)
                .and()
                .withExternal()
                .source(KYCVerificationStatus.MANUAL_REVIEW_REQUIRED)
                .target(KYCVerificationStatus.SUSPENDED)
                .event(KYCVerificationEvent.SUSPEND);
    }
}
