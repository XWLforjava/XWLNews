package com.java.two.bean;

/**
 * Created by xysdd on 2017/9/11.
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TsinghuaNewsDetail {
    @JsonProperty("newsClassTag")
    public String classTag;
    @JsonProperty("news_ID")
    public String id;
    @JsonProperty("news_Category")
    public String category;
    @JsonProperty("news_Source")
    public String source;
    @JsonProperty("news_Title")
    public String title;
    @JsonProperty("inborn_Keywords")
    public String inbornKeywords;
    @JsonProperty("news_Time")
    public String time;
    @JsonProperty("news_URL")
    public String url;
    @JsonProperty("news_Author")
    public String author;
    @JsonProperty("news_Content")
    public String content;
    @JsonProperty("lang_Type")
    public String lang;
    @JsonProperty("crawl_Source")
    public String crawlSource;
    @JsonProperty("news_Journal")
    public String journal;
    @JsonProperty("crawl_Type")
    public String crawlType;
    @JsonProperty("news_Pictures")
    public String pictures;
    @JsonProperty("news_Video")
    public String video;
    @JsonProperty("repeat_ID")
    public String repeatID;

    @JsonProperty("seggedTitle")
    public String seggedTitle;
    @JsonProperty("seggedPListOfContent")
    public List<String> seggedPListOfContent;
    @JsonProperty("wordCountOfTitle")
    public int wordCountOfTitle;
    @JsonProperty("wordCountOfContent")
    public int wordCountOfContent;

    @JsonProperty("persons")
    public List<WordCount> persons;
    @JsonProperty("locations")
    public List<WordCount> locations;
    @JsonProperty("organizations")
    public List<WordCount> organizations;

    @JsonProperty("Keywords")
    public List<WordScore> keywords;
    @JsonProperty("bagOfWords")
    public List<WordScore> bagOfWords;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WordCount {
        @JsonProperty("word")
        public String word;
        @JsonProperty("count")
        public int count;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WordScore {
        @JsonProperty("word")
        public String word;
        @JsonProperty("score")
        public double score;
    }

    /*
    {
    "seggedTitle":"...",<!--新闻分词后标题结果-->
    "seggedPListOfContent":[<!--新闻正文的分词结果-->
        "..."
    ],
    "persons":[  <!-- 人物列表 -->
        {
        "word":"弗拉基米尔",
        "count":2
        }
    ] ,
    "locations":[<!-- 地点列表 -->
    {
    "word":"里约",
    "count":2
    }
    ],"organizations":[<!--组织机构列表-->
    ],"Keywords":[ <!--  新闻中关键词列表 -->
        {
        "word":"柔道",
        "score":284.757301873237
        }
    ],
    "wordCountOfTitle":11,
    "wordCountOfContent":226,
    "bagOfWords":[
        {
        "word":"参赛",
        "score":1.0
        }
    ],
    "newsClassTag":"体育", <!--新闻所属的分类-->
    "news_ID":"201608090432c815a85453c34d8ca43a591258701e9b",<!-- 新闻ID -->
    "news_Category":"首页 > 新闻 > 环球扫描 > 正文", <!-- 新闻的类别 -->
    "news_Source":"其他", <!-- 来源 -->
    "news_Title":"德媒：俄柔道运动员里约夺金与普京密切相关", <!-- 标题 -->
    "inborn_KeyWords":"", <!--新闻关键词-->
    "news_Time":"Aug 8, 2016 12:00:00 AM",<!-- 时间 -->
    "news_URL":"http://news.21cn.com/world/guojisaomiao/a/2016/0808/15/31396661.shtml",<!-- 新闻的URL链接 -->
    "news_Author":"环球网|",<!--新闻的作者-->
    "news_Content":"...",<!-- 新闻正文 -->
    "lang_Type":"zh-CN",<!--新闻的语言类型-->
    "crawl_Source":"news.21cn.com",<!-- 爬取来源 -->
    "news_Journal":"翟潞曼",<!--记者列表-->
    "crawl_Time":"Aug 9, 2016 3:43:17 AM",<!-- 爬取时间 -->
    "news_Pictures":"http://img003.21cnimg.com/photos/album/20160808/m600/A3B78A702DF9BF0EE02ADFD5D4F53D54.jpeg",<!--新闻的图片路径-->
    "news_Video":"",
    "repeat_ID":"0"<!--与该条新闻重复的新闻ID-->
    }
     */
}
