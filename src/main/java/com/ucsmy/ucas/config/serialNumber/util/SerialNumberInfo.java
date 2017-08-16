package com.ucsmy.ucas.config.serialNumber.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 序列号生成器配置信息
 * Created by chenqilin on 2017/5/18.
 */
@ConfigurationProperties(prefix = "serialNumber")
@Component
public class SerialNumberInfo {
    /**
     * 是否开启序列号生成器
     */
    private String open;

    /**
     * 序列号前缀
     */
    private String prefix;

    /**
     * 序列号备用前缀，当出现重复主键时使用备用序列号生成器生成
     */
    private String backupPrefix;

    /**
     * 序列号编号格式
     */
    private String numberPattern;

    /**
     * 生成模式：db；redis；default
     */
    private String mode;

    /**
     * 序列号日期格式
     */
    private String datePattern;

    /**
     * db模式线程最大等待时间，毫秒
     */
    private String waitTime;

    /**
     * 序列号起始基数
     */
    private String startNum;

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getDatePattern() {
        return datePattern;
    }

    public void setDatePattern(String datePattern) {
        this.datePattern = datePattern;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getNumberPattern() {
        return numberPattern;
    }

    public void setNumberPattern(String numberPattern) {
        this.numberPattern = numberPattern;
    }

    public String getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(String waitTime) {
        this.waitTime = waitTime;
    }

    public String getBackupPrefix() {
        return backupPrefix;
    }

    public void setBackupPrefix(String backupPrefix) {
        this.backupPrefix = backupPrefix;
    }

    public String getStartNum() {
        return startNum;
    }

    public void setStartNum(String startNum) {
        this.startNum = startNum;
    }
}
