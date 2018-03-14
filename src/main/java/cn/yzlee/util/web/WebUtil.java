package cn.yzlee.util.web;


import cn.yzlee.exception.CurrentUserInfoMissingException;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Objects;

/**
 * @Author:lyz
 * @Date: 2017/12/20 13:06
 * @Desc: web工具类
 **/
public class WebUtil
{

    public static boolean isLogin(HttpServletRequest request) {
        HttpSession session = ((HttpServletRequest) request).getSession();
        return session != null && session.getAttribute(ISLOGIN) != null;
    }

    public static void login(HttpServletRequest request) {
        HttpSession session = ((HttpServletRequest) request).getSession(true);
        session.setAttribute(ISLOGIN, System.currentTimeMillis());
    }


    public static Object getCurrentUser(ServletRequest request) {
        HttpSession session = ((HttpServletRequest) request).getSession();
        if (session == null) {
            throw new CurrentUserInfoMissingException();
        }
        Object ob = null;
        if ((ob = session.getAttribute(USER_KEY)) == null) {
            throw new CurrentUserInfoMissingException();
        }
        return ob;
    }

    public static String getRealPath(HttpServletRequest request,String relativePath){
        return request.getServletContext().getRealPath(
                relativePath);
    }

    public static String getContextServer(HttpServletRequest request) {
        int port = request.getServerPort();
        return request.getScheme() + "://" + request.getServerName() + (port == 80 ? "" : ":" + port);
    }

    public static void addCurrentUser(ServletRequest request, Object object) {
        HttpSession session = ((HttpServletRequest) request).getSession(true);
        session.setAttribute(USER_KEY, object);
        login((HttpServletRequest) request);
    }

    public static void logout(HttpServletRequest request) {
        HttpSession session = ((HttpServletRequest) request).getSession(true);
        session.removeAttribute(ISLOGIN);
        session.removeAttribute(USER_KEY);
        session.invalidate();
    }


    public static void addSessionAttribute(ServletRequest request, String key, Object value) {
        HttpSession session = ((HttpServletRequest) request).getSession(true);
        session.setAttribute(key, value);
    }

    public static void removeSessionAttribute(ServletRequest request, String key) {
        HttpSession session = ((HttpServletRequest) request).getSession(true);
        session.removeAttribute(key);
    }

    public static Object getSessionAttribute(ServletRequest request, String key) {
        HttpSession session = ((HttpServletRequest) request).getSession(true);
        return session.getAttribute(key);
    }

    public static void addCookie(String name, String value, Integer age, String path, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(age);
        cookie.setHttpOnly(true);
        if (Objects.nonNull(path)) {
            cookie.setPath(path);
        }
        response.addCookie(cookie);
    }

    /**
     *根据指定的cookie名从request中拿值
     * @param request
     * @param cookieName 指定cookie名称
     */
    public static String getCookieValue(HttpServletRequest request,final String cookieName){
        String cookieValue = null;
        Cookie[] cookies = request.getCookies();
        if(Objects.nonNull(cookies)){
            cookieValue =  Arrays.stream(cookies).filter(v->{
                return cookieName.equals(v.getName());
            }).map(v->{return  v.getValue();}).findFirst().orElse(null);
        }
        return cookieValue;
    }

    public static final String USER_KEY = "user";

    public static final String ISLOGIN = "isLogin";

}
