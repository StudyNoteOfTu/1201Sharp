package activities;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tufengyi.sharp.R;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import bean.DateEntity;
import bean.Schedule;
import dao.DateUtil;
import dao.ScheduleDao;
import view.DataView;

public class FinishedListActivity extends AppCompatActivity {

    private List<Schedule> mList;
    private List<Schedule> mData;

    //sql增删改查的封装
    private ScheduleDao dao;
    //加载listview
    private MyListView lv_schedule;

    //消息回调
    Handler myHandler = new Handler();

    private TextView tv_date,tv_left;

    //适配器
    MyAdapter adapter;

    //把内容放在onResume中可以实现刷新list内容
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finishedlist);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Slide().setDuration(200));
            getWindow().setExitTransition(new Slide().setDuration(200));
        }

    }

    @Override
    protected void onResume(){
        super.onResume();
        //返回键
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Calendar cal_tv = Calendar.getInstance();
        final String toMonth = cal_tv.get(Calendar.YEAR)+"-"+ ((cal_tv.get(Calendar.MONTH)+1)<10?
                ("0"+(cal_tv.get(Calendar.MONTH)+1)):(cal_tv.get(Calendar.MONTH)+1));


        final String toDay =  cal_tv.get(Calendar.YEAR)
                +"-"+
                ((cal_tv.get(Calendar.MONTH)+1)<10?
                        ("0"+(cal_tv.get(Calendar.MONTH)+1)):(cal_tv.get(Calendar.MONTH)+1))
                +"-"+
                (cal_tv.get(Calendar.DAY_OF_MONTH)<10?
                        ("0"+cal_tv.get(Calendar.DAY_OF_MONTH)):cal_tv.get(Calendar.DAY_OF_MONTH));

        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_date.setText(toDay);

        EditTextChanged();


        final Button btn_checkAll = (Button) findViewById(R.id.btn_checkall);
        //查看当月
        btn_checkAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn_checkAll.getText().equals("查看当月")) {
                    tv_date.setText(toMonth);
                    btn_checkAll.setText("查看今天");
                }else if(btn_checkAll.getText().equals("查看今天")){
                    tv_date.setText(toDay);
                    btn_checkAll.setText("查看当月");
                }
            }
        });

        lv_schedule = findViewById(R.id.listview);

        dao = new ScheduleDao(FinishedListActivity.this);
        //将数据库中的数据调到list、mData中
        mList=dao.queryAll();
        //初始化，已进入就显示当天的已完成任务
        mData=new ArrayList<Schedule>();
        Calendar cal = Calendar.getInstance();
        int length = mList.size();
        for(int i = 0; i < length; i++){
            if(mList.get(i).getDate().contains(cal.get(Calendar.YEAR)
                    +"-"+
                    ((cal.get(Calendar.MONTH)+1)<10?
                            ("0"+(cal.get(Calendar.MONTH)+1)):(cal.get(Calendar.MONTH)+1))
                    +"-"+
                    (cal.get(Calendar.DAY_OF_MONTH)<10?
                            ("0"+cal.get(Calendar.DAY_OF_MONTH)):cal.get(Calendar.DAY_OF_MONTH)))
                    && mList.get(i).getState().equals("已完成")){
                Schedule item = mList.get(i);
                mData.add(item);
            }
        }

