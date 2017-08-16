package com.ucsmy.ucas.ca.dao;

import com.ucsmy.ucas.ca.entity.CaCerFile;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * CaCerFileMapper
 *
 * @author zhengfucheng
 * @version 1.0.0 2017-04-28
 */
@Mapper
public interface CaCerFileMapper {


    /**
     * 插入证书文件
     * @param caCerFile
     * @return
     */
    int insert(CaCerFile caCerFile);


    /**
     * 删除所有证书文件
     * @return
     */
    int delete();

    /**
     * 根据域名删除证书
     * @param domainName
     */
    int deleteByDomainName(String domainName);

    /**
     * 根据证书编号获取mongoDB文件id集合
     * @param cerUuid
     */
    List<String> queryFileIdsByCerUuid(String cerUuid);

    /**
     * @Description: 批量删除
     * @param fileIds
     * @return int
     */
    int batchDelete(List<String> fileIds);

    /**
     * @Description: 根据域名、文件类型、证书状态获取mongoDB文件id
     * @param map
     * @return String
     */
    String queryFileIdsByMap(Map<String, String> map);
}
