package bean;


public class KeyValue {
    public long id;
    public String akey;
    public String value;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAkey() {
        return akey;
    }

    public void setAkey(String akey) {
        this.akey = akey;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public KeyValue(long id, String akey, String value){
        super();
        this.id=id;
        this.akey = akey;
        this.value = value;
    }
    public KeyValue( String akey, String value){
        super();
        this.akey = akey;
        this.value = value;
    }
}
