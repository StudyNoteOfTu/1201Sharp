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

import bean.Note;
import dao.NoteDao;

public class CorrectNoteActivity  extends AppCompatActivity {

    private NoteDao noteDao;


    private EditText edt_title,edt_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        Intent intent = getIntent();
        final long id = intent.getLongExtra("getId",0);
        final  String sort = intent.getStringExtra("getSort");
        final String title = intent.getStringExtra("getTitle");
        final String content = intent.getStringExtra("getContent");





        noteDao = new NoteDao(CorrectNoteActivity.this);
        edt_title = (EditText) findViewById(R.id.edt_title);
        edt_title.setText("");
        edt_title.setText(title);
        edt_content = (EditText) findViewById(R.id.edt_content);
        edt_content.setText("");
        edt_content.setText(content);
        Button btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String title = edt_title.getText().toString().isEmpty()?
                        edt_content.getText().toString().trim()
                        :edt_title.getText().toString().trim();
                final String content = edt_content.getText().toString().trim();
                Note note = new Note(id,title,content,sort);
                noteDao.update(note);
                Toast.makeText(CorrectNoteActivity.this, "笔记修改成功", Toast.LENGTH_SHORT).show();

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
