package dao;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ScheduleHelper extends SQLiteOpenHelper {

    public ScheduleHelper(Context context){
        super(context,"date.db",null,2);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE schedule(_id INTEGER PRIMARY KEY AUTOINCREMENT,date VERCHAR(20),state VERCHAR(10),schedule VERCHAR(20),title VERCHAR(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase,int oldVersion,int newVersion){

    }
}