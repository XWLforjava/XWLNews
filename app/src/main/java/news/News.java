package news;

/**
 * Created by xysdd on 2017/9/6.
 */

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;


public class News {
    private String classTag;
    private String id;
    private String source;
    private String title;
    private String time;
    private String URL;
    private String author;
    private String lang;
    private String pictures;
    private String video;
    private String intro;

    private News(JSONObject json)
    {
        classTag = json.getString("newsClassTag");
        id = json.getString("news_ID");
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

    public String getId()
    {
        return id;
    }
    public String getTitle()
    {
        return title;
    }
    public String getIntro()
    {
        return intro;
    }
    public String getSource()
    {
        return source;
    }
    public String getAuthor()
    {
        return author;
    }
    public String getURL()
    {
        return URL;
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
            return ret;
        }
        catch(JSONException e)
        {
            return null;
        }
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
}
