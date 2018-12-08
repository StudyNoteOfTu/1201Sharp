package activities;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.tufengyi.sharp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.Note;
import bean.Schedule;
import dao.NoteDao;
import dao.ScheduleDao;


/*
 * 在主界面显示最近三天待完成日程，昨天今天明天，待完成的日程
 */
public class ProgressActivity  extends AppCompatActivity {

    private NoteDao noteDao;
    private ScheduleDao scheduleDao;
    private List<Schedule> list_sch;

    private List<Note> list_note = new ArrayList<>();
    private List<Note> list_note_life = new ArrayList<>();
    private List<Note> list_note_work =new ArrayList<>();
    private List<Note> list_note_study=new ArrayList<>();


    private Map<String,List<Note>> dataset = new HashMap<>();
    private List<String> parentList = new ArrayList<>();
  //  private List<List<Note>> children = new ArrayList<>();
    private MyExpandableListViewAdapter adapter;
    private ExpandableListView listview;


    private ImageView btn_bg;
    private Button btn_life,btn_study,btn_work;
  //  private RelativeLayout rl;


    private int circle_color[]={
      R.drawable.circle_left1,
      R.drawable.circle_left2,
      R.drawable.circle_left3,
      R.drawable.circle_left4,
      R.drawable.circle_left5,
      R.drawable.circle_left6
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        LinearLayout tv_main = (LinearLayout) findViewById(R.id.img_second);
        tv_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProgressActivity.this,MainActivity.class);

