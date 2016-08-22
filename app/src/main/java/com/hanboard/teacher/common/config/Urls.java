package com.hanboard.teacher.common.config;

/**
 * 项目名称：TeacherHD
 * 类描述：
 * 创建人：peng.an@hanboard.com
 * 作者单位：四川汉博德信息技术有限公司
 * 创建时间：2016/8/9 0009 16:44
 */
public class Urls {
    /**host*/
    public static final String SERVER_HOST = "http://110.186.73.233";
    /*port*/
    public static final String SERVER_PORT = "2009/hanboardApp/";
    /**SERVER_ADDRESS*/
    public static final String SERVER_ADDRESS = SERVER_HOST+":"+SERVER_PORT;
    /** 登录接口 */
    public static final String URL_LOGIN = SERVER_ADDRESS+"api/v1/user/login";
    /**获取所有科目*/
    public static final String URL_ALLSUBJECT = SERVER_ADDRESS+"api/v1/subject/queryAllsubject";
    /**获取所有学段*/
    public static final String URL_ALLSECTION = SERVER_ADDRESS+"api/v1/section/querySection";
    /**获取年级*/
    public static final String URL_ALLSUITOBJECT = SERVER_ADDRESS+"api/v1/suitObject/querySuitObject";
    /**获取教材*/
    public static final String URL_ALLTEXTBOOK = SERVER_ADDRESS+"api/v1/teachbook/queryTeachbook";
    /*教师添加教材*/
    public static final String URL_ADDTEXTBOOK = SERVER_ADDRESS+"api/v1/teachbookaccount/addTeachBookAccount";
    /**根据账户获取教材*/
    public static final String URL_GETCHAPTER = SERVER_ADDRESS+"api/v1/teachbookaccount/queryAllBookChapterByAccountId";
    /**通过章节id 账号id 课本id获取该章节下所有备课信息*/
    public static final String URL_GETLESSONS = SERVER_ADDRESS+"api/v1/chaptercontent/findByChapterId";
    /** App更新地址 */
    public static final String UPDATE_URL = SERVER_ADDRESS+"api/v1/publicfunction/versionupdate";
    /**
     * 获取授课课本信息
     */
    public static  final  String URL_GETPREPARECOUSER=SERVER_ADDRESS+"api/v1/teachbookaccount/queryAllBookByAccountId";
    /**
     * 获取个人全部教材课本及其章节
     */
    public static  final  String URL_GETAllCURSOR$CHAPTER=SERVER_ADDRESS+"api/v1/chapter/queryChapter";
    /**
     * 通过章节id 账号id 课本id获取该章节下所有备课信息
     */
    public static  final  String URL_GETPREPARECHATER=SERVER_ADDRESS+ "api/v1/chaptercontent/findByChapterId";
    /**
     * 添加备课
     */
    public static  final  String URL_ADDLESSONSINFO=SERVER_ADDRESS+ "api/v1/content/addContentInfo";
    /**
     *通过备课信息ID获取备课信息详细信息
     */
    public static  final  String URL_GETPRECOURSEWARE=SERVER_ADDRESS+"api/v1/content/findInfoByContentId";
    /**获取app——banner*/
    public static final String URL_GETBANNER = SERVER_ADDRESS+"api/v1/banner/andriodPadQueryBanner";
    /**
     * 获取最近备课信息
     */
    public static final String URL_NEWCOURSE = SERVER_ADDRESS+"api/v1/content/newlyCourse";
    /*删除备课信息*/
    public static final String URL_DELETELESSONS = SERVER_ADDRESS+"api/v1/content/deleteContent";
    /*删除教材*/
    public static final String URL_DELETETEXTBOOK = SERVER_ADDRESS+"api/v1/teachbookaccount/deleteTeachBookByAccountId";
    /**
     * 获取用户信息
     */
    public static final String URL_GETUSERINFO = SERVER_ADDRESS + "api/v1/user/getUserInfoById";
    /*验证原密码*/
    public static final String URL_CHECK_OLD_PWD = SERVER_ADDRESS + "api/v1/user/checkOldPwd";
    /*修改密码*/
    public static final String URL_UPDATE_PWD = SERVER_ADDRESS + "api/v1/user/updatePwd";
    /*修改电话*/
    public static final String URL_UPDATE_TEL = SERVER_ADDRESS + "api/v1/user/updateAccountInfo";
    /*修改邮箱*/
    public static final String URL_UPDATE_EMAIL = SERVER_ADDRESS + "api/v1/user/updateAccountInfo";
    /**
     * 修改姓名
     */
    public static final String URL_UPDATE_NAME = SERVER_ADDRESS + "api/v1/user/updateAccountInfo";
    /*修改昵称*/
    public static final String URL_UPDATE_NICKNAME = SERVER_ADDRESS + "api/v1/user/updateAccountInfo";
    /*上传头像*/
    public static final String URL_USERICON = SERVER_ADDRESS + "api/v1/user/uploadAvatarUrl";
    /*上传建议*/
    public static final String URL_SUGESTION= SERVER_ADDRESS + "api/v1/publicfunction/addOpinion";
    /*上传备课无文件*/
    public static final String URL_ADDLESSONSINFO_NOTFILE = SERVER_ADDRESS + "api/v1/content/addContentInfoNotFile";
}