package com.ucsmy.ucas.ca.utils;

import com.ucsmy.commons.utils.StringAndNumberUtil;
import com.ucsmy.commons.utils.StringUtils;
import com.ucsmy.ucas.ca.Exception.CertException;
import com.ucsmy.ucas.ca.constant.ConfigConstant;
import com.ucsmy.ucas.ca.entity.CertModel;
import com.ucsmy.ucas.manage.dao.ManageConfigMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @Description: 证书工具类
 * @author ucs_masiming
 * @date 2017/4/27 9:13
 * @version V1.0
 */
@Component
public class CertUtil implements ApplicationContextAware {
    @Autowired
    private ManageConfigMapper manageConfigMapper;

    private static Logger logger = LoggerFactory.getLogger(CertUtil.class);
    private static CertUtil instance = null;
    private static final String CA_CONFIG_INDEX = "/etc/pki/CA/index.txt";//ca初始化文件index.txt路径
    private static final String CA_CONFIG_SERIAL = "/etc/pki/CA/serial";//ca证书序列号文件路径
    private static final String CA_CONFIG_CRLNUMBER = "/etc/pki/CA/crlnumber";//crl序列号文件路径
    private static final String CA_VALIDITY = "36500";//证书有效期，单位天
    private static final String CRL_DAYS = "7";//crl更新有效期，单位天
    private static final String EXPECT = "expect ";

    public static synchronized CertUtil getInstance() {
        return instance;
    }

