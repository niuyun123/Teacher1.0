package com.hanboard.teacher.entity;

/**
 * 项目名称：TeacherHD
 * 创建人：peng.an@hanboard.com
 * 作者单位：四川汉博德信息技术有限公司
 * 创建时间：2016/8/4 0004 14:46
 */
public class TextBook extends Domine {
    public String description;
    public String createTime;
    public String creator;
    public boolean status;
    public String isbn;
    public String title;
    public String sectionId;
    public String sectionName;
    public String subjectId;
    public String subjectName;
    public String suitObjectId;
    public String suitObjectName;
    public String versionId;
    public String versionName;

    @Override
    public String toString() {
        return "TextBook{" +
                "description='" + description + '\'' +
                ", createTime='" + createTime + '\'' +
                ", creator='" + creator + '\'' +
                ", status=" + status +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", sectionId='" + sectionId + '\'' +
                ", sectionName='" + sectionName + '\'' +
                ", subjectId='" + subjectId + '\'' +
                ", subjectName='" + subjectName + '\'' +
                ", suitObjectId='" + suitObjectId + '\'' +
                ", suitObjectName='" + suitObjectName + '\'' +
                ", versionId='" + versionId + '\'' +
                ", versionName='" + versionName + '\'' +
                ", publishDate='" + publishDate + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public String publishDate;
}
