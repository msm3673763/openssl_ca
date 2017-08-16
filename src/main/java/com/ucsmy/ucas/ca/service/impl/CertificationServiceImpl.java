package com.ucsmy.ucas.ca.service.impl;

import com.ucsmy.commons.interceptor.domain.PageRequest;
import com.ucsmy.commons.utils.StringAndNumberUtil;
import com.ucsmy.commons.utils.UUIDGenerator;
import com.ucsmy.ucas.ca.Exception.CertException;
import com.ucsmy.ucas.ca.constant.CerStatus;
import com.ucsmy.ucas.ca.constant.CerType;
import com.ucsmy.ucas.ca.constant.ConfigConstant;
import com.ucsmy.ucas.ca.constant.FileSuffix;
import com.ucsmy.ucas.ca.dao.CaCerFileMapper;
import com.ucsmy.ucas.ca.dao.CaCerInfoMapper;
import com.ucsmy.ucas.ca.dao.CaCerMongoFileMapper;
import com.ucsmy.ucas.ca.dao.CertificationMapper;
import com.ucsmy.ucas.ca.entity.CaCerFile;
import com.ucsmy.ucas.ca.entity.CaCerInfo;
import com.ucsmy.ucas.ca.entity.CaCerMongoFile;
import com.ucsmy.ucas.ca.entity.CertModel;
import com.ucsmy.ucas.ca.ext.CertResponseBody;
import com.ucsmy.ucas.ca.ext.CertificationPojo;
import com.ucsmy.ucas.ca.ext.FilePojo;
import com.ucsmy.ucas.ca.param.QueryParam;
import com.ucsmy.ucas.ca.param.RequestParam;
import com.ucsmy.ucas.ca.service.CertificationService;
import com.ucsmy.ucas.ca.utils.CaHelper;
import com.ucsmy.ucas.ca.utils.CertUtil;
import com.ucsmy.ucas.ca.utils.FileUtil;
import com.ucsmy.ucas.commons.aop.exception.result.AosResult;
import com.ucsmy.ucas.commons.aop.exception.result.ResResult;
import com.ucsmy.ucas.commons.page.UcasPageInfo;
import com.ucsmy.ucas.manage.dao.ManageConfigMapper;
import com.ucsmy.ucas.manage.service.SysCacheService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by ucs_xujunwei on 2017/4/26.
 */
@Service
public class CertificationServiceImpl implements CertificationService{

    private static final Logger LOGGER = LoggerFactory.getLogger(CertificationServiceImpl.class);
    private static final String CRL_FILEDATA_KEY = "ucas_ca:crl:fileData";
    public static final String CRL_TIME_KEY = "ucas_ca:crl:time";
    private static final String CERTYPE_IS_NULL= "证书类型不能为空！";
    private static final String CERCODE_IS_NULL = "证书编号不能为空！";

    @Autowired
    private CertificationMapper certificationMapper;
    @Autowired
    private CaCerMongoFileMapper caCerMongoFileMapper;
    @Autowired
    private CaCerInfoMapper caCerInfoMapper;
    @Autowired
    private CaCerFileMapper caCerFileMapper;
    @Autowired
    private ManageConfigMapper manageConfigMapper;
    @Autowired
    private SysCacheService sysCacheService;

    /**
     * 分页查询证书列表
     * @param caCerInfo 查询条件实体
     * @return
     */
    @Override
    @com.ucsmy.ucas.commons.aop.annotation.Logger(printSQL = true)
    public ResResult queryCertificationListByPage(CaCerInfo caCerInfo, int pageNum, int pageSize) {
        if (StringAndNumberUtil.isNullAfterTrim(caCerInfo.getCerType())) {
            return ResResult.retFailureMsg(ResResult.ResultType.PARAMETER_ERROR, CERTYPE_IS_NULL);
        }
        caCerInfo.setCerType(caCerInfo.getCerType());
        UcasPageInfo<String> idList = this.certificationMapper.queryCertificationListByPage(
                caCerInfo, new PageRequest(pageNum, pageSize));
        List<String> ids = idList.getResultList();
        List<CertificationPojo> certificationList = new ArrayList<>();
        if (!ids.isEmpty()) {
            certificationList = this.certificationMapper.queryCertificationListById(ids);
        }
        UcasPageInfo<CertificationPojo> pageInfo = new UcasPageInfo<>();
        pageInfo.setPageNo(idList.getPageNo());
        pageInfo.setPageSize(idList.getPageSize());
        pageInfo.setTotalCount(idList.getTotalCount());
        pageInfo.setPages(idList.getPages());
        pageInfo.setResultList(certificationList);
        return ResResult.retSuccessMsg("成功", pageInfo);
    }