    /**
     *
     * @Description: ca配置初始化
     * @param serial 证书序列号
     * @return void
     * @author ucs_masiming
     * @throws Exception
     * @date 2017/4/27 9:16
     * @version V1.0
     */
    public void init(String serial) throws Exception {
        if (!StringUtils.hasText(serial)) {
            throw new CertException("证书序列号不能为空！");
        }
        String certPath = manageConfigMapper.queryByName(ConfigConstant.CA_CERT_PATH).getParamValue();

        //创建keys目录
        FileUtil.clearFileOrFolder(certPath);
        File file = new File(certPath);
        file.mkdirs();

        //创建index.txt文件
        file = new File(CA_CONFIG_INDEX);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();

        //创建serial文件
        file = new File(CA_CONFIG_SERIAL);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))) {
            bw.write(serial);
        }

        //创建crlnumber文件
        file = new File(CA_CONFIG_CRLNUMBER);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))) {
            bw.write(serial);
        }
    }

    /**
     *
     * @Description: 生成密钥
     * @param name 密钥名
     * @return void
     * @author ucs_masiming
     * @throws Exception
     * @date 2017/4/27 9:17
     * @version V1.0
     */
    public void generateKey(String name) throws Exception {
        if (!StringUtils.hasText(name)) {
            throw new CertException("密钥名不能为空！");
        }
        String certPath = manageConfigMapper.queryByName(ConfigConstant.CA_CERT_PATH).getParamValue();
        String cmd = "openssl genrsa -out " + certPath + name + ".key 2048";
        Process process = Runtime.getRuntime().exec(cmd);

        String errorMsg = "密钥生成失败";
        parseMessage(process, errorMsg);
    }

    /**
     *
     * @Description: 生成带密码的密钥
     * @param name 密钥名
     * @param password 密钥密码
     * @return 返回类型
     * @author ucs_masiming
     * @throws Exception
     * @date 2017/4/27 18:09
     * @version V1.0
     */
    public void generateKey(String name, String password) throws Exception {
        if (!StringUtils.hasText(name)) {
            throw new CertException("密钥名不能为空！");
        }
        if (password.trim().length() < 4 || password.trim().length() > 8191) {
            throw new CertException("密码必须在4到8191字符之间");
        }

        String certPath = manageConfigMapper.queryByName(ConfigConstant.CA_CERT_PATH).getParamValue();
        String path = Thread.currentThread().getContextClassLoader().getResource("/sh/key.sh").toURI().getPath();
        logger.info(path);
        String cmd = EXPECT + path + " " + certPath + name + " " + password;
        Process process = Runtime.getRuntime().exec(cmd);

        String errorMsg = "密钥生成失败";
        parseMessage(process, errorMsg);
    }

    /**
     *
     * @Description: ca证书自签名
     * @param model 证书model
     * @return void
     * @author ucs_masiming
     * @throws Exception
     * @date 2017/4/27 10:40
     * @version V1.0
     */
    public void signCa(CertModel model) throws Exception {
        String validity = validity(model);

        if (!StringUtils.hasText(model.getCaName())) {
            throw new CertException("ca证书名不能为空！");
        }

        //执行生成ca自签名证书的shell命令
        String certPath = manageConfigMapper.queryByName(ConfigConstant.CA_CERT_PATH).getParamValue();
        String configPath = manageConfigMapper.queryByName(ConfigConstant.CA_CONFIG_PATH).getParamValue();
        String cmd = "openssl req -new -x509 -days " + validity + " -key " + certPath + model.getKeyName()
                + ".key -out " + certPath + model.getCaName() + ".crt -subj /C=" + model.getCountry()
                + "/ST=" + model.getProvince() + "/L=" + model.getCity() + "/O=" + model.getCompany()
                + "/OU=" + model.getUnit() + "/CN=" + model.getCommon() + " -config " + configPath;
        Process process = Runtime.getRuntime().exec(cmd);

        String errorMsg = "生成ca自签名证书失败";
        parseMessage(process, errorMsg);
    }

    /**
     *
     * @Description: ca证书自签名(需要密钥密码)
     * @param model 证书model
     * @return void
     * @author ucs_masiming
     * @throws Exception
     * @date 2017/4/28 10:10
     * @version V1.0
     */
    public void signCaByPassword(CertModel model) throws Exception {
        String validity = validity(model);
        if (StringUtils.isEmpty(model.getKeyPass())) {
            throw new CertException("密钥密码不能为空！");
        }

        if (!StringUtils.hasText(model.getCaName())) {
            throw new CertException("ca证书名不能为空！");
        }

        String certPath = manageConfigMapper.queryByName(ConfigConstant.CA_CERT_PATH).getParamValue();
        String configPath = manageConfigMapper.queryByName(ConfigConstant.CA_CONFIG_PATH).getParamValue();
        String path = Thread.currentThread().getContextClassLoader().getResource("/sh/sign_ca.sh").toURI().getPath();
        logger.info(path);
        String cmd = EXPECT + path + " " + validity + " " + certPath + model.getKeyName() + " " + certPath
                + model.getCaName() + " " + model.getCountry() + " " + model.getProvince() + " " + model.getCity()
                + " " + model.getCompany() + " " + model.getUnit() + " " + model.getCommon()
                + " " + configPath + " " + model.getKeyPass();
        logger.info(cmd);
        Process process = Runtime.getRuntime().exec(cmd);

        String errorMsg = "生成ca自签名证书失败";
        parseMessage(process, errorMsg);
    }

    /**
     *
     * @Description: 生成证书请求
     * @param model 证书model
     * @return void
     * @author ucs_masiming
     * @throws Exception
     * @date 2017/4/28 11:55
     * @version V1.0
     */
    public void createCsr(CertModel model) throws Exception {
        validity(model);

        if (!StringUtils.hasText(model.getCsrName())) {
            throw new CertException("csr文件名不能为空！");
        }

        //如果机器码不为空，则CN=机器码，否则为域名
        String commonName = model.getCommon();
        if (!StringAndNumberUtil.isNullAfterTrim(model.getMachineCode())) {
            commonName = model.getMachineCode();
        }

        String certPath = manageConfigMapper.queryByName(ConfigConstant.CA_CERT_PATH).getParamValue();
        String configPath = manageConfigMapper.queryByName(ConfigConstant.CA_CONFIG_PATH).getParamValue();
        String cmd = "openssl req -config " + configPath + " -new -sha256 -key " + certPath + model.getKeyName()
                + ".key -out " + certPath + model.getCsrName() + ".csr -subj /C=" + model.getCountry()
                + "/ST=" + model.getProvince() + "/L=" + model.getCity() + "/O=" + model.getCompany()
                + "/OU=" + model.getUnit() + "/CN=machineCode:" + commonName + ",certCode:" + model.getCertCode();
        logger.info(cmd);
        Process process = Runtime.getRuntime().exec(cmd);//执行shell脚本

        String errorMsg = "生成证书请求文件失败";//错误提示信息
        parseMessage(process, errorMsg);
    }

    /**
     *
     * @Description: 生成证书请求（需密钥密码）
     * @param  model 证书model
     * @return void
     * @author ucs_masiming
     * @throws Exception
     * @date 2017/4/28 13:36
     * @version V1.0
     */
    public void createCsrByPass(CertModel model) throws Exception {
        validity(model);
        if (StringUtils.isEmpty(model.getKeyPass())) {
            throw new CertException("密钥密码不能为空！");
        }
        if (!StringUtils.hasText(model.getCsrName())) {
            throw new CertException("csr文件名不能为空！");
        }

        String certPath = manageConfigMapper.queryByName(ConfigConstant.CA_CERT_PATH).getParamValue();
        String configPath = manageConfigMapper.queryByName(ConfigConstant.CA_CONFIG_PATH).getParamValue();
        String path = Thread.currentThread().getContextClassLoader().getResource("/sh/csr.sh").toURI().getPath();
        logger.info(path);

        String cmd = EXPECT + path + " " + configPath + " " + certPath + model.getKeyName() + " "
                + certPath + model.getCsrName() + " " + model.getCountry() + " " + model.getProvince()
                + " " + model.getCity() + " " + model.getCompany() + " " + model.getUnit() + " "
                + model.getCommon() + " " + model.getKeyPass();
        logger.info(cmd);
        Process process = Runtime.getRuntime().exec(cmd);//执行Shell脚本
        String errorMsg = "生成证书请求文件失败";//错误信息提示
        parseMessage(process, errorMsg);
    }

    /**
     *
     * @Description: 证书签名
     * @param validity 有效期
     * @param keyName ca密钥文件名
     * @param caName ca证书名
     * @param csrName 证书请求文件名
     * @param crtName 生成证书文件名
     * @return void
     * @author ucs_masiming
     * @throws Exception
     * @date 2017/4/28 15:50
     * @version V1.0
     */
    public void signCert(String validity, String keyName, String caName,
                         String csrName, String crtName) throws Exception {
        String validityDay;
        if (!StringUtils.hasText(validity)) {
            validityDay = CA_VALIDITY;
        } else {
            validityDay = validity;
        }
        ValidationAssert.notEmpty(keyName, "ca密钥文件名不能为空！");
        ValidationAssert.notEmpty(caName, "ca证书名不能为空！");
        ValidationAssert.notEmpty(csrName, "证书请求文件名不能为空！");
        ValidationAssert.notEmpty(crtName, "生成证书文件名不能为空！");

        String certPath = manageConfigMapper.queryByName(ConfigConstant.CA_CERT_PATH).getParamValue();
        String configPath = manageConfigMapper.queryByName(ConfigConstant.CA_CONFIG_PATH).getParamValue();
        String configPathWithSAN = "<(cat " + configPath + " <(printf \"[SAN]\\nsubjectAltName=DNS:" + crtName + "\"))";
        List<String> cmds = new ArrayList<>();
        cmds.add("/bin/bash");
        cmds.add("-c");
        String cmd = "echo -e 'y\\ny\\n' | openssl ca -days " + validityDay + " -md sha256 -extensions SAN -config " + configPathWithSAN
                + " -keyfile " + certPath + keyName + ".key -cert " + certPath + caName + ".crt -in "
                + certPath + csrName + ".csr -out " + certPath + crtName + ".crt";
        cmds.add(cmd);
        logger.info(cmd);
        ProcessBuilder pb = new ProcessBuilder(cmds);
        Process process = pb.start();//执行shell脚本

        String errorMsg = "证书签名失败";//错误提示信息
        parseMessage(process, errorMsg);
    }

    /**
     *
     * @Description: 证书签名(需ca密钥密码)
     * @param validity 有效期
     * @param keyName ca密钥文件名
     * @param caName ca证书名
     * @param csrName 证书请求文件名
     * @param crtName 生成证书文件名
     * @param keyPass ca密钥密码
     * @return void
     * @author ucs_masiming
     * @throws Exception
     * @date 2017/4/28 15:50
     * @version V1.0
     */
    public void signCertByPass(String validity, String keyName, String caName, String csrName,
                               String crtName, String keyPass) throws Exception {
        String validityDay;
        if (!StringUtils.hasText(validity)) {
            validityDay = CA_VALIDITY;
        } else {
            validityDay = validity;
        }
        ValidationAssert.notEmpty(keyName, "ca密钥文件名不能为空！");
        ValidationAssert.notEmpty(caName, "ca证书名不能为空！");
        ValidationAssert.notEmpty(csrName, "证书请求文件名不能为空！");
        ValidationAssert.notEmpty(crtName, "生成证书文件名不能为空！");
        ValidationAssert.notEmpty(keyPass, "ca密钥密码不能为空！");

        String path = Thread.currentThread().getContextClassLoader().getResource("/sh/signCert.sh").toURI().getPath();
        logger.info(path);

        String certPath = manageConfigMapper.queryByName(ConfigConstant.CA_CERT_PATH).getParamValue();
        String configPath = manageConfigMapper.queryByName(ConfigConstant.CA_CONFIG_PATH).getParamValue();
        String cmd = EXPECT + path + " " + validityDay + " " + configPath + " " + certPath + keyName
                + " " + certPath + caName + " " + certPath + csrName + " " + certPath + crtName
                + " " + keyPass;
        logger.info(cmd);
        Process process = Runtime.getRuntime().exec(cmd);//执行Shell脚本
        String errorMsg = "证书签名失败";//错误信息提示
        parseMessage(process, errorMsg);
    }

    /**
     *
     * @Description: 生成信任库（将ca公钥导入到信任库）
     * @param storeName 信任库名
     * @param password 信任库密码
     * @param caName ca自签名证书名
     * @return void
     * @author ucs_masiming
     * @throws Exception
     * @date 2017/5/2 11:21
     * @version V1.0
     */
    public void createTrustStore(String storeName, String password, String caName) throws Exception {
        String certPath = manageConfigMapper.queryByName(ConfigConstant.CA_CERT_PATH).getParamValue();
        String storePath = Thread.currentThread().getContextClassLoader().getResource("/sh/trustStore.sh").toURI().getPath();
        String cmd = EXPECT + storePath + " " + certPath + storeName + " " + password + " " + certPath + caName;
        logger.info(cmd);

        Process process = Runtime.getRuntime().exec(cmd);//执行Shell脚本
        String errorMsg = "生成信息库失败";//错误信息提示
        parseMessage(process, errorMsg);
    }

    /**
     *
     * @Description: crt证书转为PKCS12格式
     * @param crtName crt证书名
     * @param keyName crt证书密钥名
     * @param caName ca证书名
     * @param p12Name p12密钥库名
     * @param password p12密钥库密码
     * @return void
     * @author ucs_masiming
     * @throws Exception
     * @date 2017/5/2 14:27
     * @version V1.0
     */
    public void buildP12(String crtName, String keyName, String caName,
                            String p12Name, String password) throws Exception {
        String certPath = manageConfigMapper.queryByName(ConfigConstant.CA_CERT_PATH).getParamValue();
        String path = Thread.currentThread().getContextClassLoader().getResource("/sh/p12.sh").toURI().getPath();
        String cmd = EXPECT + path + " " + certPath + crtName + " " + certPath + keyName + " " + certPath + caName
                + " " + certPath + p12Name + " " + password;
        logger.info(cmd);

        Process process = Runtime.getRuntime().exec(cmd);
        String errorMsg = "生成p12证书失败";//错误信息提示
        parseMessage(process, errorMsg);
    }

    /**
     *
     * @Description: crt证书转为PKCS12格式(需密码)
     * @param crtName crt证书名
     * @param keyName crt证书密钥名
     * @param caName ca证书名
     * @param p12Name p12密钥库名
     * @param password p12密钥库密码
     * @param keyPass 证书密钥密码
     * @return void
     * @author ucs_masiming
     * @throws Exception
     * @date 2017/5/2 14:27
     * @version V1.0
     */
    public void buildP12ByPass(String crtName, String keyName, String caName,
                               String p12Name, String password, String keyPass) throws Exception {
        String certPath = manageConfigMapper.queryByName(ConfigConstant.CA_CERT_PATH).getParamValue();
        String path = Thread.currentThread().getContextClassLoader().getResource("/sh/p12_password.sh").toURI().getPath();
        String cmd = EXPECT + path + " " + certPath + crtName + " " + certPath + keyName + " " + certPath + caName
                + " " + certPath + p12Name + " " + password + " " + keyPass;
        logger.info(cmd);

        Process process = Runtime.getRuntime().exec(cmd);
        String errorMsg = "生成p12证书失败";//错误信息提示
        parseMessage(process, errorMsg);
    }

    /**
     * 吊销证书
     * @param crtName 要吊销的crt名
     * @param keyName ca证书key
     * @param caName ca证书crt
     * @param crlName crl列表
     * @throws Exception
     */
    public void revokeCrt(String crtName, String keyName, String caName, String crlName) throws Exception {
        String certPath = manageConfigMapper.queryByName(ConfigConstant.CA_CERT_PATH).getParamValue();
        String path = Thread.currentThread().getContextClassLoader().getResource("/sh/crl.sh").toURI().getPath();
        String configPath = manageConfigMapper.queryByName(ConfigConstant.CA_CONFIG_PATH).getParamValue();
        String cmd = "sh " + path + " " + certPath + crtName + " " + certPath + keyName
                + " " +  certPath + caName + " " + configPath + " " + CRL_DAYS + " " + certPath + crlName;
        logger.info(cmd);
        Process process = Runtime.getRuntime().exec(cmd);
        String errorMsg = "吊销证书失败";//错误信息提示
        parseMessage(process, errorMsg);
    }

    /**
     *
     * @Description: shell脚本执行结果处理
     * @param process
     * @param errorMsg 提示信息
     * @return void
     * @author ucs_masiming
     * @throws IOException, InterruptedException
     * @date 2017/4/28 13:43
     * @version V1.0
     */
    private void parseMessage(Process process, String errorMsg) throws Exception {
        //读取标准错误流
        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream(), "gb2312"))) {
            while ((line = br.readLine()) != null) {
                sb.append(line);
                logger.info(line);
            }

            //waitFor()判断Process进程是否终止，通过返回值判断是否正常终止，0代表正常终止
            int c = process.waitFor();
            if (c != 0) {
                throw new CertException(errorMsg + "，错误信息如下：\n" + sb.toString());
            } else {//输出结果信息
                if (StringUtils.hasText(sb.toString())) {
                    logger.info(sb.toString());
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new CertException(errorMsg + "，错误信息如下：\n" + sb.toString());
        }
    }

    /**
     *
     * @Description: 参数校验
     * @return String
     * @author ucs_masiming
     * @throws CertException
     * @date 2017/4/28 9:49
     * @version V1.0
     */
    private String validity(CertModel model) {
        String validity = model.getValidity();
        if (!StringUtils.hasText(validity)) {//如果有效期为空，则默认为100年
            validity = CA_VALIDITY;
        }

        if (!StringUtils.hasText(model.getKeyName())
                || !StringUtils.hasText(model.getCountry()) || !StringUtils.hasText(model.getProvince())
                || !StringUtils.hasText(model.getCity()) || !StringUtils.hasText(model.getUnit())
                || !StringUtils.hasText(model.getCompany()) || !StringUtils.hasText(model.getCommon())) {
            throw new CertException("请求参数存在空值！");
        }

        if (model.getCountry().length() != 2) {
            throw new CertException("国家名长度必须为2");
        }
        return validity;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        instance = (CertUtil) applicationContext.getBean("certUtil");
    }

}
