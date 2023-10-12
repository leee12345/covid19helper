package bjfu.it.lihongjing.covid_19helper.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import bjfu.it.lihongjing.covid_19helper.MainActivity;
import bjfu.it.lihongjing.covid_19helper.MyHeSuanActivity;
import bjfu.it.lihongjing.covid_19helper.MyMiaoActivity;
import bjfu.it.lihongjing.covid_19helper.MyTripActivity;
import bjfu.it.lihongjing.covid_19helper.R;

public class MyFragment extends Fragment {
    final public static String TEL_KEY = "tel";
    final public static String NAME_KEY = "name";

    String tags = "";
    private ImageView back;
    public static MyFragment newInstance(String name,String tel) {
        Bundle args = new Bundle();
        MyFragment fragment = new MyFragment();
        args.putString("name", name);
        args.putString("tel", tel);
        fragment.setArguments(args);
        return fragment;
    }
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        String name=this.getArguments().getString("name");
        String tel=this.getArguments().getString("tel");

        View v = inflater.inflate(R.layout.activity_wd, container, false);

        back=v.findViewById(R.id.back_wd);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra(TEL_KEY, tel);
                intent.putExtra(NAME_KEY, name);
                startActivity(intent);
            }
        });

        AdapterView.OnItemClickListener itemClickListener=
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(position==0){
                            //TODO:跳转到我的核酸检测结果
                            Intent intent=new Intent(getActivity(), MyHeSuanActivity.class);
                            intent.putExtra(TEL_KEY, tel);
                            intent.putExtra(NAME_KEY, name);
                            startActivity(intent);
                            getActivity().finish();
                        }
                        else if(position==1){
                            //TODO:跳转到我的疫苗注射结果
                            Intent intent=new Intent(getActivity(), MyMiaoActivity.class);
                            intent.putExtra(TEL_KEY, tel);
                            intent.putExtra(NAME_KEY, name);
                            startActivity(intent);
                            getActivity().finish();
                        }else if(position==2){
                            //TODO:跳转到通信大数据行程
                            Intent intent=new Intent(getActivity(), MyTripActivity.class);
                            intent.putExtra(TEL_KEY, tel);
                            intent.putExtra(NAME_KEY, name);
                            startActivity(intent);
                            getActivity().finish();
                        }
                        else{

                        }
                    }
                };
        //为listView注册单击监听器
        ListView listView =v.findViewById(R.id.wd_list);
        listView.setOnItemClickListener(itemClickListener);
        return v;
    }

}
