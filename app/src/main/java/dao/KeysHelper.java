package dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class KeysHelper extends SQLiteOpenHelper {

    public KeysHelper(Context context){
        super(context,"map.db",null,1);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE map(_id INTEGER PRIMARY KEY AUTOINCREMENT,akey VERCHAR(20),value VERCHAR(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase,int oldVersion,int newVersion){

    }
}