    /**
     * 查询ca根证书
     * @return
     */
    @Override
    public AosResult queryCACertification(){
        CertificationPojo caCert = this.certificationMapper.queryCACertification();
        if(caCert == null){
            return AosResult.retFailureMsg("根证书不存在");
        }
        return AosResult.retSuccessMsg("", caCert);
    }

    /**
     * 撤销证书
     * @param param
     * @return
     */
    @Override
    @Transactional
    public ResResult revokeCertification(RequestParam param){
        if (StringAndNumberUtil.isNullAfterTrim(param.getCerCode())) {
            return ResResult.retFailureMsg(ResResult.ResultType.PARAMETER_ERROR, CERCODE_IS_NULL);
        }

        if (StringAndNumberUtil.isNullAfterTrim(param.getCerType())) {
            return ResResult.retFailureMsg(ResResult.ResultType.PARAMETER_ERROR, CERTYPE_IS_NULL);
        }

        try {
            //1获取要吊销的证书文件
            Map<String, Object> map = new HashMap<>();
            map.put("cerUuid", param.getCerCode());
            map.put("fileType", FileSuffix.CRT.getSuffix());
            map.put("cerType", param.getCerType());
            CertificationPojo certification = this.certificationMapper.queryCertByIdAndFileType(map);
            if (certification == null) {
                return ResResult.retFailureMsg("编号为[" + param.getCerCode() +
                        "]的证书不存在，吊销失败", null);
            }

            if (CerStatus.INVALID.getIndex().equals(certification.getCerStatus())) {
                return ResResult.retFailureMsg("该证书已被吊销，吊销失败", null);
            }

            String crlName;
            List<FilePojo> fileList =  certification.getFileList();
            if (fileList!=null && fileList.size()>1) {//超过一个文件，证书出错，直接返回错误
                return ResResult.retFailureMsg("存在多个证书，吊销失败", null);
            } else if (fileList!=null && fileList.size()==1) {      //只有一个文件的情况
                String crtName = certification.getFileName() + "." + FileSuffix.CRT.getSuffix();
                String certPath = this.manageConfigMapper.queryByName(ConfigConstant.CA_CERT_PATH).getParamValue();
                // 获取文件,先从本地磁盘找，如果没有再从mongodb找
                File crtFile = new File(certPath + crtName);
                boolean flag = true;  //继续执行标志
                if (!crtFile.exists() || !crtFile.isFile()) {  //文件在本地不存在，需要从mangodb取回
                    String fileId = fileList.get(0).getFileDBId();
                    CaCerMongoFile crtMongoFile = caCerMongoFileMapper.findOne(fileId);
                    if (crtMongoFile == null) {           //mongodb上面找不到文件,不需要进行后续操作
                        flag = false;
                    } else {                   //将文件保存在本地
                        FileUtil.saveFile(certPath + crtName, crtMongoFile.getFileData());
                    }
                }
                if (flag) {
                    //2获取ca证书
                    // 先在本地找，找不到再从mongodb找，再找不到，返回错误
                    CertificationPojo caCert = this.certificationMapper.queryCACertification();
                    if (caCert == null) {
                        return ResResult.retFailureMsg("ca根证书不存在，吊销失败", null);
                    }
                    String caName = caCert.getDomainName();
                    String keyName = caName + "." + FileSuffix.KEY.getSuffix();
                    String caCrtName = caName + "." + FileSuffix.CRT.getSuffix();
                    //先查询本地
                    File keyFile = new File(certPath + keyName);
                    File caCrtFile = new File(certPath + caCrtName);
                    if (!keyFile.exists() || !keyFile.isFile() || !caCrtFile.exists() || !caCrtFile.isFile()) {
                        //文件在本地不存在，需要从mangodb取回
                        List<FilePojo> caFileList = caCert.getFileList();
                        FilePojo caKey = null;
                        FilePojo caCrt = null;
                        if (caFileList != null) {
                            for (FilePojo file : caFileList) {
                                if (FileSuffix.CRT.getSuffix().equals(file.getFileType())) {
                                    caCrt = file;
                                    continue;
                                }
                                if (FileSuffix.KEY.getSuffix().equals(file.getFileType())) {
                                    caKey = file;
                                    continue;
                                }
                            }
                        }
                        if (caKey == null || caCrt == null) {
                            return ResResult.retFailureMsg("证书错误，吊销失败", null);
                        }
                        CaCerMongoFile keyMongoFile = caCerMongoFileMapper.findOne(caKey.getFileDBId());
                        CaCerMongoFile caCrtMongoFile = caCerMongoFileMapper.findOne(caCrt.getFileDBId());
                        if (keyMongoFile == null || caCrtMongoFile == null) {   //mongodb上面找不到文件,直接返回错误
                            return ResResult.retFailureMsg("ca根证书不存在，吊销失败", null);
                        }
                        //将文件保存在本地
                        FileUtil.saveFile(certPath + keyName, keyMongoFile.getFileData());
                        FileUtil.saveFile(certPath + caCrtName, caCrtMongoFile.getFileData());
                    }
                    //3执行撤销操作
                    String crlFileId = this.manageConfigMapper.queryByName(ConfigConstant.CRL_FILE_ID).getParamValue();
                    crlName = crlFileId + "." + FileSuffix.CRL.getSuffix();
                    CertUtil.getInstance().revokeCrt(crtName, keyName, caCrtName, crlName);
                    //4将crl保存回mangodb
                    //CaCerMongoFile crlMongoFile = new CaCerMongoFile();
                    //crlMongoFile.setFileId(crlFileId);
                    //crlMongoFile.setCreateDate(new Date());
                    //crlMongoFile.setFileName(crlFileId);
                    //crlMongoFile.setFileType(FileSuffix.CRL.getSuffix());
                    //crlMongoFile.setFileData(FileUtil.readFile(certPath + crlName));
                    //caCerMongoFileMapper.save(crlMongoFile);

                    //add by ucs_masiming 吊销列表放入redis中
                    String crlDataStr = org.apache.commons.codec.binary.Base64
                            .encodeBase64String(FileUtil.readFile(certPath + crlName));
                    sysCacheService.set(CRL_FILEDATA_KEY, crlDataStr, 60*60*12);//1天内有效
                    sysCacheService.set(CRL_TIME_KEY, System.currentTimeMillis(), 60*60*12);//吊销时间
                }
            } else {           //没有文件的情况

            }
            map.clear();
            map.put("certId", certification.getCerUuid());
            map.put("crlUrl", this.manageConfigMapper.queryByName(ConfigConstant.CRL_DOWNLOAD_PATH).getParamValue());
            int result = this.certificationMapper.updateCertStatusAndCrl(map);
            if (result == 0) {
                return ResResult.retFailureMsg("数据库更新失败", null);
            }

            //String url = manageConfigMapper.queryValueByKey(ConfigConstant.UCAS_CA_SERVER_PATH);
            //Map<String, Object> resultMap = TwaGateUtil.getInstance().httpGet(url);
            //if (!"0".equals(resultMap.get("retcode"))) {
            //    return ResResult.retFailureMsg("刷新双向网关吊销列表失败，"
            //            + resultMap.get("retmsg"), null);
            //}
            return ResResult.retSuccessMsg("吊销成功");
        } catch (Exception e) {
            throw new CertException("吊销异常：" + e.getMessage());
        }
    }

