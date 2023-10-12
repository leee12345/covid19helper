package bjfu.it.lihongjing.covid_19helper.utils;

import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class LeakAct {
    private static LeakAct singleton;

    private LeakAct() {
    }

    public static LeakAct getInstance() {
        if (singleton == null) {
            singleton = new LeakAct();
        }
        return singleton;
    }

    /**
     * 单例造成的内存泄露
     */
    private TextView mTextViewLeak = null;
    public void setTextViewLeak(TextView textView) {
        mTextViewLeak = textView;
    }

    private WeakReference<TextView> mTextView = null;
    public void setTextView(TextView textView) {
        mTextView = new WeakReference<TextView>(textView);
    }

    public static class HttpUtil {

        public static void sendHttpRequest(String address, Callback callback){
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build();

            Request request = new Request.Builder().url(address).build();
            Call call = client.newCall(request);
            call.enqueue(callback);
        }
        public static void sendOkHttpRequest(String address, Callback callback){
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(address).build();
            client.newCall(request).enqueue(callback);
        }

    }
}