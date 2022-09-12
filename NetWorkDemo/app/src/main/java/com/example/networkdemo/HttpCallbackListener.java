package com.example.networkdemo;

/**
 * 回调接口，在sendHttpRequest中开启线程后，服务器响应的数据无法返回，因此定义一个接口利用Java的回调机制来返回数据
 * @author admin
 */
public interface HttpCallbackListener {

    /**
     * 服务器响应的数据
     * @param response 服务器响应的数据
     */
    void onFinish(String response);

    /**
     * 错误信息
     * @param e 错误信息
     */
    void onError(Exception e);
}