    /**
     * 下载撤销列表文件
     * @return
     */
    @Override
    public ResResult downloadCrlFile(){
        byte[] bytes;
        try {
            String certPath = this.manageConfigMapper.queryByName(ConfigConstant.CA_CERT_PATH).getParamValue();
            String crlFileId = this.manageConfigMapper.queryByName(ConfigConstant.CRL_FILE_ID).getParamValue();
            String crlName = crlFileId + "." + FileSuffix.CRL.getSuffix();
            File file = new File(certPath + crlName);
            if (file.exists() && file.isFile()) {
                bytes = FileUtil.readFile(certPath + crlName);
            } else {
                return ResResult.retFailureMsg("文件不存在！", null);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResResult.retFailureMsg(e.getMessage(), null);
        }
        Map<String, String> map = new HashMap<>();
        map.put("crl", org.apache.commons.codec.binary.Base64.encodeBase64String(bytes));
        return ResResult.retSuccessMsg("成功！", map);
    }

    @Override
    @com.ucsmy.ucas.commons.aop.annotation.Logger(printSQL = true)
    public void createCACertificate(CaCerInfo caCerInfo) {
        //0. 检查数据库是否存在
        if(this.isCACertificateExist()) {
            caCerMongoFileMapper.deleteAll();
            caCerFileMapper.delete();
            caCerInfoMapper.delete();
        }
        try {
            //
            String password = CaHelper.generateRandomPassword();
            //1. 初始化
            this.initCACerInfo(caCerInfo);
            //2. 生成密钥对
            CertUtil.getInstance().generateKey(caCerInfo.getDomainName());
            //3. 根证书自签名
            this.signCA(caCerInfo);
            //4. 生成信任库
            CertUtil.getInstance().createTrustStore(caCerInfo.getDomainName(), password, caCerInfo.getDomainName());
            //4. 保存数字证书信息
            this.insertCertificateInfo(caCerInfo);
            //5. 保存数字证书文件
            this.insertCertificateFile(caCerInfo, password, FileSuffix.KEY, FileSuffix.CRT, FileSuffix.JKS);
        }catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            new RuntimeException("CA证书生成失败");
        }
    }

