package com.example.networkdemo;

/**
 * 回调接口
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
