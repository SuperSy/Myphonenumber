package DAO;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import model.People;

/**
 * Created by Sy on 2016/2/25.
 */
public class biz implements db {
    private Context context = null;
    private ContentResolver resolver = null;

    public biz(Context context) {
        this.context = context;
        resolver = context.getContentResolver();
    }


    @Override
    public List mQuery() {
        People people = null;
        ArrayList al = new ArrayList();
        String name = null;
        String num = null;
        String email = null;
        Uri uri = Uri.parse("content://com.android.contacts/contacts");
        Cursor cursor = resolver.query(uri, new String[]{ContactsContract.RawContacts.Data._ID}, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            //String idstr = id+"";
            uri = Uri.parse("content://com.android.contacts/contacts/" + id + "/data");
            Cursor cursor2 = resolver.query(uri, new String[]{ContactsContract.Data.DATA1, ContactsContract.Data.MIMETYPE}, null, null, null);
            while (cursor2.moveToNext()) {
                String data = cursor2.getString(cursor2.getColumnIndex("data1"));
                if (cursor2.getString(cursor2.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/name")) {       //如果是名字
                    name = data;
                } else if (cursor2.getString(cursor2.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/phone_v2")) {  //如果是电话
                    num = data;
                } else if (cursor2.getString(cursor2.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/email_v2")) {  //如果是email
                    email = data;
                }
                people = new People(name, num, email);
            }
            al.add(people);
        }
        return al;
    }

    @Override
    public void mInsert(People people) {
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        long contact_id = ContentUris.parseId(resolver.insert(uri, values));

        uri = Uri.parse("content://com.android.contacts/data");
        //add Name
        values.put("raw_contact_id", contact_id);
        values.put(ContactsContract.Data.MIMETYPE, "vnd.android.cursor.item/name");
        values.put("data1", people.getName());
        resolver.insert(uri, values);
        values.clear();
        //add Phone
        values.put("raw_contact_id", contact_id);
        values.put(ContactsContract.Data.MIMETYPE, "vnd.android.cursor.item/phone_v2");
        values.put("data1", people.getNum());
        resolver.insert(uri, values);
        values.clear();
        //add email
        values.put("raw_contact_id", contact_id);
        values.put(ContactsContract.Data.MIMETYPE, "vnd.android.cursor.item/email_v2");
        values.put("data1", people.getEmail());
        resolver.insert(uri, values);
    }

    @Override
    public void mDel(String name) {
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{ContactsContract.RawContacts.Data._ID}, "display_name=?", new String[]{name}, null);
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            //根据id删除data中的相应数据
            resolver.delete(uri, "display_name=?", new String[]{name});
            uri = Uri.parse("content://com.android.contacts/data");
            resolver.delete(uri, "raw_contact_id=?", new String[]{id + ""});
        }
    }

    @Override
    public void mUpdata(String... str) {
        Uri uri = Uri.parse("content://com.android.contacts/data");//对data表的所有数据操作
        Uri uri2 = Uri.parse("content://com.android.contacts/data/phones/filter/" + str[0]);
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        Cursor cursor = resolver.query(uri, new String[]{ContactsContract.Data.DISPLAY_NAME}, null, null, null);
        if(cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            if(str[1] != null) {
                values.put("data1", str[1]);
                resolver.update(uri, values, "mimetype=? and raw_contact_id=?", new String[]{"vnd.android.cursor.item/phone_v2", id + ""});
            }
            if(str[0] != null) {
                values.put("data1", str[0]);
                resolver.update(uri, values, "mimetype=? and raw_contact_id=?", new String[]{"vnd.android.cursor.item/name", id + ""});
            }
            if(str[2] != null) {
                values.put("data1", str[2]);
                resolver.update(uri, values, "mimetype=? and raw_contact_id=?", new String[]{"vnd.android.cursor.item/email_v2", id + ""});
            }
        }
    }


}
