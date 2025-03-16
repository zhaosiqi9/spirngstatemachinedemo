package com.statemachinedemo.configuration;

import com.statemachinedemo.persist.RedisStateMachineContextRepository;
import com.statemachinedemo.persist.RedisStateMachinePersister;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.statemachine.persist.RepositoryStateMachinePersist;

/**
 * @date 2025-03-11 16:58:03
 */
@Configuration
public class StateMachinePersistConfig<S extends Enum<S>, E extends Enum<E>> {


    @Bean
    public RedisStateMachinePersister<S, E> redisStateMachinePersister(
            RepositoryStateMachinePersist persist,
            RedisStateMachineContextRepository repository
    ) {
        return new RedisStateMachinePersister<>(persist, repository);
    }

    @Bean
    public RepositoryStateMachinePersist stateMachinePersist(RedisStateMachineContextRepository repository) {
        return new RepositoryStateMachinePersist<>(repository);
    }

    @Bean
    public RedisStateMachineContextRepository stateMachineContextRepository(RedisConnectionFactory factory) {
        return new RedisStateMachineContextRepository(factory);
    }


}
