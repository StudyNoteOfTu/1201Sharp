package dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskReviewHelper extends SQLiteOpenHelper{


        public TaskReviewHelper(Context context){
            super(context,"review.db",null,1);
        }

        public void onCreate(SQLiteDatabase db){
            db.execSQL("CREATE TABLE review(_id INTEGER PRIMARY KEY AUTOINCREMENT,date VERCHAR(20),title VERCHAR(10),schedule VERCHAR(20),objId VERCHAR(20),progress VERCHAR(20),name VERCHAR(20))");
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase,int oldVersion,int newVersion){

        }
    }
