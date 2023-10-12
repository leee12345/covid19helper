package bjfu.it.lihongjing.covid_19helper.bean.news;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import bjfu.it.lihongjing.covid_19helper.bean.news.News;

public class NewsList {
    public int code;
    public String msg;
    @SerializedName("newslist")
    public List<News> newsList ;
}