    @Override
    @com.ucsmy.ucas.commons.aop.annotation.Logger(printSQL = true)
    public ResResult queryCertPassword(QueryParam param) {
        if (param==null || StringAndNumberUtil.isNullAfterTrim(param.getCerCode())) {
            return ResResult.retFailureMsg(ResResult.ResultType.PARAMETER_ERROR, CERCODE_IS_NULL);
        }

        Map<String, String> map = new HashMap<>();
        map.put("cerUuid", param.getCerCode());
        map.put("fileType", FileSuffix.P12.getSuffix());
        String password = caCerInfoMapper.queryPasswordByMap(map);
        if (StringAndNumberUtil.isNullAfterTrim(password)) {
            return ResResult.retFailureMsg("编号[" + param.getCerCode() + "]的证书不存在！", null);
        }
        map.clear();
        map.put("password", password);
        return ResResult.retSuccessMsg("查询成功！", map);
    }

    @Override
    @Transactional
    public ResResult deleteCertificate(RequestParam param) {
        if (param==null || StringAndNumberUtil.isNullAfterTrim(param.getCerCode())) {
            return ResResult.retFailureMsg(ResResult.ResultType.PARAMETER_ERROR, CERCODE_IS_NULL);
        }

        if (StringAndNumberUtil.isNullAfterTrim(param.getCerType())) {
            return ResResult.retFailureMsg(ResResult.ResultType.PARAMETER_ERROR, CERTYPE_IS_NULL);
        }

        //判断证书是否存在
        CertResponseBody body = caCerInfoMapper.getBeanById(param.getCerCode());
        if (body == null) {
            return ResResult.retFailureMsg("编号为[" + param.getCerCode() +
                    "]的证书不存在或已被删除，删除失败", null);
        }

        //判断证书是否已经吊销，未吊销不能删除
        if (CerStatus.VALID.getIndex().equals(body.getCerStatus())) {
            return ResResult.retFailureMsg("编号为[" + param.getCerCode() + "]的证书未吊销，不能删除！", null);
        }

        //1、删除mongodb文件
        List<String> fileIds = caCerFileMapper.queryFileIdsByCerUuid(body.getCerCode());
        for (String fileId : fileIds) {
            caCerMongoFileMapper.delete(fileId);
        }

        //2、删除服务器文件
        String certPath = manageConfigMapper.queryByName(ConfigConstant.CA_CERT_PATH).getParamValue();
        FileSuffix[] values = FileSuffix.values();
        File file;
        for (FileSuffix fs : values) {
            if (FileSuffix.CRL.getSuffix().equals(fs.getSuffix())
                    || FileSuffix.JKS.getSuffix().equals(fs.getSuffix())) {
                continue;
            }
            String path = certPath + body.getFileName() + "." + fs.getSuffix();
            file = new File(path);
            if (file.exists()) {
                boolean flag = file.delete();
                if (!flag) {
                    return ResResult.retFailureMsg("删除编号[" + param.getCerCode() + "]失败！", null);
                }
            }
        }

        //3、删除数据库
        int i = caCerFileMapper.batchDelete(fileIds);
        int j = caCerInfoMapper.deleteByCerUuid(body.getCerCode());
        if (i==0 || j==0) {
            throw new CertException("数据库删除失败");
        }
        return ResResult.retSuccessMsg("删除成功！", null);
    }

