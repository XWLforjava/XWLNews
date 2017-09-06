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

public class NewsDetail {
    private String seggedTitle;

    private NewsDetail(JSONObject json)
    {

    }
    public static NewsDetail fromID(String ID)
    {
        Client client = ClientBuilder.newClient();
        Response response = client.target("http://166.111.68.66:2042/news/action/query/detail?newsId="+ID)
                .request(MediaType.TEXT_PLAIN_TYPE)
                .get();
        String s = response.readEntity(String.class);
        return new NewsDetail(JSONObject.fromObject(s));
    }
}
