package com.ucsmy.ucas.ca.dao;

/**
 * CaCerInfoMapper
 *
 * @author zhengfucheng
 * @version 1.0.0 2017-04-28
 */

import com.ucsmy.ucas.ca.entity.CaCerInfo;
import com.ucsmy.ucas.ca.ext.CertResponseBody;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CaCerInfoMapper {

    /**
     *
     * @param caCerInfo
     * @return
     */
    int insert(CaCerInfo caCerInfo);


    /**
     * 删除所有证书信息
     * @return
     */
    int delete();

    /**
     * @Description: 根据caCerInfo获取CaCerInfo
     * @param     caCerInfo
     * @return    CaCerInfo
     */
    CaCerInfo getBeanByBean(CaCerInfo caCerInfo);

    /**
     * @Description: 根据map获取证书密码
     * @param     map
     * @return    password
     */
    String queryPasswordByMap(Map<String, String> map);

    /**
     * @Description: 根据域名删除证书
     * @param   domainName
     * @return  int
     */
    int deleteByDomainName(String domainName);

    /**
     * @Description: 根据map获取CaCerInfo集合
     * @param map
     * @return List<CertResponseBody>
     */
    List<CertResponseBody> queryBeansByMap(Map<String, String> map);

    /**
     * @Description: 根据域名和文件类型获取证书文件id
     * @param caCerInfo
     * @return List<String>
     */
    List<String> queryFileIdsByCNAndType(CaCerInfo caCerInfo);

    /**
     * @Description: 根据域名和文件类型获取证书文件id
     * @param cerUuid
     * @return CertResponseBody
     */
    CertResponseBody getBeanById(@Param("cerUuid") String cerUuid);

    /**
     * @Description: 根据域名获取list
     * @param domainName
     * @return list
     */
    List<CaCerInfo> getBeanByCN(String domainName);

    /**
     * @Description: 根据证书编号删除证书信息
     * @param cerCode 证书编号
     * @return list
     */
    int deleteByCerUuid(String cerCode);
}
