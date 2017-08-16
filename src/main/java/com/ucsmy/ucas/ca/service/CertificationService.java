package com.ucsmy.ucas.ca.service;

import com.ucsmy.ucas.ca.entity.CaCerInfo;
import com.ucsmy.ucas.ca.param.QueryParam;
import com.ucsmy.ucas.ca.param.RequestParam;
import com.ucsmy.ucas.commons.aop.exception.result.AosResult;
import com.ucsmy.ucas.commons.aop.exception.result.ResResult;

/**
 * Created by ucs_xujunwei on 2017/4/26.
 */
public interface CertificationService {


    /**
     * 分页查询证书列表
     * @param caCerInfo 查询条件实体
     * @param pageNum 当前页
     * @param pageSize 每页大小
     * @return
     */
    ResResult queryCertificationListByPage(CaCerInfo caCerInfo, int pageNum, int pageSize);

    /**
     * 查询ca根证书
     * @return
     */
    AosResult queryCACertification();

    /**
     * 撤销证书
     * @param requestParam
     * @return
     */
    ResResult revokeCertification(RequestParam requestParam);

    /**
     * 下载撤销列表文件
     * @return
     */
    ResResult downloadCrlFile();

    /**
     * 创建 证书
     * @param caCerInfo 创建证书信息
     */
    ResResult createCertificate(CaCerInfo caCerInfo);


    /**
     * 下载 证书
     * @param fileIds
     */
    ResResult downloadCertificate(String... fileIds);

    /**
     * 根据域名和文件类型下载证书
     * @param cerUuid 证书编号
     * @param fileType 文件类型
     */
    ResResult downloadCert(String cerUuid, String fileType);

    /**
     * 初始化 CA 根证书
     * @param caCerInfo
     */
    void createCACertificate(CaCerInfo caCerInfo);

    /**
     * 获取证书密码
     * @param queryParam
     */
    ResResult queryCertPassword(QueryParam queryParam);

    /**
     * 删除证书接口
     * @param requestParam
     */
    ResResult deleteCertificate(RequestParam requestParam);

    /**
     * 获取证书信息接口
     * @param queryParam
     */
    ResResult queryCertInfo(QueryParam queryParam);

    /**
     * 根据域名和文件类型获取文件id
     * @param caCerInfo
     */
    ResResult queryFileIdsByCNAndType(CaCerInfo caCerInfo);
}
