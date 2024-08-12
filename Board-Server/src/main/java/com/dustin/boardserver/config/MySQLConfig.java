package com.dustin.boardserver.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

// @Configuration: 이 클래스가 스프링의 설정 클래스임을 나타냅니다.
// 애플리케이션 컨텍스트에 빈(bean)들을 정의할 수 있습니다.
@Configuration

// @MapperScan: 이 어노테이션은 MyBatis 매퍼 인터페이스를 스캔할 패키지를 지정합니다.
// 이 경우, "com.dustin.boardserver" 패키지 내의 매퍼 인터페이스들을 자동으로 인식하고 스프링 빈으로 등록합니다.
@MapperScan(basePackages = "com.dustin.boardserver")
public class MySQLConfig {

    // @Bean: 이 메서드는 스프링 컨테이너에서 관리하는 빈(bean)을 정의합니다.
    // 이 경우, SqlSessionFactory 객체를 생성하여 반환하는 역할을 합니다.
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        // SqlSessionFactoryBean을 생성하여 SqlSessionFactory를 구성합니다.
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();

        // 데이터베이스 연결 풀을 관리하는 DataSource를 설정합니다.
        sessionFactory.setDataSource(dataSource);

        // PathMatchingResourcePatternResolver를 사용하여 매퍼 XML 파일의 경로를 설정합니다.
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactory.setMapperLocations(resolver.getResources("classpath:mappers/*.xml"));

        // MyBatis 설정 파일을 로드하여 SqlSessionFactory에 설정합니다.
        Resource myBatisConfig = new PathMatchingResourcePatternResolver().getResource("classpath:mybatis-config.xml");
        sessionFactory.setConfigLocation(myBatisConfig);

        // SqlSessionFactory 객체를 반환합니다. 이 객체는 MyBatis와 상호작용할 때 사용됩니다.
        return sessionFactory.getObject();
    }
}