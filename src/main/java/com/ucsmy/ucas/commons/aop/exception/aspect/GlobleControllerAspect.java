package com.ucsmy.ucas.commons.aop.exception.aspect;

import com.fasterxml.jackson.databind.JsonNode;
import com.ucsmy.commons.utils.StringAndNumberUtil;
import com.ucsmy.ucas.ca.constant.FileEnum;
import com.ucsmy.ucas.ca.constant.RedisEnum;
import com.ucsmy.ucas.ca.constant.ResultConstEnum;
import com.ucsmy.ucas.ca.service.OauthService;
import com.ucsmy.ucas.ca.utils.ThreadLocalUitls;
import com.ucsmy.ucas.commons.aop.exception.result.ResResult;
import com.ucsmy.ucas.commons.aop.exception.utils.JsonUtils;
import com.ucsmy.ucas.config.LoginTypeConfig;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局拦截 主要拦截异常和ticket
 */
@Order(1)
@Component
@Aspect
public class GlobleControllerAspect {

    @Autowired
    private OauthService oauthService;
    @Autowired
    private LoginTypeConfig loginTypeConfig;

    /**
     * 方法切入点
     */
    @Pointcut("execution(* com.ucsmy.ucas.ca.web.CaCertController.*(..))")
    public void pointerCutMethod() {
    }

    /**
     * 环绕通知
     *
     * @param joinPoint
     * @return
     */
    @Around(value = "pointerCutMethod()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        /** 测试方法不做拦截 直接过 */
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes()).getRequest();
        String servletPath = request.getServletPath();
        //if(servletPath.contains(FileEnum.FILE_IMG_SHOW.getValue())|| servletPath.indexOf("/test/") > -1 || servletPath.contains(CommonEnum.CORPORATE_ERECEIPT_SAVE.getValue()) || servletPath.contains(CommonEnum.CORPORATE_ERECEIPT_UPDATESTATUS.getValue())){
        //    return joinPoint.proceed();
        //}
        /** 传入的所有参数数组 */
        Object[] args = joinPoint.getArgs();
        String ticket;
        String contentType = request.getContentType();
        if (contentType!=null && contentType.contains(FileEnum.FILE_UPLOAD_TYPE.getValue())) {
            ticket = request.getParameter(RedisEnum.TICKET.getValue());
        } else {/** 拦截ticket */
            String jsonStr = JsonUtils.formatObjectToJson(args[0]);
            JsonNode jsonNode = JsonUtils.jsonToJsonNode(jsonStr).get(RedisEnum.TICKET.getValue());
            if (jsonNode == null || StringAndNumberUtil.isNullAfterTrim(jsonNode.asText()) || "null".equalsIgnoreCase(jsonNode.asText())) {
                // ticket为空
                return ResResult.retFailureMsg(ResultConstEnum.NOTICKET.getDescr(), null);
            }
            ticket = jsonNode.asText();
        }
        if (StringAndNumberUtil.isNullAfterTrim(ticket)) {
            return ResResult.retFailureMsg(ResultConstEnum.NOTICKET.getDescr(), null);
        }
        if (!isTicket(ticket)) {//ticket长度判断
            return ResResult.retFailureMsg(ResultConstEnum.INVALIDTICKET.getDescr(), null);
        } else {
            // 获取校验ticket的token
            ResResult tokenResult = oauthService.getTokenFromClient(RedisEnum.UCAS_CA.getValue(),
                    loginTypeConfig.getClientId(), loginTypeConfig.getClientSecret(),
                    loginTypeConfig.getScope());
            if (tokenResult.getRes() != ResResult.ResultType.SUCCESS.getIndex()) {
                return tokenResult;
            }
            // 校验ticket
            String access_token = (String) tokenResult.getData();
            ResResult validResult = oauthService.isValidTicket(access_token, ticket, servletPath);
            if (validResult.getRes() != ResResult.ResultType.SUCCESS.getIndex()) {
                return validResult;
            } else {// 校验通过 共享token
                ThreadLocalUitls.setToken(access_token);
            }
        }
        // 外面还有拦截，此处不用处理即可
        return joinPoint.proceed();

    }

    private boolean isTicket(String ticket) {
        if (StringAndNumberUtil.isNullAfterTrim(ticket) || ticket.length() > 100) return false;
        return true;
    }
}