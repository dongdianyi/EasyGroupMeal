package gzkj.easygroupmeal.httpUtil;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ddy on 2018/3/27.
 * 请求之前的设置
 */

public class RetrofitBean {
    private static POSTService postService;

    public static POSTService getApi() {
        postService = null;
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        //初始化retrofit框架
        Retrofit build = new Retrofit.Builder()
                .client(okHttpBuilder.build())
                //1.配置主机地址
                .baseUrl(HttpUrl.BASE_URL)
                //2.解析json的工具
                .addConverterFactory(GsonConverterFactory.create(new Gson())).build();

        //读取接口上面的参数
        postService = build.create(POSTService.class);
        return postService;
    }

    public static POSTService getApiQCC() {
        postService = null;
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        //初始化retrofit框架
        Retrofit build = new Retrofit.Builder()
                .client(okHttpBuilder.build())
                //1.配置主机地址
                .baseUrl(HttpUrl.QCC_URL)
                //2.解析json的工具
                .addConverterFactory(GsonConverterFactory.create(new Gson())).build();
        //读取接口上面的参数
        postService = build.create(POSTService.class);
        return postService;
    }
}