    @Override
    @com.ucsmy.ucas.commons.aop.annotation.Logger(printSQL = true)
    public ResResult queryCertInfo(QueryParam param) {
        if (param==null || StringAndNumberUtil.isNullAfterTrim(param.getCerCode())) {
            return ResResult.retFailureMsg(CERCODE_IS_NULL, null);
        }

        //判断证书是否存在
        CertResponseBody body = caCerInfoMapper.getBeanById(param.getCerCode());
        if (body == null) {
            return ResResult.retFailureMsg("编号[" + param.getCerCode() + "]的证书不存在！", null);
        } else {
            return ResResult.retSuccessMsg("成功", body);
        }
    }

    @Override
    public ResResult queryFileIdsByCNAndType(CaCerInfo caCerInfo) {
        if (StringAndNumberUtil.isNullAfterTrim(caCerInfo.getDomainName())) {
            return ResResult.retFailureMsg(ResResult.ResultType.PARAMETER_ERROR, "域名不能为空！");
        }
        caCerInfo.setCerStatus(ConfigConstant.CER_STATUS__1);
        List<String> list = caCerInfoMapper.queryFileIdsByCNAndType(caCerInfo);
        if (list.isEmpty()) {
            return ResResult.retFailureMsg("证书不存在或已吊销！", null);
        }
        return ResResult.retSuccessMsg("成功", list.toArray(new String[list.size()]));
    }

