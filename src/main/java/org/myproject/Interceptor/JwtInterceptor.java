package org.myproject.Interceptor;


import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.myproject.utils.JwtUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    @Resource
    private JwtUtil jwtUtil;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 从请求头中获取token
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("token is null");
            return false;
        }
        String token = header.substring(7);
        // 校验token是否合法
        if(!jwtUtil.validateToken(token)){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("token is invalid");
            return false;
        }
        Claims claims = jwtUtil.parseToken(token);
        // 从Claims中获取用户id
        String userId = claims.getSubject();
        // 从Claims中获取用户名
        String username = claims.get("username").toString();
        // 将用户id和用户名添加到请求中
        request.setAttribute("userId", userId);
        request.setAttribute("username", username);
        return true;
    }
}
