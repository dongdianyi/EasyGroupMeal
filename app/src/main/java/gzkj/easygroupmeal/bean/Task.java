package gzkj.easygroupmeal.bean;

import java.io.Serializable;
import java.util.List;

public class Task {

    /**
     * result : 200
     * resultDesc : 查询成功
     * resultObj : [{"dispatchId":"311","endTime":"07:35","isWarn":"0","receptId":"b5b56075-0749-4724-8300-614a3e2316b3","recordId":"1255","startTime":"07:30","taskContent":"发现问题及时报修","taskCycle":"1,2,3,4,5","taskId":"5002","taskLevel":"2","taskName":"检查设施设备及用具是否能正常使用，并向厨师长询问上午热菜计划","taskRemind":"5","taskType":"1"},{"dispatchId":"311","endTime":"09:00","isWarn":"0","receptId":"b5b56075-0749-4724-8300-614a3e2316b3","recordId":"1257","startTime":"08:55","taskContent":"地面无垃圾，无积水，物品摆放整齐","taskCycle":"1,2,3,4,5","taskId":"5004","taskLevel":"2","taskName":"清理加工间卫生","taskRemind":"5","taskType":"1"},{"dispatchId":"294","endTime":"16:30","isWarn":"0","receptId":"b5b56075-0749-4724-8300-614a3e2316b3","recordId":"1229","startTime":"16:25","taskContent":"按时到岗，穿戴整洁工作衣帽，按照正确方式洗手","taskCycle":"1","taskId":"2013","taskLevel":"2","taskName":"更衣洗手上岗","taskRemind":"5","taskType":"1"},{"dispatchId":"294","endTime":"18:30","isWarn":"0","receptId":"b5b56075-0749-4724-8300-614a3e2316b3","recordId":"1232","startTime":"17:30","taskContent":"佩戴一次性卫生手套口罩头发收进帽子里，见到客人亲切问好并使用礼貌用语","taskCycle":"1","taskId":"2016","taskLevel":"2","taskName":"到所属窗口与卖饭","taskRemind":"5","taskType":"1"},{"dispatchId":"294","endTime":"18:30","isWarn":"0","receptId":"b5b56075-0749-4724-8300-614a3e2316b3","recordId":"1250","startTime":"17:30","taskContent":"桌面的残食要及时清理保持桌面整洁，地面无垃圾","taskCycle":"1","taskId":"3016","taskLevel":"2","taskName":"分工清理餐桌残食，地面垃圾，收残台有人收餐盘并将餐具分类","taskRemind":"5","taskType":"1"},{"dispatchId":"294","endTime":"19:10","isWarn":"0","receptId":"b5b56075-0749-4724-8300-614a3e2316b3","recordId":"1234","startTime":"19:00","taskContent":"经检查合格后方可下班","taskCycle":"1","taskId":"2018","taskLevel":"2","taskName":"更衣下班","taskRemind":"5","taskType":"1"}]
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
        public ResultObjBean() {
        }

        public ResultObjBean(String startTime, String endTime, String taskContent) {
            this.endTime = endTime;
            this.startTime = startTime;
            this.taskContent = taskContent;
        }

        /**
         * dispatchId : 311
         * endTime : 07:35
         * isWarn : 0
         * receptId : b5b56075-0749-4724-8300-614a3e2316b3
         * recordId : 1255
         * startTime : 07:30
         * taskContent : 发现问题及时报修
         * taskCycle : 1,2,3,4,5
         * taskId : 5002
         * taskLevel : 2
         * taskName : 检查设施设备及用具是否能正常使用，并向厨师长询问上午热菜计划
         * taskRemind : 5
         * taskType : 1
         */

        private String endTime;
        private String isWarn;
        private String receptId;
        private String recordId;
        private String startTime;
        private String taskContent;
        private String taskName;
        private String taskType;
        private String taskStatus;
        private String submitId;
        private boolean isCheck=false;

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

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }


        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getIsWarn() {
            return isWarn;
        }

        public void setIsWarn(String isWarn) {
            this.isWarn = isWarn;
        }

        public String getReceptId() {
            return receptId;
        }

        public void setReceptId(String receptId) {
            this.receptId = receptId;
        }

        public String getRecordId() {
            return recordId;
        }

        public void setRecordId(String recordId) {
            this.recordId = recordId;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getTaskContent() {
            return taskContent;
        }

        public void setTaskContent(String taskContent) {
            this.taskContent = taskContent;
        }




        public String getTaskName() {
            return taskName;
        }

        public void setTaskName(String taskName) {
            this.taskName = taskName;
        }


        public String getTaskType() {
            return taskType;
        }

        public void setTaskType(String taskType) {
            this.taskType = taskType;
        }
    }
}
