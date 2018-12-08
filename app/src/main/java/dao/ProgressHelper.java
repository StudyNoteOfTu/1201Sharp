package dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//暂时没用
public class ProgressHelper extends SQLiteOpenHelper {
    public ProgressHelper(Context context){
        super(context,"progress.db",null,1);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE progress(_id INTEGER PRIMARY KEY AUTOINCREMENT,startday VERCHAR(20),dueday VERCHAR(20),assignment VERCHAR(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase,int oldVersion,int newVersion){

    }
}
