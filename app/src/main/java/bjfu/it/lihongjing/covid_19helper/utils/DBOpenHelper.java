package bjfu.it.lihongjing.covid_19helper.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import java.util.ArrayList;

import bjfu.it.lihongjing.covid_19helper.bean.user.User;


public class DBOpenHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;


    public DBOpenHelper(Context context){
        super(context,"db_test",null,1);
        db = getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS user(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "img TEXT," +
                "tel TEXT," +
                "name TEXT," +
                "password TEXT,"+
                "hesuan INTEGER,"+
                "yimiao INTEGER)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
    }

    public void addUser(String tel,String name,String password){
        String img="https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fcdn.duitang.com%2Fuploads%2Fitem%2F201309%2F26%2F20130926095128_SiPMh.jpeg&refer=http%3A%2F%2Fcdn.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1629520415&t=df0001bb707d75d1b433ba6b5baddc76";
        Integer he=0;
        Integer yi=1;
        db.execSQL("INSERT INTO user (img,tel,name,password,hesuan,yimiao) VALUES(?,?,?,?,?,?)",new Object[]{img,tel,name,password,he,yi});
    }
    public void deleteUser(String tel,String password){
        db.execSQL("DELETE FROM user WHERE tel= AND password ="+tel+password);
    }
    public void updateUser(String tel, String password){
        db.execSQL("UPDATE user SET password = ? WHERE tel= ?",new Object[]{password,tel});
    }


    public ArrayList<User> getAllDataUser(){

        ArrayList<User> list = new ArrayList<User>();
        Cursor cursor = db.query("user",null,null,null,null,null,"name DESC");
        while(cursor.moveToNext()){
            String tel = cursor.getString(cursor.getColumnIndex("tel"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String password = cursor.getString(cursor.getColumnIndex("password"));
            Integer hesuan = cursor.getInt(cursor.getColumnIndex("hesuan"));
            Integer yimiao = cursor.getInt(cursor.getColumnIndex("yimiao"));
            list.add(new User(tel,name,password,hesuan,yimiao));
        }
        return list;
    }
    public boolean queryOneUser(String tn, String pwd){
        Cursor cursor = db.query("user",null,null,null,null,null,"name DESC");
        while(cursor.moveToNext()){
            String tel = cursor.getString(cursor.getColumnIndex("tel"));
            String password = cursor.getString(cursor.getColumnIndex("password"));
            if(TextUtils.equals(tel,tn)&&TextUtils.equals(password,pwd)){
                cursor.close();
                return true;
            }
        }
        return false;
    }
    public String queryUserImg(String tn){
        String imgurl="";
        Cursor cursor = db.query("user",null,null,null,null,null,"name DESC");
        while(cursor.moveToNext()){
            String tel = cursor.getString(cursor.getColumnIndex("tel"));
            if(TextUtils.equals(tel,tn)){
                imgurl= cursor.getString(1);

            }
        }
        cursor.close();
        return imgurl;
    }
    public String queryUserName(String tn,String pwd){
        String name="";
        Cursor cursor = db.query("user",null,null,null,null,null,"name DESC");
        while(cursor.moveToNext()){
            String tel = cursor.getString(cursor.getColumnIndex("tel"));
            String password = cursor.getString(cursor.getColumnIndex("password"));
            if(TextUtils.equals(tel,tn)&&TextUtils.equals(password,pwd)){
                name= cursor.getString(3);

            }
        }
        cursor.close();
        return name;
    }
    public Integer queryUserHe(String name){
        Integer result=0;
        Cursor cursor = db.query("user",null,null,null,null,null,"name DESC");
        while(cursor.moveToNext()){
            String un = cursor.getString(cursor.getColumnIndex("name"));
            if(TextUtils.equals(name,un)){
                result= cursor.getInt(5);

            }
        }
        cursor.close();
        return result;
    }
    public Integer queryUserMiao(String name){
        Integer result=1;
        Cursor cursor = db.query("user",null,null,null,null,null,"name DESC");
        while(cursor.moveToNext()){
            String un = cursor.getString(cursor.getColumnIndex("name"));
            if(TextUtils.equals(name,un)){
                result= cursor.getInt(6);

            }
        }
        cursor.close();
        return result;
    }
}
