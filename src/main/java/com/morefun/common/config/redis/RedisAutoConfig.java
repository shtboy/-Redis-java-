package com.morefun.common.config.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisAutoConfig {

    @Bean
    public RedisConnectionFactory redisConnectionFactory(JedisPoolConfig jedisPool,
                                                         RedisStandaloneConfiguration jedisConfig) {
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory(jedisConfig);
        connectionFactory.setPoolConfig(jedisPool);
        return connectionFactory;
    }

    @Configuration
    public static class JedisConf {
        @Value("${spring.redis.host}")
        private String host;
        @Value("${spring.redis.port}")
        private Integer port;
        @Value("${spring.redis.password}")
        private String password;
        @Value("${spring.redis.database}")
        private Integer database;

        @Value("${spring.redis.jedis.pool.max-active}")
        private Integer maxActive;
        @Value("${spring.redis.jedis.pool.max-idle}")
        private Integer maxIdle;
        @Value("${spring.redis.jedis.pool.max-wait:}")
        private Long maxWait;
        @Value("${spring.redis.jedis.pool.min-idle}")
        private Integer minIdle;

        @Bean
        public JedisPoolConfig jedisPool() {
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxIdle(maxIdle);
            jedisPoolConfig.setMaxWaitMillis(maxWait);
            jedisPoolConfig.setMaxTotal(maxActive);
            jedisPoolConfig.setMinIdle(minIdle);
            return jedisPoolConfig;
        }

        @Bean
        public RedisStandaloneConfiguration jedisConfig() {
            RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
            config.setHostName(host);
            config.setPort(port);
            config.setDatabase(database);
            config.setPassword(RedisPassword.of(password));
            return config;
        }

        @Bean("objRedisTemplate")
        public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
            RedisTemplate<String, Object> template = new RedisTemplate<>();
            template.setConnectionFactory(redisConnectionFactory);
            //1.修改键的序列化策略
            template.setKeySerializer(new StringRedisSerializer());
            //2.修改hash类型的value中key的序列化方式
            template.setHashKeySerializer(new StringRedisSerializer());
            //4.设置value的序列化策略
            Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
            template.setValueSerializer(jackson2JsonRedisSerializer);
            //5.修改hash类型value中value的序列化方式
            template.setHashValueSerializer(jackson2JsonRedisSerializer);
            ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();
            objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
            jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
            template.afterPropertiesSet();
            return template;
        }

        @Bean("strRedisTemplate")
        public RedisTemplate strRedisTemplate(RedisConnectionFactory factory) {
            RedisTemplate redisTemplate = new RedisTemplate();
            RedisSerializer stringSerializer = new StringRedisSerializer();
            redisTemplate.setConnectionFactory(factory);
            redisTemplate.setKeySerializer(stringSerializer);
            redisTemplate.setValueSerializer(stringSerializer);
            redisTemplate.setHashKeySerializer(stringSerializer);
            redisTemplate.setHashValueSerializer(stringSerializer);
            return redisTemplate;
        }
    }


//    // 优化预减库存脚本
//    @Bean(name = "stock")
//    public DefaultRedisScript<Long> stockScript() {
//        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
//        //放在和application.yml 同层目录下
//        redisScript.setLocation(new ClassPathResource("stock.lua"));
//        redisScript.setResultType(Long.class);
//        return redisScript;
//    }



}
