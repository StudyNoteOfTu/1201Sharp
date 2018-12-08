package dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import bean.Assignment;
import bean.TaskReview;

public class TaskReviewDao {
    private TaskReviewHelper helper;

    //构造器
    public TaskReviewDao(Context context){
        helper = new TaskReviewHelper(context);
    }

    //增加数据
    public void insert(TaskReview tr){
        //插入数据
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date",tr.getDudate());
        values.put("schedule",tr.getSchedule());
        values.put("title",tr.getTitle());
        values.put("objId",tr.getObjId());
        values.put("progress",tr.getProgress());
        values.put("name",tr.getName());
        long id = db.insert("review",null,values);
        tr.setId(id);
        db.close();
    }

    /*
     * 删除对应id的数据
     */
    public int delete(long id){
        SQLiteDatabase db = helper.getWritableDatabase();
        int count = db.delete("review","_id=?",new String[]{id+""});
        db.close();
        return count;
    }

    /*
     * 更新
     */
    public int update(TaskReview tr){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date",tr.getDudate());
        values.put("schedule",tr.getSchedule());
        values.put("title",tr.getTitle());
        values.put("objId",tr.getObjId());
        values.put("progress",tr.getProgress());
        values.put("name",tr.getName());
        int count = db.update("review",values,"_id=?",new String[]{tr.getId()+""});
        db.close();
        return count;
    }

    /*
     * 遍历数据库
     */
    public List<TaskReview> queryAll(){
        SQLiteDatabase db;
        db = helper.getWritableDatabase();
        Cursor c = db.query("review",null,null,null,null,null,null);
        List<TaskReview> list = new ArrayList<TaskReview>();
        while(c.moveToNext()){
            long id = c.getLong(c.getColumnIndex("_id"));
            //注意顺序
            String date = c.getString(1);
            String title = c.getString(2);
            String schedule = c.getString(3);
            String objId = c.getString(4);
            String progress = c.getString(5);
            String name = c.getString(6);

            list.add(new TaskReview(id,date,title,schedule,objId,progress,name));
        }
        c.close();
        db.close();
        return list;
    }
}