    @Override
    public ResResult downloadCert(String cerUuid, String fileType) {
        if (StringAndNumberUtil.isNullAfterTrim(cerUuid)) {
            return ResResult.retFailureMsg(ResResult.ResultType.PARAMETER_ERROR, CERCODE_IS_NULL);
        }
        if (StringAndNumberUtil.isNullAfterTrim(fileType)) {
            return ResResult.retFailureMsg(ResResult.ResultType.PARAMETER_ERROR, "文件类型不能为空！");
        }

        if (!fileType.equals(FileSuffix.KEY.getSuffix()) && !fileType.equals(FileSuffix.CRT.getSuffix())
            && !fileType.equals(FileSuffix.CSR.getSuffix()) && !fileType.equals(FileSuffix.P12.getSuffix())) {
            return ResResult.retFailureMsg(ResResult.ResultType.PARAMETER_ERROR, "文件类型不符合规定！");
        }

        Map<String, String> map = new HashMap<>();
        map.put("cerUuid", cerUuid);
        map.put("fileType", fileType);
        String fileId = caCerFileMapper.queryFileIdsByMap(map);
        if (StringAndNumberUtil.isNullAfterTrim(fileId)) {
            return ResResult.retFailureMsg(ResResult.ResultType.QUERY_NO_RESULT, "该证书不存在！");
        }
        CaCerMongoFile mongoFile = caCerMongoFileMapper.findOne(fileId);
        if (mongoFile == null) {
            return ResResult.retFailureMsg(ResResult.ResultType.QUERY_NO_RESULT, "该证书不存在mongoDB数据库！");
        }
        //byte[]数组转为base64字符串
        String base64Str = org.apache.commons.codec.binary.Base64.encodeBase64String(mongoFile.getFileData());
        map.clear();
        map.put("fileName", mongoFile.getFileName());
        map.put("fileType", fileType);
        map.put("fileData", base64Str);
        return ResResult.retSuccessMsg("下载成功！", map);
    }

    @Override
    @com.ucsmy.ucas.commons.aop.annotation.Logger(printSQL = true)
    public ResResult createCertificate(CaCerInfo caCerInfo) {
        if (StringAndNumberUtil.isNullAfterTrim(caCerInfo.getCreateUser())) {
            LOGGER.error("createUser不能为空！");
            return ResResult.retFailureMsg(ResResult.ResultType.PARAMETER_ERROR, "createUser不能为空！");
        }
        List<CaCerInfo> list = caCerInfoMapper.getBeanByCN(caCerInfo.getDomainName());
        String fileName = caCerInfo.getDomainName();
        if (!list.isEmpty()) {
            fileName = fileName + "_" + (list.size()+1);
        }
        caCerInfo.setFileName(fileName);
        try {
            CertificationPojo caCert = this.certificationMapper.queryCACertification();
            String password = CaHelper.generateRandomPassword();
            //1. 初始化
            this.initCerInfo(caCerInfo, caCert);
            //2. 生成密钥对
            CertUtil.getInstance().generateKey(fileName);
            //3. 生成数字证书签名请求
            this.createCSR(caCerInfo, fileName);
            //4. 颁发证书
            this.signCertificate(caCerInfo.getValidity(), caCert, fileName);
            //5. 生成 p12
            this.buildP12(caCert, password, fileName);
            //5. 保存数字证书信息
            this.insertCertificateInfo(caCerInfo);
            //6. 保存数字证书文件
            this.insertCertificateFile(caCerInfo, password, FileSuffix.KEY, FileSuffix.CSR, FileSuffix.CRT, FileSuffix.P12);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResResult.retFailureMsg("CA证书生成失败，" + ex.getMessage(), null);
        }
        CertResponseBody body = caCerInfoMapper.getBeanById(caCerInfo.getCerUuid());
        return ResResult.retSuccessMsg("证书生成成功！", body);
    }

    /**
     * @param caCert
     * @param password
     */
    private void buildP12(CertificationPojo caCert,
            String password, String fileName) throws Exception {
        CertUtil.getInstance().buildP12(
                fileName, fileName, caCert.getDomainName(), fileName, password);
    }

    /**
     * ca签发证书
     * @param validity
     * @param caCert
     * @param fileName
     */
    private void signCertificate(String validity, CertificationPojo caCert,
            String fileName) throws Exception {
        CertUtil.getInstance().signCert(validity,
                caCert.getDomainName(), caCert.getDomainName(),
                fileName, fileName);
    }

