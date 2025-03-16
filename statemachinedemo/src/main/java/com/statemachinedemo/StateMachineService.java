package com.statemachinedemo;

import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

/**
 * @author polly
 * @date 2025.03.15 11:04:19
 */
@Component
public class StateMachineService<S extends Enum<S>, E extends Enum<E>> {

    public void sendEvent(Message<E> message, StateMachine<S, E> stateMachine) {
        stateMachine.start();
        stateMachine.sendEvent(message);
    }
}
