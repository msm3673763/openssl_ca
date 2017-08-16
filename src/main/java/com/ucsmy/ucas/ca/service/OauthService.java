package com.ucsmy.ucas.ca.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.ucsmy.commons.utils.StringAndNumberUtil;
import com.ucsmy.ucas.ca.constant.RedisEnum;
import com.ucsmy.ucas.ca.utils.HttpAgent;
import com.ucsmy.ucas.commons.aop.exception.result.ResResult;
import com.ucsmy.ucas.commons.aop.exception.utils.JsonUtils;
import com.ucsmy.ucas.config.LoginTypeConfig;
import com.ucsmy.ucas.manage.service.SysCacheService;
import org.apache.http.entity.ByteArrayEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ucs_guichang on 2017/4/28.
 */
@Service
public class OauthService {

    @Autowired
    private LoginTypeConfig loginTypeConfig;
    @Autowired
    private SysCacheService sysCacheService;

    private static Logger log = LoggerFactory.getLogger(OauthService.class);

    public static final String MSG = "调用统一认证平台失败";
    public static final String NULL_MSG = "调用统一认证平台返回为空";

    public JsonNode get(String myUrl, HashMap<String, String> params) throws IOException {
        JsonNode result = null;
        try {
            String res = HttpAgent.getInstance().get(loginTypeConfig.getUrl() + myUrl, params);
            if (!StringAndNumberUtil.isNullAfterTrim(res))
                result = JsonUtils.jsonToJsonNode(res);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("json格式化出错--" + e.getMessage());
        }
        return result;
    }

    public JsonNode post(String myUrl, HashMap<String, Object> params) throws Exception {
        JsonNode result = null;
        Map<String, String> headers = new HashMap();
        headers.put("Content-Type", "application/json");

        String res = HttpAgent.getInstance().post(loginTypeConfig.getUrl() + myUrl, headers,
                new ByteArrayEntity(JsonUtils.formatObjectToJson(params).getBytes(Charset.forName("UTF-8"))));
        if (!StringAndNumberUtil.isNullAfterTrim(res)) {
            result = JsonUtils.jsonToJsonNode(res);
        }
        return result;
    }

