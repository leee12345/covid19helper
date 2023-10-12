package bjfu.it.lihongjing.covid_19helper.bean.dangerLevel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DangerData {
    @SerializedName("dangerCount")
    @Expose
    private Integer dangerCount;
    @SerializedName("dangerLevel")
    @Expose
    private Integer dangerLevel;
    @SerializedName("dangerPros")
    @Expose
    private List<DangerPro> dangerProList;

    public DangerData(Integer dangerCount, Integer dangerLevel, List<DangerPro> dangerProList) {
        this.dangerCount = dangerCount;
        this.dangerLevel = dangerLevel;
        this.dangerProList = dangerProList;
    }

    public Integer getDangerCount() {
        return dangerCount;
    }

    public void setDangerCount(Integer dangerCount) {
        this.dangerCount = dangerCount;
    }

    public Integer getDangerLevel() {
        return dangerLevel;
    }

    public void setDangerLevel(Integer dangerLevel) {
        this.dangerLevel = dangerLevel;
    }

    public List<DangerPro> getDangerProList() {
        return dangerProList;
    }

    public void setDangerProList(List<DangerPro> dangerProList) {
        this.dangerProList = dangerProList;
    }
}
