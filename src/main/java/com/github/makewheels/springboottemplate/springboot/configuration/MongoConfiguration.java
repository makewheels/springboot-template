package com.github.makewheels.springboottemplate.springboot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import javax.annotation.Resource;

@Configuration
public class MongoConfiguration {
    @Resource
    private MongoDatabaseFactory mongoDbFactory;
    @Resource
    private MongoMappingContext mongoMappingContext;

    /**
     * 转换类配置
     */
    @Bean
    public MappingMongoConverter mappingMongoConverter() {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory);
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, mongoMappingContext);
        //不保存 _class 属性到mongo
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return converter;
    }
}
