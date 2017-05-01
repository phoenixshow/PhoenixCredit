package com.phoenix.credit.common;

/**
 * 配置网络请求相关的地址
 */
public class AppNetConfig {
    public static final String IP_ADDRESS = "192.168.0.100";
    public static final String BASE_URL = "http://" + IP_ADDRESS + ":8080/credit/";

    public static final String PRODUCT = BASE_URL + "product";//访问投资
    public static final String LOGIN = BASE_URL + "login";//登录
    public static final String INDEX = BASE_URL + "index";//访问首页
    public static final String USER_REGISTER = BASE_URL + "UserRegister";//注册
    public static final String FEEDBACK = BASE_URL + "FeedBack";//用户反馈
    public static final String UPDATE = BASE_URL + "update.json";//更新应用
}
