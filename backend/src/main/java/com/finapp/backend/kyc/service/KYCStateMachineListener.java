package com.finapp.backend.kyc.service;

import com.finapp.backend.kyc.enums.KYCVerificationEvent;
import com.finapp.backend.kyc.enums.KYCVerificationStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.stereotype.Component;

@Component
public class KYCStateMachineListener extends StateMachineListenerAdapter<KYCVerificationStatus, KYCVerificationEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(KYCStateMachineListener.class);

    @Override
    public void stateChanged(State<KYCVerificationStatus, KYCVerificationEvent> from, State<KYCVerificationStatus, KYCVerificationEvent> to) {
        LOGGER.info("State changed from {} to {}", from != null ? from.getId() : "null", to.getId());
    }

    @Override
    public void eventNotAccepted(Message<KYCVerificationEvent> event) {
        LOGGER.error("Event not accepted: {}", event);
    }
}
