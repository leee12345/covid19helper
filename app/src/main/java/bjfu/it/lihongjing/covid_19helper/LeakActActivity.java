package bjfu.it.lihongjing.covid_19helper;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class LeakActActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leak_act);
        /**
         * 匿名内部类，导致的内存泄露
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                LeakActActivity.this.getApplicationContext();
                while (true){
                    try {
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        e.printStackTrace();

                    }
                }
            }
        }).start();
        new Thread(new staticRunnable());
    }
    private static class staticRunnable implements Runnable{
        @Override
        public void run(){
            //Error
            //
            while (true){
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
}