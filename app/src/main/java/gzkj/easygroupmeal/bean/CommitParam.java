package gzkj.easygroupmeal.bean;

import android.util.Base64;

import com.google.gson.Gson;

import java.io.Serializable;

import gzkj.easygroupmeal.utli.MD5Util;
import okhttp3.RequestBody;

/**
 * Created by ddy on 2018/3/27
 * 传参数设置
 */
public class CommitParam implements Serializable {

    private String mobile;
    private String pwd;
    private String randCode;
    private String userName;
    private String postType;
    private String teamName;
    private String schoolZone;
    private String otherName;
    private String companyName;
    private String longitude;
    private String latitude;
    private String province;
    private String city;
    private String area;
    private String address;
    private String memberJson;
    private String companyId;
    private String businessCode;
    private String companyScore;
    private String teamId;
    private String userType;
    private String workUnit;
    private String imageUrl;
    private String content;
    private String taskStatus;
    private String recordId;
    private String errorReason;
    private String taskId;
    private String taskCycle;
    private String taskRemind;
    private String taskJson;
    private String receptId;
    private String taskType;
    private String isWarn;
    private String startTime;
    private String endTime;
    private String taskContent;
    private String taskName;
    private String type;
    private String submitId;
    private String messageId;
    private String job;
    private String head_image;
    private String image;
    private int verfityId;
    private String resultType;
    private String userId;
    private String cause;
    private String date;
    private String clockId;
    private String id;
    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setVerfityId(int verfityId) {
        this.verfityId = verfityId;
    }


    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public void setHead_image(String head_image) {
        this.head_image = head_image;
    }


    public void setJob(String job) {
        this.job = job;
    }


    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }


    public void setSubmitId(String submitId) {
        this.submitId = submitId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public void setIsWarn(String isWarn) {
        this.isWarn = isWarn;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }


    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


    public void setTaskContent(String taskContent) {
        this.taskContent = taskContent;
    }


    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }


    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }


    public void setReceptId(String receptId) {
        this.receptId = receptId;
    }


    public void setTaskCycle(String taskCycle) {
        this.taskCycle = taskCycle;
    }


    public void setTaskRemind(String taskRemind) {
        this.taskRemind = taskRemind;
    }


    public void setTaskJson(String taskJson) {
        this.taskJson = taskJson;
    }


    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }


    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }


    public void setErrorReason(String errorReason) {
        this.errorReason = errorReason;
    }


    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public void setWorkUnit(String workUnit) {
        this.workUnit = workUnit;
    }


    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }


    public void setUserType(String userType) {
        this.userType = userType;
    }


    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }


    public void setCompanyScore(String companyScore) {
        this.companyScore = companyScore;
    }


    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }


    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }


    public void setSchoolZone(String schoolZone) {
        this.schoolZone = schoolZone;
    }


    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }


    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }


    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }


    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }


    public void setProvince(String province) {
        this.province = province;
    }


    public void setCity(String city) {
        this.city = city;
    }


    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public void setMemberJson(String memberJson) {
        this.memberJson = memberJson;
    }


    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public void setPwd(String pwd) {
        this.pwd = MD5Util.md5(pwd);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }


    public void setRandCode(String randCode) {
        this.randCode = randCode;
    }

    public RequestBody getBody(String sid,String adress,CommitParam commitParam){
        String obj=new Gson().toJson(commitParam);//传参转json
        Bean bean=new Bean(sid+"$"+MD5Util.md5(adress+sid+MD5Util.md5(Base64.encodeToString(obj.getBytes(), Base64.NO_WRAP)).toUpperCase()).toUpperCase(),
                "1",
                Base64.encodeToString(obj.getBytes(), Base64.NO_WRAP),
                MD5Util.md5(Base64.encodeToString(obj.getBytes(), Base64.NO_WRAP)).toUpperCase());
        String obj2=new Gson().toJson(bean);//整体转json
        return RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj2);
    }
}
