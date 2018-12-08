package dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import bean.KeyValue;

public class KeysDao {
    private KeysHelper helper;

    //构造器
    public KeysDao(Context context){
        helper = new KeysHelper(context);
    }

    //增加数据
    public void insert(KeyValue keyValue){
        //插入数据
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("akey",keyValue.getAkey());
        values.put("value",keyValue.getValue());
        long id = db.insert("map",null,values);
        keyValue.setId(id);
        db.close();
    }

    /*
     * 删除对应id的数据
     */
    public int delete(long id){
        SQLiteDatabase db = helper.getWritableDatabase();
        int count = db.delete("map","_id=?",new String[]{id+""});
        db.close();
        return count;
    }

    /*
     * 更新
     */
    public int update(KeyValue keyValue){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("akey",keyValue.getAkey());
        values.put("value",keyValue.getValue());
        int count = db.update("map",values,"_id=?",new String[]{keyValue.getId()+""});
        db.close();
        return count;
    }

    /*
     * 遍历数据库
     */
    public List<KeyValue> queryAll(){
        SQLiteDatabase db;
        db = helper.getWritableDatabase();
        Cursor c = db.query("map",null,null,null,null,null,null);
        List<KeyValue> list = new ArrayList<KeyValue>();
        while(c.moveToNext()){
            long id = c.getLong(c.getColumnIndex("_id"));
            String akey = c.getString(1);
            String value = c.getString(2);
            list.add(new KeyValue(id,akey,value));
        }
        c.close();
        db.close();
        return list;
    }
}