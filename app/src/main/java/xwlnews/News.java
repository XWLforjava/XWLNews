package xwlnews;

/**
 * Created by xysdd on 2017/9/6.
 */

import javax.ws.rs.client.*;
import javax.ws.rs.core.*;

import net.sf.json.*;

public class News {
    private static String tag[] = {"", "科技", "教育", "军事", "国内", "社会", "文化", "汽车", "国际", "体育", "财经", "健康", "娱乐"};

    private static int nTotalPages, nTotalRecords;

    private String classTag;
    private String ID;
    private String source;
    private String title;
    private String time;
    private String URL;
    private String author;
    private String lang;
    private String pictures;
    private String video;
    private String intro;

    private NewsDetail detail;

    public static String[] getTag() {
        return tag;
    }

    public String getClassTag() {
        return classTag;
    }

    public String getID() {
        return ID;
    }

    public String getSource() {
        return source;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getURL() {
        return URL;
    }

    public String getAuthor() {
        return author;
    }

    public String getLang() {
        return lang;
    }

    public String getPictures() {
        return pictures;
    }

    public String getVideo() {
        return video;
    }

    public String getIntro() {
        return intro;
    }

    public NewsDetail getDetail()
    {
        if (detail == null)
            detail = NewsDetail.fromID(ID);
        return detail;
    }

    public static int totalPages()
    {
        return nTotalPages;
    }
    public static int totalRecords()
    {
        return nTotalRecords;
    }

    public static String Tag(int code)
    {
        return tag[code];
    }

    public static News[] latest(int pageNo, int pageSize)
    {
        return getNewsList("http://166.111.68.66:2042/news/action/query/latest?pageNo="+pageNo+"&pageSize="+pageSize);
    }
    public static News[] latest(int pageNo, int pageSize, int category)
    {
        return getNewsList("http://166.111.68.66:2042/news/action/query/latest?pageNo="+pageNo+"&pageSize="+pageSize+"&category="+category);
    }
    public static News[] search(String keyword, int pageNo, int pageSize)
    {
        return getNewsList("http://166.111.68.66:2042/news/action/query/search?keyword="+keyword+"&pageNo="+pageNo+"&pageSize="+pageSize);
    }
    public static News[] search(String keyword, int pageNo, int pageSize, int category)
    {
        return getNewsList("http://166.111.68.66:2042/news/action/query/search?keyword="+keyword+"&pageNo="+pageNo+"&pageSize="+pageSize+"&category="+category);
    }

    private News(JSONObject json)
    {
        classTag = json.getString("newsClassTag");
        ID = json.getString("news_ID");
        source = json.getString("news_Source");
        title = json.getString("news_Title");
        time = json.getString("news_Time");
        URL = json.getString("news_URL");
        author = json.getString("news_Author");
        lang = json.getString("lang_Type");
        pictures = json.getString("news_Pictures");
        video = json.getString("news_Video");
        intro = json.getString("news_Intro");
    }

    private static News[] getNewsList(String URL)
    {
        Client client = ClientBuilder.newClient();
        Response response = client.target(URL)
                .request(MediaType.TEXT_PLAIN_TYPE)
                .get();
        String s = response.readEntity(String.class);
        try
        {
            JSONObject json = JSONObject.fromObject(s);
            JSONArray list = json.getJSONArray("list");
            int size = list.size();
            News ret[] = new News[size];
            for (int i = 0; i < size; i++)
                ret[i] = new News(list.getJSONObject(i));
            nTotalPages = json.getInt("totalPages");
            nTotalRecords = json.getInt("totalRecords");
            return ret;
        }
        catch(JSONException e)
        {
            return null;
        }
    }
}
