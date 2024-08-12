package com.dustin.boardserver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

// @Configuration: 이 클래스가 스프링의 설정 클래스임을 나타냅니다.
// 애플리케이션 컨텍스트에 빈(bean)들을 정의하고 설정을 관리합니다.
@Configuration
public class RedisConfig {

    // @Value 어노테이션: 외부 설정 파일(application.properties 또는 application.yml)에서 값을 주입받습니다.
    // Redis 서버의 호스트 주소를 주입받아 저장합니다.
    @Value("${spring.data.redis.host}")
    private String redisHost;

    // Redis 서버의 포트 번호를 주입받아 저장합니다.
    @Value("${spring.data.redis.port}")
    private int redisPort;

    // Redis 서버의 비밀번호를 주입받아 저장합니다.
    @Value("${spring.data.redis.password}")
    private String redisPwd;

    // 기본 캐시 만료 시간을 주입받아 저장합니다. 이 값은 초 단위입니다.
    @Value("${expire.defaultTime}")
    private long defaultExpireSecond;

    // ObjectMapper를 빈(bean)으로 등록합니다. 이 ObjectMapper는 JSON 직렬화 및 역직렬화를 수행하는 데 사용됩니다.
    @Bean
    public ObjectMapper objectMapper() {
        // 새로운 ObjectMapper 인스턴스를 생성합니다.
        ObjectMapper mapper = new ObjectMapper();

        // 날짜를 타임스탬프 대신 ISO-8601 형식으로 직렬화하도록 설정을 비활성화합니다.
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Java 8의 날짜 및 시간 API와 JDK 8 모듈을 등록하여 지원합니다.
        mapper.registerModules(new JavaTimeModule(), new Jdk8Module());

        // 구성된 ObjectMapper 객체를 반환합니다.
        return mapper;
    }

    // RedisConnectionFactory를 빈(bean)으로 등록합니다. 이 팩토리는 Redis 서버와의 연결을 관리합니다.
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // RedisStandaloneConfiguration 객체를 생성하고, Redis 서버의 호스트, 포트 및 비밀번호를 설정합니다.
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setPort(redisPort);
        redisStandaloneConfiguration.setHostName(redisHost);
        redisStandaloneConfiguration.setPassword(redisPwd);

        // LettuceConnectionFactory를 사용하여 RedisConnectionFactory를 생성하고 반환합니다.
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
        return lettuceConnectionFactory;
    }

    // RedisCacheManager를 빈(bean)으로 등록합니다. 이 매니저는 Redis 캐시를 관리합니다.
    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory,
                                               ObjectMapper objectMapper) {
        // RedisCacheConfiguration 객체를 생성하여 캐시 설정을 구성합니다.
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues() // null 값 캐시를 비활성화합니다.
                .entryTtl(Duration.ofSeconds(defaultExpireSecond)) // 기본 캐시 만료 시간을 설정합니다.
                .serializeKeysWith(RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(new StringRedisSerializer())) // 키를 문자열로 직렬화합니다.
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper))); // 값을 JSON으로 직렬화합니다.

        // 설정된 RedisCacheManager를 반환합니다. 이 매니저는 Redis 서버와의 연결 팩토리를 사용합니다.
        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory)
                .cacheDefaults(configuration) // 기본 캐시 설정을 적용합니다.
                .build();
    }
}