    /**
     *
     * @param caCerInfo
     */
    private void createCSR(CaCerInfo caCerInfo, String fileName) throws Exception {
        CertModel certModel = new CertModel();
        certModel.setCertCode(caCerInfo.getCerUuid());
        certModel.setKeyName(fileName);
        certModel.setCsrName(fileName);
        certModel.setCountry(caCerInfo.getCountry());
        certModel.setProvince(caCerInfo.getProvince());
        certModel.setCity(caCerInfo.getCity());
        certModel.setCompany(caCerInfo.getOrgName());
        certModel.setUnit(caCerInfo.getOrgUnitName());
        certModel.setCommon(caCerInfo.getDomainName());
        certModel.setMachineCode(caCerInfo.getMachineCode());
        certModel.setValidity(caCerInfo.getValidity());
        CertUtil.getInstance().createCsr(certModel);
    }

    /**
     *
     * @param caCerInfo
     * @param caCert
     */
    private void initCerInfo(CaCerInfo caCerInfo, CertificationPojo caCert) {
        String certUUID = UUIDGenerator.generate();
        caCerInfo.setCerUuid(certUUID);
        caCerInfo.setCountry(caCert.getCountry());
        caCerInfo.setProvince(caCert.getProvince());
        caCerInfo.setOrgName(caCert.getOrgName());
        Date date = new Date();
        caCerInfo.setCreateDate(date);
        if (StringAndNumberUtil.isNullAfterTrim(caCerInfo.getValidity())) {
            caCerInfo.setValidity(manageConfigMapper.queryByName(ConfigConstant.CA_CERT_VALIDITY).getParamValue());
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_YEAR, Integer.parseInt(caCerInfo.getValidity()));
        caCerInfo.setDeadTime(c.getTime());
        caCerInfo.setCerStatus(CerStatus.VALID.getIndex());
    }

    @Override
    public ResResult downloadCertificate(String... fileIds) {
        ByteArrayOutputStream byteArrayOutputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        ZipOutputStream zipOutputStream = null;
        ZipEntry zipEntry;
        List<CaCerMongoFile> list = new ArrayList<>();
        InputStream inputStream = null;
        byte[] data;
        byte[] buffer = new byte[1024];
        for (String fileId: fileIds) {
            CaCerMongoFile caCerMongoFile = caCerMongoFileMapper.findOne(fileId);
            list.add(caCerMongoFile);
        }
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
            zipOutputStream = new ZipOutputStream(bufferedOutputStream);

            for(CaCerMongoFile caCerMongoFile : list) {
                data = caCerMongoFile.getFileData();
                inputStream = new ByteArrayInputStream(data);
                zipEntry = new ZipEntry(caCerMongoFile.getFileName() + "." + caCerMongoFile.getFileType());
                zipEntry.setSize(data.length);
                zipEntry.setCompressedSize(data.length);
                zipEntry.setMethod(ZipEntry.DEFLATED);
                zipOutputStream.putNextEntry(zipEntry);
                int count;
                while((count = inputStream.read(buffer, 0, 1024)) != -1) {
                    zipOutputStream.write(buffer, 0, count);
                }
                zipOutputStream.closeEntry();
            }
            zipOutputStream.close();
            bufferedOutputStream.close();
            return ResResult.retSuccessMsg("成功", byteArrayOutputStream.toByteArray());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResResult.retFailureMsg("失败", new byte[0]);
        } finally {
            try {
                if (zipOutputStream != null) {
                    zipOutputStream.close();
                }
                if (bufferedOutputStream != null) {
                    bufferedOutputStream.close();
                }
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }catch (Exception ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        }
    }


    /**
     * 初始化
     * @param caCerInfo
     */
    private void initCACerInfo(CaCerInfo caCerInfo) throws Exception {
        String certUUID = UUIDGenerator.generate();
        caCerInfo.setCerUuid(certUUID);
        caCerInfo.setCerType(CerType.ROOT.getIndex());
        caCerInfo.setCreateDate(new Date());
        caCerInfo.setCerStatus(CerStatus.VALID.getIndex());
        caCerInfo.setCerUuid(CaHelper.generateUUID());
        caCerInfo.setCreateDate(new Date());
        caCerInfo.setFileName(caCerInfo.getDomainName());
        CertUtil.getInstance().init(ConfigConstant.CA_SERIAL);
    }

