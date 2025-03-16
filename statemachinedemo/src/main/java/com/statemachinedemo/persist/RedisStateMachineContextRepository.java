package com.statemachinedemo.persist;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.messaging.MessageHeaders;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachineContextRepository;
import org.springframework.statemachine.kryo.MessageHeadersSerializer;
import org.springframework.statemachine.kryo.StateMachineContextSerializer;
import org.springframework.statemachine.kryo.UUIDSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @date 2025-03-12 08:16:51
 */
public class RedisStateMachineContextRepository<S, E> implements StateMachineContextRepository<S, E, StateMachineContext<S, E>> {
    private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.addDefaultSerializer(StateMachineContext.class, new StateMachineContextSerializer());
        kryo.addDefaultSerializer(MessageHeaders.class, new MessageHeadersSerializer());
        kryo.addDefaultSerializer(UUID.class, new UUIDSerializer());
        return kryo;
    });
    private final RedisOperations<String, byte[]> redisOperations;

    public RedisStateMachineContextRepository(RedisConnectionFactory redisConnectionFactory) {
        this.redisOperations = createDefaultTemplate(redisConnectionFactory);
    }

    private static RedisTemplate<String, byte[]> createDefaultTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, byte[]> template = new RedisTemplate();
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setConnectionFactory(connectionFactory);
        template.afterPropertiesSet();
        return template;
    }

    public String generateStateMachineKey(String id) {
        return "stateMachineKey: " + id;
    }

    public void save(StateMachineContext<S, E> context, String id) {
        this.redisOperations.opsForValue().set(generateStateMachineKey(id), this.serialize(context), 1L, TimeUnit.DAYS);
    }

    public boolean deleteStateMachine(String id) {
        return this.redisOperations.delete(generateStateMachineKey(id));
    }

    public StateMachineContext<S, E> getContext(String id) {
        return this.deserialize(this.redisOperations.opsForValue().get(generateStateMachineKey(id)));
    }

    private byte[] serialize(StateMachineContext<S, E> context) {
        Kryo kryo = kryoThreadLocal.get();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Output output = new Output(out);
        kryo.writeObject(output, context);
        output.close();
        return out.toByteArray();
    }

    private StateMachineContext<S, E> deserialize(byte[] data) {
        if (data != null && data.length != 0) {
            Kryo kryo = kryoThreadLocal.get();
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            Input input = new Input(in);
            return (StateMachineContext) kryo.readObject(input, StateMachineContext.class);
        } else {
            return null;
        }
    }
}