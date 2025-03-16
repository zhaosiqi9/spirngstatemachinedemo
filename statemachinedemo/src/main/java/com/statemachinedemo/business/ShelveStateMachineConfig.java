package com.statemachinedemo.business;

import com.statemachinedemo.constant.StateMachineBeanNameConstant;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

/**
 * @date 2025-03-11 15:14:52
 */
@Configuration
@EnableStateMachineFactory(name = StateMachineBeanNameConstant.SHELVE_STATE_MACHINE_NAME)
public class ShelveStateMachineConfig extends EnumStateMachineConfigurerAdapter<ShelveState, ShelveEvent> {
    @Override
    public void configure(StateMachineStateConfigurer<ShelveState, ShelveEvent> states) throws Exception {
        states.withStates()
                .initial(ShelveState.NEW)
                .states(EnumSet.allOf(ShelveState.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<ShelveState, ShelveEvent> transitions) throws Exception {
        transitions
                .withExternal().source(ShelveState.NEW).target(ShelveState.EXECUTING).event(ShelveEvent.SHELVE_START)
                .and()
                .withExternal().source(ShelveState.EXECUTING).target(ShelveState.SUCCESS).event(ShelveEvent.SHELVE_SUCCESS)
                .and()
                .withExternal().source(ShelveState.EXECUTING).target(ShelveState.FAILED).event(ShelveEvent.SHELVE_FAILURE);
    }
}
