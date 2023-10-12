package bjfu.it.lihongjing.covid_19helper.bean.dangerLevel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DangerLevelInfo {
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("data")
    @Expose
    private List<DangerData> dangerDataList;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("successAndNotNull")
    @Expose
    private boolean successAndNotNull;

    public DangerLevelInfo(String code, List<DangerData> dangerDataList, String message, boolean success, boolean successAndNotNull) {
        this.code = code;
        this.dangerDataList = dangerDataList;
        this.message = message;
        this.success = success;
        this.successAndNotNull = successAndNotNull;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<DangerData> getDangerDataList() {
        return dangerDataList;
    }

    public void setDangerDataList(List<DangerData> dangerDataList) {
        this.dangerDataList = dangerDataList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccessAndNotNull() {
        return successAndNotNull;
    }

    public void setSuccessAndNotNull(boolean successAndNotNull) {
        this.successAndNotNull = successAndNotNull;
    }
}
