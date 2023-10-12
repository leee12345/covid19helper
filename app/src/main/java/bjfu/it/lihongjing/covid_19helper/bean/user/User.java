package bjfu.it.lihongjing.covid_19helper.bean.user;

public class User {
    private String tel;            //电话
    private String name;            //姓名
    private String password;        //密码
    private Integer hesuan;         //核酸 0：阴 1：阳
    private Integer yimiao;         //疫苗 0：未接种 1：已接种

    public User(String tel,String name, String password,Integer hesuan,Integer yimiao) {
        this.tel=tel;
        this.name = name;
        this.password = password;
        this.hesuan=hesuan;//默认阴
        this.yimiao=yimiao;//默认已接种
    }
    public String getTel() {
        return tel;
    }
    public void setTel(String tel) {
        this.tel = tel;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Integer getHesuan() {
        return hesuan;
    }
    public void setHesuan(Integer hesuan) {
        this.hesuan = hesuan;
    }
    public Integer getYimiao() {
        return yimiao;
    }
    public void setYimiao(Integer yimiao) {
        this.yimiao = yimiao;
    }
    @Override
    public String toString() {
        return "User{" +
                "tel='" + tel + '\'' +
                "name='" + name + '\'' +
                ", password='" + password + '\''+
                ", hesuan='" + hesuan + '\'' +
                ", yimiao='" + yimiao + '\'' +
                '}';
    }
}
