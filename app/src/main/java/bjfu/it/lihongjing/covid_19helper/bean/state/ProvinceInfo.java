package bjfu.it.lihongjing.covid_19helper.bean.state;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProvinceInfo implements Serializable {
    @SerializedName("provinceName")
    public String provinceName;

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    @Override
    public String toString() {
        return "ProvinceName{" +
                "provinceName='" + provinceName + '\'' +
                '}';
    }
}
