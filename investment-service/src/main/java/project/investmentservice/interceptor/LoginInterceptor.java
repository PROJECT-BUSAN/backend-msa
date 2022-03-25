package project.investmentservice.interceptor;


import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import project.investmentservice.annotation.LoginRequired;
import project.investmentservice.exceptions.AuthException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Deprecated
@Component
public class LoginInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean hasAnnotation = checkAnnotation(handler, LoginRequired.class);
        
        if (hasAnnotation) {
            System.out.println("request.getSession() = " + request.getClass().getName());
            Long userId = (Long) request.getAttribute("userId");
            System.out.println("userId = " + userId);
            String userName = (String) request.getAttribute("username");
            System.out.println("userName = " + userName);
            if (userId == null || userName == null) {
                return false;
//                throw new AuthException("Login Required");
            }
        }
        return true;
    }
    
    private boolean checkAnnotation(Object handler, Class<LoginRequired> LoginClass) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        //LoginClass anntotation이 있는 경우
        if (handlerMethod.getMethodAnnotation(LoginClass) != null || 
                handlerMethod.getBeanType().getAnnotation(LoginClass) != null) {
            return true;
        }

        //annotation이 없는 경우
        return false;
    }
}
