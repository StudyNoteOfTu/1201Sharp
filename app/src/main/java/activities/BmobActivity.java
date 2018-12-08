package activities;


import android.app.ActivityOptions;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.tufengyi.sharp.R;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import bean.TaskReview;

import cn.bmob.v3.Bmob;
import dao.DateUtil;
import dao.ScheduleDao;
import dao.TaskReviewDao;

public class BmobActivity extends AppCompatActivity {
    private LinearLayout btn_query,btn_list;
    private List<TaskReview> mData;
    private ListView lv_assignment;
    private TaskReviewDao taskReviewDao;
    private ScheduleDao scheduleDao;

    MyAdapter adapter;

    private int[] dw={
            R.drawable.bmb_back1,
            R.drawable.bmb_back2,
            R.drawable.bmb_back3,
            R.drawable.bmb_back4,
            R.drawable.bmb_back5,
            R.drawable.bmb_back6
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onResume(){
        super.onResume();
        Bmob.initialize(this,"f4939b1bc990101036756a6398c7a159");
        setContentView(R.layout.activity_bmob);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_bmob);
        fab.setRippleColor(Color.TRANSPARENT);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(new Intent(BmobActivity.this,AddAssignmentActivity.class),ActivityOptions.makeSceneTransitionAnimation(BmobActivity.this).toBundle());
                }
            }
        });



        initView();
        lv_assignment = findViewById(R.id.listview);
       // lv_assignment.setOnItemClickListener(new MyOnItemClickListener());
        taskReviewDao = new TaskReviewDao(this);
        scheduleDao = new ScheduleDao(this);
        mData = taskReviewDao.queryAll();

        //排序显示，时间晚的在上（降序排序）
        Collections.sort(mData, new Comparator<TaskReview>() {
            @Override
            public int compare(TaskReview task1, TaskReview task2) {
                //将String型转成long型（时间戳）进行排序(从上到下降序)
                long num1 = DateUtil.stringToDate(task1.getDudate());
                long num2 = DateUtil.stringToDate(task2.getDudate());
                if (num1 < num2) {
                    return 1;
                } else if (num1 > num2) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        adapter = new MyAdapter();
        lv_assignment.setAdapter(adapter);
        lv_assignment.setEmptyView(findViewById(R.id.img_isEmpty));
        findViewById(R.id.img_isEmpty).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BmobActivity.this, "暂时没有任务，快去发布/获取吧！", Toast.LENGTH_SHORT).show();
            }
        });

        lv_assignment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BoomMenuButton bmb = (BoomMenuButton) view.findViewById(R.id.bmb2);
                bmb.boom();
            }
        });

    }
    @Override
    protected void onPause(){
        overridePendingTransition(0,0);
        super.onPause();
    }

    private void initView(){

        LinearLayout tv_home = (LinearLayout) findViewById(R.id.img_first);
        tv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BmobActivity.this,ProgressActivity.class);
                startActivity(intent);
            }
        });


        LinearLayout tv_main = (LinearLayout) findViewById(R.id.img_second);
        tv_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(BmobActivity.this,MainActivity.class);
                startActivity(intent);

            }
        });


        btn_query = (LinearLayout) findViewById(R.id.btn_query);
    //    btn_query.getBackground().setAlpha(150);
        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(BmobActivity.this,CatchAssignmentActivity.class);
//                startActivity(intent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(new Intent(BmobActivity.this,CatchAssignmentActivity.class),ActivityOptions.makeSceneTransitionAnimation(BmobActivity.this).toBundle());
                }
            }
        });
        btn_list = (LinearLayout) findViewById(R.id.btn_list);
        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(new Intent(BmobActivity.this,KeysListActivity.class),ActivityOptions.makeSceneTransitionAnimation(BmobActivity.this).toBundle());
                }
            }
        });



    }

    private class MyAdapter extends BaseAdapter{
        public int getCount(){
            return mData.size();
        }
        public Object getItem(int position){
            return mData.get(position);
        }
        public long getItemId(int position){
            return position;
        }
        public View getView(final int position,View convertView,final ViewGroup parent){
            final ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bmob, null);

                final int num = (int) (Math.random()*6);
                viewHolder = new ViewHolder();
                viewHolder.ll_bmob_item = (LinearLayout) convertView.findViewById(R.id.bmob_clip);
                viewHolder.ll_bmob_item.setBackgroundResource(dw[num]);
                viewHolder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
                viewHolder.tv_title =(TextView) convertView.findViewById(R.id.tv_title);
                viewHolder.bmb2 = (BoomMenuButton) convertView.findViewById(R.id.bmb2);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            final TaskReview tr = mData.get(position);
            viewHolder.tv_date.setText(tr.getDudate());
            viewHolder.tv_title.setText(tr.getTitle());


            viewHolder.bmb2.clearBuilders();

            HamButton.Builder builder1 = new HamButton.Builder()
                    .normalImageRes(R.drawable.update)
                    .normalText("个人进度")
                    .subNormalText("edit this schedule")
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            Intent intent = new Intent(BmobActivity.this,TaskUpdateProgressActivity.class);
                            intent.putExtra("getId",tr.getId());
                            intent.putExtra("getSchedule",tr.getSchedule());
                            intent.putExtra("getDate",tr.getDudate());
                            intent.putExtra("getTitle",tr.getTitle());
                            intent.putExtra("getObjId",tr.getObjId());

                            //增加个人任务和名字进度本地数据库
                            intent.putExtra("getProgress",tr.getProgress());
                            intent.putExtra("getName",tr.getName());
                            startActivity(intent);

                        }
                    });
            viewHolder.bmb2.addBuilder(builder1);

            HamButton.Builder builder2 = new HamButton.Builder()
                    .normalImageRes(R.drawable.lajitong)
                    .normalText("删除任务")
                    .subNormalText("delete")
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {

                            final DialogInterface.OnClickListener listener = new android.content.DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog,int which){
                                    mData.remove(tr);
                                    taskReviewDao.delete(tr.getId());
                                    notifyDataSetChanged();
                                }
                            };
                            AlertDialog.Builder builder = new AlertDialog.Builder(BmobActivity.this);
                            builder.setTitle("确定要删除吗？");
                            builder.setPositiveButton("确定",listener);
                            builder.setNegativeButton("取消",null);
                            builder.show();
                        }
                    });
            viewHolder.bmb2.addBuilder(builder2);

            HamButton.Builder builder3 = new HamButton.Builder()
                    .normalImageRes(R.drawable.board)
                    .subNormalText("check")
                    .normalText("团队进度")
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                           Intent intent = new Intent(BmobActivity.this,TaskReviewActivity.class);
                           intent.putExtra("getSchedule",tr.getSchedule());
                           intent.putExtra("getObjId",tr.getObjId());
                           startActivity(intent);
                        }
                    });
            viewHolder.bmb2.addBuilder(builder3);

//            HamButton.Builder builder4 = new HamButton.Builder()
//                    .normalImageRes(R.drawable.bmob_update_btn_check_off_holo_light)
//                    .subNormalText("unkown")
//                    .normalText("暂时不知道做些什么")
//                    .listener(new OnBMClickListener() {
//                        @Override
//                        public void onBoomButtonClick(int index) {
//                        }
//                    });
//            viewHolder.bmb2.addBuilder(builder4);


            return convertView;

        }
    }
    class ViewHolder{
        LinearLayout ll_bmob_item;
        TextView tv_date;
        TextView tv_title;
        BoomMenuButton bmb2;
    }


}