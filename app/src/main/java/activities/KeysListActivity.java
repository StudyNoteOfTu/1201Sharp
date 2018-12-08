package activities;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.KeyListener;
import android.transition.Slide;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.tufengyi.sharp.R;

import java.security.Key;
import java.util.List;

import bean.Assignment;
import bean.KeyValue;
import dao.KeysDao;

public class KeysListActivity extends AppCompatActivity {

    private List<KeyValue> keys ;
    private KeysDao mapDao;
    private MyListView lv_keys;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_keyandvalue);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Slide().setDuration(350));
            getWindow().setExitTransition(new Slide().setDuration(200));
        }
        //返回键
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mapDao = new KeysDao(this);
        lv_keys = (MyListView) findViewById(R.id.lv_keys);
        lv_keys.setOnItemClickListener(new KeysListActivity.MyOnItemClickListener());
        keys = mapDao.queryAll();
        adapter = new MyAdapter();
        lv_keys.setAdapter(adapter);

    }
    @Override
    protected void onPause(){
        super.onPause();
//        overridePendingTransition(0,0);
    }


    private class MyAdapter extends BaseAdapter {
        public int getCount(){
            return keys.size();
        }
        public Object getItem(int position){
            return keys.get(position);
        }
        public long getItemId(int position){
            return position;
        }
        public View getView(int position, View convertView, ViewGroup parent){
            View item = convertView != null ? convertView : View.inflate(
                    getApplicationContext(),R.layout.item_keys,null
            );


            TextView tv_schedule = item.findViewById(R.id.tv_schedule);
            final TextView edt_keys = item.findViewById(R.id.edt_keys);
            Button btn_copy = item.findViewById(R.id.btn_copycode);
            btn_copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String code = edt_keys.getText().toString().trim();
                    //获取剪贴板管理器：
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    // 创建普通字符型ClipData
                    ClipData mClipData = ClipData.newPlainText("任务分享码", code);
                    // 将ClipData内容放到系统剪贴板里。
                    cm.setPrimaryClip(mClipData);
                    Toast.makeText(getApplicationContext(),code.equals("")? "暂无分享码":"复制成功:"+code,Toast.LENGTH_SHORT).show();
                }
            });

            final KeyValue kv = keys.get(position);
            edt_keys.setText(kv.getAkey());
            tv_schedule.setText("任务名称："+kv.getValue());

            Button btn_delete = item.findViewById(R.id.btn_delete);
            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final DialogInterface.OnClickListener listener = new android.content.DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog,int which){
                            keys.remove(kv);
                            mapDao.delete(kv.getId());
                            notifyDataSetChanged();
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(KeysListActivity.this);
                    builder.setTitle("确定要删除吗？");
                    builder.setPositiveButton("确定",listener);
                    builder.setNegativeButton("取消",null);
                    builder.show();
//                    keys.remove(kv);
//                    mapDao.delete(kv.getId());
//                    notifyDataSetChanged();
                }
            });
            return item;

        }
    }

    private class MyOnItemClickListener implements AdapterView.OnItemClickListener{
        public void onItemClick(AdapterView<?> parent,View view,int position,long id){
            KeyValue kv = (KeyValue) parent.getItemAtPosition(position);
            long idCopy = kv.getId();
            String title = kv.getValue();
            String key = kv.getAkey();
//            //这里修改一下
//            Intent intent = new Intent(KeysListActivity.this,CheckKeys.class);
//            intent.putExtra("getId",idCopy);
//            intent.putExtra("getValue",title);
//            intent.putExtra("getKey",key);
//            startActivity(intent);

        }
    }

}
