package gzkj.easygroupmeal.biz;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public interface IModelBiz {
     void getData(String Request , String flag, String url, RequestBody data, onGetDataListener onGetDataListener);
     void getData(String Request , String flag, String url, List<MultipartBody.Part> data, onGetDataListener onGetDataListener);
}
