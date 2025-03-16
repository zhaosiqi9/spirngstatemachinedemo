package com.statemachinedemo.business;

import com.statemachinedemo.constant.StateMachineBeanNameConstant;
import com.statemachinedemo.constant.StateMachineStateConstant;
import com.statemachinedemo.persist.RedisStateMachinePersister;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.annotation.OnStateEntry;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;

/**
 * @date 2025-03-11 17:05:55
 */
@WithStateMachine(name = StateMachineBeanNameConstant.SHELVE_STATE_MACHINE_NAME)
@Component
@Slf4j
public class ShelveEventListener {

    @Autowired
    private RedisStateMachinePersister<ShelveState, ShelveEvent> persister;


    @OnStateEntry(source = StateMachineStateConstant.NEW_STATE, target = StateMachineStateConstant.EXECUTING_STATE)
    public void shelveStart(Message<ShelveEvent> message, StateMachine<ShelveState, ShelveEvent> stateMachine) {
        log.info("message: {}", message);
        log.info("开始: ");
        log.info("stateMachine: {}", stateMachine);
        try {
            persister.persist(stateMachine, (String) message.getHeaders().get("businessId"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @OnStateEntry(source = StateMachineStateConstant.EXECUTING_STATE, target = StateMachineStateConstant.SUCCESS_STATE)
    public void shelveSuccess(Message<ShelveEvent> message, StateMachine<ShelveState, ShelveEvent> stateMachine) {
        log.info("message: {}", message);
        log.info("完成: ");
        log.info("stateMachine: {}", stateMachine);
        try {
            persister.persist(stateMachine, (String) message.getHeaders().get("businessId"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
