package com.ucsmy.ucas.ca.dao;

import com.ucsmy.ucas.ca.entity.CaCerMongoFile;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Mongodb 接口
 *
 * @author zhengfucheng
 * @version 1.0.0 2017-04-26
 */
@Mapper
public interface CaCerMongoFileMapper extends MongoRepository<CaCerMongoFile, String> {



}
