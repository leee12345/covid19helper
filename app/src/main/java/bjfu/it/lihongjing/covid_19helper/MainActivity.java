package bjfu.it.lihongjing.covid_19helper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import bjfu.it.lihongjing.covid_19helper.fragment.HesuanFragment;
import bjfu.it.lihongjing.covid_19helper.fragment.MyFragment;
import bjfu.it.lihongjing.covid_19helper.fragment.TopLevelFragment;
import bjfu.it.lihongjing.covid_19helper.fragment.YiMiaoFragment;

public class MainActivity extends AppCompatActivity {
    TabLayout mTabLayout;
    ViewPager mViewPager;
    List fragments = new ArrayList();
    String[] tab_titles;
    int[] tab_imgs;

    @Override
    protected void onCreate(@androidx.annotation.Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        String name = intent.getStringExtra(LoginActivity.NAME_KEY);
        String tel=intent.getStringExtra(LoginActivity.TEL_KEY);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab);
        mTabLayout=findViewById(R.id.tab_layout);
        mViewPager=findViewById(R.id.view_pager);
        fragments.add(TopLevelFragment.newInstance(name,tel));//将TopLevelAvtivity改为Fragment
        fragments.add(HesuanFragment.newInstance());
        fragments.add(YiMiaoFragment.newInstance());
        fragments.add(MyFragment.newInstance(name,tel));
        tab_titles = new String[]{"首页",  "核酸检测", "疫苗注射","我的"};
        tab_imgs=new int[]
                {R.drawable.sy,R.drawable.hesuan,R.drawable.miao,R.drawable.wd}; setTabs(tab_titles,tab_imgs);

        mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), fragments));

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }


    private void setTabs(String[] tab_titles, int[] tab_imgs){
        for (int i = 0; i < tab_titles.length; i++) {
            TabLayout.Tab tab = mTabLayout.newTab();
            View view = getLayoutInflater().inflate(R.layout.tab_item,null);
            tab.setCustomView(view);
            TextView tvTitle = view.findViewById(R.id.tv_des);
            tvTitle.setText(tab_titles[i]);
            ImageView imgTab =  view.findViewById(R.id.iv_top);
            imgTab.setImageResource(tab_imgs[i]);
            if (i==0){
                mTabLayout.addTab(tab,true);
            }else {
                mTabLayout.addTab(tab,false);
            }
        }
    }


    public class FragmentAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragments;
        public FragmentAdapter(FragmentManager fragmentManager, List<Fragment> mFragments) {
            super(fragmentManager);
            this.mFragments = mFragments;
        }
        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}
