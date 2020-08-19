package com.runlion.multilingual;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author sixiaojie
 * @date 2020-08-19-10:00
 */
@Configuration
@MapperScan(value = "com.runlion.multilingual.mapper")
@ComponentScan(value = "com.runlion.multilingual")
public class MultilingualAutoConfiguration {
}
