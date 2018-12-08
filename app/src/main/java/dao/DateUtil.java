package dao;


import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static long stringToDate(String dateString){
        long re_time = -1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d;
        try{
            d=sdf.parse(dateString+":00");
            long l = d.getTime();
            re_time=l;
//            String str = String.valueOf(l);
//            re_time = str.substring(0,10);
        }catch(ParseException e){

        }
        return re_time;
    }
}