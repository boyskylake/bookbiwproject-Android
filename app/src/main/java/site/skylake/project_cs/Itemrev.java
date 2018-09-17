package site.skylake.project_cs;

public class Itemrev {
    private String mname;
    private String mid;
    private String mdetail;

    public Itemrev(String mname, String mid, String mdetail) {
        this.mname = mname;
        this.mid = mid;
        this.mdetail = mdetail;
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
