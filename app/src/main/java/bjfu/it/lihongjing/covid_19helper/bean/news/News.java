package bjfu.it.lihongjing.covid_19helper.bean.news;

import com.google.gson.annotations.SerializedName;

public class News {
    @SerializedName("ctime")
    public String time;
    public String title;
    public String description;
    public String picUrl;
    public String url;
    public String source;
}
