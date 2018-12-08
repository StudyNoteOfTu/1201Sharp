package activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tufengyi.sharp.R;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import org.w3c.dom.Text;

import bean.Assignment;
import bean.Task;
import bean.TaskReview;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import dao.TaskReviewDao;

public class TaskUpdateProgressActivity extends AppCompatActivity{
    private TextView tv_date,tv_objId;
    private EditText edt_key,edt_name,edt_progress;
    private Button btn_update;
    private ExpandableTextView tv_task,tv_name;

 //   private AssignmentDao assignmentDao;
    private TaskReviewDao taskReviewDao;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bmob.initialize(this,"f4939b1bc990101036756a6398c7a159");
        setContentView(R.layout.activity_updatetask);
        Intent intent = getIntent();
        final String task = intent.getStringExtra("getSchedule");
        final String title = intent.getStringExtra("getTitle");
        final String date = intent.getStringExtra("getDate");
       final long  id = intent.getLongExtra("getId",0);
       String objId = intent.getStringExtra("getObjId");
       String name = intent.getStringExtra("getName");
       //增加获得任务
        //只上传，只有点击保存本地进度才真正保存（返回也保存本地进度）
       String progress = intent.getStringExtra("getProgress");

        //返回键
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        taskReviewDao = new TaskReviewDao(this);
       tv_name = (ExpandableTextView) findViewById(R.id.tv_name);
       tv_name.setText(title);

       tv_date=(TextView) findViewById(R.id.tv_date);
       tv_date.setText(date);

        tv_task = (ExpandableTextView) findViewById(R.id.tv_task);
        tv_task.setText(task);
        tv_objId = (TextView) findViewById(R.id.tv_objId);
        tv_objId.setText(objId);
        edt_key = (EditText)findViewById(R.id.edt_key_task);
        edt_name = (EditText)findViewById(R.id.edt_name);
        edt_name.setText(name);
        edt_progress = (EditText) findViewById(R.id.edt_progress);
        edt_progress.setText(progress);
        Button btn_tolast = (Button) findViewById(R.id.tolast);
        btn_tolast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_key.getText().toString().trim().equals("")) {
                    Toast.makeText(TaskUpdateProgressActivity.this, "请先输入成员编号", Toast.LENGTH_SHORT).show();
                } else {
                    int lastIndex = Integer.parseInt(edt_key.getText().toString().trim())-1;
                    edt_key.setText(String.valueOf(lastIndex));
                }
            }
        });
    Button btn_tonext = (Button) findViewById(R.id.tonext);
            btn_tonext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (edt_key.getText().toString().trim().equals("")) {
                        Toast.makeText(TaskUpdateProgressActivity.this, "请先输入成员编号", Toast.LENGTH_SHORT).show();
                    } else {
                        int lastIndex = Integer.parseInt(edt_key.getText().toString().trim())+1;
                        edt_key.setText(String.valueOf(lastIndex));
                    }
                }
            });

        //获得云端数据 if判断用户是否输入号数
        Button btn_getInfo = (Button) findViewById(R.id.btn_getInfo);
        btn_getInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_key.getText().toString().trim().equals("")) {
                    Toast.makeText(TaskUpdateProgressActivity.this, "请输入成员编号", Toast.LENGTH_SHORT).show();
                } else {
                    //  String keyNum = edt_key.getText().toString().trim();
                    final int index = Integer.parseInt(edt_key.getText().toString().trim()) - 1;
                    String objectId = tv_objId.getText().toString().trim();
                        BmobQuery<TaskReview> query = new BmobQuery<TaskReview>();
                        //                query.addWhereContains("objectId",objectId);
                        //                query.addWhereContains("objectId","4623413ad6");
                        query.getObject(objectId, new QueryListener<TaskReview>() {
                            @Override
                            public void done(TaskReview object, BmobException e) {
                                if (e == null) {
                                    Toast.makeText(TaskUpdateProgressActivity.this, "添加成功" + object.getMlist().size(), Toast.LENGTH_SHORT).show();
                                    // mTask = object.getMlist();
                                    String name = object.getMlist().get(index).getName();
                                    String progress = object.getMlist().get(index).getTask();

                                    edt_name.setText(name);
                                    edt_progress.setText(progress);

                                } else {
                                    Toast.makeText(TaskUpdateProgressActivity.this, "查询失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        });
                }
            }
        });

        //上传数据到云端
        btn_update = (Button) findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //让用户只需要输入自己的位置（数字0~4）
                //看看是否需要-1 (让用户输入自己的号数)
                if (edt_key.getText().toString().equals("")) {
                    Toast.makeText(TaskUpdateProgressActivity.this, "请输入成员编号", Toast.LENGTH_SHORT).show();
                } else {
                    int index = Integer.parseInt(edt_key.getText().toString().trim()) - 1;
                    String key = "mlist." + (Integer.parseInt(edt_key.getText().toString().trim()) - 1);
                    final String objId = tv_objId.getText().toString().trim();
                  //  String name = edt_key.getText().toString().trim()+". "+edt_name.getText().toString().trim();
                    final String name =edt_name.getText().toString().trim();
                    final String progress = edt_progress.getText().toString().trim();
                    //更新本地数据库进度
                    TaskReview trLo = new TaskReview(id, date, title, task, objId, progress, name);
                    taskReviewDao.update(trLo);
                        //更新云端数据库进度
                        TaskReview tr = new TaskReview();
                        tr.setValue(key, new Task(name, progress));
                        tr.update(objId, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Toast.makeText(TaskUpdateProgressActivity.this, "更新成功", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(TaskUpdateProgressActivity.this, e.getMessage() + e.getErrorCode(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                }
            }
        });



    }




}
