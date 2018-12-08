package activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tufengyi.sharp.R;

import java.util.Calendar;

import bean.Note;
import cn.bmob.v3.Bmob;
import dao.NoteDao;

public class AddingNoteActivity extends AppCompatActivity {

    private NoteDao noteDao;

    private EditText edt_title,edt_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        Intent intent = getIntent();
        final  String sort = intent.getStringExtra("getSort").isEmpty()?"生活":intent.getStringExtra("getSort");



        noteDao = new NoteDao(AddingNoteActivity.this);
        edt_title = (EditText) findViewById(R.id.edt_title);
        edt_content = (EditText) findViewById(R.id.edt_content);
        edt_title.setText("");
        edt_content.setText("");

        final Button btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edt_title.getText().toString().isEmpty() && edt_content.getText().toString().isEmpty())
                {
                    Toast.makeText(AddingNoteActivity.this, "请先输入笔记信息噢", Toast.LENGTH_SHORT).show();
                }else{
                Calendar cal_temp = Calendar.getInstance();
                final String title = edt_title.getText().toString().isEmpty()?
                        "来自"+(cal_temp.get(Calendar.MONTH)+1)+"月"+cal_temp.get(Calendar.DAY_OF_MONTH)+"日的笔记"
                        :edt_title.getText().toString().trim();
                final String content = edt_content.getText().toString().trim();
                Note note = new Note(title,content,sort);
                noteDao.insert(note);
                Toast.makeText(AddingNoteActivity.this, "笔记添加成功", Toast.LENGTH_SHORT).show();

                }
            }
        });


        final Button btn_back = (Button) findViewById(R.id.back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }



}
