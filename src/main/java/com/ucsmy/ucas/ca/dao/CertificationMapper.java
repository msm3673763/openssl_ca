package com.ucsmy.ucas.ca.dao;

import com.ucsmy.commons.interceptor.domain.PageRequest;
import com.ucsmy.ucas.ca.entity.CaCerInfo;
import com.ucsmy.ucas.ca.ext.CertificationPojo;
import com.ucsmy.ucas.commons.page.UcasPageInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by ucs_xujunwei on 2017/4/26.
 */
@Mapper
public interface CertificationMapper {
    /**
     * 分页查询证书id列表
     * @param certificationPojo 查询条件实体
     * @param pageRequest 分页信息
     * @return
     */
    UcasPageInfo<String> queryCertificationListByPage(@Param("certification") CaCerInfo caCerInfo, PageRequest pageRequest);

    /**
     * 根据id列表查询具体证书列表
     * @param ids 证书id集合
     * @return
     */
    List<CertificationPojo> queryCertificationListById(@Param("ids") List<String> ids);

    /**
     * 查询ca根证书
     * @return
     */
    CertificationPojo queryCACertification();

    /**
     * 根据id查询证书
     * @param certId 证书id
     * @return
     */
    CertificationPojo queryCertificationById(String certId);

    /**
     * 根据id和文件类型查询证书
     * @param param 参数
     * @return
     */
    CertificationPojo queryCertificationByIdAndFileType(Map<String, Object> param);

    /**
     * 修改证书状态
     * @param param 参数
     * @return
     */
    int updateCertStatusAndCrl(Map<String, Object> param);

    /**
     * 根据域名和文件类型查询证书
     * @param param 参数
     * @return
     */
    CertificationPojo queryCertByIdAndFileType(Map<String, Object> param);
}
