package activities;



import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.tufengyi.sharp.R;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import bean.DateEntity;
import dao.DateUtil;
import dao.ScheduleDao;
import bean.Schedule;
import view.DataView;

public class MainActivity extends AppCompatActivity {

    private int[] backs = {
            R.drawable.main_back1,
            R.drawable.main_back2,
            R.drawable.main_back3,
            R.drawable.main_back4,
            R.drawable.main_back5,
            R.drawable.main_back6
    };

    private DataView dataView;

//    implements MyLinearLayout.OnScrollListener
//    private MyLinearLayout mLastScrollView;

    //用来右滑打开菜单
  //  private DrawerLayout mDrawerLayout;

    //这里看一下list可不可以删掉（应该不可以）
    //理由：list存着不改的数据，然后在类似复制+修改的过程存在mData中并显示出来
    private List<Schedule> mList;
    private List<Schedule> mData;
   // private List<BackGround> mBack;

    //sql增删改查的封装
    private ScheduleDao dao;

    //private BackGroundDao backGroundDao;

    //加载listview
    private ListView lv_schedule;

    //消息回调
    Handler myHandler = new Handler();

    private TextView tv_search,tv_left;
    private LinearLayout llv_edt;

    //适配器
    MyAdapter adapter;

    //把内容放在onResume中可以实现刷新list内容
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//
        llv_edt = (LinearLayout) findViewById(R.id.lv_edt);
        llv_edt.getBackground().setAlpha(50);


        //仅create活动的时候初始到今天
        tv_left = (TextView) findViewById(R.id.tv_left);
        tv_search= (TextView) findViewById(R.id.tv_day);


//        Button btn_list =(Button) findViewById(R.id.btn_list);
//        btn_list.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this,FinishedListActivity.class);
//                startActivity(intent);
//            }
//        });


    }

    @Override
    protected void onResume(){
        super.onResume();





        BoomMenuButton bmb = (BoomMenuButton) findViewById(R.id.bmb);
        assert bmb != null;
        bmb.setButtonEnum(ButtonEnum.TextOutsideCircle);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_2_2);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_2_2);
        //更改原片颜色
        bmb.setNormalColor(0xFF8C5EFF);
       // 背景颜色
      //  bmb.setBackgroundColor(Color.BLACK);
        //设置阴影颜色
         //bmb.setShadowColor(Color.WHITE);
        //设置打开后的背景颜色
        // bmb.setDimColor(Color.WHITE);
        bmb.clearBuilders();
        TextOutsideCircleButton.Builder builder1 = new TextOutsideCircleButton.Builder()
                .normalImageRes(R.drawable.complete)
                .normalText("查看已完成")
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            startActivity(new Intent(MainActivity.this,FinishedListActivity.class),ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                        }
                    }
                });
        bmb.addBuilder(builder1);

        TextOutsideCircleButton.Builder builder2 = new TextOutsideCircleButton.Builder()
                .normalImageRes(R.drawable.addimg)
                .normalText("建立新任务")
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        Intent intent = new Intent(MainActivity.this,AddingActivity.class);
                        startActivity(intent);
                    }
                });
        bmb.addBuilder(builder2);

        //通过edittext进行过滤listview
        //要注意的是，后面进行排序后的展现，不可以设置if(mData.size>0)否则还没存入数据的时候就调用此方法会出bug




        EditTextChanged();
        //试试吧tv放到上面去（先告诉你


        initView();

        dao = new ScheduleDao(MainActivity.this);
        //将数据库中的数据调到list、mData中
        mList=dao.queryAll();

        Calendar cal = Calendar.getInstance();

        final String data = cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DAY_OF_MONTH);
        //  info = (TextView) findViewById(R.id.info);
        dataView = (DataView) findViewById(R.id.week);
        dataView.getBackground().setAlpha(250);
        dataView.setOnSelectListener(new DataView.OnSelectListener() {
            @Override
            public void onSelected(DateEntity date) {
                tv_search.setText(date.date.contains(data)? "今天":date.date);
                tv_left.setText("正在查看：");
            }
        });
        dataView.getData(data);



        //初始化，已进入就显示当天的
        mData=new ArrayList<Schedule>();

