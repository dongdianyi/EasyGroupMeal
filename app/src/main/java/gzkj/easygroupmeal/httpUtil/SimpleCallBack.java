package gzkj.easygroupmeal.httpUtil;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ddy on 2018/4/2
 * 自定义根据返回json设置结果
 */

public class SimpleCallBack implements Callback<ResponseBody> {

    private String result="";
    private static String TAG;
    public SimpleCallBack(String tag) {
        TAG = tag;
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        try {
            if (response.body() != null) {
                result = response.body().string();
            }
            JSONObject jsonObject = new JSONObject(result);
            if (TextUtils.isEmpty(result)||result.equals("")||result.trim().equals("")) {
                showError(0, new Throwable("亲！取得数据为空"));
            } else if ("0".equals(jsonObject.getString("result"))) {
                showData(0, result);
            } else {
                if (jsonObject.getString("result") != null && !jsonObject.getString("result").equals("")) {
                    //result不为0时返回的json数据
                    Log.e(TAG,result);
                    showData(1,jsonObject.getString("result"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        t.printStackTrace();
        showError(-1, t);
    }

    protected void showError(int i, Throwable t) {
        Log.e("error", t.toString());
    }

    protected void showData(int i, String json) {
        //i=0 正确数据 i=1 非正常数据
        Log.e(TAG, json);
    }
}
