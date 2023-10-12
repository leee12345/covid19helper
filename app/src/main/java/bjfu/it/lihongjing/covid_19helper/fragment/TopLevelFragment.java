package bjfu.it.lihongjing.covid_19helper.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import bjfu.it.lihongjing.covid_19helper.NewsActivity;
import bjfu.it.lihongjing.covid_19helper.ProvincesNameActivity;
import bjfu.it.lihongjing.covid_19helper.R;
import bjfu.it.lihongjing.covid_19helper.SegregationActivity;
import bjfu.it.lihongjing.covid_19helper.adapter.BannerAdapter;
import bjfu.it.lihongjing.covid_19helper.bean.dangerLevel.DangerData;
import bjfu.it.lihongjing.covid_19helper.bean.dangerLevel.DangerLevelInfo;
import bjfu.it.lihongjing.covid_19helper.bean.dangerLevel.DangerPro;
import bjfu.it.lihongjing.covid_19helper.network.DangerApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopLevelFragment  extends Fragment {
    final public static String TEL_KEY = "tel";
    final public static String NAME_KEY = "name";
    String tags = "";
    // 声明控件
    private ViewPager mViewPager;
    private List<ImageView> mlist;
    private TextView mTextView;
    private LinearLayout mLinearLayout;
    // 广告图素材
    private int[] bannerImages = { R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4 };
    // 广告语
    private String[] bannerTexts = { "本地疫情信息", "复工须知", "新冠预防", "医院预约指南" };
    // ViewPager适配器与监听器
    private BannerAdapter mAdapter;
    private TopLevelFragment.BannerListener bannerListener;
    // 圆圈标志位
    private int pointIndex = 0;
    // 线程标志
    private boolean isStop = false;
    private ListView listView;
    private ListView highLevelLv;
    private ListView midLevelLv;
    private TextView highLevelTv;
    private TextView midLevelTv;
    private List<String> highList;
    private List<String> midList;

    public static TopLevelFragment newInstance(String name,String tel) {
        Bundle args = new Bundle();
        TopLevelFragment fragment = new TopLevelFragment();
        args.putString("name", name);
        args.putString("tel", tel);
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View v = inflater.inflate(R.layout.fragment_top, container, false);
        initView(v);
        initData();
        initAction();
        return v;
    }

    /**
     * 初始化事件
     */
    private void initAction() {
        bannerListener = new BannerListener();
        mViewPager.setOnPageChangeListener(bannerListener);
        //取中间数来作为起始位置
        int index = (Integer.MAX_VALUE / 2) - (Integer.MAX_VALUE / 2 % mlist.size());
        //用来出发监听器
        mViewPager.setCurrentItem(index);
        mLinearLayout.getChildAt(pointIndex).setEnabled(true);
        // 开启新线程，2秒一次更新Banner
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (!isStop) {
                    SystemClock.sleep(2000);
                    if(getActivity()==null)
                        return;
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                        }
                    });
                }
            }
        }).start();
        refreshListView();
        highLevelLv.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,highList));
        midLevelLv.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, midList));
        highLevelLv.setVisibility(View.VISIBLE);
        midLevelLv.setVisibility(View.VISIBLE);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        String name=this.getArguments().getString("name");
        String tel=this.getArguments().getString("tel");
        mViewPager.setOnPageChangeListener(new BannerListener());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(position==0){
                            //TODO:跳转到省份列表
                            Intent intent=new Intent(getActivity(), ProvincesNameActivity.class);
                            intent.putExtra(TEL_KEY, tel);
                            intent.putExtra(NAME_KEY, name);
                            startActivity(intent);
                            getActivity().finish();
                        }
                        else if(position==1){
                            //TODO:跳转到防疫小知识
                            Intent intent=new Intent(getActivity(), NewsActivity.class);
                            intent.putExtra(TEL_KEY, tel);
                            intent.putExtra(NAME_KEY, name);
                            startActivity(intent);
                            getActivity().finish();
                        }
                        else if(position==2){
                            //TODO:跳转到隔离政策
                            Intent intent=new Intent(getActivity(), SegregationActivity.class);
                            intent.putExtra(TEL_KEY, tel);
                            intent.putExtra(NAME_KEY, name);
                            startActivity(intent);
                            getActivity().finish();
                        }
                        else {

                        }
                    }
                });
        //风险等级
        highList = new ArrayList<>();
        midList = new ArrayList<>();
        highLevelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshListView();
                highLevelLv.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,highList));
                if(highLevelLv.getVisibility()==View.VISIBLE){
                    highLevelLv.setVisibility(View.GONE);
                }else if(highLevelLv.getVisibility()==View.GONE){
                    highLevelLv.setVisibility(View.VISIBLE);
                }
            }
        });
        midLevelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshListView();
                midLevelLv.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, midList));
                if(midLevelLv.getVisibility()==View.VISIBLE){
                    midLevelLv.setVisibility(View.GONE);
                }else if(midLevelLv.getVisibility()==View.GONE){
                    midLevelLv.setVisibility(View.VISIBLE);
                }
            }
        });
        //点
        mlist = new ArrayList<ImageView>();
        View view;
        LinearLayout.LayoutParams params;
        for (int i = 0; i < bannerImages.length; i++) {
            // 设置广告图
            ImageView imageView = new ImageView(getActivity());
            imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            imageView.setBackgroundResource(bannerImages[i]);
            mlist.add(imageView);
            // 设置圆圈点
            view = new View(getActivity());
            params = new LinearLayout.LayoutParams(5, 5);
            params.leftMargin = 10;
            view.setBackgroundResource(R.drawable.points_bg);
            view.setLayoutParams(params);
            view.setEnabled(false);

            mLinearLayout.addView(view);
        }
        mAdapter = new BannerAdapter(mlist);
        mViewPager.setAdapter(mAdapter);
    }

    /**
     * 初始化View操作
     */
    private void initView(View v) {
        mViewPager = v.findViewById(R.id.viewpager);
        mTextView =  v.findViewById(R.id.tv_bannertext);
        mLinearLayout = v.findViewById(R.id.points);
        highLevelTv = v.findViewById(R.id.highText);
        midLevelTv = v.findViewById(R.id.midText);
        highLevelLv = v.findViewById(R.id.highList);
        midLevelLv = v.findViewById(R.id.midList);
        listView = v.findViewById(R.id.functionList);
    }
    private void refreshListView() {
        Call<DangerLevelInfo> callLevel = DangerApiClient.getInstance().getApi().getDangerLevelInfos();
        callLevel.enqueue(new Callback<DangerLevelInfo>() {
            @Override
            public void onResponse(Call<DangerLevelInfo> call, Response<DangerLevelInfo> response) {
                List<DangerData> dangerDataList = response.body().getDangerDataList();
                highList.clear();
                midList.clear();
                for(DangerData dangerData:dangerDataList){
                    Integer dangerLevel = dangerData.getDangerLevel();
                    if(dangerLevel==1){
                        for(DangerPro dangerPro: dangerData.getDangerProList()){
                            if(!highList.contains(dangerPro.getProvinceName()))
                                highList.add(dangerPro.getProvinceName());
                        }
                    }else if(dangerLevel==2){
                        for(DangerPro dangerPro: dangerData.getDangerProList()){
                            if(!midList.contains(dangerPro.getProvinceName()))
                                midList.add(dangerPro.getProvinceName());
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<DangerLevelInfo> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //实现VierPager监听器接口
    class BannerListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            int newPosition = position % bannerImages.length;
            mTextView.setText(bannerTexts[newPosition]);
            mLinearLayout.getChildAt(newPosition).setEnabled(true);
            mLinearLayout.getChildAt(pointIndex).setEnabled(false);
            // 更新标志位
            pointIndex = newPosition;

        }

    }

    public void onDestroy() {
        // 关闭定时器
        isStop = true;
        super.onDestroy();
    }
}
