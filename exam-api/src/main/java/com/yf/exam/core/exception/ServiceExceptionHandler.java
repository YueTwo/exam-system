package com.yf.exam.core.exception;

import com.yf.exam.core.api.ApiRest;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

/**
 * 统一异常处理类
 * @author bool 
 * @date 2019-06-21 19:27
 */
@RestControllerAdvice
public class ServiceExceptionHandler {

    /**
     * 应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器
     * @param binder
     */
    @InitBinder
    public void initWebBinder(WebDataBinder binder){

    }

    /**
     * 把值绑定到Model中，使全局@RequestMapping可以获取到该值
     * @param model
     */
    @ModelAttribute
    public void addAttribute(Model model) {
        
    }

    /**
     * 捕获ServiceException
     * @param e
     * @return
     */
    @ExceptionHandler({com.yf.exam.core.exception.ServiceException.class})
    @ResponseStatus(HttpStatus.OK)
    public ApiRest serviceExceptionHandler(ServiceException e) {
        return new ApiRest(e);
    }

    /**
     * 捕获 Shiro 的授权异常，返回统一结构，避免抛出 500
     * @param e
     * @return
     */
    @ExceptionHandler({org.apache.shiro.authz.AuthorizationException.class})
    @ResponseStatus(HttpStatus.OK)
    public ApiRest authorizationExceptionHandler(org.apache.shiro.authz.AuthorizationException e) {
        // 返回统一的错误结构，前端会接收到 code != 0 表示失败
        com.yf.exam.core.exception.ServiceException se = new com.yf.exam.core.exception.ServiceException("无权限执行此操作，请联系管理员");
        return new ApiRest(se);
    }

}