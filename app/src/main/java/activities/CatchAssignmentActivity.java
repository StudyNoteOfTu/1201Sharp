package activities;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.tufengyi.sharp.R;

import bean.KeyValue;
import bean.Schedule;
import bean.TaskReview;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import dao.KeysDao;
import dao.ScheduleDao;
import dao.TaskReviewDao;

//通过id添加事件到sql
public class CatchAssignmentActivity extends AppCompatActivity {
    private TaskReviewDao taskReviewDao;
    private ScheduleDao scheduleDao;
    private KeysDao keysDao;
    private EditText edt_id;
    private Button btn_catch,btn_save;
    private EditText edt_date,edt_schedule,edt_title;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this,"f4939b1bc990101036756a6398c7a159");
        setContentView(R.layout.activity_catch);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Slide().setDuration(350));
            getWindow().setExitTransition(new Slide().setDuration(260));
        }

        //返回键
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        scheduleDao = new ScheduleDao(this);
        taskReviewDao = new TaskReviewDao(this);
        keysDao = new KeysDao(this);
        edt_date = (EditText) findViewById( R.id.edt_date);
        edt_schedule = (EditText) findViewById(R.id.edt_schedule);
        edt_id=(EditText) findViewById(R.id.edt_id);
        edt_title=(EditText) findViewById(R.id.edt_title);
        btn_catch = (Button) findViewById(R.id.btn_catch);
        btn_catch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = edt_id.getText().toString().trim();

                if(id.isEmpty()){
                    Toast.makeText(CatchAssignmentActivity.this, "请输入分享码", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(CatchAssignmentActivity.this, "正在拉取...", Toast.LENGTH_SHORT).show();
                BmobQuery<TaskReview> bmobQuery = new BmobQuery<TaskReview>();
                bmobQuery.getObject(id,new QueryListener<TaskReview>(){
                    @Override
                    public void done(TaskReview object, BmobException e){
                        if(e==null){
                            Toast.makeText(CatchAssignmentActivity.this,"查询成功",Toast.LENGTH_SHORT).show();
                            String sch = object.getSchedule();
                            String date = object.getDudate();
                            String title = object.getTitle();
                            edt_schedule.setText(sch);
                            edt_date.setText(date);
                            edt_title.setText(title);
                        }else{
                            Toast.makeText(CatchAssignmentActivity.this,"查询失败"+e.getMessage(),Toast.LENGTH_SHORT).show();
                            edt_id.setText("");
                        }
                    }

                });
            }}
        });
        btn_save=(Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date =edt_date.getText().toString().trim();
                String schedule = edt_schedule.getText().toString().trim();
                String title = edt_title.getText().toString().trim();
                String objId = edt_id.getText().toString().trim();
                //传入date，任务，title，id
                //progress 初始化为“”

                if(objId.isEmpty()&&date.isEmpty()){
                    Toast.makeText(CatchAssignmentActivity.this, "请先获取分享码对应的任务噢", Toast.LENGTH_SHORT).show();
                }else {
                    TaskReview tr = new TaskReview(date.equals("") ? "" : date, title, schedule.equals("") ? "" : schedule, objId, "", "");
                    taskReviewDao.insert(tr);

                    KeyValue kv = new KeyValue(objId, title.equals("") ? "未命名任务" : title);
                    keysDao.insert(kv);

                    //这里注意一下，title如果为空，看看是否要把schedule默认为title
                    //同时也加入到Schedule中
                    Schedule sch = new Schedule(date.equals("") ? "" : date, "未完成", schedule.equals("") ? "" : schedule, "(多人)" + title);
                    scheduleDao.insert(sch);

                    Toast.makeText(CatchAssignmentActivity.this, "已为您添加到时间轴中", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CatchAssignmentActivity.this, BmobActivity.class);
                    startActivity(intent);
                }
            }
        });



    }
    @Override
    protected void onPause(){
        super.onPause();
        overridePendingTransition(0,0);
    }

}
