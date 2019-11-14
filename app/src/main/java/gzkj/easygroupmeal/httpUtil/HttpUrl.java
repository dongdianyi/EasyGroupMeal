package gzkj.easygroupmeal.httpUtil;

/**
 * Created by ddy on 2018/5/23.
 * 一些固定的值和网址
 * 打包签名：84cd95a1dfd928bd4d65adecfde71f02
 * 未打包签名：f1e6d4f9c81c1c1080fe59b624bd90f5
 */


public interface HttpUrl {
    //base64编码
    // String strBase64 =  Base64.encodeToString(obj.getBytes(), Base64.NO_WRAP),
    // base64解码
    // String str2 = new String(Base64.decode(strBase64.getBytes(), Base64.NO_WRAP));

    //企查查接口
    String QCC_URL="http://api.qichacha.com/";
    String QCC_KEY="aaa34e791e4541aeb5bd6aec9c3309f0";
    String QCC_SECRETKEY="C0924B8769A3B92C8BD2802A0A3DBD59";
//    String BASE_URL="http://192.168.1.110:8080/";
    String BASE_URL="http://www.qiaobaikeji.com/goods_user/";
    //用户协议
    String AGREEMENT_URL="http://www.qiaobaikeji.com/agree.html";
    //登录
    String LOGIN_URL="android/1.0/USR1002/1.0";
    //退出登录
    String SIGNOUT_URL="android/1.0/USR1016/1.0";
    //注册
    String REGISTER_URL="android/1.0/USR1000/1.0";
    //忘记密码
    String FORGETPWD_URL="android/1.0/USR1010/1.0";
    //验证码
    String CODE_URL="android/1.0/USR1001/1.0";
    //修改用户信息
    String MODIFYINFOR_URL="android/1.0/USR1003/1.0";
    //上传图片
    String AVATAE_URL="file/saveFile";
    //公司名模糊查询
    String QUERYCOMPANY_URL="android/1.0/COM1003/1.0";
    //团队名模糊查询
    String QUERYTEAM_URL="android/1.0/TEM1003/1.0";
    //成员加入已有公司
    String JOINCOMPANY_URL="android/1.0/COM1001/1.0";
    //成员加入已有团队
    String JOINTEAM_URL="android/1.0/TEM1002/1.0";
    //创建公司
    String CREATCOMPANY_URL="android/1.0/COM1000/1.0";
    //创建团队
    String CREATTEAM_URL="android/1.0/TEM1000/1.0";
    //甲方认证
    String AUTHENTICATION_URL="android/1.0/USR1007/1.0";
    //用户查询
    String USERINFO_URL="android/1.0/USR1006/1.0";
    //添加公告
    String ADDNOTICE_URL="android/1.0/USR1008/1.0";
    //查询公告
    String QUERYNOTICE_URL="android/1.0/USR1009/1.0";
    //紧急任务
    String URGENTTASK_URL="android/1.0/TAK1004/1.0";
    //派发任务
    String GENERALTASK_URL="android/1.0/TAK1001/1.0";
    //派发任务员工及一级任务展示接口
    String SHOWTASK_URL="android/1.0/TAK1011/1.0";
    //二级任务模板展示接口
    String TEMPLATETASK_URL="android/1.0/TAK1009/1.0";
    //个人任务列表接口
    String PERSONALTASK_URL="android/1.0/TAK1005/1.0";
    //日程个人任务列表接口
    String SCHEDULETASK_URL="android/1.0/TAK1006/1.0";
    //员工任务列表接口
    String TEAMTASK_URL="android/1.0/TAK1010/1.0";
    //提交任务
    String SUBMITTASK_URL="android/1.0/TAK1002/1.0";
    //未完成备注
    String NOCOMPLETE_URL="android/1.0/TAK1018/1.0";
    //任务修改
    String UPDATETASK_URL="android/1.0/TAK1000/1.0";
    //修改任务状态
    String UPDATESTATETASK_URL="android/1.0/TAK1007/1.0";
    //任务删除
    String DELETETASK_URL="android/1.0/TAK1012/1.0";
    //查询所有团队及成员
    String TEAM_URL="android/1.0/COM1009/1.0";
    //查询公司下所有团队信息
    String TEAMUSER_URL="android/1.0/COM1008/1.0";
    //团队添加成员
    String ADDSTAFF_URL="android/1.0/TEM1001/1.0";
    //团队添加甲方
    String ADDCHARGE_URL="android/1.0/USR1011/1.0";
    //创建公司后，加入成员接口
    String ADDSTAFF2_URL="android/1.0/COM1005/1.0";
    //消息列表
    String MESSAGE_URL="android/1.0/TAK1014/1.0";
    //消息删除
    String MESSAGEDEL_URL="android/1.0/TAK1016/1.0";
    //校区
    String SCHOOL_URL="android/1.0/TEM1007/1.0";
    //申请列表
    String APPLYLIST_URL="android/1.0/USR1012/1.0";
    //申请操作
    String APPLY_URL="android/1.0/USR1013/1.0";
    //红点
    String APPLYREDDOT_URL="android/1.0/USR1005/1.0";
    //删除成员
    String DELETESTAFF_URL="android/1.0/USR1015/1.0";
    //添加不打卡日期
    String ADDNOPTC_URL="android/1.0/ATT1001/1.0";
    //删除不打卡日期
    String DELETENOPTC_URL="android/1.0/ATT1002/1.0";
    //不打卡日期查询
    String QUREYNOPTC_URL="android/1.0/ATT1003/1.0";
    //提交请假申请
    String LEAVE_URL="android/1.0/ATT1000/1.0";
    //我的请假记录
    String MYLEAVE_URL="android/1.0/ATT1004/1.0";
    //请假申请列表
    String LEAVEAUDIT_URL="android/1.0/ATT1005/1.0";
    //请假操作
    String LEAVEOPERATION_URL="android/1.0/ATT1006/1.0";

