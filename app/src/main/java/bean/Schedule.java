package bean;


public class Schedule {

    public long id;
    public String date;
    public String state;
    public String schedule;
    public String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    //构造器
    public Schedule(Long id,String date,String state,String schedule,String title){
        super();
        this.id=id;
        this.state = state;
        this.date = date;
        this.schedule = schedule;
        this.title = title;
    }
    public Schedule(Long id,String date,String schedule,String title){
        super();
        this.id=id;
        this.date = date;
        this.schedule = schedule;
        this.title = title;
    }

    public Schedule(String date,String state,String schedule,String title){
        super();
        this.state=state;
        this.date = date;
        this.schedule = schedule;
        this.title = title;
    }
    public Schedule(String date,String schedule,String title){
        super();
        this.date = date;
        this.schedule = schedule;
        this.title = title;
    }
}
