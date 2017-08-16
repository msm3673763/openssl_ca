package com.ucsmy.ucas.ca.entity;

import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * 数字证书的 Mongo 中的文件
 *
 * @author zhengfucheng
 * @version 1.0.0 2017-04-26
 */
public class CaCerMongoFile implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 7590567311621270129L;

    /**
     * 主键，唯一标识
     */
    @Id
    private String fileId;

    /**
     * 保存的字节数据
     */
    private byte[] fileData;

    /**
     * 创建日期
     */
    private Date createDate;

    /**
     * 文件名
     */
    private String fileName;


    /**
     * 文件类型
     */
    private String fileType;


    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

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
}
