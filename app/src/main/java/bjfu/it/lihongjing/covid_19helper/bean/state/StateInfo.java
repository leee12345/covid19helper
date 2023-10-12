package bjfu.it.lihongjing.covid_19helper.bean.state;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StateInfo implements Serializable {
    @SerializedName("provinceName")
    private String provinceName;
    @SerializedName("confirmedCount")
    private String total_add;

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getTotal_add() {
        return total_add;
    }

    public void setTotal_add(String total_add) {
        this.total_add = total_add;
    }
}
