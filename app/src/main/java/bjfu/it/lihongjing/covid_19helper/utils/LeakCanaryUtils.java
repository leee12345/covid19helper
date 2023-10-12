package bjfu.it.lihongjing.covid_19helper.utils;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class LeakCanaryUtils {
    private static List<WeakReference<Context>> contextList = new ArrayList<>();

    public static void addContext(Context context) {
        contextList.add(new WeakReference<>(context));
    }

    public static Context getContext(int index) {
        if (index < 0 || index >= contextList.size()) {
            return null;
        }
        WeakReference<Context> contextWeakReference = contextList.get(index);
        return contextWeakReference.get();
    }
}
