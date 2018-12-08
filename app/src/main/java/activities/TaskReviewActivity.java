package activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tufengyi.sharp.R;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.Task;
import bean.TaskReview;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;


//把id存在数据库里，这样就不用每次输入id

public class TaskReviewActivity extends AppCompatActivity {
    private EditText edt_objId;
    private Button btn_review;
    private List<Task> mTask;
    private ExpandableTextView tv_task;

    private ExpandableListView listview;
    private MyExpandableListViewAdapter adapter;
    private Map<String,List<String>> dataset = new HashMap<>();
    private List<String> parentList = new ArrayList<>();
    private List<List<String>> children = new ArrayList<>();

    private int[] dw={
            R.drawable.bmb_back1,
            R.drawable.bmb_back2,
            R.drawable.bmb_back3,
            R.drawable.bmb_back4,
            R.drawable.bmb_back5,
            R.drawable.bmb_back6
    };

    private int[] memBack = {
            R.drawable.mem_back1,
            R.drawable.mem_back2,
            R.drawable.mem_back3,
            R.drawable.mem_back4,
            R.drawable.mem_back5,
            R.drawable.mem_back6
    };

//随机色
//    private int[] backs = {
//           R.color.r1,
//           R.color.r2,
//           R.color.r3,
//           R.color.r4,
//           R.color.r5,
//           R.color.r6,
//           R.color.r7,
//           R.color.r8,
//           R.color.r9,
//           R.color.ra,
//           R.color.rb,
//           R.color.rc,
//           R.color.rd,
//           R.color.re,
//           R.color.rf,
//           R.color.rg,
//           R.color.rh,
//           R.color.ri,
//           R.color.rj
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this,"f4939b1bc990101036756a6398c7a159");
        setContentView(R.layout.activity_taskreview);
        Intent intent = getIntent();
        final String task = intent.getStringExtra("getSchedule");
        String objectId = intent.getStringExtra("getObjId");
        tv_task = (ExpandableTextView) findViewById(R.id.tv_task);
        tv_task.setText(task);

        //返回键
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //获取listview的id
        listview = (ExpandableListView) findViewById(R.id.expandablelistview);

        edt_objId = (EditText) findViewById(R.id.edt_objId) ;
        edt_objId.setText(objectId);

        btn_review = (Button) findViewById(R.id.btn_update);
        btn_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentList.clear();
                children.clear();
                 String objectId = edt_objId.getText().toString().trim();
                BmobQuery<TaskReview> query = new BmobQuery<TaskReview>();
                query.getObject(objectId,new QueryListener<TaskReview>(){
                    @Override
                    public void done(TaskReview object, BmobException e){
                        if(e==null){
                            Toast.makeText(TaskReviewActivity.this,"添加成功"+object.getMlist().size(),Toast.LENGTH_SHORT).show();
                            mTask = object.getMlist();
                            int size_mTask = mTask.size();
                            for(int i =0;i<size_mTask;i++){
                                parentList.add(mTask.get(i).getName());
                                List<String> templist = new ArrayList<>();
                                children.add(templist);
                            }
                            for(int j = 0;j<size_mTask;j++){
                                children.get(j).add(mTask.get(j).getTask());
                            }

                            for(int k=0;k<size_mTask;k++){
                                dataset.put(parentList.get(k),children.get(k));
                            }

                            adapter = new MyExpandableListViewAdapter();
                            listview.setAdapter(adapter);


                            //String name = object.getMlist().get(1).getName();
                          //  myAdapter = new MyAdapter();
                          //  lv_task.setAdapter(myAdapter);
                        }else{
                            Toast.makeText(TaskReviewActivity.this,"查询失败"+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }

                });



            }
        });
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
            if (dataset == null) {
                Toast.makeText(TaskReviewActivity.this, "dataset为空", Toast.LENGTH_SHORT).show();
                return 0;
            }
            return dataset.size();
        }

        //  获得某个父项的子项数目
        @Override
        public int getChildrenCount(int parentPos) {
            if (dataset.get(parentList.get(parentPos)) == null) {
                Toast.makeText(TaskReviewActivity.this, "\" + parentList[parentPos] + \" + 数据为空", Toast.LENGTH_SHORT).show();
                return 0;
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
                LayoutInflater inflater = (LayoutInflater) TaskReviewActivity
                        .this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.parent_item, null);
            }
            int index = (int) (Math.random() * 6);

            final int num = (int) (Math.random()*6);
            view.setTag(R.layout.parent_item, parentPos);
            view.setTag(R.layout.child_item, -1);
            TextView text = (TextView) view.findViewById(R.id.parent_title);
            LinearLayout ll_parent_item = (LinearLayout) view.findViewById(R.id.ll_parent_item);
            LinearLayout ll_parent_back = (LinearLayout) view.findViewById(R.id.parent_item_back);
           ll_parent_back.setBackgroundResource(memBack[num]);
            //设置随机颜色的背景,可以通过这个设置parentitem背景
       //     ll_parent_item.setBackgroundColor(getResources().getColor(backs[1]));

            text.setText(String.valueOf(parentPos+1)+"."+parentList.get(parentPos));
            return view;
        }

        //  获得子项显示的view
        @Override
        public View getChildView(int parentPos, int childPos, boolean b, View view, ViewGroup viewGroup) {
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) TaskReviewActivity
                        .this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.child_item, null);
            }
            view.setTag(R.layout.parent_item, parentPos);
            view.setTag(R.layout.child_item, childPos);
            TextView text = (TextView) view.findViewById(R.id.child_title);
            text.setText(dataset.get(parentList.get(parentPos)).get(childPos));
//            text.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(TaskReviewActivity.this, "点到了内置的textview",
//                            Toast.LENGTH_SHORT).show();
//                }
//            });
            return view;
        }

        //  子项是否可选中，如果需要设置子项的点击事件，需要返回true
        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    }

}
