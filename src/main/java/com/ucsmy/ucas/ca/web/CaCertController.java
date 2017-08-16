package com.ucsmy.ucas.ca.web;/*
 * Copyright (c) 2017 UCSMY.
 * All rights reserved.
 * Created on 2017/7/25

 * Contributors:
 *      - initial implementation
 */

import com.ucsmy.ucas.ca.entity.CaCerInfo;
import com.ucsmy.ucas.ca.param.DownloadParam;
import com.ucsmy.ucas.ca.param.PageParam;
import com.ucsmy.ucas.ca.param.QueryParam;
import com.ucsmy.ucas.ca.param.RequestParam;
import com.ucsmy.ucas.ca.service.CertificationService;
import com.ucsmy.ucas.commons.aop.exception.result.ResResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 证书相关接口
 *
 * @author ucs_masiming
 * @since 2017/7/25
 */
@RestController
@RequestMapping("/certificate")
public class CaCertController {

    @Autowired
    private CertificationService certificationService;

    /**
     * 创建证书接口
     * @param caCerInfo
     */
    @RequestMapping(value = "/createCertificate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResResult createCertificate(@RequestBody CaCerInfo caCerInfo) {
        return certificationService.createCertificate(caCerInfo);
    }

    /**
     * 吊销证书接口
     * @param revokeParam
     */
    @RequestMapping(value = "/revokeCertificate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResResult revokeCertificate(@RequestBody RequestParam revokeParam) {
        ResResult result;
        try {
            result = certificationService.revokeCertification(revokeParam);
        } catch (Exception e) {
            return ResResult.retFailureMsg(e.getMessage(), null);
        }
        return result;
    }

    /**
     * 删除证书接口
     * @param revokeParam
     */
    @RequestMapping(value = "/deleteCertificate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResResult deleteCertificate(@RequestBody RequestParam revokeParam) {
        ResResult result;
        try {
            result = certificationService.deleteCertificate(revokeParam);
        } catch (Exception e) {
            return ResResult.retFailureMsg(e.getMessage(), null);
        }
        return result;
    }

    /**
     * 获取设备证书密码接口
     * @param param
     */
    @RequestMapping(value = "/queryCertPassword", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResResult queryCertPassword(@RequestBody QueryParam param) {
        return certificationService.queryCertPassword(param);
    }

    /**
     * 获取证书信息接口
     * @param queryParam
     */
    @RequestMapping(value = "/queryCertInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResResult queryCertInfo(@RequestBody QueryParam queryParam) {
        return certificationService.queryCertInfo(queryParam);
    }

    /**
     * 分页查询证书列表
     * @param pageParam
     */
    @RequestMapping(value = "/queryCertListByPage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResResult queryCertListByPage(@RequestBody PageParam pageParam) {
        CaCerInfo caCerInfo = new CaCerInfo();
        caCerInfo.setCerType(pageParam.getCerType());
        return certificationService.queryCertificationListByPage(caCerInfo, pageParam.getPageNum(),
                pageParam.getPageSize());
    }

    /**
     * 下载证书
     * @param param
     */
    @RequestMapping(value = "/downloadCertificate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResResult downloadCertificate(@RequestBody DownloadParam param) {
        return certificationService.downloadCert(param.getCerCode(), param.getFileType());
    }

    /**
     * 下载吊销列表
     */
    @RequestMapping(value = "/downloadCrl", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResResult downloadCrl(@RequestBody CaCerInfo caCerInfo) {
        return certificationService.downloadCrlFile();
    }
}
