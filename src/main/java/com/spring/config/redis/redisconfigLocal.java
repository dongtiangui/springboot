package com.spring.config.redis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class redisconfigLocal {
    
    @Bean(name = "jedisPoolConfig")
    public JedisPoolConfig jedisPoolConfig(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(15);
        jedisPoolConfig.setMaxTotal(20);
        jedisPoolConfig.setLifo(true);
        jedisPoolConfig.setMaxWaitMillis(60000);
        return jedisPoolConfig;
    }
    @Bean(name = "redisStandaloneConfigurationLocal")
    public RedisStandaloneConfiguration redisStandaloneConfiguration(){
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setDatabase(0);
        redisStandaloneConfiguration.setPort(6379);
        redisStandaloneConfiguration.setHostName("127.0.0.1");
        return redisStandaloneConfiguration;
    }
    @Bean("jedisConnectionFactoryLocal")
    public JedisConnectionFactory jedisConnectionFactory(){
        JedisConnectionFactory jedisConnectionFactory =
                new JedisConnectionFactory(redisStandaloneConfiguration());
        jedisConnectionFactory.setPoolConfig(jedisPoolConfig());
        return jedisConnectionFactory;
    }
    @Bean(name = "cacheManagerLocalRedis")
    public CacheManager cacheManager(@Autowired @Qualifier(value = "jedisConnectionFactoryLocal") JedisConnectionFactory jedisConnectionFactory){
        return RedisCacheManager.create(jedisConnectionFactory);
    }
    @Bean(name = "redisTemplateLocal")
    public RedisTemplate<String,Object> redisTemplate(){
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        redisTemplate.setDefaultSerializer(jdkSerializationRedisSerializer());
        redisTemplate.setStringSerializer(stringRedisSerializer());
        redisTemplate.setHashKeySerializer(jdkSerializationRedisSerializer());
        redisTemplate.setKeySerializer(stringRedisSerializer());
        redisTemplate.setValueSerializer(stringRedisSerializer());
        return redisTemplate;
    }

    @Bean(name = "StringRedisTemplateLocal")
    public StringRedisTemplate stringRedisTemplate(){
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        redisTemplate.setDefaultSerializer(jdkSerializationRedisSerializer());
        redisTemplate.setStringSerializer(stringRedisSerializer());
        redisTemplate.setKeySerializer(stringRedisSerializer());
        redisTemplate.setValueSerializer(stringRedisSerializer());
        return redisTemplate;
    }
    @Bean
    public JdkSerializationRedisSerializer jdkSerializationRedisSerializer(){
        return new JdkSerializationRedisSerializer();
    }
    @Bean
    public StringRedisSerializer stringRedisSerializer(){
        return new StringRedisSerializer();
    }
    @Bean
    public GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer(){
        return new GenericJackson2JsonRedisSerializer();
    }

}
