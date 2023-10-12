package bjfu.it.lihongjing.covid_19helper;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bjfu.it.lihongjing.covid_19helper.adapter.TitleAdapter;
import bjfu.it.lihongjing.covid_19helper.bean.news.News;
import bjfu.it.lihongjing.covid_19helper.bean.news.NewsList;
import bjfu.it.lihongjing.covid_19helper.bean.news.Title;
import bjfu.it.lihongjing.covid_19helper.fragment.TopLevelFragment;
import bjfu.it.lihongjing.covid_19helper.utils.DBOpenHelper;
import bjfu.it.lihongjing.covid_19helper.utils.LeakAct;
import bjfu.it.lihongjing.covid_19helper.utils.Utility;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class NewsActivity extends AppCompatActivity {
    final public static String TEL_KEY = "tel";
    final public static String NAME_KEY = "name";

    private static final int  ITEM_ALL= 1;
    private static final int  ITEM_DOMESTIC= 2;
    private static final int  ITEM_FOREIGN= 3;
    private static final int  ITEM_MIAO= 4;


    private List<Title> titleList = new ArrayList<Title>();
    private ListView listView;
    private TitleAdapter adapter;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private SwipeRefreshLayout refreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Intent intent = getIntent();
        String name = intent.getStringExtra(TopLevelFragment.NAME_KEY);
        String tel=intent.getStringExtra(TopLevelFragment.TEL_KEY);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navigationView = (NavigationView)findViewById(R.id.nav_view);
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        refreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_layout);
        listView = (ListView)findViewById(R.id.wd_list);
        //toolbar
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("疫情新闻");

        //refreshLayou
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        //为listview设置adapter
        adapter = new TitleAdapter(this,R.layout.list_view_item, titleList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent intent = new Intent(NewsActivity.this, ContentActivity.class);
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Title title = titleList.get(position);
                intent.putExtra("title",actionBar.getTitle());
                intent.putExtra("uri",title.getUri());
                intent.putExtra(TEL_KEY, tel);
                intent.putExtra(NAME_KEY, name);
                startActivity(intent);
            }
        });

        //navigationView
        navigationView.setCheckedItem(R.id.nav_all);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_all:
                        handleCurrentPage("疫情新闻",ITEM_ALL);
                        break;
                    case R.id.nav_domestic:
                        handleCurrentPage("国内疫情新闻",ITEM_DOMESTIC);
                        break;
                    case R.id.nav_foreign:
                        handleCurrentPage("国际疫情新闻",ITEM_FOREIGN);
                        break;
                    case R.id.nav_miao:
                        handleCurrentPage("疫苗信息",ITEM_MIAO);
                        break;
                    case R.id.nav_return:
                        Intent intent=new Intent(NewsActivity.this,MainActivity.class);
                        intent.putExtra(TEL_KEY, tel);
                        intent.putExtra(NAME_KEY, name);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
        //为refreshLayout设置监听
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                int itemName = parseString((String)actionBar.getTitle());
                requestNew(ITEM_ALL);
            }
        });
        requestNew(ITEM_ALL);
    }
    /**
     *  判断是否是当前页面,如果不是则 请求处理数据
     */
    private void handleCurrentPage(String text, int item){
        ActionBar actionBar = getSupportActionBar();
        if (!text.equals(actionBar.getTitle().toString())){
            actionBar.setTitle(text);
            requestNew(item);
            refreshLayout.setRefreshing(true);
        }
    }

    /**
     * 请求处理数据
     */
    public void requestNew(int itemName){

        // 根据返回到的 URL 链接进行申请和返回数据
        String address = response( itemName);    // key
        LeakAct.HttpUtil. sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NewsActivity.this, "新闻加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final NewsList newlist = Utility.parseJsonWithGson(responseText);
                final int code = newlist.code;
                final String msg = newlist.msg;
                if (code == 200){
                    titleList.clear();
                    for (News news:newlist.newsList){
                        Title title = new Title(news.title,news.description,news.picUrl, news.url);
                        titleList.add(title);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            listView.setSelection(0);
                            refreshLayout.setRefreshing(false);
                        };
                    });
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NewsActivity.this, "数据错误返回",Toast.LENGTH_SHORT).show();
                            refreshLayout.setRefreshing(false);
                        }
                    });
                }



            }
        });


    }




    /**
     * 输入不同的类型选项，返回对应的 URL 链接
     */
    private String response(int itemName){
        String address = "http://api.tianapi.com/topnews/index?key=2c2ef9246d635ada001fa1013eb1ef93&word=新冠";
        switch(itemName){
            case ITEM_ALL:
                break;
            case ITEM_DOMESTIC:
                address = address.replaceAll("新冠","中国疫情");
                break;
            case ITEM_FOREIGN:
                address = address.replaceAll("新冠","国际疫情");
                break;
            case ITEM_MIAO:
                address = address.replaceAll("新冠","疫苗");
                break;

            default:
        }
        return address;
    }
    /**
     * 通过 actionbar.getTitle() 的参数，返回对应的 ItemName
     */
    private int parseString(String text){
        if (text.equals("疫情新闻")){
            return ITEM_ALL;
        }
        if (text.equals("国内疫情新闻")){
            return ITEM_DOMESTIC;
        }
        if (text.equals("国际疫情新闻")){
            return ITEM_FOREIGN;
        }
        if (text.equals("疫苗信息")){
            return ITEM_MIAO;
        }
        return ITEM_ALL;
    }

    /**
     * 对侧边栏按钮进行处理，打开侧边栏
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }

    /**
     * 对返回键进行处理，如果侧边栏打开则关闭侧边栏，否则关闭 activity
     */
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawers();
        }else{
            finish();
        }
    }


}