//        if(mData.size()>=0) {     //这里必须设置为>=0否则一开始是没有数据的，当editchange时候app会崩溃！
        //这里list已经添加好了属性，可以进行排序
        Collections.sort(mData, new Comparator<Schedule>() {
            @Override
            public int compare(Schedule schedule1, Schedule schedule2) {
                //将String型转成long型（时间戳）进行排序(从上到下升序)
                //获得时间，仍然正常排序？
                long num1 = DateUtil.stringToDate(schedule1.getDate());
                long num2 = DateUtil.stringToDate(schedule2.getDate());
                if (num1 > num2) {
                    return 1;
                } else if (num1 < num2) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        //
        assert lv_schedule != null;

        adapter = new MyAdapter();
        lv_schedule.setAdapter(adapter);
        lv_schedule.setEmptyView(findViewById(R.id.img_empty));

        //
        lv_schedule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BoomMenuButton bmb = (BoomMenuButton) view.findViewById(R.id.bmb2);
                bmb.boom();
            }
        });

    }

    @Override
    protected void onPause(){
        super.onPause();
        overridePendingTransition(0,0);
    }







    /*
     * 配适器
     */
    private class MyAdapter extends BaseAdapter {

//        private View.OnClickListener mDelClickListener;
        //这里改成了public
//        private MyLinearLayout.OnScrollListener mScrollListener;

        //对应要展现的是mData数据，所以这里面使用mData而不是mList
        public int getCount(){
            return mData.size();
            // return 1000;
        }
        public Object getItem(int position){
            return mData.get(position);
            //return null;
        }
        public long getItemId(int position){
            return position;
            //return 0;
        }
        public View getView(final int position, View convertView, final ViewGroup parent){
            final ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_finished, null);

                viewHolder = new ViewHolder();
                viewHolder.text_date = (TextView) convertView.findViewById(R.id.tv_date);
                viewHolder.text_title =(TextView) convertView.findViewById(R.id.tv_title);
                viewHolder.bmb2 = (BoomMenuButton) convertView.findViewById(R.id.bmb2);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Calendar cal = Calendar.getInstance();

            final Schedule sch = mData.get(position);

            viewHolder.text_title.setText(sch.getTitle());
            //在任务item的右边textview中
            //如果是今天就是今天，如果不是今天就是具体时间，如果具体时间没有定，那么就显示“未定时间”
            //注意的是这里只是显示，具体的时间数据是存在数据库中的
            viewHolder.text_date.setText(sch.getDate());

            //如果是今天那么设置字体颜色变化或者背景变化



            viewHolder.bmb2.clearBuilders();

            HamButton.Builder builder1 = new HamButton.Builder()
                    .normalImageRes(R.drawable.bianji)
                    .normalText("编辑事件")
                    .subNormalText("check this schedule")
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            Intent intent = new Intent(FinishedListActivity.this,CorrectingActivity.class);
                            intent.putExtra("getId",sch.getId());
                            intent.putExtra("getDate",sch.getDate());
                            intent.putExtra("getSchedule",sch.getSchedule());
                            intent.putExtra("getState",sch.getState());
                            intent.putExtra("getTitle",sch.getTitle());
//                                startActivity(intent);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(FinishedListActivity.this).toBundle());
                            }
                        }
                    });
            viewHolder.bmb2.addBuilder(builder1);

            HamButton.Builder builder2 = new HamButton.Builder()
                    .normalImageRes(R.drawable.lajitong)
                    .normalText("删除事件")
                    .subNormalText("delete this schedule")
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            final DialogInterface.OnClickListener listener = new android.content.DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog,int which){
                                    mData.remove(sch);
                                    mList.remove(sch);
                                    dao.delete(sch.getId());
                                    notifyDataSetChanged();
                                }
                            };
                            AlertDialog.Builder builder = new AlertDialog.Builder(FinishedListActivity.this);
                            builder.setTitle("确定要删除吗？");
                            builder.setPositiveButton("确定",listener);
                            builder.setNegativeButton("取消",null);
                            builder.show();
                        }
                    });
            viewHolder.bmb2.addBuilder(builder2);

            HamButton.Builder builder3 = new HamButton.Builder()
                    .normalImageRes(R.drawable.unfinish)
                    .subNormalText(" un finished")
                    .normalText("未完成")
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            long id = sch.getId();
                            String date = sch.getDate();
                            String title = sch.getTitle();
                            String schedule = sch.getSchedule();
                            String state ="未完成";

                            //更新状态为已完成
                            Schedule finishedSch = new Schedule(id,date,state,schedule,title);
                            dao = new ScheduleDao(FinishedListActivity.this);
                            dao.update(finishedSch);
                            //remove之后可以再时间轴那里查看
                            mData.remove(position);
                            adapter.notifyDataSetChanged();


                            Toast.makeText(FinishedListActivity.this,"设置为未完成，在时间轴中查看",Toast.LENGTH_SHORT).show();
                        }
                    });
            viewHolder.bmb2.addBuilder(builder3);
            // }

            return convertView;

        }
    }

    class ViewHolder{
        TextView text_date;
        TextView text_title;
        BoomMenuButton bmb2;
    }





    /*
     * 设置过滤器
     */
    private void EditTextChanged()
    {

        tv_date.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                //消息回调，进行更改mData
                myHandler.post(eChanged);
            }
        });

    }

    Runnable eChanged = new Runnable() {
        @Override
        public void run() {
            //获得editText中的数据（过滤后留下的目标）
            String data = tv_date.getText().toString();
            //清空mData
            mData.clear();
            //往mData中添加schedule
            //如果是今天就只过滤出今天
            Calendar cal = Calendar.getInstance();
//            getmDataSub(mData, data.contains(cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DAY_OF_MONTH))?
//            "今天".trim():data);这个逻辑是错误的，因为 getmDataSub里面的getDate需要的得到的不是“今天”而是“今天”的yyyymmdd
            getmDataSub(mData, data.contains("今天")?
                    cal.get(Calendar.YEAR)
                            +"-"+
                            ((cal.get(Calendar.MONTH)+1)<10?
                                    ("0"+(cal.get(Calendar.MONTH)+1)):(cal.get(Calendar.MONTH)+1))
                            +"-"+
                            (cal.get(Calendar.DAY_OF_MONTH)<10?
                                    ("0"+cal.get(Calendar.DAY_OF_MONTH)):cal.get(Calendar.DAY_OF_MONTH))
                    :data);

            //listview刷新
            adapter.notifyDataSetChanged();

        }
    };

    /*
     * 判断对应数据的Date是否和editText中的一致，如果不一致则过滤掉（不显示）
     */
    private void getmDataSub(List<Schedule> mData, String data) {
        int length = mList.size();
        for(int i = 0; i < length; i++){
            if(mList.get(i).getDate().contains(data)
                    && mList.get(i).getState().equals("已完成")){
                Schedule item = mList.get(i);
                mData.add(item);
            }
        }
    }



}
