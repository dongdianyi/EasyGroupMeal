package gzkj.easygroupmeal.httpUtil;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by ddy on 2019/3/27.
 * 请求的数据
 */

public interface POSTService {

    @Headers({"Content-Type: application/json","Accept: application/json"})//json方式提交需要添加头使用body
    @POST()
    Call<ResponseBody> getPostData(@Url String url, @Body RequestBody info);

    @Multipart
    @POST("file/saveFile")
    Call<ResponseBody> getUpFile(@Part List<MultipartBody.Part> file);

    @GET("ECIMatch/CompanyVerify")
    Call<ResponseBody> getQCC(@Header("Token") String Token, @Header("Timespan") String Timespan, @Query("key") String key, @Query("regNo") String regNo, @Query("companyName") String companyName, @Query("frName") String frName, @Query("dtype") String dtype);
}
