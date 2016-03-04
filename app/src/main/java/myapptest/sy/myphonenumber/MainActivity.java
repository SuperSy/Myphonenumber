package myapptest.sy.myphonenumber;

import android.app.AlertDialog;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

import DAO.biz;
import model.People;

public class MainActivity extends AppCompatActivity {

    private List<People> mList = null;
    private ListView lv = null;
    private ItemAdapter itemAdapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.lv);
//        biz biz = new biz(this);
//        People people = new People("哈喽","12345678","fsfsdfsdfdsf");
//        biz.mInsert(people);
        loading();



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog();
            }
        });

    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x001){
                loading();
            }
            super.handleMessage(msg);
        }
    };

    private void loading(){
        biz biz = new biz(MainActivity.this);
        mList = biz.mQuery();
        itemAdapter = new ItemAdapter(MainActivity.this,mList,mHandler);
        lv.setAdapter(itemAdapter);
    }

    public void dialog(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View layout = inflater.inflate(R.layout.insert_layout, null);
        final AlertDialog builder = new AlertDialog.Builder(this).create();

        builder.setView(layout);
        builder.setCancelable(false);
        builder.show();

        final EditText et_name=(EditText)layout.findViewById(R.id.et_name);
        final EditText et_num=(EditText)layout.findViewById(R.id.et_num);
        final EditText et_email=(EditText)layout.findViewById(R.id.et_emial);


        Button ok_bt=(Button) layout.findViewById(R.id.ok_bt);
        Button no_bt=(Button) layout.findViewById(R.id.no_bt);

        ok_bt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                biz biz = new biz(MainActivity.this);

                final String name = et_name.getText().toString();
                final String num = et_num.getText().toString();
                final String email = et_email.getText().toString();

                Log.d("CLICK", name+","+num+","+email);

                final People people = new People(name,num,email);

                if((name!= null)&&(num != null)&&(email != null)){
                    biz.mInsert(people);
                }else {
                     builder.dismiss();
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = 0x001;
                        mHandler.sendMessage(message);
                    }
                }).start();
                builder.dismiss();
            }
        });
        no_bt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                builder.dismiss();
            }
        });

    }
}
