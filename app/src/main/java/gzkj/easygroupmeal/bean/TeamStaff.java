package gzkj.easygroupmeal.bean;

import java.util.List;

public class TeamStaff {

    /**
     * result : 200
     * resultDesc : 查询成功
     * resultObj : [{"teamId":"93","teamName":"123","teamStaff":[{"postName":"餐厅经理","postType":"CM","schoolZone":"123","userId":"b5b56075-0749-4724-8300-614a3e2316b3","userName":"我是餐厅经理"},{"postName":"班组长","postType":"leader","schoolZone":"123","userId":"a47c4eee-0e96-46ec-9cbb-aa7dbd2a3010","userName":"我是班组长"}]}]
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

    public static class ResultObjBean {
        /**
         * teamId : 93
         * teamName : 123
         * teamStaff : [{"postName":"餐厅经理","postType":"CM","schoolZone":"123","userId":"b5b56075-0749-4724-8300-614a3e2316b3","userName":"我是餐厅经理"},{"postName":"班组长","postType":"leader","schoolZone":"123","userId":"a47c4eee-0e96-46ec-9cbb-aa7dbd2a3010","userName":"我是班组长"}]
         */

        private String teamId;
        private String teamName;
        private List<TeamStaffBean> teamStaff;

        public String getTeamId() {
            return teamId;
        }

        public void setTeamId(String teamId) {
            this.teamId = teamId;
        }

        public String getTeamName() {
            return teamName;
        }

        public void setTeamName(String teamName) {
            this.teamName = teamName;
        }

        public List<TeamStaffBean> getTeamStaff() {
            return teamStaff;
        }

        public void setTeamStaff(List<TeamStaffBean> teamStaff) {
            this.teamStaff = teamStaff;
        }

        public static class TeamStaffBean {
            /**
             * postName : 餐厅经理
             * postType : CM
             * schoolZone : 123
             * userId : b5b56075-0749-4724-8300-614a3e2316b3
             * userName : 我是餐厅经理
             */

            private String postType;
            private String userId;
            private String userName;

            public String getPostType() {
                return postType;
            }

            public void setPostType(String postType) {
                this.postType = postType;
            }


            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }
        }
    }
}
