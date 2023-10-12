package bjfu.it.lihongjing.covid_19helper.bean.state;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NewAdd implements Serializable {
    @SerializedName("provinceName")
    public String provinceName;
    @SerializedName("yesterdayLocalConfirmedCount")
    private String new_add;

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getNew_add() {
        return new_add;
    }

    public void setNew_add(String new_add) {
        this.new_add = new_add;
    }
}
