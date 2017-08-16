package com.ucsmy.ucas.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by ucs_leijinming on 2017/5/2.
 */
@ConfigurationProperties(prefix = "loginType")
@PropertySource("classpath:config/common.yml")
@Component
public class LoginTypeConfig {
    private Boolean localType;

    private String url;

    private String getTokenUrl;
    private String clientId;
    private String clientSecret;
    private String grantType;
    private String scope;

    private String validTicketUrl;
    private String getTicketUrl;
    private String scopeManage;
    private String oauth2Url;
    private String mainIndex;

    private String testClientId;
    private String testClientSecret;

    public String getTestClientId() {
        return testClientId;
    }

    public void setTestClientId(String testClientId) {
        this.testClientId = testClientId;
    }

    public String getTestClientSecret() {
        return testClientSecret;
    }

    public void setTestClientSecret(String testClientSecret) {
        this.testClientSecret = testClientSecret;
    }

    public String getScopeManage() {
        return scopeManage;
    }

    public void setScopeManage(String scopeManage) {
        this.scopeManage = scopeManage;
    }

    public String getValidTicketUrl() {
        return validTicketUrl;
    }

    public void setValidTicketUrl(String validTicketUrl) {
        this.validTicketUrl = validTicketUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getGetTicketUrl() {
        return getTicketUrl;
    }

    public void setGetTicketUrl(String getTicketUrl) {
        this.getTicketUrl = getTicketUrl;
    }

    public String getLogout() {
        return logout;
    }

    public void setLogout(String logout) {
        this.logout = logout;
    }

    private String logout;

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Boolean getLocalType() {
        return localType;
    }

    public void setLocalType(Boolean localType) {
        this.localType = localType;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getMainIndex() {
        return mainIndex;
    }

    public void setMainIndex(String mainIndex) {
        this.mainIndex = mainIndex;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getGetTokenUrl() {
        return getTokenUrl;
    }

    public void setGetTokenUrl(String getTokenUrl) {
        this.getTokenUrl = getTokenUrl;
    }

    public String getOauth2Url() {
        return oauth2Url;
    }

    public void setOauth2Url(String oauth2Url) {
        this.oauth2Url = oauth2Url;
    }
}
