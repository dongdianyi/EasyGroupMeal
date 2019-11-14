package gzkj.easygroupmeal.bean;

public class MemberJson {
    private String postType;
    private String flag;
    private String userName;
    private String telnum;
    private String schoolZone;
    private String job;

    public MemberJson(String flag,String postType, String userName, String telnum, String schoolZone, String job) {
        this.flag = flag;
        this.postType = postType;
        this.userName = userName;
        this.telnum = telnum;
        this.schoolZone = schoolZone;
        this.job = job;
    }

    public MemberJson(String postType, String userName, String telnum, String schoolZone, String job) {
        this.postType = postType;
        this.userName = userName;
        this.telnum = telnum;
        this.schoolZone = schoolZone;
        this.job = job;
    }

    public MemberJson(String flag, String postType, String userName, String telnum) {
        this.flag = flag;
        this.postType = postType;
        this.userName = userName;
        this.telnum = telnum;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getSchoolZone() {
        return schoolZone;
    }

    public void setSchoolZone(String schoolZone) {
        this.schoolZone = schoolZone;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

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

    public String getTelnum() {
        return telnum;
    }

    public void setTelnum(String telnum) {
        this.telnum = telnum;
    }
}