    /**
     * 根证书签名
     * @param caCerInfo
     */
    private void signCA(CaCerInfo caCerInfo) throws Exception {
        CertModel certModel = new CertModel();
        certModel.setValidity(null);
        certModel.setKeyName(caCerInfo.getDomainName());
        certModel.setCaName(caCerInfo.getDomainName());
        certModel.setCountry(caCerInfo.getCountry());
        certModel.setProvince(caCerInfo.getProvince());
        certModel.setCity(caCerInfo.getCity());
        certModel.setCompany(caCerInfo.getOrgName());
        certModel.setUnit(caCerInfo.getOrgUnitName());
        certModel.setCommon(caCerInfo.getDomainName());
        CertUtil.getInstance().signCa(certModel);
    }

    /**
     * 验证 CA 根证书是否已经创建
     * @return
     */
    private boolean isCACertificateExist() {
        CertificationPojo caCert = this.certificationMapper.queryCACertification();
        if(null != caCert) {
            return true;
        }
        return false;
    }


    /**
     * 上传数字证书文件 mongodb
     * @param fileId
     * @param fileName
     * @param suffix
     * @param parentPath
     */
    private void uploadCertificateThroughMongoDB(String fileId, String fileName, String suffix, String parentPath) {
        CaCerMongoFile caCerMongoFile = new CaCerMongoFile();
        caCerMongoFile.setFileId(fileId);
        caCerMongoFile.setCreateDate(new Date());
        InputStream inputStream = null;
        File file;
        String filePath = parentPath + fileName + "." + suffix;
        try {
            file = new File(filePath);
            inputStream = new FileInputStream(file);
            byte[] fileData = IOUtils.toByteArray(inputStream);
            caCerMongoFile.setFileData(fileData);
            caCerMongoFile.setFileId(fileId);
            caCerMongoFile.setFileName(fileName);
            caCerMongoFile.setFileType(suffix);
            caCerMongoFileMapper.save(caCerMongoFile);
        } catch (FileNotFoundException e) {
            LOGGER.error("上传数字证书文件 mongodb方法异常：", e);
        } catch (IOException e) {
            LOGGER.error("上传数字证书文件 mongodb方法异常：", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOGGER.error("上传数字证书文件 mongodb方法异常：", e);
                }
            }
        }
    }

    /**
     * 保存证书文件
     * @param caCerInfo
     * @param password
     */
    private void insertCertificateFile(CaCerInfo caCerInfo, String password, FileSuffix... fileSuffixs) {
        String parentPath = manageConfigMapper.queryByName(ConfigConstant.CA_CERT_PATH).getParamValue();
        for(FileSuffix fileSuffix : fileSuffixs) {
            String fileId = UUIDGenerator.generate();
            this.uploadCertificateThroughMongoDB(fileId, caCerInfo.getFileName(), fileSuffix.getSuffix(), parentPath);
            this.insertCertificateThroughMySQL(fileId, caCerInfo.getCerUuid(), fileSuffix.getSuffix(), password);
        }
    }

    /**
     * 保存 数字证书文件 到 mysql
     * @param fileId
     * @param cerUuid
     * @param suffix
     * @param password
     */
    private void insertCertificateThroughMySQL(String fileId, String cerUuid, String suffix, String password) {
        CaCerFile caCerFile = new CaCerFile();
        caCerFile.setFileDBId(fileId);
        caCerFile.setCerUuid(cerUuid);
        caCerFile.setFileType(suffix);
        caCerFile.setFileDate(new Date());
        if(FileSuffix.P12.getSuffix().equals(suffix)) {
            caCerFile.setP12Secret(password);
        } else if (FileSuffix.JKS.getSuffix().equals(suffix)) {
            caCerFile.setP12Secret(password);
        }
        caCerFileMapper.insert(caCerFile);
    }

    /**
     * 保存证书信息 mysql
     * @param caCerInfo
     */
    private void insertCertificateInfo(CaCerInfo caCerInfo) {
        caCerInfoMapper.insert(caCerInfo);
    }
}
