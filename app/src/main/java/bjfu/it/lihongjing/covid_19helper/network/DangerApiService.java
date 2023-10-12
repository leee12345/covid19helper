package bjfu.it.lihongjing.covid_19helper.network;

import bjfu.it.lihongjing.covid_19helper.bean.dangerLevel.DangerLevelInfo;
import retrofit2.Call;
import retrofit2.http.GET;

public interface DangerApiService {
    @GET("/2021/0202/196/1680100273140422643-135.json")
    Call<DangerLevelInfo> getDangerLevelInfos();
}
