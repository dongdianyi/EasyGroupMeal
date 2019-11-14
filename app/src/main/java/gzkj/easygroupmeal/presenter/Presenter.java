package gzkj.easygroupmeal.presenter;

import android.os.Handler;

import com.google.gson.JsonSyntaxException;

import gzkj.easygroupmeal.biz.IModelBiz;
import gzkj.easygroupmeal.biz.ModelBiz;
import gzkj.easygroupmeal.biz.onGetDataListener;
import gzkj.easygroupmeal.view.IView;


public class Presenter {
    private IModelBiz iModelBiz;
    private IView iView;
    private Handler mHandler = new Handler();

    public Presenter(IView iView) {
        this.iModelBiz = new ModelBiz();
        this.iView = iView;
    }

    public void getData(String request, final String flag, String url) {
        iModelBiz.getData(request, flag, url, iView.data(), new onGetDataListener() {

            @Override
            public void success(final String data) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            iView.toActivityData(flag, data);
                        } catch (JsonSyntaxException e) {
//                            iView.fail(flag, e);
                        }
                    }
                });
            }

            @Override
            public void failed(final Throwable t) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iView.fail(flag, t);
                    }
                });
            }

            @Override
            public void invalid(String s) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iView.invalid(s);
                    }
                });

            }
        });
    }

    public void getDataFile(String request, final String flag, String url) {
        iModelBiz.getData(request, flag, url, iView.dataFile(), new onGetDataListener() {

            @Override
            public void success(final String data) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            iView.toActivityData(flag, data);
                        } catch (JsonSyntaxException e) {
//                            iView.fail(flag, e);
                        }
                    }
                });
            }

            @Override
            public void failed(final Throwable t) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iView.fail(flag, t);
                    }
                });
            }

            @Override
            public void invalid(String s) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iView.invalid(s);
                    }
                });
            }
        });
    }
}
