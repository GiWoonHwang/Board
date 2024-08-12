package com.dustin.boardserver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;


// @Configuration: 이 클래스가 스프링 설정 클래스임을 나타냅니다.
// 이 클래스는 스프링 애플리케이션 컨텍스트에서 빈(bean) 정의를 포함할 수 있습니다.
@Configuration
public class DatabaseConfig {

    // @ConfigurationProperties: 이 어노테이션은 지정된 접두사(prefix)로 시작하는 속성들을 바인딩하여
    // DataSource 객체를 구성하는 데 사용합니다.
    // "spring.datasource" 접두사는 외부 설정 파일(application.properties 또는 application.yml)에서
    // 데이터베이스 연결 정보를 가져오는 데 사용됩니다.
    @ConfigurationProperties(prefix = "spring.datasource")

    // @Bean: 이 메서드는 스프링 컨테이너에 의해 관리되는 빈(bean)을 정의합니다.
    // 이 경우, DataSource 빈이 생성됩니다.
    @Bean
    public DataSource dataSource() {
        // DataSourceBuilder를 사용하여 DataSource 객체를 생성합니다.
        // 여기서는 외부 설정 파일에서 주입된 값들을 사용하여 DataSource를 구성하고 반환합니다.
        return DataSourceBuilder.create().build();
    }
}


