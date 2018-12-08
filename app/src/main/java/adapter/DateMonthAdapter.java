package adapter;


import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tufengyi.sharp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import bean.DateEntity;
import bean.Schedule;
import dao.ScheduleDao;

public class DateMonthAdapter extends BaseAdapter<DateEntity> {
    private String dateString;

    //尝试在window中加入点点标记
  //  private ScheduleDao scheduleDao;
   // private List<Schedule> mList;


    public DateMonthAdapter(Context c) {
        super(c);
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    @Override
    public void setData(ArrayList<DateEntity> beanList) {
        super.setData(beanList);
    }

    private int selectedPosition = -1;// 选中的位置

    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

      //  mList  = scheduleDao.queryAll();
        //检索查看date，如果date存在一致，那么点view变色
//        int length = mList.size();
//        for(int i = 0; i < length; i++){
//
//
//        }


        ViewHolder holder;
        DateEntity info = getData().get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_month_data, null);
            holder.data = (TextView) convertView.findViewById(R.id.data);
            holder.luna = (TextView) convertView.findViewById(R.id.luna);

           // holder.ptr = convertView.findViewById(R.id.view_ptr);

            holder.bg = convertView.findViewById(R.id.bg);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (TextUtils.isEmpty(info.date)) {
            //如果是空的那么全透
            holder.data.setText("");
            holder.luna.setText("");
           // holder.bg.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_ffffff));
       //     holder.bg.getBackground().setAlpha(10);
        } else {

            holder.luna.setText(info.luna);
            holder.data.setText(info.day);
//
//            int length = mList.size();
//            for(int i = 0; i < length; i++){
//                if(mList.get(i).getDate().contains(info.date)){
//                    holder.ptr.setBackgroundColor(Color.BLACK);
//                }
//            }


            if (dateString.equals(info.date)) {
                if (info.isToday) {
                   // holder.bg.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_ffffff));
                   // holder.bg.getBackground().setAlpha(10);
                    holder.data.setBackgroundResource(R.drawable.select_green_bg);
                    holder.data.setTextColor(ContextCompat.getColor(mContext, R.color.color_28d19d));
                   // holder.data.getBackground().setAlpha(10);
                    holder.luna.setTextColor(ContextCompat.getColor(mContext, R.color.color_28d19d));
                } else {
                   // holder.bg.setBackgroundResource(R.drawable.select_bg);
                    holder.bg.setBackgroundResource(R.color.color_ffffff);
                   // holder.bg.getBackground().setAlpha(10);
                  //  holder.data.setTextColor(ContextCompat.getColor(mContext, R.color.color_ffffff));
                //holder.luna.setTextColor(ContextCompat.getColor(mContext, R.color.color_ffffff));
                }

            } else {
                if (info.isToday) {
                  //  holder.bg.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_ffffff));
                  //  holder.bg.getBackground().setAlpha(10);
                    holder.data.setBackgroundResource(R.drawable.select_green_bg);
                    holder.data.setTextColor(ContextCompat.getColor(mContext, R.color.color_28d19d));
                    holder.luna.setTextColor(ContextCompat.getColor(mContext, R.color.color_28d19d));
                } else {
                 //   holder.bg.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_ffffff));
                  //  holder.bg.getBackground().setAlpha(10);

                    holder.data.setTextColor(ContextCompat.getColor(mContext, R.color.color_000000));

                    holder.luna.setTextColor(ContextCompat.getColor(mContext, R.color.color_000000));
                }
            }
        }
        if (selectedPosition == position) {
            if (!TextUtils.isEmpty(info.date)) {
                if (info.isToday) {

                    holder.data.setBackgroundResource(R.drawable.select_green_bg);


                    holder.data.setTextColor(ContextCompat.getColor(mContext, R.color.color_ffffff));
                    holder.luna.setTextColor(ContextCompat.getColor(mContext, R.color.color_ffffff));
                } else {
                    //holder.bg.setBackgroundResource(R.drawable.select_bg);
               //     holder.bg.getBackground().setAlpha(10);

                    holder.data.setBackgroundResource(R.drawable.select_bg);
                    holder.data.setTextColor(ContextCompat.getColor(mContext, R.color.color_000000));
                    holder.luna.setTextColor(ContextCompat.getColor(mContext, R.color.color_000000));
                }
            }
        } else {
            if (info.isToday) {
               // holder.bg.setBackgroundResource(R.drawable.select_green_bg);
            //    holder.bg.getBackground().setAlpha(10);

                holder.data.setBackgroundResource(R.drawable.select_green_bg);
                holder.data.setTextColor(ContextCompat.getColor(mContext, R.color.color_28d19d));
              //  holder.data.getBackground().setAlpha(10);
                holder.luna.setTextColor(ContextCompat.getColor(mContext, R.color.color_28d19d));
            } else {
              //  holder.bg.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_ffffff));
             //   holder.bg.getBackground().setAlpha(10);
                holder.data.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_ffffff));
                holder.data.setTextColor(ContextCompat.getColor(mContext, R.color.color_000000));
                holder.data.getBackground().setAlpha(0);
                holder.luna.setTextColor(ContextCompat.getColor(mContext, R.color.color_000000));
            }

        }
        return convertView;
    }

    public class ViewHolder {
        TextView data;
        TextView luna ;
        View bg ;
        View ptr;
    }
}
