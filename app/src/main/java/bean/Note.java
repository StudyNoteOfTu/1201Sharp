package bean;

public class Note {

    public long id;
    public String title;
    public String content;
    public String sort;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Note(long id, String title,String content,String sort ){
        super();
        this.id = id;
        this.title = title;
        this.content = content;
        this.sort = sort;
    }

     public Note(String title,String content,String sort ){
        super();
        this.title = title;
        this.content = content;
        this.sort = sort;
    }

}
