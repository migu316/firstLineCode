package com.example.servicebestpractice;

/**回调接口
 * @author admin
 */
public interface DownloadListener {
    /**
     * 通知当前的下载进度
     * @param progress 进度
     */
    void onProgress(int progress);

    /**
     * 通知下载完成
     */
    void onSuccess();

    /**
     * 通知下载失败
     */
    void onFailed();

    /**
     * 通知下载暂停
     */
    void onPaused();

    /**
     * 通知下载取消
     */
    void onCanceled();
}
