package activities;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.transition.Slide;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;


import com.example.tufengyi.sharp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dao.ScheduleDao;
import bean.Schedule;

public class CorrectingActivity extends AppCompatActivity implements View.OnTouchListener{
    private EditText edt_schedule,edt_test,edt_title;
    private ScheduleDao dao;
    private long id;
  //  private Spinner spinner;
   // private ImageView imageView;
    private TextView tv_state;
    ArrayAdapter<String> adapter;
    List<String> state = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correct);

        Intent intent = getIntent();
        id = intent.getLongExtra("getId", 0);
        String date = intent.getStringExtra("getDate");
        String schedule = intent.getStringExtra("getSchedule");
        String stateString = intent.getStringExtra("getState");
        String title = intent.getStringExtra("getTitle");

        dao = new ScheduleDao(this);

//        initSpinner();

        //返回键
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //  imageView = (ImageView) findViewById(R.id.image_situation);
        edt_title = (EditText) findViewById(R.id.edt_title);
        edt_title.setText(title);
        edt_test = (EditText) findViewById(R.id.test);
        edt_test.setText(date);
        edt_test.setOnTouchListener(this);
        tv_state = (TextView) findViewById(R.id.edt_state);
        tv_state.setText(stateString);
//        switch (tv_state.getText().toString().trim()){
//            case "稍微放松": imageView.setImageDrawable(getResources().getDrawable(R.drawable.lefttime));break;
//            case "稍微紧张": imageView.setImageDrawable(getResources().getDrawable(R.drawable.add)); break;
//            case "非常紧张": imageView.setImageDrawable(getResources().getDrawable(R.drawable.maincal));break;
//        }
        edt_schedule = (EditText) findViewById(R.id.edt_schedule);
        edt_schedule.setText(schedule);

        Button confirmToBack = (Button) findViewById(R.id.btn_confirm);
        confirmToBack.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                long idCopy = id;
                String date = edt_test.getText().toString().trim();
                String schedule = edt_schedule.getText().toString().trim();
                String state = tv_state.getText().toString().trim();
                String title = edt_title.getText().toString();
                //传入id，不可以漏掉！
                Schedule sch = new Schedule(idCopy, date.equals("") ? "" : date, state, schedule.equals("") ? "" : schedule, title);

                //更新数据库
                dao.update(sch);

                //下面这个intent和finish重复了，而且会撤销动画
//                Intent intent = new Intent(CorrectingActivity.this,MainActivity.class);
//                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = View.inflate(this,R.layout.date_time_dialog,null);
            final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
            final TimePicker timePicker = (TimePicker) view.findViewById(R.id.time_picker);
            builder.setView(view);

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());
            datePicker.init(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH),null);

            timePicker.setIs24HourView(true);
            timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
            timePicker.setCurrentMinute(Calendar.MINUTE);

            if(v.getId() == R.id.test){
                final int inType = edt_test.getInputType();
                edt_test.setInputType(InputType.TYPE_NULL);
                edt_test.onTouchEvent(event);
                edt_test.setInputType(inType);
                edt_test.setSelection(edt_test.getText().length());

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

                        edt_test.setText(sb);
                        dialog.cancel();
                    }
                });
            }
            Dialog dialog = builder.create();
            dialog.show();
        }
        return true;
    }
}
