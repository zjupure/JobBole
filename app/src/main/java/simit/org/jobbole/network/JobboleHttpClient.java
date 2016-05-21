package simit.org.jobbole.network;


import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import simit.org.jobbole.bean.RSSFeed;
import simit.org.jobbole.parser.IPageParser;

/**
 * Created by liuchun on 2016/4/1.
 */
public class JobboleHttpClient {
    // OkHttpClient
    private final OkHttpClient client;
    // Handler
    private final Handler handler;

    /** 私有化构造方法 */
    private JobboleHttpClient(){
        //
        client = new OkHttpClient();
        handler = new Handler(Looper.getMainLooper());
    }

    /** 内部静态类保证线程安全 */
    private static class InnerHolder{
        private static final JobboleHttpClient INSTANCE = new JobboleHttpClient();
    }

    public static JobboleHttpClient getInstance(){
        return InnerHolder.INSTANCE;
    }

    /** 返回OkHttpClient */
    public static OkHttpClient getClient(){
        return getInstance().client;
    }

    /** 返回Hander发送消息 */
    private static Handler getHandler(){
        return getInstance().handler;
    }

    /** Synchronous Get */
    public static <T> T syncGet(String url, IPageParser<T> parser){
        Request request = new Request.Builder()
                .url(url)
                .build();
        //发送请求
        try{
            Response response = getClient().newCall(request).execute();
            T resp = parser.parse(response);
            parser.onSuccess(resp);

            return  resp;
        }catch (Exception e){
            parser.onFailure(e);
        }
        return null;
    }

    /** Asynchronous Get */
    public static <T> void asynGet(String url, final IPageParser<T> parser){
        Request request = new Request.Builder()
                .url(url)
                .build();
        //发送请求
        getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                // 发送到主线程
                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        parser.onFailure(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(!response.isSuccessful()){
                    onFailure(call, new IOException("network response failed " + response));
                    return;
                }
                try{
                    // 从网络响应中解析数据
                    final T resp = parser.parse(response);
                    // 发送到主线程
                    getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            parser.onSuccess(resp);
                        }
                    });

                }catch (Exception e){
                    parser.onFailure(e);
                }

            }
        });
    }

    /** 获取Feed数据流 */
    public static void getRssFeed(String url, IPageParser<RSSFeed> parser){
        asynGet(url, parser);
    }

    /** 获取HTML页面信息 */
    public static void getPageSource(String url, IPageParser<String> parser){
        asynGet(url, parser);
    }
}
