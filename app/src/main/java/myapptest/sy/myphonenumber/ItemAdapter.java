package myapptest.sy.myphonenumber;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import DAO.biz;
import model.People;

/**
 * Created by Sy on 2016/2/25.
 */
public class ItemAdapter extends BaseAdapter{
    private Handler mHandler;
    private String callnum,delname = null;
    private List<People> mList = null;
    private Context context = null;
    public ItemAdapter(Context context,List<People> mList,Handler mHandler) {
        this.mHandler = mHandler;
        this.context = context;
        this.mList = mList;
        this.mInflater = LayoutInflater.from(context);
    }

    private LayoutInflater mInflater = null;

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class viewHolder{
        public TextView tv_name,tv_num,tv_email;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder viewHolder = null;
        if (convertView == null){
            viewHolder = new viewHolder();
            convertView = mInflater.inflate(R.layout.item_layout,null);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.item_name);
            viewHolder.tv_num = (TextView) convertView.findViewById(R.id.item_num);
            viewHolder.tv_email = (TextView) convertView.findViewById(R.id.item_email);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ItemAdapter.viewHolder) convertView.getTag();
        }

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dialogDel();
                return true;
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });

        viewHolder.tv_name.setText(mList.get(position).getName());
        viewHolder.tv_num.setText(mList.get(position).getNum());
        viewHolder.tv_email.setText(mList.get(position).getEmail());
        callnum = mList.get(position).getNum();
        delname = mList.get(position).getName();
        return convertView;
    }


    private void  dialog(){
        DialogInterface.OnClickListener dialogOnclicListener=new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case Dialog.BUTTON_POSITIVE:
                        Intent intent =new Intent();
                        intent.setAction("android.intent.action.CALL");
                        intent.setData(Uri.parse("tel:" + callnum));
                        //根据意图过滤器参数激活电话拨号功能
                        context.startActivity(intent);
                        break;
                    case Dialog.BUTTON_NEGATIVE:
                        dialogSMS();
                        break;
                    case Dialog.BUTTON_NEUTRAL:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示"); //设置标题
        builder.setMessage("想要的操作"); //设置内容
        builder.setPositiveButton("拨号", dialogOnclicListener);
        builder.setNegativeButton("发消息", dialogOnclicListener);
        builder.setNeutralButton("取消", dialogOnclicListener);
        builder.create().show();
    }

    private void dialogSMS(){
        final EditText text = new EditText(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("输入短信内容");
        DialogInterface.OnClickListener dialogOnclicListener=new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case Dialog.BUTTON_POSITIVE:
                        String SMStext = text.getText().toString();
                        SmsManager smsManager=SmsManager.getDefault();//获得短信管理器

                        smsManager.sendTextMessage(callnum, null, SMStext, null, null);   //短信发送
                        Toast.makeText(context,"发送成功",Toast.LENGTH_SHORT).show();
                        break;
                    case Dialog.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };
        builder.setView(text);
        builder.setPositiveButton("发送", dialogOnclicListener);
        builder.setNegativeButton("取消", dialogOnclicListener);
        builder.show();

    }

    private void dialogDel(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        DialogInterface.OnClickListener dialogOnclicListener=new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case Dialog.BUTTON_POSITIVE:
                        biz biz = new biz(context);
                        biz.mDel(delname);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Message message = new Message();
                                message.what = 0x001;
                                mHandler.sendMessage(message);
                            }
                        }).start();
                        break;
                    case Dialog.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        builder.setTitle("确定要删除吗?");
        builder.setPositiveButton("确定", dialogOnclicListener);
        builder.setNegativeButton("取消", dialogOnclicListener);
        builder.show();
    }

}
