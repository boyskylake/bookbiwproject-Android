package site.skylake.project_cs;

public class Item {
    private String mname;
    private String mid;
    private String mdetail;

    public Item(String name, String id, String detail){
        mname = name;
        mid = id;
        mdetail = detail;


    }


    public String getMname() {
        return mname;
    }


    public String getMid() {
        return mid;
    }

    public String getMdetail() {
        return mdetail;
    }
}
