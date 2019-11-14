package gzkj.easygroupmeal.bean;

public class Login {

    /**
     * result : 200
     * resultDesc : 登录成功
     * resultObj : {"post_type":"GM","sid":"70871d2a-6021-456a-b200-45348dacc0dc","status":"0","telnum":"13568683001","userName":"我是总监"}
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
        /**
         * post_type : GM
         * sid : 70871d2a-6021-456a-b200-45348dacc0dc
         * status : 0
         * telnum : 13568683001
         * userName : 我是总监
         */

        private String post_type;
        private String sid;
        private String status;
        private String userName;
        private String companyName;
        private String postName;
        private String recordId;
        private String submitId;
        private String taskStatus;
        private String userId;
        private String head;
        private String verifyState;
        private int verfityCount;
        private int messageCount;

        public int getMessageCount() {
            return messageCount;
        }

        public void setMessageCount(int messageCount) {
            this.messageCount = messageCount;
        }

        public int getVerfityCount() {
            return verfityCount;
        }

        public void setVerfityCount(int verfityCount) {
            this.verfityCount = verfityCount;
        }

        public String getVerifyState() {
            return verifyState;
        }

        public void setVerifyState(String verifyState) {
            this.verifyState = verifyState;
        }

        public String getHead() {
            if (head==null) {
                return "";
            }
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getRecordId() {
            return recordId;
        }

        public void setRecordId(String recordId) {
            this.recordId = recordId;
        }

        public String getSubmitId() {
            return submitId;
        }

        public void setSubmitId(String submitId) {
            this.submitId = submitId;
        }

        public String getTaskStatus() {
            return taskStatus;
        }

        public void setTaskStatus(String taskStatus) {
            this.taskStatus = taskStatus;
        }

        public String getCompanyName() {
            if (companyName==null) {
                return "";
            }
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getPostName() {
            return postName;
        }

        public void setPostName(String postName) {
            this.postName = postName;
        }

        public String getPost_type() {
            return post_type;
        }

        public void setPost_type(String post_type) {
            this.post_type = post_type;
        }

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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
    }
}
