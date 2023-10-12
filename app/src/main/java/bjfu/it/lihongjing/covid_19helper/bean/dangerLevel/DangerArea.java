package bjfu.it.lihongjing.covid_19helper.bean.dangerLevel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DangerArea{
    @SerializedName("areaName")
    @Expose
    private String areaName;
    @SerializedName("cityName")
    @Expose
    private String cityName;

    public DangerArea(String areaName, String cityName) {
        this.areaName = areaName;
        this.cityName = cityName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}