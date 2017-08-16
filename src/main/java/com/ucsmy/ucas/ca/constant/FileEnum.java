package com.ucsmy.ucas.ca.constant;

/**
 * 附件表的附件类型
 *
 * @author ucs_guichang
 * @since 2017/07/20
 */
public enum FileEnum {

    COMMA(",", "文件路径分隔符"),
    WELL("#", "替换符"),
    EMP("\"\"", ""),
    IMG_TYPE("gif,jpg,jpeg,png,bmp", "图片文件类型"),
    IMG_REF("imgRef", "关联显示的图片"),

    FILEID_LIST("_fileIds", "下载文件文件id列表"),
    FILE_LIST("_files", "上传文件返回mongodb文件列表"),

    FILE_UPLOAD_TYPE("multipart/form-data", "文件上传声明类型"),
    FILE_IMG_SHOW("fileUpDownloadCtr/imageShow", "图片预览路径"),

    EXCLUDE_FILTER_URL("/fileUpDownloadCtr/", "此url为文件上传接口url，AosFilter中做特殊处理");

    private String value;
    private String descr;

    FileEnum(String value, String descr) {
        this.value = value;
        this.descr = descr;
    }

    public String getValue() {
        return value;
    }

    public String getDescr() {
        return descr;
    }
}