                startActivity(intent);
            }
        });

        LinearLayout tv_bmob = (LinearLayout) findViewById(R.id.img_third);
        tv_bmob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProgressActivity.this,BmobActivity.class);
                startActivity(intent);
            }
        });




    }



    @Override
    protected void onResume() {
        super.onResume();
        Calendar cal = Calendar.getInstance();
        scheduleDao = new ScheduleDao(ProgressActivity.this);
        noteDao = new NoteDao(ProgressActivity.this);


        //   lv_task =  findViewById(R.id.lv_task);
        //lv_undefined = findViewById(R.id.lv_undefined);
        listview = (ExpandableListView) findViewById(R.id.expandablelistview);
        parentList = new ArrayList<String>();
        parentList.add("生活");
        parentList.add("学习");
        parentList.add("工作");


        //暂时还缺child添加，还有两个的item布局
        list_note.clear();
        list_note = noteDao.queryAll();
        list_note_life.clear();
        list_note_work.clear();
        list_note_study.clear();
        for (int i = 0; i < list_note.size(); i++) {
            if (list_note.get(i).getSort().contains("生活")) {
                list_note_life.add(list_note.get(i));
            }
            if (list_note.get(i).getSort().contains("学习")) {
                list_note_study.add(list_note.get(i));
            }
            if (list_note.get(i).getSort().contains("工作")) {
                list_note_work.add(list_note.get(i));
            }
        }

        dataset.put(parentList.get(0), list_note_life);
        dataset.put(parentList.get(1), list_note_study);
        dataset.put(parentList.get(2), list_note_work);

        adapter = new MyExpandableListViewAdapter();
        listview.setAdapter(adapter);

        list_sch = scheduleDao.queryAll();

        //接下来我们进行一次统计
        //  1.统计本月中近三天未完成任务
        int todayLeft = 0;
        int length = list_sch.size();
        for (int i = 0; i < length; i++) {
            if (list_sch.get(i).getState().contains("未完成") &&
                    (list_sch.get(i).getDate().contains(cal.get(Calendar.YEAR)
                            + "-" +
                            ((cal.get(Calendar.MONTH) + 1) < 10 ?
                                    ("0" + (cal.get(Calendar.MONTH) + 1)) : (cal.get(Calendar.MONTH) + 1))
                            + "-" +
                            (cal.get(Calendar.DAY_OF_MONTH) < 10 ?
                                    ("0" + cal.get(Calendar.DAY_OF_MONTH)) : cal.get(Calendar.DAY_OF_MONTH)))
                            || list_sch.get(i).getDate().contains(cal.get(Calendar.YEAR)
                            + "-" +
                            ((cal.get(Calendar.MONTH) + 1) < 10 ?
                                    ("0" + (cal.get(Calendar.MONTH) + 1)) : (cal.get(Calendar.MONTH) + 1))
                            + "-" +
                            ((cal.get(Calendar.DAY_OF_MONTH) - 1) < 10 ?
                                    ("0" + (cal.get(Calendar.DAY_OF_MONTH) - 1)) : (cal.get(Calendar.DAY_OF_MONTH) - 1)))
                            || list_sch.get(i).getDate().contains(cal.get(Calendar.YEAR)
                            + "-" +
                            ((cal.get(Calendar.MONTH) + 1) < 10 ?
                                    ("0" + (cal.get(Calendar.MONTH) + 1)) : (cal.get(Calendar.MONTH) + 1))
                            + "-" +
                            ((cal.get(Calendar.DAY_OF_MONTH) + 1) < 10 ?
                                    ("0" + (cal.get(Calendar.DAY_OF_MONTH) + 1)) : (cal.get(Calendar.DAY_OF_MONTH) + 1))))) {
                todayLeft += 1;
            }
        }

        // 2.统计本月的团队任务个数
        int taskLeft = 0;
        for (int i = 0; i < length; i++) {
            if (list_sch.get(i).getTitle().contains("多人") && list_sch.get(i).getState().contains("未完成")
                    && (list_sch.get(i).getDate().contains(cal.get(Calendar.YEAR) + "-" + ((cal.get(Calendar.MONTH) + 1) < 10 ?
                    ("0" + (cal.get(Calendar.MONTH) + 1)) : (cal.get(Calendar.MONTH) + 1)))
            )) {
                taskLeft += 1;
            }
        }
        LinearLayout ll_left = (LinearLayout) findViewById(R.id.ll_todayleft);
        ll_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProgressActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        LinearLayout ll_taskLeft = (LinearLayout) findViewById(R.id.ll_taskleft);
        ll_taskLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProgressActivity.this, BmobActivity.class);
                startActivity(intent);
            }
        });
        TextView tv_left = (TextView) findViewById(R.id.todayLeft);
        //   tv_left.getBackground().setAlpha(100);
        TextView tv_taskLeft = (TextView) findViewById(R.id.taskLeft);
        //  tv_taskLeft.getBackground().setAlpha(100);

        if (todayLeft == 0) {
            tv_left.setText("最近没有任务");
        } else {
            tv_left.setText("您最近有" + todayLeft + "项待完成任务");
        }
        if (taskLeft == 0) {
            tv_taskLeft.setText("最近没有团队任务");
        } else {
            tv_taskLeft.setText("您最近有" + taskLeft + "个团队任务");
        }

    }



    @Override
    protected void onPause(){
        super.onPause();
        overridePendingTransition(0,0);
    }


    private class MyExpandableListViewAdapter extends BaseExpandableListAdapter {

        //  获得某个父项的某个子项
        @Override
        public Object getChild(int parentPos, int childPos) {
            return dataset.get(parentList.get(parentPos)).get(childPos);
        }

        //  获得父项的数量
        @Override
        public int getGroupCount() {

            return dataset.size();
        }

        //  获得某个父项的子项数目
        @Override
        public int getChildrenCount(int parentPos) {
            if(dataset.get(parentList.get(parentPos)).size()==0){
                Toast.makeText(ProgressActivity.this, "暂时此夹无笔记，去添加吧", Toast.LENGTH_SHORT).show();
            }
            return dataset.get(parentList.get(parentPos)).size();
        }

        //  获得某个父项
        @Override
        public Object getGroup(int parentPos) {
            return dataset.get(parentList.get(parentPos));
        }

        //  获得某个父项的id
        @Override
        public long getGroupId(int parentPos) {
            return parentPos;
        }

        //  获得某个父项的某个子项的id
        @Override
        public long getChildId(int parentPos, int childPos) {
            return childPos;
        }

        //  按函数的名字来理解应该是是否具有稳定的id，这个函数目前一直都是返回false，没有去改动过
        @Override
        public boolean hasStableIds() {
            return false;
        }

        //  获得父项显示的view
        @Override
        public View getGroupView(int parentPos, boolean b, View view, ViewGroup viewGroup) {
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) ProgressActivity
                        .this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.note_parent_item, null);
            }
            //随机色
