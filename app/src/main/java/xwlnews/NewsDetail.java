package xwlnews;

/**
 * Created by xysdd on 2017/9/6.
 */

import javax.ws.rs.client.*;
import javax.ws.rs.core.*;
import net.sf.json.*;
import java.util.HashMap;

public class NewsDetail {
    private String seggedTitle;
    private String seggedPListOfContent[];
    private HashMap<String, Integer> persons;
    private HashMap<String, Integer> locations;
    private String organizations[];
    private HashMap<String, Double> keywords;
    private int wordCountOfTitle;
    private int wordCountOfContent;
    private HashMap<String, Double> bagOfWords;
    private String classTag;
    private String ID;
    private String category;
    private String source;
    private String title;
    private String inbornKeywords;
    private String time;
    private String URL;
    private String author;
    private String content;
    private String lang;
    private String crawlSource;
    private String journal;
    private String crawlTime;
    private String pictures;
    private String video;
    private String repeatID;

    public String getSeggedTitle() {
        return seggedTitle;
    }

    public String[] getSeggedPListOfContent() {
        return seggedPListOfContent;
    }

    public HashMap<String, Integer> getPersons() {
        return persons;
    }

    public HashMap<String, Integer> getLocations() {
        return locations;
    }

    public String[] getOrganizations() {
        return organizations;
    }

    public HashMap<String, Double> getKeywords() {
        return keywords;
    }

    public int getWordCountOfTitle() {
        return wordCountOfTitle;
    }

    public int getWordCountOfContent() {
        return wordCountOfContent;
    }

    public HashMap<String, Double> getBagOfWords() {
        return bagOfWords;
    }

    public String getClassTag() {
        return classTag;
    }

    public String getID() {
        return ID;
    }

    public String getCategory() {
        return category;
    }

    public String getSource() {
        return source;
    }

    public String getTitle() {
        return title;
    }

    public String getInbornKeywords() {
        return inbornKeywords;
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

    public String getContent() {
        return content;
    }

    public String getLang() {
        return lang;
    }

    public String getCrawlSource() {
        return crawlSource;
    }

    public String getJournal() {
        return journal;
    }

    public String getCrawlTime() {
        return crawlTime;
    }

    public String getPictures() {
        return pictures;
    }

    public String getVideo() {
        return video;
    }

    public String getRepeatID() {
        return repeatID;
    }

    private NewsDetail(JSONObject json)
    {
        JSONArray seggedPlistOfContentArray, personsArray, locationsArray, organizationsArray, keywordsArray, bagOfWordsArray;
        seggedPlistOfContentArray = json.getJSONArray("seggedPListOfContent");
        seggedPListOfContent = new String[seggedPlistOfContentArray.size()];
        for (int i = 0; i < seggedPListOfContent.length; i++)
            seggedPListOfContent[i] = seggedPlistOfContentArray.getString(i);

        personsArray = json.getJSONArray("persons");
        persons = new HashMap<>();
        for (int i = 0; i < personsArray.size(); i++)
        {
            JSONObject t = personsArray.getJSONObject(i);
            persons.put(t.getString("word"), t.getInt("count"));
        }

        locationsArray = json.getJSONArray("locations");
        locations = new HashMap<>();
        for (int i = 0; i < locationsArray.size(); i++)
        {
            JSONObject t = locationsArray.getJSONObject(i);
            locations.put(t.getString("word"), t.getInt("count"));
        }

        organizationsArray = json.getJSONArray("organizations");
        organizations = new String[organizationsArray.size()];
        for (int i = 0; i < organizations.length; i++)
            organizations[i] = organizationsArray.getString(i);

        keywordsArray = json.getJSONArray("Keywords");
        keywords = new HashMap<>();
        for (int i = 0; i < keywordsArray.size(); i++)
        {
            JSONObject t = keywordsArray.getJSONObject(i);
            keywords.put(t.getString("word"), t.getDouble("score"));
        }

        bagOfWordsArray = json.getJSONArray("bagOfWords");
        bagOfWords = new HashMap<>();
        for (int i = 0; i < bagOfWordsArray.size(); i++)
        {
            JSONObject t = bagOfWordsArray.getJSONObject(i);
            bagOfWords.put(t.getString("word"), t.getDouble("score"));
        }

        seggedTitle = json.getString("seggedTitle");
        wordCountOfTitle = json.getInt("wordCountOfTitle");
        wordCountOfContent = json.getInt("wordCountOfContent");
        classTag = json.getString("newsClassTag");
        ID = json.getString("news_ID");
        category = json.getString("news_Category");
        source = json.getString("news_Source");
        title = json.getString("news_Title");
        inbornKeywords = json.getString("inborn_KeyWords");
        time = json.getString("news_Time");
        URL = json.getString("news_URL");
        author = json.getString("news_Author");
        content = json.getString("news_Content");
        lang = json.getString("lang_Type");
        crawlSource = json.getString("crawl_Source");
        journal = json.getString("news_Journal");
        crawlTime = json.getString("crawl_Time");
        pictures = json.getString("news_Pictures");
        video = json.getString("news_Video");
        repeatID = json.getString("repeat_ID");
    }
    static NewsDetail fromID(String ID)
    {
        Client client = ClientBuilder.newClient();
        Response response = client.target("http://166.111.68.66:2042/news/action/query/detail?newsId="+ID)
                .request(MediaType.TEXT_PLAIN_TYPE)
                .get();
        String s = response.readEntity(String.class);
        return new NewsDetail(JSONObject.fromObject(s));
    }
}
