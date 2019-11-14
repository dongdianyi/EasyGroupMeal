package gzkj.easygroupmeal.bean;

import java.io.Serializable;
import java.util.List;

import gzkj.easygroupmeal.utli.DateUtil;

public class Company {


    /**
     * result : 200
     * resultDesc : 查询成功，有数据
     * resultObj : [{"teamName":"123","companyId":"49","address":"浆水泉西路98号","teamId":93,"companyName":"山东乔柏信息科技有限公司","schoolZone":"123","otherName":"123"}]
     */

    private String result;
    private String resultDesc;
    private List<ResultObjBean> resultObj;

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

    public List<ResultObjBean> getResultObj() {
        return resultObj;
    }

    public void setResultObj(List<ResultObjBean> resultObj) {
        this.resultObj = resultObj;
    }

    public static class ResultObjBean implements Serializable {
        /**
         * teamName : 123
         * companyId : 49
         * address : 浆水泉西路98号
         * teamId : 93
         * companyName : 山东乔柏信息科技有限公司
         * schoolZone : 123
         * otherName : 123
         */

        private String teamName;
        private String companyId;
        private String address;
        private int teamId;
        private String companyName;
        private String schoolZone;
        private String otherName;
        private String postName;
        private String postType;
        private String userName;
        private String userId;
        private String content;
        private String title;
        private String createtime;
        private String message_id;
        private String url;
        private String flag;
        private String head;
        private String telnum;
        private boolean isCheck;


        private int verfityId;
        private String verfityName;
        private long createTime;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getPostType() {
            return postType;
        }

        public void setPostType(String postType) {
            this.postType = postType;
        }

        public String getTelnum() {
            return telnum;
        }

        public void setTelnum(String telnum) {
            this.telnum = telnum;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getVerfityId() {
            return verfityId;
        }

        public void setVerfityId(int verfityId) {
            this.verfityId = verfityId;
        }

        public String getVerfityName() {
            return verfityName;
        }

        public void setVerfityName(String verfityName) {
            this.verfityName = verfityName;
        }


        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public long getCreatetime() {
            return DateUtil.date2TimeStamp(createtime,"yyyy-MM-dd HH:mm");
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getMessage_id() {
            return message_id;
        }

        public void setMessage_id(String message_id) {
            this.message_id = message_id;
        }

        public String getUrl() {
            if (url==null) {
                return "";
            }
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPostName() {
            return postName;
        }

        public void setPostName(String postName) {
            this.postName = postName;
        }

        public String getUserName() {
            if (userName==null) {
                return "";
            }
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getTeamName() {
            return teamName;
        }

        public void setTeamName(String teamName) {
            this.teamName = teamName;
        }

        public String getCompanyId() {
            return companyId;
        }

        public void setCompanyId(String companyId) {
            this.companyId = companyId;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getTeamId() {
            return teamId;
        }

        public void setTeamId(int teamId) {
            this.teamId = teamId;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getSchoolZone() {
            return schoolZone;
        }

        public void setSchoolZone(String schoolZone) {
            this.schoolZone = schoolZone;
        }
    }
}
