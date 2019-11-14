package gzkj.easygroupmeal.view;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public interface IView {
    void  toActivityData(String flag,String object);
    void  fail(String flag,Throwable t);
    void  invalid(String t);

    RequestBody data();
    List<MultipartBody.Part> dataFile();
}