//        Calendar maincal = Calendar.getInstance();


//        if(mData.size()>=0) {     //这里必须设置为>=0否则一开始是没有数据的，当editchange时候app会崩溃！
        //这里list已经添加好了属性，可以进行排序
        Collections.sort(mList, new Comparator<Schedule>() {
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

        int length = mList.size();
        for(int i = 0; i < length; i++){
            if(mList.get(i).getDate()
                    .contains(cal.get(Calendar.YEAR)
                            +"-"+
                            ((cal.get(Calendar.MONTH)+1)<10?
                                    ("0"+(cal.get(Calendar.MONTH)+1)):(cal.get(Calendar.MONTH)+1))
                            +"-"+
                            (cal.get(Calendar.DAY_OF_MONTH)<10?
                            ("0"+cal.get(Calendar.DAY_OF_MONTH)):cal.get(Calendar.DAY_OF_MONTH)))
                    && mList.get(i).getState().contains("未完成")){
                Schedule item = mList.get(i);
                mData.add(item);
            }
        }

        //
        assert lv_schedule != null;

        adapter = new MyAdapter();
        lv_schedule.setAdapter(adapter);
        lv_schedule.setEmptyView(findViewById(R.id.img_empty));
        findViewById(R.id.img_empty).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "今天没有日程，快去添加吧！", Toast.LENGTH_SHORT).show();
            }
        });

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




    private void initView(){


        LinearLayout tv_home = (LinearLayout) findViewById(R.id.img_first);
        tv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ProgressActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout tv_Bmob = (LinearLayout) findViewById(R.id.img_third);
        tv_Bmob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,BmobActivity.class);
                startActivity(intent);

            }
        });

        //准备listview的item的点击事件
        lv_schedule = findViewById(R.id.listview);

    }


    /*
     * 配适器
     */
    private class MyAdapter extends BaseAdapter{


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
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, null);

                int index = (int) (Math.random() * 6);

                viewHolder = new ViewHolder();
                viewHolder.ll_main = (LinearLayout) convertView.findViewById(R.id.lin_root);
               // viewHolder.ll_main.getBackground().setAlpha(180);
                viewHolder.ll_item = (LinearLayout) convertView.findViewById(R.id.ll_clip);
                viewHolder.ll_item.setBackgroundResource(backs[index]);
              //  viewHolder.ll_item.getBackground().setAlpha(190);

                viewHolder.text_date = (TextView) convertView.findViewById(R.id.show_time);
                viewHolder.text_schedule =(TextView) convertView.findViewById(R.id.show_schedule);
                viewHolder.text_tab = (TextView) convertView.findViewById(R.id.tv_tab);
                viewHolder.bmb2 = (BoomMenuButton) convertView.findViewById(R.id.bmb2);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Calendar cal = Calendar.getInstance();

            final Schedule sch = mData.get(position);
            //如果position=0（第一个）则不能进行position-1，否则出现错误
            if(position>0){
            final Schedule lastSch = mData.get(position-1);
                    viewHolder.text_tab.setText(sch.getDate().substring(11, 13)
                            .equals(lastSch.getDate().substring(11, 13)) ? "" : sch.getDate().substring(11, 13) + ":00");

            }else{
                    viewHolder.text_tab.setText(sch.getDate().substring(11, 13) + ":00");

            }

            //如果是今天就显示今天
            //注意textview的过滤效果
