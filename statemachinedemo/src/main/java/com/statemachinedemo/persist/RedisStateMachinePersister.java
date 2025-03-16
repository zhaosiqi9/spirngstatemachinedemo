package com.statemachinedemo.persist;

import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.persist.AbstractStateMachinePersister;

/**
 * @date 2025-03-12 08:18:16
 */
@Slf4j
public class RedisStateMachinePersister<S, E> extends AbstractStateMachinePersister<S, E, String> {

    private RedisStateMachineContextRepository repository;

    public RedisStateMachinePersister(
            StateMachinePersist<S, E, String> stateMachinePersist,
            RedisStateMachineContextRepository repository) {
        super(stateMachinePersist);
        this.repository = repository;
    }

    public boolean deleteStateMachine(String cmdCode) {
        return this.repository.deleteStateMachine(cmdCode);
    }

    public StateMachineContext<S, E> parseStateMachineContext(String cmdCode) {
        return this.repository.getContext(cmdCode);
    }

    /**
     * 状态机日志打印
     *
     * @param machine
     * @param id
     */
    public void log(StateMachine<S, E> machine, String id) {
        log.info("状态机日志 -- StateMachine Id：{}，当前状态：{}", id, machine.getState());
    }
}
