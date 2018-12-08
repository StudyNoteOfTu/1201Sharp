package dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import bean.Schedule;

public class ScheduleDao {

    private ScheduleHelper helper;

    //构造器
    public ScheduleDao(Context context){
        helper = new ScheduleHelper(context);
    }

    //增加数据
    public void insert(Schedule schedule){
        //插入数据
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date",schedule.getDate());
        values.put("schedule",schedule.getSchedule());
        values.put("state",schedule.getState());
        values.put("title",schedule.getTitle());
        long id = db.insert("schedule",null,values);
        schedule.setId(id);
        db.close();
    }

    /*
     * 删除对应id的数据
     */
    public int delete(long id){
        SQLiteDatabase db = helper.getWritableDatabase();
        int count = db.delete("schedule","_id=?",new String[]{id+""});
        db.close();
        return count;
    }

    /*
     * 更新
     */
    public int update(Schedule schedule){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date",schedule.getDate());
        values.put("schedule",schedule.getSchedule());
        values.put("state",schedule.getState());
        values.put("title",schedule.getTitle());
        int count = db.update("schedule",values,"_id=?",new String[]{schedule.getId()+""});
        db.close();
        return count;
    }

    /*
     * 遍历数据库
     */
    public List<Schedule> queryAll(){
        SQLiteDatabase db;
        db = helper.getWritableDatabase();
        Cursor c = db.query("schedule",null,null,null,null,null,null);
        List<Schedule> list = new ArrayList<Schedule>();
        while(c.moveToNext()){
            long id = c.getLong(c.getColumnIndex("_id"));
            String date = c.getString(1);
            String state = c.getString(2);
            String schedule = c.getString(3);
            String title = c.getString(4);
            list.add(new Schedule(id,date,state,schedule,title));
        }
        c.close();
        db.close();
        return list;
    }
}