package com.ucsmy.ucas.ca.web;

import com.ucsmy.ucas.ca.entity.CaCerInfo;
import com.ucsmy.ucas.ca.param.RequestParam;
import com.ucsmy.ucas.ca.service.CertificationService;
import com.ucsmy.ucas.commons.aop.exception.result.AosResult;
import com.ucsmy.ucas.commons.aop.exception.result.ResResult;
import com.ucsmy.ucas.config.shiro.ShiroRealmImpl;
import com.ucsmy.ucas.config.shiro.ShiroUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * Created by ucs_xujunwei on 2017/4/26.
 */
@Controller
@RequestMapping("/cert")
public class CertificationController {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private CertificationService certificationService;

    @RequestMapping("/list/{type}")
    @ResponseBody
    public ResResult queryCertificationList(@PathVariable String type, CaCerInfo caCerInfo,
            int pageNum, int pageSize){
        caCerInfo.setCerType(type);
        return this.certificationService.queryCertificationListByPage(caCerInfo, pageNum, pageSize);
    }

    /**
     * 单一 创建证书
     * @param caCerInfo 数字证书信息
     * @return
     */
    @RequestMapping(value = "/server", method = RequestMethod.POST)
    @ResponseBody
    public ResResult createCertificate(CaCerInfo caCerInfo) {
        ShiroRealmImpl.LoginUser user = ShiroUtils.getContextUser();
        caCerInfo.setCreateUser(user.getLoginUserName());
        return this.certificationService.createCertificate(caCerInfo);
    }

    /**
     * 初始化 CA 根证书
     * @return
     */
    @RequestMapping(value = "/ca", method = RequestMethod.POST)
    @ResponseBody
    public AosResult createCACertificate(CaCerInfo caCerInfo) {
        this.certificationService.createCACertificate(caCerInfo);
        return AosResult.retSuccessMsg("根证书生成成功");
    }

    /**
     * 查询根证书
     * @return
     */
    @RequestMapping(value = "/ca", method = RequestMethod.GET)
    @ResponseBody
    public AosResult queryCACertificate(){
        return this.certificationService.queryCACertification();
    }


    /**
     * 吊销指定证书
     * @param requestParam
     * @return
     */
    @RequestMapping(value = "/revoke")
    @ResponseBody
    public ResResult revokeCertificate(RequestParam requestParam){
        ResResult result;
        try {
            result = this.certificationService.revokeCertification(requestParam);
        } catch (Exception e) {
            return ResResult.retFailureMsg(e.getMessage(), null);
        }
        return result;
    }

    /**
     * 删除证书接口
     * @param requestParam
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public ResResult deleteCertificate(RequestParam requestParam) {
        ResResult result;
        try {
            result = certificationService.deleteCertificate(requestParam);
        } catch (Exception e) {
            return ResResult.retFailureMsg(e.getMessage(), null);
        }
        return result;
    }

    @RequestMapping(value = "/ca/file/{fileIds}", method = RequestMethod.GET)
    public String download(@PathVariable String fileIds, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        OutputStream outputStream = null;
        try {
            String[] fileIdArray = fileIds.split(",");
            ResResult resResult = certificationService.downloadCertificate(fileIdArray);
            httpServletResponse.setContentType("application/force-download");// 设置强制下载不打开
            httpServletResponse.addHeader("Content-Disposition",
                    "attachment;fileName=ucsmy.zip");
            outputStream = httpServletResponse.getOutputStream();
            outputStream.write((byte[]) resResult.getData());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            try {
                if(outputStream != null){
                    outputStream.close();
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        return null;
    }
}
