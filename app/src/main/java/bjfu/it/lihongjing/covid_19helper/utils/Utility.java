package bjfu.it.lihongjing.covid_19helper.utils;

import com.google.gson.Gson;

import bjfu.it.lihongjing.covid_19helper.bean.news.NewsList;

public class Utility {
    public static NewsList parseJsonWithGson(final String requestText){
        Gson gson = new Gson();
        return gson.fromJson(requestText, NewsList.class);
    }

}
