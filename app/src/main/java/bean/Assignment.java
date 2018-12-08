package bean;


import cn.bmob.v3.BmobObject;

public class Assignment extends BmobObject {
    public long id;
    public String progress;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }
    public Assignment(long id,String progress){
        super();
        this.id=id;
        this.progress = progress;
    }

}

