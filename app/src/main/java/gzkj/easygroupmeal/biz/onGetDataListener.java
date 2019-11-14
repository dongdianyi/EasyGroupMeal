package gzkj.easygroupmeal.biz;


public interface onGetDataListener {
    void success(String data);

    void failed(Throwable t);

    void invalid(String s);

}
