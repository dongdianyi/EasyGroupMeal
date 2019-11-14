package gzkj.easygroupmeal.bean;

import java.util.List;

import gzkj.easygroupmeal.utli.DateUtil;

public class Notice  {

    /**
     * result : 200
     * resultDesc : 查询成功
     * resultObj : {"banners":[{"url":"http://pic.qiantucdn.com/58pic/19/73/71/5710796ea93d1_1024.jpg"},{"url":"http://bpic.588ku.com/back_pic/03/79/10/7657c26e6a33c38.jpg"}],"notices":[{"company_id":"49","content":"我再发一条公告","createtime":"2019-04-22 14:36:02.0","post_name":"总监","user_name":"我是总监"},{"company_id":"49","content":"我要发布一个公告","createtime":"2019-04-22 11:25:56.0","post_name":"总监","user_name":"我是总监"}]}
     */

    private String result;
    private String resultDesc;
    private ResultObjBean resultObj;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public ResultObjBean getResultObj() {
        return resultObj;
    }

    public void setResultObj(ResultObjBean resultObj) {
        this.resultObj = resultObj;
    }

    public static class ResultObjBean {
        private List<BannersBean> banners;
        private List<NoticesBean> notices;

        public List<BannersBean> getBanners() {
            return banners;
        }

        public void setBanners(List<BannersBean> banners) {
            this.banners = banners;
        }

        public List<NoticesBean> getNotices() {
            return notices;
        }

        public void setNotices(List<NoticesBean> notices) {
            this.notices = notices;
        }

        public static class BannersBean {
            /**
             * url : http://pic.qiantucdn.com/58pic/19/73/71/5710796ea93d1_1024.jpg
             */

            private String url;
            private String details;

            public String getDetails() {
                return details;
            }

            public void setDetails(String details) {
                this.details = details;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public static class NoticesBean {
            /**
             * company_id : 49
             * content : 我再发一条公告
             * createtime : 2019-04-22 14:36:02.0
             * post_name : 总监
             * user_name : 我是总监
             */

            private String content;
            private String createtime;
            private String post_name;
            private String user_name;


            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public long getCreatetime() {
                return DateUtil.date2TimeStamp(createtime,"yyyy-MM-dd HH:mm:ss");
            }

            public void setCreatetime(String createtime) {
                this.createtime = createtime;
            }

            public String getPost_name() {
                return post_name;
            }

            public void setPost_name(String post_name) {
                this.post_name = post_name;
            }

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }
        }
    }
}
