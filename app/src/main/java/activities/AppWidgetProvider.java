package activities;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.provider.SyncStateContract;
import android.widget.RemoteViews;

import com.example.tufengyi.sharp.R;

public class AppWidgetProvider extends android.appwidget.AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds){

            super.onUpdate(context, appWidgetManager, appWidgetIds);
            for(int i=0;i<appWidgetIds.length;i++) {


                Intent intent = new Intent(context, AddingActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                        0);

                //小部件在Launcher桌面的布局
                RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_sharp);

                //事件
                remoteViews.setOnClickPendingIntent(R.id.widget_button, pendingIntent);

                //更新AppWidget

                appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
            }

    }



    public void onEnabled(Context context){
    }

    public void onDisabled(Context context){

    }
    @Override
    public void onDeleted(Context context,int[] appWidgetIds){
        super.onDeleted(context,appWidgetIds);
    }

    @Override
    public void onReceive(Context context,Intent intent){

        if(intent == null) return;
        String action = intent.getAction();
        if(action.equals("action_button")){
            RemoteViews remoteViews = new
                    RemoteViews(context.getPackageName(),R.layout.widget_sharp);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context,AppWidgetProvider.class);
            appWidgetManager.updateAppWidget(componentName,remoteViews);

        }
        super.onReceive(context,intent);
    }

    public AppWidgetProvider(){}



}