//            int index = (int) (Math.random() * 18);

            view.setTag(R.layout.note_parent_item, parentPos);
            view.setTag(R.layout.note_child_item, -1);
            final String tempSort = parentList.get(parentPos).trim();
            TextView text = (TextView) view.findViewById(R.id.parent_title);
            final View sharedView = (View) view.findViewById(R.id.sharedView);
            final Button btn_add_notes = (Button) view.findViewById(R.id.btn_add_notes);
            btn_add_notes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProgressActivity.this,AddingNoteActivity.class);
                    intent.putExtra("getSort",tempSort);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(ProgressActivity.this, sharedView, "shareNames").toBundle());
                    }
                }
            });
            //设置随机颜色的背景,可以通过这个设置parentitem背景
            //     ll_parent_item.setBackgroundColor(getResources().getColor(backs[1]));
           // Typeface textFont_text = Typeface.createFromAsset(getAssets(),"fonts/SourceHanSansCN-Regular.otf.ttf");
         //   text.setTypeface(textFont_text);
            text.setText(parentList.get(parentPos));
            return view;
        }

        //  获得子项显示的view
        @Override
        public View getChildView(final int parentPos, final int childPos, boolean b, View view, ViewGroup viewGroup) {
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) ProgressActivity
                        .this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.note_child_item, null);
            }
            view.setTag(R.layout.note_parent_item, parentPos);
            view.setTag(R.layout.note_child_item, childPos);
            final Note noteTemp = dataset.get(parentList.get(parentPos)).get(childPos);

            int randomNum = (int) (Math.random()*6);
            View circle_left = (View) view.findViewById(R.id.circle_left);
            circle_left.setBackgroundResource(circle_color[randomNum]);
            Button btn_delete = (Button) view.findViewById(R.id.delete_note);
            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final DialogInterface.OnClickListener listener = new android.content.DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog,int which){
                            dataset.get(parentList.get(parentPos)).remove(noteTemp);
                            noteDao.delete(noteTemp.getId());
                            notifyDataSetChanged();
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProgressActivity.this);
                    builder.setTitle("确定要删除吗？");
                    builder.setPositiveButton("确定",listener);
                    builder.setNegativeButton("取消",null);
                    builder.show();
                }
            });

            final Note note_item= dataset.get(parentList.get(parentPos)).get(childPos);

            final TextView text = (TextView) view.findViewById(R.id.child_title);
            text.setText(note_item.getTitle());
            //text.setText(note_item.getTitle()+"11"+note_item.getSort()+"11"+note_item.getContent());//测试结果：数据储存正常
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String item_sort = note_item.getSort();
                    String item_title =note_item.getTitle();
                    String item_content = note_item.getContent();
                    long item_id = note_item.getId();

                   // Toast.makeText(ProgressActivity.this, item_sort+"\n"+item_title+"\n"+item_content, Toast.LENGTH_LONG).show();
                    //测试结果正常
                    Intent intent = new Intent(ProgressActivity.this,CorrectNoteActivity.class);
                    intent.putExtra("getSort",item_sort);
                    intent.putExtra("getTitle",item_title);
                    intent.putExtra("getContent",item_content);
                    intent.putExtra("getId",item_id);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(ProgressActivity.this, text, "note_title").toBundle());
                    }

//                    Toast.makeText(ProgressActivity.this, "点到了内置的textview",
//                            Toast.LENGTH_SHORT).show();
                }
            });
            return view;
        }

        //  子项是否可选中，如果需要设置子项的点击事件，需要返回true
        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    }



}
