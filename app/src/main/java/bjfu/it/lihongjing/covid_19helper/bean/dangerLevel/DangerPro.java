package bjfu.it.lihongjing.covid_19helper.bean.dangerLevel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DangerPro {
    @SerializedName("dangerAreas")
    @Expose
    private List<DangerArea> dangerAreas;

    @SerializedName("provinceId")
    @Expose
    private String provinceId;

    @SerializedName("provinceName")
    @Expose
    private String provinceName;

    @SerializedName("provinceShortName")
    @Expose
    private String provinceShortName;

    public DangerPro(List<DangerArea> dangerAreas, String provinceId, String provinceName, String provinceShortName) {
        this.dangerAreas = dangerAreas;
        this.provinceId = provinceId;
        this.provinceName = provinceName;
        this.provinceShortName = provinceShortName;
    }

    public List<DangerArea> getDangerAreas() {
        return dangerAreas;
    }

    public void setDangerAreas(List<DangerArea> dangerAreas) {
        this.dangerAreas = dangerAreas;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceShortName() {
        return provinceShortName;
    }

    public void setProvinceShortName(String provinceShortName) {
        this.provinceShortName = provinceShortName;
    }

}
