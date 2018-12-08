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
import android.widget.Toast;


import com.example.tufengyi.sharp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dao.ScheduleDao;
import bean.Schedule;

public class AddingActivity extends AppCompatActivity implements View.OnTouchListener{

    private EditText edt_schedule,edt_test,edt_title;
    private ScheduleDao dao;
//    private Spinner spinner;
    private ImageView imageView;
    private TextView tv_state;

    List<String> state = new ArrayList<String>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setEnterTransition(new Slide().setDuration(200));
//            getWindow().setExitTransition(new Slide().setDuration(150));
//        }

        dao = new ScheduleDao(AddingActivity.this);
       // initSpinner();
        initView();
    }

//    @Override
//    protected void onPause(){
//        super.onPause();
//        overridePendingTransition(0,0);
//    }

    private void initView(){

        //返回键
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //设置一下默认时间
        Calendar cal = Calendar.getInstance();
        //不严谨，换成下面那个uT
//        final String undefinedTime = cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DAY_OF_MONTH)+" "
//                +cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE);

        final String undefinedTime=cal.get(Calendar.YEAR)
                +"-"+
                ((cal.get(Calendar.MONTH)+1)<10?
                        ("0"+(cal.get(Calendar.MONTH)+1)):(cal.get(Calendar.MONTH)+1))
                +"-"+
                (cal.get(Calendar.DAY_OF_MONTH)<10?
                        ("0"+cal.get(Calendar.DAY_OF_MONTH)):cal.get(Calendar.DAY_OF_MONTH))
                +" "
                +(cal.get(Calendar.HOUR_OF_DAY)<10?"0"+cal.get(Calendar.HOUR_OF_DAY):cal.get(Calendar.HOUR_OF_DAY))
                +":"+
                (cal.get(Calendar.MINUTE)<10?"0"+cal.get(Calendar.MINUTE):cal.get(Calendar.MINUTE));

        // imageView = (ImageView) findViewById(R.id.image_situation);
        edt_title = (EditText) findViewById(R.id.edt_title);
        edt_test = (EditText) findViewById(R.id.test);
        edt_test.setOnTouchListener(this);
        tv_state = (TextView) findViewById(R.id.edt_state);
        edt_schedule = (EditText)findViewById(R.id.edt_schedule);
        Button confirmToBack = (Button) findViewById(R.id.btn_confirm);
        confirmToBack.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v){
                String date = edt_test.getText().toString().trim();
                String schedule = edt_schedule.getText().toString().trim();
                String state = tv_state.getText().toString().trim();
                String title = edt_title.getText().toString().trim();

                if(schedule.isEmpty()&&title.isEmpty()){
                    Toast.makeText(AddingActivity.this, "请输入具体信息噢！", Toast.LENGTH_SHORT).show();
                }else {
                    //如果用户只输入了schedule，那么标题就用schedule代替，注意最大行数的控制
                    Schedule sch = new Schedule(date.equals("") ? undefinedTime : date, state, schedule.equals("") ? "" : schedule, title.equals("") ? schedule : title);

                    //更新数据库
                    dao.insert(sch);


                    //  回到MainActivity
                    Intent intent = new Intent(AddingActivity.this, MainActivity.class);
                    startActivity(intent);

                }
            }
        });
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

    /*
     * 预览图片
     */
    private class itemSelected implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> parent,View view,int position,long id){
            ArrayAdapter<String> adapter = (ArrayAdapter<String>)parent.getAdapter();
        //    tv_state.setText(adapter.getItem(position));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent){}
    }
}
