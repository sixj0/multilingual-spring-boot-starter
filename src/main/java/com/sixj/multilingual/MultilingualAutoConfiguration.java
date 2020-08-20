package com.sixj.multilingual;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author sixiaojie
 * @date 2020-08-19-10:00
 */
@Configuration
@MapperScan(value = "com.sixj.multilingual.mapper")
@ComponentScan(value = "com.sixj.multilingual")
public class MultilingualAutoConfiguration {
}
