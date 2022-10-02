package com.example.servicebestpractice;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 下载任务
 */
public class DownloadTask extends AsyncTask<String, Integer, Integer> {

    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_FAILED = 1;
    public static final int TYPE_PAUSED = 2;
    public static final int TYPE_CANCELED = 3;

    private DownloadListener listener;

    private boolean isCanceled = false;

    private boolean isPaused = false;

    private int lastProgress;

    /**
     * 从DownloadService类中传递进来的listener对象
     */
    public DownloadTask(DownloadListener listener) {
        this.listener = listener;
    }

    @Override
    protected Integer doInBackground(String... params) {
        InputStream is = null;
        RandomAccessFile savedFile = null;
        File file = null;
        try {
            // 记录已下载的文件长度
            long downloadLength = 0;
            // 来自MainActivity传递过来的url参数
            String downloadUrl = params[0];
            // 通过最后一个"/"符号，分理出文件名
            String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
            // android中SD卡下的download目录的路径
            String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            // 可以理解为download目录下的fileName文件
            file = new File(directory + fileName);
            // 判断是否存在，如果存在，获取文件的字节长度
            if (file.exists()) {
                downloadLength = file.length();
            }
            // 获取需要下载的文件的大小
            long contentLength = getContentLength(downloadUrl);
            // 如果需要下载的文件大小是0，则返回TYPE_FAILED到onPostExecute
            if (contentLength == 0) {
                return TYPE_FAILED;
            } else if (contentLength == downloadLength) {
                // 已下载字节和文件总字节相等，说明下载完成了
                return TYPE_SUCCESS;
            }
            // 使用OKHttp创建连接
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    // 断点下载，指定从哪个字节开始下载
                    .addHeader("RANGE", "bytes=" + downloadLength + "-")
                    .url(downloadUrl)
                    .build();
            // 获取响应的数据
            Response response = client.newCall(request).execute();
            if (response != null) {
                // 将响应的数据转换成字节流
                is = response.body().byteStream();
                // RandomAccessFile既可以读取文件内容，也可以向文件输出数据。同时，RandomAccessFile支持“随机访问”的方式，
                // 程序快可以直接跳转到文件的任意地方来读写数据。"rw": 打开以便读取和写入。
                // RandomAccessFile类有两个构造函数，其实这两个构造函数基本相同，只不过是指定文件的形式不同
                // 一个需要使用String参数来指定文件名，一个使用File参数来指定文件本身。
                // 除此之外，创建RandomAccessFile对象时还需要指定一个mode参数，该参数指定RandomAccessFile的访问模式，
                savedFile = new RandomAccessFile(file, "rw");
                // 跳过已下载的字节 将文件指针定位到downloadLength位置
                savedFile.seek(downloadLength);
                byte[] b = new byte[1024];
                int total = 0;
                int len;
                // 不断把响应的数据写入到文件中，同时监听是否点击暂停或者取消
                while ((len = is.read(b)) != -1) {
                    if (isCanceled) {
                        return TYPE_CANCELED;
                    } else if (isPaused) {
                        return TYPE_PAUSED;
                    } else {
                        total += len;
                    }
                    // b 这一次写的数据
                    // 0 这次从b的0处开始写
                    // len 这次写的长度
                    savedFile.write(b, 0, len);
                    // 计算已下载的百分比
                    int progress = (int) ((total + downloadLength) * 100 / contentLength);
                    // 将进度实时传递给onProgressUpdate进行更新到通知上
                    publishProgress(progress);
                }
            }
            // 读取完毕，关闭资源
            response.body().close();
            // 返回TYPE_SUCCESS
            return TYPE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (is != null) {
                    is.close();
                }
                if (savedFile != null) {
                    savedFile.close();
                }
                if (isCanceled && file != null) {
                    file.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 如果上面的代码出错，即返回失败
        return TYPE_FAILED;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        // 获取传递过来的进度
        int progress = values[0];
        if (progress > lastProgress) {
            // 将进度传递到DownloadService中进行更新前台服务，同时把这次的进度设置未lastProgress
            listener.onProgress(progress);
            lastProgress = progress;
        }
    }

    // 接受返回过来的参数，进行判断
    @Override
    protected void onPostExecute(Integer status) {
        switch (status) {
            case TYPE_SUCCESS:
                listener.onSuccess();
                break;
            case TYPE_FAILED:
                listener.onFailed();
                break;
            case TYPE_PAUSED:
                listener.onPaused();
                break;
            case TYPE_CANCELED:
                listener.onCanceled();
                break;
            default:
                break;
        }
    }

    public void pauseDownload() {
        isPaused = true;
    }

    public void cancelDownload() {
        isCanceled = true;
    }

    /**
     * 获取需要下载的资源的长度
     * @param downloadUrl 下载的资源的url
     * @return 返回长度
     * @throws Exception 抛出错误
     */
    private long getContentLength(String downloadUrl) throws Exception {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(downloadUrl)
                .build();
        Response response = client.newCall(request).execute();
        if (response != null && response.isSuccessful()) {
            long contentLength = response.body().contentLength();
            response.body().close();
            return contentLength;
        }
        return 0;
    }
}