//               viewHolder.text_date.setText(sch.getDate()
//                    .contains(cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DAY_OF_MONTH))?
//            "今天":sch.getDate());
            viewHolder.text_schedule.setText(sch.getTitle());
            //在任务item的右边textview中
            //如果是今天就是今天，如果不是今天就是具体时间，如果具体时间没有定，那么就显示“未定时间”
            //注意的是这里只是显示，具体的时间数据是存在数据库中的
            viewHolder.text_date.setText(sch.getDate()
                    .contains(cal.get(Calendar.YEAR)
                            +"-"+
                            ((cal.get(Calendar.MONTH)+1)<10?
                                    ("0"+(cal.get(Calendar.MONTH)+1)):(cal.get(Calendar.MONTH)+1))
                            +"-"+
                            (cal.get(Calendar.DAY_OF_MONTH)<10?
                                    ("0"+cal.get(Calendar.DAY_OF_MONTH)):cal.get(Calendar.DAY_OF_MONTH)))?
            "今天":sch.getDate());



            viewHolder.bmb2.clearBuilders();
                HamButton.Builder builder1 = new HamButton.Builder()
                        .normalImageRes(R.drawable.bianji)
                        .normalText("编辑事件")
                        .subNormalText("edit this schedule")
                        .listener(new OnBMClickListener() {
                            @Override
                            public void onBoomButtonClick(int index) {
                                Intent intent = new Intent(MainActivity.this,CorrectingActivity.class);
                                intent.putExtra("getId",sch.getId());
                                intent.putExtra("getDate",sch.getDate());
                                intent.putExtra("getSchedule",sch.getSchedule());
                                intent.putExtra("getState",sch.getState());
                                intent.putExtra("getTitle",sch.getTitle());
                                startActivity(intent);
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("确定要删除吗？");
                                builder.setPositiveButton("确定",listener);
                                builder.setNegativeButton("取消",null);
                                builder.show();
                        }
                    });
            viewHolder.bmb2.addBuilder(builder2);

            HamButton.Builder builder3 = new HamButton.Builder()
                    .normalImageRes(R.drawable.gou)
                    .subNormalText("finished")
                    .normalText("已完成")
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            long id = sch.getId();
                            String date = sch.getDate();
                            String title = sch.getTitle();
                            String schedule = sch.getSchedule();
                            String state ="已完成";

                            //更新状态为已完成
                            Schedule finishedSch = new Schedule(id,date,state,schedule,title);
                            dao = new ScheduleDao(MainActivity.this);
                            dao.update(finishedSch);
                            //这里仅仅remove，然后再已完成清单中可以查看
                            mData.remove(position);
                            adapter.notifyDataSetChanged();


                            Toast.makeText(MainActivity.this,"成功加入清单",Toast.LENGTH_SHORT).show();
                        }
                    });
            viewHolder.bmb2.addBuilder(builder3);
           // }

            return convertView;




        }
    }

    class ViewHolder{
        TextView text_date;
        TextView text_schedule;
        TextView text_tab;
        LinearLayout ll_main;
        LinearLayout ll_item;
        BoomMenuButton bmb2;
    }



    /*
     * 设置过滤器
     */
    private void EditTextChanged()
    {
      //  edt_search = (TextView) findViewById(R.id.tv_day);
       // now_day = (TextView) findViewById(R.id.now_day);
        tv_search.addTextChangedListener(new TextWatcher() {

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
            String data = tv_search.getText().toString();
            //清空mData
            mData.clear();
            //往mData中添加schedule
            //如果是今天就只过滤出今天
            Calendar cal = Calendar.getInstance();
//            getmDataSub(mData, data.contains(cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DAY_OF_MONTH))?
//            "今天".trim():data);这个逻辑是错误的，因为 getmDataSub里面的getDate需要的得到的不是“今天”而是“今天”的yyyymmdd
            getmDataSub(mData, data.contains("今天")?
                 cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DAY_OF_MONTH):data);

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
                    && mList.get(i).getState().contains("未完成")){
                Schedule item = mList.get(i);
                mData.add(item);
            }
        }
    }



}