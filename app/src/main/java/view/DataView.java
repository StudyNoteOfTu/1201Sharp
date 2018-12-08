package view;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tufengyi.sharp.R;

import java.util.ArrayList;

import adapter.DateAdapter;
import bean.DateEntity;
import utils.DataUtils;

public class DataView extends LinearLayout implements  AdapterView.OnItemClickListener {
    private ArrayList<DateEntity> millisList ;
    //上一周，下一周，当前时间
//    private TextView frontWeekTv ;
//    private TextView nextWeekTv ;
//    private TextView currentDateTv ;
    private GridView list ;
    private DateAdapter mAdapter ;
    private ArrayList<DateEntity> datas ;
    private String dataFormate = "yyyy-MM-dd" ;
    private String currentData ;
    private DatePopupWindow popupWindow ;
    private GestureDetector gestureDetector;
    private final int RIGHT = 0;
    private final int LEFT =1;
    private final int POP = 2;

    public DataView(Context context) {
        this(context,null);
    }

    public DataView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DataView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        datas = new ArrayList<>();
        millisList = new ArrayList<>();
        //获得周视图上面 几个textview 的view
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_data,null,false);

        //分发点击事件
//        frontWeekTv = (TextView)view.findViewById(R.id.front_week);
//        frontWeekTv.setOnClickListener(this);
//        nextWeekTv = (TextView)view.findViewById(R.id.next_week);
//        nextWeekTv.setOnClickListener(this);
//        currentDateTv = (TextView)view.findViewById(R.id.now_day);
//        currentDateTv.setOnClickListener(this);
        //周视图部份gridview
        //使用gridview网格视图
        list = (GridView) view.findViewById(R.id.list);
        list.setSelector(new ColorDrawable(Color.TRANSPARENT));
        //手势
        gestureDetector = new GestureDetector(context,onGestureListener);

        mAdapter = new DateAdapter(getContext(),datas);
        list.setAdapter(mAdapter);
        list.setOnItemClickListener(this);
        addView(view);

        list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }

    private GestureDetector.OnGestureListener onGestureListener =
            new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                       float velocityY) {
                    float x = e2.getX() - e1.getX();
                    float y = e2.getY() - e1.getY();

//                    if (x > 100) {
//                        doResult(RIGHT);
//                    } else if (x < -100) {
//                        doResult(LEFT);
//                    } else if(y > 70) {
//                        doResult(POP);
//                    }
                    //通过if else 语句设置优先级，优先下滑，其次左右
                    //注意用户手滑习惯
                    if(y > 300 && x <70 || y> 300 && x > -70){      //补上对x的限制
                        doResult(POP);
                    }else if( x < -70){
                        doResult(RIGHT);
                    }else if( x > 70){
                        doResult(LEFT);
                    }
                    return true;
                }
            };
    public void doResult(int action) {

        switch (action) {
            case RIGHT:
                getData(DataUtils.getSomeDays(currentData,+7));
                Toast.makeText(getContext(), "跳至下一周", Toast.LENGTH_SHORT).show();
                break;
            case LEFT:
                getData(DataUtils.getSomeDays(currentData,-7));
                Toast.makeText(getContext(), "跳至上一周", Toast.LENGTH_SHORT).show();
                break;
            case POP:
                showPopupWindow(this);
                break;

        }
    }


    /*
     * 获得传入的今天的日期
     */
    public void getData(String dateNumber){
        datas.clear();
        millisList.clear();
       // currentDateTv.setText(dateNumber);

        if (TextUtils.isEmpty(dateNumber)){
            dateNumber = DataUtils.getCurrDate(dataFormate);
        }

        //获得这周七天的arraylist
        millisList = DataUtils.getWeek(dateNumber);

        if (millisList==null || millisList.size()<=0){
            return;
        }

        //获得这周七天的list
        datas.addAll(millisList);
        //获得周中的今天
        for (int i=0;i<millisList.size();i++){
            if (dateNumber.equals(millisList.get(i).date)){
                mAdapter.setSelectedPosition(i);
                currentData = millisList.get(i).date;
                //currentDateTv.setText(currentData);
                if (onSelectListener!=null){
                    onSelectListener.onSelected(millisList.get(i));
                }
            }
        }
        if (TextUtils.isEmpty(currentData)){
            mAdapter.setSelectedPosition(0);
            currentData = millisList.get(0).date ;
       //     currentDateTv.setText(millisList.get(0).date);
            if (onSelectListener!=null){
                onSelectListener.onSelected(millisList.get(0));
            }
        }
        mAdapter.notifyDataSetChanged();
    }

//    @Override
//    public void onClick(View view) {
//        int id = view.getId();
//        if (id==frontWeekTv.getId()){
//            getData(DataUtils.getSomeDays(currentData,-7));
//        }else if (id==nextWeekTv.getId()){
//            getData(DataUtils.getSomeDays(currentData,+7));
//        }else if (id==currentDateTv.getId()){
//            showPopupWindow(this);
//        }
//    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DateEntity entity = datas.get(position);
        currentData = entity.date ;
      //  currentDateTv.setText(currentData);
        mAdapter.setSelectedPosition(position);
        if (onSelectListener!=null){
            onSelectListener.onSelected(millisList.get(position));
        }
    }

    private void showPopupWindow(View v) {
        popupWindow = new DatePopupWindow(getContext(),currentData);


        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAsDropDown(v);
        popupWindow.setOnItemClick(new DatePopupWindow.OnItemClick() {
            @Override
            public void onItemClick(String date) {
                getData(date);
            }
        });
    }

    /**
     * 选中日期之后的回调
     */
    private OnSelectListener onSelectListener ;
    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }
    public interface OnSelectListener{
        void onSelected(DateEntity date);
    }
}