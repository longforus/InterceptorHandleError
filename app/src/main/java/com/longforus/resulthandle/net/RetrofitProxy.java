package com.longforus.resulthandle.net;

import android.util.Log;
import com.longforus.resulthandle.conf.Constant;
import com.longforus.resulthandle.exception.ResultException;
import com.longforus.resulthandle.exception.ResultStatus;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Void Young on 11/10/2016  9:09 PM.
 * Description : retrofit的代理类
 */

public class RetrofitProxy {
    private static final String TAG = "RetrofitProxy";
    private static Retrofit sRetrofit;
    private static OkHttpClient sClient;

    static {
        sClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
                                            .writeTimeout(10, TimeUnit.SECONDS)
                                            .readTimeout(10, TimeUnit.SECONDS)
                                            .addInterceptor(new ErrorInterceptor())//引入错误处理Interceptor
                                            .build();
        initRetrofit(sClient);
    }

    private RetrofitProxy() {
    }

    public static Retrofit getInstance() {
        initRetrofit(null);
        return sRetrofit;
    }

    private static void initRetrofit(OkHttpClient client) {
        if (sRetrofit == null) {
            synchronized (RetrofitProxy.class) {
                if (sRetrofit == null) {
                    sRetrofit = new Retrofit.Builder().client(client == null ? sClient : client)//使用自定义的client
                                                      .baseUrl(Constant.BASE_URL)
                                                      .addConverterFactory(GsonConverterFactory.create())//结果bean的转化工厂
                                                      .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//回调适配器工厂
                                                      .build();
                }
            }
        }
    }

    /**
     * 直接创建请求对象类
     */
    public static <T> T createService(Class<T> serviceClazz) {
        return sRetrofit.create(serviceClazz);
    }

    private static class ErrorInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request oldRequest = chain.request();
            Response response = chain.proceed(oldRequest);
            byte[] respBytes = response.body()
                                   .bytes();
            String respString = new String(respBytes);
            try {
                JSONObject object = new JSONObject(respString);
                int code = (int) object.get("total");//模拟校验情况,实际使用中根据情况来实际判断
                Log.i(TAG, "intercept: code = "+code);//实际返回的code是303  這里模拟使用中返回了结果但是,结果并非完全预期的情况
                if (code != ResultStatus.OK) {//开启判断制造异常,查看效果
                    throw new ResultException("返回码异常", code);//抛出自定义异常,在subscriber的onError中被接收,达到分离处理的目的
                }
                return response.newBuilder()
                               .body(ResponseBody.create(null, respBytes))
                               .build();//在前面获取bytes的时候response的stream已经被关闭了,要重新生成response
            } catch (JSONException e) {
                e.printStackTrace();
                throw new ResultException("解析异常", ResultStatus.PARSE_ERROR);
            }
        }
    }
}
