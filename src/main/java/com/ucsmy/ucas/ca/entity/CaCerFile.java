package com.ucsmy.ucas.ca.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 数字证书文件表(CA_CER_FILE)
 *
 * @author xujunwei
 * @version 1.0.0 2017-04-26
 */
public class CaCerFile implements Serializable {
    /** 版本号 */
    private static final long serialVersionUID = -1535701856361823230L;
    
    /** 文件存储ID */
    private String fileDBId;
    
    /** 证书UUID */
    private String cerUuid;
    
    /** 文件类型 */
    private String fileType;
    
    /** P12密码 */
    private String p12Secret;
    
    /** 文件创建日期 */
    private Date fileDate;
    
    /**
     * 获取文件存储ID
     * 
     * @return 文件存储ID
     */
    public String getFileDBId() {
        return this.fileDBId;
    }
     
    /**
     * 设置文件存储ID
     * 
     * @param fileDBId
     *          文件存储ID
     */
    public void setFileDBId(String fileDBId) {
        this.fileDBId = fileDBId;
    }
    
    /**
     * 获取证书UUID
     * 
     * @return 证书UUID
     */
    public String getCerUuid() {
        return this.cerUuid;
    }
     
    /**
     * 设置证书UUID
     * 
     * @param cerUuid
     *          证书UUID
     */
    public void setCerUuid(String cerUuid) {
        this.cerUuid = cerUuid;
    }
    
    /**
     * 获取文件类型
     * 
     * @return 文件类型
     */
    public String getFileType() {
        return this.fileType;
    }
     
    /**
     * 设置文件类型
     * 
     * @param fileType
     *          文件类型
     */
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    
    /**
     * 获取P12密码
     * 
     * @return P12密码
     */
    public String getP12Secret() {
        return this.p12Secret;
    }
     
    /**
     * 设置P12密码
     * 
     * @param p12Secret
     *          P12密码
     */
    public void setP12Secret(String p12Secret) {
        this.p12Secret = p12Secret;
    }
    
    /**
     * 获取文件创建日期
     * 
     * @return 文件创建日期
     */
    public Date getFileDate() {
        return this.fileDate;
    }
     
    /**
     * 设置文件创建日期
     * 
     * @param fileDate
     *          文件创建日期
     */
    public void setFileDate(Date fileDate) {
        this.fileDate = fileDate;
    }
}