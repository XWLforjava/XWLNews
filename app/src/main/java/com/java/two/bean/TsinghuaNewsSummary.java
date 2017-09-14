package com.java.two.bean;

/**
 * Created by xysdd on 2017/9/11.
 */
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TsinghuaNewsSummary implements Serializable {
    @JsonProperty("newsClassTag")
    public String classTag;
    @JsonProperty("news_ID")
    public String id;
    @JsonProperty("news_Source")
    public String source;
    @JsonProperty("news_Title")
    public String title;
    @JsonProperty("news_Time")
    public String time;
    @JsonProperty("news_URL")
    public String url;
    @JsonProperty("news_Author")
    public String author;
    @JsonProperty("lang_Type")
    public String lang;
    @JsonProperty("news_Pictures")
    public String pictures;
    @JsonProperty("news_Video")
    public String video;
    @JsonProperty("news_Intro")
    public String intro;
    /*
    {
    "newsClassTag":"科技",<!--新闻所属的分类-->
    "news_ID":"201608100421f2d8cf63b03d431eb847d4b3e7af8f24", <!-- 新闻id-->
    "news_Source":"其他",<!-- 新闻来源 -->
    "news_Title":"Prisma爆红这么久 现在才有中国追随者",<!--标题 -->
    "news_Time":"Aug 9, 2016 12:00:00 AM",<!--时间 -->
    "news_URL":"http://tech.qianlong.com/2016/0809/819245.shtml",<!--新闻的URL链接 -->
    "news_Author":"第一财经日报",<!--新闻的作者-->
    "lang_Type":"zh-CN",<!--语言类型 -->
    "news_Pictures":"http://upload.qianlong.com/2016/0809/1470711910844.jpg",<!--新闻的图片路径-->
    "news_Video":"",
    "news_Intro":"[对于大多数Prisma用户而言，最大的不满依然来自于图片处理的时间太长，一..."<!-- 简介 -->
    }
     */

    public boolean hasread = false;
}
