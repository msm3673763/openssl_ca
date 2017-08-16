package com.ucsmy.ucas.ca.constant;

/**
 *
 * @Description: 参数常量
 * @author ucs_masiming
 * @date 2017/4/27 9:34
 * @version V1.0
 */
public class ConfigConstant {

    //证书保存路径
    public static final String CA_CERT_PATH = "CA_CERT_PATH";

    //openssl.cnf配置文件路径
    public static final String CA_CONFIG_PATH = "CA_CONFIG_PATH";

    //crl文件id
    public static final String CRL_FILE_ID = "CRL_FILE_ID";

    //crl下载路径
    public static final String CRL_DOWNLOAD_PATH = "CRL_DOWNLOAD_PATH";

    public static final String CA_SERIAL = "1000";

    //证书有效期
    public static final String CA_CERT_VALIDITY = "CA_CERT_VALIDITY";
    public static final String CER_TYPE__1 = "1";//证书类型：1、应用证书，2、设备证书，3、ca证书
    public static final String CER_STATUS__1 = "1";//证书状态：1、有效，2、无效
    public static final String TRUST_STORE_PATH = "TRUST_STORE_PATH";
    public static final String TRUST_STORE_PASS = "TRUST_STORE_PASS";
    public static final String KEY_STORE_PATH = "KEY_STORE_PATH";
    public static final String KEY_STORE_PASS = "KEY_STORE_PASS";
    public static final String UCAS_CA_SERVER_PATH = "UCAS_CA_SERVER_PATH";
}
