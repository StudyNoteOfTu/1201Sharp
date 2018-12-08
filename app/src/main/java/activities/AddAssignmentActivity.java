package activities;


import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.transition.Slide;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.tufengyi.sharp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import bean.KeyValue;
import bean.Schedule;
import bean.Task;
import bean.TaskReview;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import dao.KeysDao;
import dao.ScheduleDao;
import dao.TaskReviewDao;


public class AddAssignmentActivity extends AppCompatActivity implements View.OnTouchListener{
    private EditText edt_date,edt_schedule,edt_name,edt_num;
    private KeysDao mapDao;
    private EditText edt_seecode;
    private TaskReviewDao taskReviewDao;
    private ScheduleDao scheduleDao;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this,"f4939b1bc990101036756a6398c7a159");
        setContentView(R.layout.activity_addassignment);
        initView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Slide().setDuration(350));
            getWindow().setExitTransition(new Slide().setDuration(200));
        }
    }
    @Override
    public boolean onTouch(View v, MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){

            //用Dialog形式弹出Datepicker和TimePicker
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = View.inflate(this,R.layout.date_time_dialog,null);
            final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
            final TimePicker timePicker = (TimePicker) view.findViewById(R.id.time_picker);
            builder.setView(view);

            //每次打开datepicker和datepicker，默认获得的时间和日期就是系统时间
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());
            datePicker.init(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH),null);

            timePicker.setIs24HourView(true);
            timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
            timePicker.setCurrentMinute(cal.get(Calendar.MINUTE));

            if(v.getId() == R.id.edt_date){
                final int inType = edt_date.getInputType();
                edt_date.setInputType(InputType.TYPE_NULL);
                edt_date.onTouchEvent(event);
                edt_date.setInputType(inType);
                edt_date.setSelection(edt_date.getText().length());
                builder.setTitle("选取时间");
                builder.setPositiveButton("确  定", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog,int which){
                        StringBuffer sb = new StringBuffer();

                        sb.append(datePicker.getYear());
                        sb.append("-");
                        if(datePicker.getMonth()+1>=0 && datePicker.getMonth()+1<10){
                            sb.append("0");
                        }
                        sb.append(datePicker.getMonth()+1);
                        sb.append("-");
                        if(datePicker.getDayOfMonth()>=0 && datePicker.getDayOfMonth()<10){
                            sb.append("0");
                        }
                        sb.append(datePicker.getDayOfMonth());
                        sb.append(" ");
                        if(timePicker.getCurrentHour()>=0 && timePicker.getCurrentHour()<10){
                            sb.append("0");
                        }
                        sb.append(timePicker.getCurrentHour());
                        sb.append(":");
                        if(timePicker.getCurrentMinute()>=0 && timePicker.getCurrentMinute()<10){
                            sb.append("0");
                        }
                        sb.append(timePicker.getCurrentMinute());


                        edt_date.setText(sb);
                        dialog.cancel();
                    }
                });
            }
            Dialog dialog = builder.create();
            dialog.show();
        }
        return true;
    }

    private void initView(){
        //返回键
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mapDao = new KeysDao(this);
        taskReviewDao = new TaskReviewDao(this);
        scheduleDao = new ScheduleDao(this);
        edt_num = (EditText) findViewById(R.id.edt_num);
        edt_date = (EditText)findViewById(R.id.edt_date);
        edt_date.setOnTouchListener(AddAssignmentActivity.this);
        edt_name=(EditText) findViewById(R.id.edt_name);
        edt_schedule = (EditText) findViewById(R.id.edt_schedule);
        Button btn_copy = (Button) findViewById(R.id.btn_copy);
        btn_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = edt_seecode.getText().toString().trim();
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("任务分享码", code);
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                Toast.makeText(getApplicationContext(),code.equals("")? "暂无分享码":"复制成功:"+code,Toast.LENGTH_SHORT).show();
                //这里加个if判断

            }
        });
        Button btn_confirm = (Button) findViewById(R.id.btn_add_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String num = edt_num.getText().toString().trim();
                //final int num = Integer.parseInt(edt_num.getText().toString().trim());
               // List<TaskReview.Member> mlist = new ArrayList<>();


                //获得了时间，标题，安排，任务还需要人员，本地不需要
                final String date = edt_date.getText().toString().trim();
                final String schedule = edt_schedule.getText().toString().trim();
                final String title = edt_name.getText().toString().trim();


                //希望提交具体时间、标题、安排、人员信息
                if(date.equals("")||title.equals("")||num.equals("")){
                    Toast.makeText(AddAssignmentActivity.this, "请填全信息", Toast.LENGTH_SHORT).show();
                }else {
                TaskReview tr = new TaskReview();
                int nums = Integer.parseInt(num);
                List<Task> mlist = new ArrayList<Task>();
                for(int i=0;i<nums;i++){
                    mlist.add(new Task((i+1)+". "+"成员"+(i+1),"进度"+(i+1)));
                }
                tr.addAllUnique("mlist",mlist);

                tr.setDudate(date);
                tr.setTitle(title);
                tr.setSchedule(schedule);


                    //保存到TaskReview中，只保存同学信息
                    tr.save(new SaveListener<String>() {
                        @Override
                        public void done(String objectId, BmobException e) {
                            if (e == null) {
                                Toast.makeText(AddAssignmentActivity.this, "创建数据成功，自动为您添加到备忘录    分享码：" + objectId, Toast.LENGTH_SHORT).show();
//
                                edt_seecode = (EditText) findViewById(R.id.edt_seecode);
                                edt_seecode.setText(objectId);


                                //多导入一个objectId
                                //增加progress
                                TaskReview ass = new TaskReview(date, title.equals("") ? "未命名任务" : title, schedule, objectId, "", "");
                                taskReviewDao.insert(ass);

                                //
                                Schedule sch = new Schedule(date.equals("") ? "" : date, "未完成", schedule.equals("") ? "" : schedule, "(多人)" + title);
                                scheduleDao.insert(sch);

                                KeyValue kv = new KeyValue(objectId, title.equals("") ? "未命名任务" : title);
                                mapDao.insert(kv);
//                            Toast.makeText(AddAssignmentActivity.this,"加入数据库成功",Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(AddAssignmentActivity.this, "创建数据失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
}