package gzkj.easygroupmeal.biz;

import android.os.Message;
import android.util.Base64;
import android.util.Log;

import com.github.jdsjlzx.util.WeakHandler;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import gzkj.easygroupmeal.app.MyApplication;
import gzkj.easygroupmeal.bean.Bean;
import gzkj.easygroupmeal.httpUtil.RetrofitBean;
import gzkj.easygroupmeal.httpUtil.SimpleCallBack;
import gzkj.easygroupmeal.utli.DataCleanManager;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class ModelBiz implements IModelBiz {
    //数据请求获取数据
    private onGetDataListener onGetDataListener;
    private RequestBody body;
    private List<MultipartBody.Part> fileBody;
    private Call<ResponseBody> data;
    private String flag, url;
    private Message msg;

    private WeakHandler mHandler = new WeakHandler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //搜索好友
                    if (msg.obj.equals("POST")) {
                        data = RetrofitBean.getApi().getPostData(url, body);
                    }
                    data.enqueue(new SimpleCallBack(flag) {
                        @Override
                        protected void showData(int i, String json) {
                            super.showData(i, json);
                            if (i == 0) {
                                Bean bean = new Gson().fromJson(json, Bean.class);
                                String string = new String(Base64.decode(bean.getBody().getBytes(), Base64.NO_WRAP));
                                Log.e("body数据", string);
                                try {
                                    JSONObject jsonObject = new JSONObject(string);
                                    if ("200".equals(jsonObject.getString("result"))) {
                                        onGetDataListener.success(string);
                                    } else if ("318".equals(jsonObject.getString("result"))) {
                                        onGetDataListener.invalid(jsonObject.getString("resultDesc"));
                                    } else {
                                        onGetDataListener.failed(new Throwable(jsonObject.getString("resultDesc")));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    onGetDataListener.failed(e);
                                }
                            } else {
                                onGetDataListener.failed(new Throwable(json));
                            }
                        }

                        @Override
                        protected void showError(int i, Throwable t) {
                            super.showError(i, t);
                            onGetDataListener.failed(t);
                        }
                    });
                    break;
                case 0x01:
                    onGetDataListener.success("清理完成");
                    break;
                case 0x02:
                    onGetDataListener.failed(new Throwable("清理失败"));
                    break;
                case 3:
                    //搜索好友
                    if (msg.obj.equals("POST")) {
                        data = RetrofitBean.getApi().getUpFile(fileBody);
                    }
                    data.enqueue(new SimpleCallBack(flag) {
                        @Override
                        protected void showData(int i, String json) {
                            super.showData(i, json);
                            Log.e("json数据", json);
                            if (i == 0) {
                                onGetDataListener.success(json);
                            } else {
                                onGetDataListener.failed(new Throwable(json));
                            }
                        }

                        @Override
                        protected void showError(int i, Throwable t) {
                            super.showError(i, t);
                            onGetDataListener.failed(t);
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void getData(String request, String flag, String url, RequestBody data, onGetDataListener onGetDataListener) {
        this.onGetDataListener = onGetDataListener;
        this.body = data;
        this.flag = flag;
        this.url = url;
        msg = Message.obtain();
        if (flag.equals("clear")) {
            try {
                DataCleanManager.cleanInternalCache(MyApplication.getContextObject());
                msg.what = 0x01;
            } catch (Exception e) {
                e.printStackTrace();
                msg.what = 0x02;
            }
            mHandler.sendMessageDelayed(msg, 1000);
        } else {
            msg.what = 0;
            msg.obj = request;
            mHandler.sendMessage(msg);
        }

    }

    //文件图片
    @Override
    public void getData(String request, String flag, String url, List<MultipartBody.Part> data, gzkj.easygroupmeal.biz.onGetDataListener onGetDataListener) {
        this.onGetDataListener = onGetDataListener;
        this.fileBody = data;
        this.flag = flag;
        this.url = url;
        msg = Message.obtain();
        msg.what = 3;
        msg.obj = request;
        mHandler.sendMessage(msg);
    }

}
