package com.ucsmy.ucas.ca.ext;

import java.io.Serializable;
import java.util.Date;

/**
 * 文件信息
 * @author xujunwei
 * @version 1.0.0 2017-04-26
 */
public class FilePojo implements Serializable{

    private static final long serialVersionUID = -5561361262205957371L;

    /** 文件存储ID */
    private String fileDBId;

    /** 文件类型 */
    private String fileType;

    /**
     * 通行码
     */
    private String p12Secret;

    public String getFileDBId() {
        return fileDBId;
    }

    public void setFileDBId(String fileDBId) {
        this.fileDBId = fileDBId;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getP12Secret() {
        return p12Secret;
    }

    public void setP12Secret(String p12Secret) {
        this.p12Secret = p12Secret;
    }
}