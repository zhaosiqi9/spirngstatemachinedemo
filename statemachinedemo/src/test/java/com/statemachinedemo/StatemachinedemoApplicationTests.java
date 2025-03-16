package com.statemachinedemo;

import com.statemachinedemo.business.ShelveEvent;
import com.statemachinedemo.business.ShelveEventProducer;
import com.statemachinedemo.business.ShelveState;
import com.statemachinedemo.persist.RedisStateMachinePersister;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StatemachinedemoApplicationTests {

    @Autowired
    private ShelveEventProducer producer;

    @Autowired
    private RedisStateMachinePersister<ShelveState, ShelveEvent> persister;

    @Test
    void contextLoads() {
        producer.sendStartEvent("1");
        System.out.println(persister.parseStateMachineContext("1"));
        producer.sendSuccessEvent("1");
        System.out.println(persister.parseStateMachineContext("1"));
    }

}
