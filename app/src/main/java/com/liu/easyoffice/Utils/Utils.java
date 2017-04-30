package com.liu.easyoffice.Utils;

/**
 * Created by hui on 2016/9/21.
 */

public class Utils {
    //    public static final String URL = "http://192.168.31.198:8080/";    //宿舍服务器
//    public static final String URL = "http://10.40.5.51:8080/";        //鸡哥服务器
//    public static final String URL = "http://10.40.5.27:8080/";        //我服务器
    public static final String URL = "http://192.168.1.108:8080/";     //腾讯服务器

    //public static final String URL="http://10.40.5.54:8080/";
    //public static final String URL_LGY = "http://10.40.5.49:8080/eoproject/";
    //public static final String URL="http://192.168.31.149:8080/";

    public static final String XUTIL_URL = URL + "Xutils/";
    public static final String UPLOAD_IMG = XUTIL_URL + "/upload/";
    public static final String IMG_URL = UPLOAD_IMG;
    public static final String LOGIN_URL = XUTIL_URL + "login";
    public static final String CONTACTS_URL = XUTIL_URL + "contacts";
    public static final String SEARCH_FRIEND_URL = XUTIL_URL + "searchfriend";
    public static final String GET_COMPANY = XUTIL_URL + "getcompany";//获取公司信息
    public static final String GET_GROUP_MEMBER = XUTIL_URL + "getmembergroup";
    public static final String GET_GROUP_CURRENT_ID = XUTIL_URL + "getgroupcurrentid";
    public static final String GET_GROUPS = XUTIL_URL + "getcurrentgroup";
    public static final String GET_ALL_GROUPS = XUTIL_URL + "GetAllGroup";//获取所有部门的所有信息
    public static final String CREATE_CHAT_GROUP_ID = XUTIL_URL + "ChatGroupManager";//发送讨论组的id到服务器；
    public static final String GET_CHAT_GROUP = XUTIL_URL + "GetAllChatGroup";//获取当前所有讨论组；
    public static final String REGIST_MESSAGE = XUTIL_URL + "MessageSend";//注册验证码
    public static final String REGIST = XUTIL_URL + "Regist";
    public static final String TELEXIST = XUTIL_URL + "TelExist";
    public static final String NEWPASSWORD = XUTIL_URL + "ForgetPassword";
    public static final String CONTACT_CARD = XUTIL_URL + "ContactCard";
    public static final String GET_CURRENT_CHATGROUP_USERS = XUTIL_URL + "GetCurrentChatGroup";//获取当前所有讨论组；
    public static final String MANAGER_GROUP = XUTIL_URL + "ManageGroup";//添加好友/添加子部门，修改部门信息
    public static final String PHONE_CONTACTS = XUTIL_URL + "TelContacts";//将本地联系人传到服务器进行匹配；
    public static final String GET_PARENTGROUP_LEADER = XUTIL_URL + "GetParentGroupAndLeader";//获取当前group的父group信息，以及当前团队的leader
    public static final String GET_ALL_ANNOUNCEMENT = XUTIL_URL + "servlet/GetAllAnnouncement";//获取所有公告
    public static final String CREATE_COMPANY = XUTIL_URL + "servlet/CreateCompany";//创建公司
    public static final String SEND_ANNOUNCEMENT = XUTIL_URL + "servlet/SendAnnouncement";
    public static final String UPDATE_USER = XUTIL_URL + "UpdateUser";//更新用户信息
    public static final String MY_COMPANY = XUTIL_URL + "servlet/MyCompany";//我的公司

    //签到模块的servlet
    public static final String SEND_SETTING_INFO = XUTIL_URL + "receiversettinginfo"; //发送设置信息到服务器；
    public static final String QUERY_SETTING_INFO = XUTIL_URL + "querysettinginfobycompanyid";  //普通员工根据公司Id,查询公司的考勤制度
    public static final String INSERT_SIGNIN_INFO = XUTIL_URL + "insertsigninoffinfo";  //插入签到数据到服务器，为以后的查询做准备
    public static final String INSERT_SIGNOFF_INFO = XUTIL_URL + "insertsignoffinfo";  //插入签退数据到服务器，为以后的查询做准备
    public static final String QUERY_SIGN_TIME = XUTIL_URL + "querysignontime";  //根据员工Id和签到日期查询服务器端的签到时间
    public static final String QUERY_SIGN_STATUS = XUTIL_URL + "querysignstatus";  //根据companyId、日期的查询员工的签到签退状态
    public static final String QUERY_WORK_TIMES = XUTIL_URL + "queryworktimes";  //根据companyId、月份查询员工的工作总时长并且排序
    public static final String QUERY_HARDWORKINGCHECKING = XUTIL_URL + "queryhardworkingchecking";  //根据companyId和日期所选 以公司员工的工作时长要大于上榜时长，并且降序排列
    public static final String QUERY_HARDWORKINGTIME = XUTIL_URL + "queryhardworkingtime";  //根据companyId和日期所选 以公司员工的工作时长要大于上榜时长，并且降序排列
    public static final String QUERY_LATETIME = XUTIL_URL + "querylatetime";  //根据companyId和日期所选 查询signinandoffinfo的迟到字段，并且降序排列
    public static final String QUERY_PERSON_CHECKING_INFO = XUTIL_URL + "querypersoncheckinginfo";  //根据companyId和年月以及employeeId查询个人考勤的相关信息(本月平均工时、正常、早退、迟到、旷工、请假、出差)
    public static final String QUERY_PERSON_IMAGE = XUTIL_URL + "querypersonportrait";  //根据员工Id查询出员工user对象，然后获取其头像
    public static final String QUERY_SIGNiNANDOFFINFO = XUTIL_URL + "querysigninandoffinfo";  //根据员工Id、公司Id以及当前日期查询出是否有签到或者签退信息
    public static final String SHOW_SIGNSETTINGINFO = XUTIL_URL + "showsignsetttinginfo";  //根据员工的Id显示其公司设定的签到信息


    public static class Receiver {
        public static final String CHANGE_TILE = "改聊天标题";
        public static final String UPDATE_CHATGROUP_LIST = "更新讨论组列表";
        public static final String ADD_CHILD_GROUP = "添加部门";
        public static final String ADD_MEMBER = "添加成员";
    }

    public static final int APPROVE_LEAVE = 1;
    public static final int APPROVE_TRIP = 2;
    public static final int APPROVE_MONEY = 3;
    public static final int APPROVE_LATE = 4;

    public static final int LEAVE_SHIJIA = 1;
    public static final int LEAVE_BINGJIA = 2;
    public static final int LEAVE_NIANJIA = 3;
    public static final int LEAVE_DIAOXIUJIA = 4;
    public static final int LEAVE_HUNJIA = 5;
    public static final int LEAVE_CHANJIA = 6;
    public static final int LEAVE_LUTUJIA = 7;
    public static final int LEAVE_QITA = 8;


}
