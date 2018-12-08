package adapter;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tufengyi.sharp.R;

import java.util.ArrayList;
import java.util.List;

import bean.DateEntity;
import bean.Schedule;
import dao.ScheduleDao;

public class DateAdapter extends BaseAdapter {
//    private ScheduleDao scheduleDao;
//    private List<Schedule> mList;
    private Context mContext ;
    private ArrayList<DateEntity> datas ;
    private String[] weekTitle = {"日","一", "二", "三", "四", "五", "六"};
    public DateAdapter(Context mContext, ArrayList<DateEntity> datas) {
        super();

        this.mContext = mContext;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public DateEntity getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private int selectedPosition = -1;// 选中的位置

    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        DateEntity info = datas.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_data, null);

            holder.week_num = (TextView) convertView.findViewById(R.id.week_num);

            holder.item_data = (TextView) convertView.findViewById(R.id.item_data);
           // holder.item_data.getBackground().setAlpha(10);

            holder.layout_week = convertView.findViewById(R.id.layout_week);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.week_num.setText(weekTitle[position]);
        holder.item_data.setText(info.day);
        if (selectedPosition == position) {
            //holder.week_num.setTextColor(ContextCompat.getColor(mContext,R.color.color_ffffff));
            holder.item_data.setTextColor(ContextCompat.getColor(mContext,R.color.color_ffffff));
            if (info.isToday){
                holder.item_data.setBackgroundResource(R.drawable.select_green_bg);
            }else {
                holder.item_data.setBackgroundResource(R.drawable.select_bg);
            }

        } else {
            if (info.isToday){
               // holder.week_num.setTextColor(ContextCompat.getColor(mContext,R.color.color_ffffff));
                holder.item_data.setBackgroundResource(R.drawable.select_green_bg);
                holder.item_data.setTextColor(ContextCompat.getColor(mContext,R.color.color_ffffff));
            }else {
               // holder.week_num.setTextColor(ContextCompat.getColor(mContext,R.color.color_999999));
                holder.item_data.setTextColor(ContextCompat.getColor(mContext,R.color.color_333333));
                holder.item_data.setBackgroundColor(ContextCompat.getColor(mContext,R.color.color_ffffff));
                holder.item_data.getBackground().setAlpha(0);
            }

        }
        return convertView;
    }
    public class ViewHolder {
        TextView week_num;
        TextView item_data;
        ImageView image;
        View layout_week;
    }
}

