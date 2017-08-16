package com.ucsmy.ucas.ca.dao;

import com.ucsmy.ucas.ca.entity.CaCerMongoFile;
import com.ucsmy.ucas.ca.utils.CaHelper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;


/**
 * Mongodb 测试类
 *
 * @author zhengfucheng
 * @version 1.0.0 2017-04-26
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CaCerMongoFileMapperTest {

    private final String id = CaHelper.generateUUID();
    private final byte[] data = "测试的字节数据".getBytes();
    private final String fileName = "测试文件名称";

    @Autowired
    private CaCerMongoFileMapper caCerMongoFileMapper;

    @Test
    public void processTest() {
        insertTest();
        getTest();
        deleteTest();
    }

    public void insertTest() {
        CaCerMongoFile caCerMongoFile = new CaCerMongoFile();
        caCerMongoFile.setFileId(id);
        caCerMongoFile.setFileData(data);
        caCerMongoFile.setFileName(fileName);
        caCerMongoFile.setCreateDate(new Date());
        caCerMongoFileMapper.save(caCerMongoFile);

        CaCerMongoFile result = caCerMongoFileMapper.findOne(id);
        Assert.assertEquals(result.getFileName(), caCerMongoFile.getFileName());
    }

    public void getTest() {
        CaCerMongoFile caCerMongoFile = caCerMongoFileMapper.findOne(id);
        Assert.assertArrayEquals(caCerMongoFile.getFileData(), data);
    }

    public void deleteTest() {
        caCerMongoFileMapper.delete(id);
        CaCerMongoFile caCerMongoFile = caCerMongoFileMapper.findOne(id);
        Assert.assertEquals(caCerMongoFile, null);
    }

}