package gzkj.easygroupmeal.bean;

import java.io.Serializable;
import java.util.List;

public class Date implements Serializable {

    /**
     * result : 200
     * resultDesc : 成功
     * resultObj : [{"cause":"备注","clockId":"1","date":"2019-7-4","teamId":"99"},{"cause":"哈哈","clockId":"2","date":"2019-07-04","teamId":"99"},{"cause":"","clockId":"3","date":"2019-07-04","teamId":"99"},{"cause":"","clockId":"4","date":"2019-07-05","teamId":"99"},{"cause":"","clockId":"5","date":"2019-07-04","teamId":"99"}]
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

    public static class ResultObjBean implements Serializable{
        /**
         * cause : 备注
         * clockId : 1
         * date : 2019-7-4
         * teamId : 99
         */

        private String cause;
        private String clockId;
        private String date;
        private String teamId;
        private String check_status;
        private String endTime;
        private String startTime;
        private String userName;
        private String id;

        public ResultObjBean(String cause, String clockId, String date, String teamId) {
            this.cause = cause;
            this.clockId = clockId;
            this.date = date;
            this.teamId = teamId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCheck_status() {
            return check_status;
        }

        public void setCheck_status(String check_status) {
            this.check_status = check_status;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getCause() {
            if (cause==null) {
                return "";
            }
            return cause;
        }

        public void setCause(String cause) {
            this.cause = cause;
        }

        public String getClockId() {
            return clockId;
        }

        public void setClockId(String clockId) {
            this.clockId = clockId;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTeamId() {
            return teamId;
        }

        public void setTeamId(String teamId) {
            this.teamId = teamId;
        }
    }
}
