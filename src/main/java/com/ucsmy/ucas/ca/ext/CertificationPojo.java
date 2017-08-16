package com.ucsmy.ucas.ca.ext;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 数字证书信息
 * @author xujunwei
 * @version 1.0.0 2017-04-26
 */
public class CertificationPojo implements Serializable{

    private static final long serialVersionUID = 9047834125459644373L;

    /** 证书UUID */
    private String cerUuid;

    /** 证书类型（1应用服务器，2终端，3ca根证书类） */
    private String cerType;

    /** 域名 */
    private String domainName;

    /** 国家 */
    private String country;

    /** 省份 */
    private String province;

    /** 城市 */
    private String city;

    /** 组织名 */
    private String orgName;

    /** 组织单位名 */
    private String orgUnitName;

    /** 邮箱 */
    private String email;

    /** 机器码 */
    private String machineCode;

    /** 创建时间 */
    private Date createDate;

    /** 创建人 */
    private String createUser;

    /** 失效时间 */
    private Date deadTime;

    /** 证书状态（1有效，2失效） */
    private String cerStatus;

    private String fileType;

    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    /** 文件列表 */
    private List<FilePojo> fileList;

    /**
     * 获取机器码
     *
     * @return 机器码
     */
    public String getMachineCode() {
        return machineCode;
    }

    /**
     * 设置机器码
     *
     * @param machineCode
     *         机器码
     */
    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
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
     * 获取证书类型（1应用服务器，2终端，3ca根证书类）
     *
     * @return 证书类型
     */
    public String getCerType() {
        return this.cerType;
    }

    /**
     * 设置证书类型（1应用服务器，2终端，3ca根证书类）
     *
     * @param cerType
     *          证书类型
     */
    public void setCerType(String cerType) {
        this.cerType = cerType;
    }

    /**
     * 获取域名
     *
     * @return 域名
     */
    public String getDomainName() {
        return this.domainName;
    }

    /**
     * 设置域名
     *
     * @param domainName
     *          域名
     */
    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    /**
     * 获取国家
     *
     * @return 国家
     */
    public String getCountry() {
        return this.country;
    }

    /**
     * 设置国家
     *
     * @param country
     *          国家
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * 获取省份
     *
     * @return 省份
     */
    public String getProvince() {
        return this.province;
    }

    /**
     * 设置省份
     *
     * @param province
     *          省份
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * 获取组织名
     *
     * @return 组织名
     */
    public String getOrgName() {
        return this.orgName;
    }

    /**
     * 设置组织名
     *
     * @param orgName
     *          组织名
     */
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    /**
     * 获取组织单位名
     *
     * @return 组织单位名
     */
    public String getOrgUnitName() {
        return this.orgUnitName;
    }

    /**
     * 设置组织单位名
     *
     * @param orgUnitName
     *          组织单位名
     */
    public void setOrgUnitName(String orgUnitName) {
        this.orgUnitName = orgUnitName;
    }

    /**
     * 获取邮箱
     *
     * @return 邮箱
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * 设置邮箱
     *
     * @param email
     *          邮箱
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取创建时间
     *
     * @return 创建时间
     */
    public Date getCreateDate() {
        return this.createDate;
    }

    /**
     * 设置创建时间
     *
     * @param createDate
     *          创建时间
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 获取创建人
     *
     * @return 创建人
     */
    public String getCreateUser() {
        return this.createUser;
    }

    /**
     * 设置创建人
     *
     * @param createUser
     *          创建人
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    /**
     * 获取失效时间
     *
     * @return 失效时间
     */
    public Date getDeadTime() {
        return this.deadTime;
    }

    /**
     * 设置失效时间
     *
     * @param deadTime
     *          失效时间
     */
    public void setDeadTime(Date deadTime) {
        this.deadTime = deadTime;
    }

    /**
     * 获取证书状态（1有效，2失效）
     *
     * @return 证书状态（1有效，2失效）
     */
    public String getCerStatus() {
        return this.cerStatus;
    }

    /**
     * 设置证书状态（1有效，2失效）
     *
     * @param cerStatus
     *          证书状态（1有效，2失效）
     */
    public void setCerStatus(String cerStatus) {
        this.cerStatus = cerStatus;
    }

    /**
     * 获取文件列表
     * @return 文件列表
     */
    public List<FilePojo> getFileList() {
        return fileList;
    }

    /**
     * 设置文件列表
     * @param fileList 文件列表
     */
    public void setFileList(List<FilePojo> fileList) {
        this.fileList = fileList;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


}