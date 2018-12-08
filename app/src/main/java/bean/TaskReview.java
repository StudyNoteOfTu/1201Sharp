package bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

public class TaskReview extends BmobObject {

    private long id;

    private String dudate;
    private String title;
    private String schedule;
    private String progress;
    private String objId;
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getDudate() {
        return dudate;
    }

    public void setDudate(String dudate) {
        this.dudate = dudate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    private List<Task> mlist;

    public List<Task> getMlist() {
        return mlist;
    }

    public void setMlist(List<Task> mlist) {
        this.mlist = mlist;
    }


    //6
    public TaskReview(long id,String dudate,String title,String schedule,String objId,String progress,String name){
        super();
        this.id = id;
        this.dudate = dudate;
        this.title = title;
        this.schedule = schedule;
        this.progress = progress;
        this.objId = objId;
        this.name = name;
    }

//    //4
//    public TaskReview(String dudate,String title,String schedule){
//        super();
//        this.dudate = dudate;
//        this.title = title;
//        this.schedule = schedule;
//    }
    //5
    public TaskReview(String dudate,String title,String schedule,String objId,String progress,String name){
        super();
        this.dudate = dudate;
        this.title = title;
        this.schedule = schedule;
        this.objId = objId;
        this.progress = progress;
        this.name = name;
    }

    public TaskReview(){}

}

//public class Task{
//    private String name;
//    private String task;
//    public Task(String name,String task){
//        this.name = name;
//        this.task = task;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getTask() {
//        return task;
//    }
//
//    public void setTask(String task) {
//        this.task = task;
//    }
//}
