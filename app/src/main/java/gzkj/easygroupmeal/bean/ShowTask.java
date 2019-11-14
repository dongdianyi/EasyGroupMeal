package gzkj.easygroupmeal.bean;

import java.util.List;

public class ShowTask {

    /**
     * result : 200
     * resultDesc : 查询成功
     * resultObj : {"staffList":[{"teamName":"123","postType":"CM","teamId":93,"userName":"我是餐厅经理","userId":"b5b56075-0749-4724-8300-614a3e2316b3"}],"taskList":[{"fid":"0","taskName":"开创粗加工","taskId":1},{"fid":"0","taskName":"开创切配","taskId":2},{"fid":"0","taskName":"开创保洁","taskId":3},{"fid":"0","taskName":"开创面点","taskId":4},{"fid":"0","taskName":"开创烹饪","taskId":5},{"fid":"0","taskName":"开创保管","taskId":6},{"fid":"0","taskContent":"","taskName":"报业粗加工","taskId":8},{"fid":"0","taskName":"报业切配","taskId":9},{"fid":"0","taskName":"报业保洁","taskId":10},{"fid":"0","taskName":"报业面点","taskId":11},{"fid":"0","taskName":"报业烹饪","taskId":12},{"fid":"0","taskName":"报业保管","taskId":13},{"fid":"0","taskName":"报业洗消","taskId":14},{"fid":"0","taskName":"山财保管","taskId":17},{"fid":"0","taskName":"太古保管","taskId":20},{"fid":"0","taskName":"英才保管","taskId":21},{"fid":"0","taskName":"英才切配","taskId":22},{"fid":"0","taskName":"英才面点","taskId":23},{"fid":"0","taskName":"英才烹饪","taskId":24},{"fid":"0","taskName":"英才保洁","taskId":25},{"fid":"0","taskName":"英才洗消","taskId":26},{"fid":"0","taskName":"鲁能保管","taskId":28}]}
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
        private List<StaffListBean> staffList;
        private List<TaskListBean> taskList;

        public List<StaffListBean> getStaffList() {
            return staffList;
        }

        public void setStaffList(List<StaffListBean> staffList) {
            this.staffList = staffList;
        }

        public List<TaskListBean> getTaskList() {
            return taskList;
        }

        public void setTaskList(List<TaskListBean> taskList) {
            this.taskList = taskList;
        }

        public static class StaffListBean {
            /**
             * teamName : 123
             * postType : CM
             * teamId : 93
             * userName : 我是餐厅经理
             * userId : b5b56075-0749-4724-8300-614a3e2316b3
             */

            private String postType;
            private String userName;
            private String userId;


            public String getPostType() {
                return postType;
            }

            public void setPostType(String postType) {
                this.postType = postType;
            }


            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }
        }

        public static class TaskListBean {
            /**
             * fid : 0
             * taskName : 开创粗加工
             * taskId : 1
             * taskContent :
             */

            private String taskName;
            private int taskId;


            public String getTaskName() {
                return taskName;
            }

            public void setTaskName(String taskName) {
                this.taskName = taskName;
            }

            public int getTaskId() {
                return taskId;
            }

            public void setTaskId(int taskId) {
                this.taskId = taskId;
            }

        }
    }
}