    /**
     * 根据应用client获取token
     *
     * @param app   应用标识：aos_api、aos_web
     * @param scope <p>获取或校验ticket：client_ticket</p>
     *              <p>用户管理(中划线区分)：client_usermanage_status-client_usermanage</p>
     * @return
     */
    public ResResult getTokenFromClient(String app, String clientId,
                String clientSecret, String scope) {
        // 如果redis中存在则直接返回，不存在则去统一认证取
        String tokenKey = RedisEnum.UCAS_CA.getValue() + RedisEnum.SPESTR.getValue()
                + RedisEnum.TOKEN.getValue() + RedisEnum.SPESTR.getValue()
                + app + RedisEnum.SPESTR.getValue() + scope;// 组合key
        String token = sysCacheService.getString(tokenKey);
        if (token != null) {
            return ResResult.retSuccessMsg("成功", token);
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("client_id", clientId);
        params.put("client_secret", clientSecret);
        params.put("grant_type", loginTypeConfig.getGrantType());
        params.put("scope", scope);
        try {
            JsonNode get = get(loginTypeConfig.getGetTokenUrl(), params);
            if (get == null) {
                return ResResult.retFailureMsg(NULL_MSG, null);
            } else {
                // 失败示例：{"errcode": 40012,"errmsg": "unauthorized_client: 错误的秘钥"}
                // 成功示例：{
                //    "access_token": "6cd40f73124d47cdb40d89af1c2c03e8",
                //    "refresh_token": "591c24d659c546a5ab1a92407be51800",
                //    "scope":"client_userinfo",
                //    "expires_in": 7200
                //}
                if (get.get("errcode") != null) {
                    String getMsg = get.toString().replaceAll("\"", "");
                    log.error("获取token(" + app + "," + scope + ")" + ":【" + MSG + "--" + getMsg + "】");
                    return ResResult.retFailureMsg(get.get("errmsg") == null ? getMsg : "$" + get.get("errmsg").asText(), null);
                } else {
                    String access_token = get.get("access_token").asText();
                    int expires_in = get.get("expires_in").asInt();
                    // 放进redis缓存
                    sysCacheService.set(tokenKey, access_token, expires_in);
                    return ResResult.retSuccessMsg("成功", access_token);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResResult.retFailureMsg(e.getMessage(), null);
        }
    }

    public ResResult getTicket() {
        // 此处应首先从redis中获取ticket，若没有再进行下面的操作
        String ticketKey = RedisEnum.UCAS_CA.getValue() + RedisEnum.SPESTR.getValue()
                + RedisEnum.TICKET.getValue() + RedisEnum.SPESTR.getValue() + "test";
        String ticket = sysCacheService.getString(ticketKey);
        if (ticket != null) {
            return ResResult.retSuccessMsg("成功", ticket);
        }

        ResResult tokenResult = getTokenFromClient(RedisEnum.UCAS_CA_CLIENT.getValue(),
                loginTypeConfig.getTestClientId(), loginTypeConfig.getTestClientSecret(),
                loginTypeConfig.getScope());
        if (tokenResult.getRes() != ResResult.ResultType.SUCCESS.getIndex()) {
            return tokenResult;
        }

        String access_token = (String) tokenResult.getData();
        HashMap<String, String> params = new HashMap<>();
        params.put("access_token", access_token);
        params.put("scope", loginTypeConfig.getScopeManage());

        try {
            JsonNode get = get(loginTypeConfig.getGetTicketUrl(), params);
            if (get == null) {
                return ResResult.retFailureMsg(NULL_MSG, null);
            } else {
                // 失败示例：{"errcode":40011,"errmsg":"invalid_request: 无效的请求或access_token失败"}
                // 成功示例：{
                //    "call_ticket": "6cd40f73124d47cdb40d89af1c2c03e8",
                //    "expiresss_in": "7200"
                //}
                if (get.get("errcode") != null) {
                    String getMsg = get.toString().replaceAll("\"", "");
                    log.error("获取测试ticket" + ":【" + MSG + "--" + getMsg + "】");
                    return ResResult.retFailureMsg(get.get("errmsg") == null ? getMsg : "$" + get.get("errmsg").asText(), null);
                } else {
                    String call_ticket = get.get("call_ticket").asText();
                    int expires_in = get.get("expires_in").asInt();
                    // 放进redis缓存
                    sysCacheService.set(ticketKey, call_ticket, expires_in);
                    return ResResult.retSuccessMsg("成功", call_ticket);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResResult.retFailureMsg(e.getMessage(), null);
        }
    }

    public ResResult isValidTicket(String token, String call_ticket, String authorize_uri) {
        // 此处应首先从redis中获取ticket，若没有再进行下面的操作
        String ticketKey = RedisEnum.UCAS_CA.getValue() + RedisEnum.SPESTR.getValue() + RedisEnum.TICKET.getValue()
                + RedisEnum.SPESTR.getValue() + call_ticket + RedisEnum.SPESTR.getValue() + authorize_uri;
        String ticket = sysCacheService.getString(ticketKey);
        if (ticket != null) {
            return ResResult.retSuccessMsg("成功", null);
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("access_token", token);
        params.put("call_ticket", call_ticket);
        params.put("authorize_uri", authorize_uri);

        try {
            JsonNode get = get(loginTypeConfig.getValidTicketUrl(), params);
            if (get == null) {
                return ResResult.retFailureMsg(NULL_MSG, null);
            } else {
                //成功返回样例：{"expires_in":33702,"client_name":"test_web","client_id":"2c9182e2-5be5e836-015b-e771e9c9"}
                //错误返回样例：{"errcode":40011,"errmsg":"invalid_request: 无效的请求或access_token失败"}
                if (get.get("errcode") != null) {
                    String getMsg = get.toString().replaceAll("\"", "");
                    log.error("校验ticket" + ":【" + MSG + "--" + getMsg + "】");
                    return ResResult.retFailureMsg(get.get("errmsg") == null ? getMsg : get.get("errmsg").asText(), null);
                } else {
                    int expires_in = get.get("expires_in").asInt();// 过期时间 秒
                    // 放进redis缓存
                    sysCacheService.set(ticketKey, call_ticket, expires_in);
                    return ResResult.retSuccessMsg("成功", null);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResResult.retFailureMsg(e.getMessage(), null);
        }
    }

}
