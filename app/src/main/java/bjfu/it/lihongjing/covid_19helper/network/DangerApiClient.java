package bjfu.it.lihongjing.covid_19helper.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DangerApiClient {
    private static final String BASE_URL="https://file1.dxycdn.com";
    private static DangerApiClient dangerApiClient;
    private static Retrofit retrofit;
    private DangerApiClient(){
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

    }
    public static synchronized DangerApiClient getInstance(){
        if(dangerApiClient == null){
            dangerApiClient = new DangerApiClient();
        }
        return dangerApiClient;
    }
    public DangerApiService getApi(){
        return retrofit.create(DangerApiService.class);
    }
}
