package dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import bean.Note;

public class NoteDao {

    private NoteHelper helper;

    //构造器
    public NoteDao(Context context){
        helper = new NoteHelper(context);
    }

    //增加数据
    public void insert(Note note){
        //插入数据
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title",note.getTitle());
        values.put("content",note.getContent());
        values.put("sort",note.getSort());
        long id = db.insert("note",null,values);
        note.setId(id);
        db.close();
    }

    /*
     * 删除对应id的数据
     */
    public int delete(long id){
        SQLiteDatabase db = helper.getWritableDatabase();
        int count = db.delete("note","_id=?",new String[]{id+""});
        db.close();
        return count;
    }

    /*
     * 更新
     */
    public int update(Note note){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title",note.getTitle());
        values.put("content",note.getContent());
        values.put("sort",note.getSort());
        int count = db.update("note",values,"_id=?",new String[]{note.getId()+""});
        db.close();
        return count;
    }

    /*
     * 遍历数据库
     */
    public List<Note> queryAll(){
        SQLiteDatabase db;
        db = helper.getWritableDatabase();
        Cursor c = db.query("note",null,null,null,null,null,null);
        List<Note> list = new ArrayList<Note>();
        while(c.moveToNext()){
            long id = c.getLong(c.getColumnIndex("_id"));
            String title = c.getString(1);
            String content = c.getString(2);
            String sort = c.getString(3);
            list.add(new Note(id,title,content,sort));
        }
        c.close();
        db.close();
        return list;
    }
}
