package com.statemachinedemo.business;

import com.statemachinedemo.StateMachineService;
import com.statemachinedemo.constant.StateMachineBeanNameConstant;
import com.statemachinedemo.persist.RedisStateMachinePersister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.ObjectStateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author polly
 * @date 2025.03.15 11:07:06
 */
@Component
public class ShelveEventProducer {

    @Resource(name = StateMachineBeanNameConstant.SHELVE_STATE_MACHINE_NAME)
    private StateMachineFactory<ShelveState, ShelveEvent> factory;

    @Resource
    private StateMachineService<ShelveState, ShelveEvent> stateMachineService;

    @Autowired
    private RedisStateMachinePersister<ShelveState, ShelveEvent> persister;


    public ObjectStateMachine<ShelveState, ShelveEvent> getFreshStateMachine() {
        ObjectStateMachine<ShelveState, ShelveEvent> stateMachine = (ObjectStateMachine<ShelveState, ShelveEvent>) factory.getStateMachine();
        stateMachine.setBeanName(StateMachineBeanNameConstant.SHELVE_STATE_MACHINE_NAME);
        return stateMachine;
    }

    public void sendStartEvent(String cmdCode) {
        // 第一个事件工厂创建
        ObjectStateMachine<ShelveState, ShelveEvent> stateMachine = getFreshStateMachine();
        Message<ShelveEvent> message = MessageBuilder.withPayload(ShelveEvent.SHELVE_START)
                .setHeader("businessId", cmdCode)
                .build();
        stateMachineService.sendEvent(message, stateMachine);
    }

    public void sendSuccessEvent(String cmdCode) {
        // wes监听的成功处理
        ObjectStateMachine<ShelveState, ShelveEvent> stateMachine = getFreshStateMachine();
        try {
            persister.restore(stateMachine, cmdCode);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // stateMachine.setBeanName(StateMachineBeanNameConstant.SHELVE_STATE_MACHINE_NAME);
        Message<ShelveEvent> message = MessageBuilder.withPayload(ShelveEvent.SHELVE_SUCCESS)
                .setHeader("businessId", cmdCode)
                .build();
        stateMachineService.sendEvent(message, stateMachine);
    }
}