    String LEAVEOPERATIONINTERFACE="ATT1006";
    String LEAVEINTERFACE="ATT1000";
    String MYLEAVEINTERFACE="ATT1004";
    String LEAVEAUDITINTERFACE="ATT1005";
    String QUREYNOPICINTERFACE="ATT1003";
    String ADDNOPTCINTERFACE="ATT1001";
    String DELETENOPTCINTERFACE="ATT1002";
    String LOGININTERFACE="USR1002";
    String SIGNOUTINTERFACE="USR1016";
    String REGISTERINTERFACE="USR1000";
    String FORGETPWDINTERFACE="USR1010";
    String CODEINTERFACE="USR1001";
    String MODIFYINFORINTERFACE="USR1003";
    String QUERYCOMPANYINTERFACE="COM1003";
    String QUERYTEAMINTERFACE="TEM1003";
    String JOINCOMPANYINTERFACE="COM1001";
    String JOINTEAMINTERFACE="TEM1002";
    String CREATCOMPANYINTERFACE="COM1000";
    String CREATTEAMINTERFACE="TEM1000";
    String AUTHENTICATIONINTERFACE="USR1007";
    String USERINFOINTERFACE="USR1006";
    String ADDNOTICEINTERFACE="USR1008";
    String QUERYNOTICEINTERFACE="USR1009";
    String URGENTTASKINTERFACE="TAK1004";
    String GENERALTASKINTERFACE="TAK1001";
    String SHOWTASKINTERFACE="TAK1011";
    String TEMPLATETASKINTERFACE="TAK1009";
    String PERSONALTASKINTERFACE="TAK1005";
    String SUBMITTASKINTERFACE="TAK1002";
    String TEAMTASKINTERFACE="TAK1010";
    String TEAMINTERFACE="COM1009";
    String UPDATETASKINTERFACE="TAK1000";
    String DELETETASKINTERFACE="TAK1012";
    String UPDATESTATETASKINTERFACE="TAK1007";
    String TEAMUSERINTERFACE="COM1008";
    String ADDSTAFFINTERFACE="TEM1001";
    String ADDCHARGEINTERFACE="USR1011";
    String ADDSTAFF2INTERFACE="COM1005";
    String MESSAGEINTERFACE="TAK1014";
    String MESSAGEDELINTERFACE="TAK1016";
    String SCHEDULETASKINTERFACE="TAK1006";
    String SCHOOLINTERFACE="TEM1005";
    String APPLYLISTINTERFACE="USR1012";
    String APPLYINTERFACE="USR1013";
    String APPLYREDDOTINTERFACE="USR1005";
    String DELETESTAFFINTERFACE="USR1015";
    String NOCOMPLETE="TAK1018";

    // GM:总监 CM:餐厅经理 leader:班组长 partA:甲方
    String[]POSITION={"GM","CM","leader","partA"};
    String PHONENUM="0531-55505245";

    String STATUSZERO="0";
    String STATUSONE="1";
    String STATUSTWO="2";

}
