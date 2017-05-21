package com.example.user.contractpractice;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText et_name, et_phone, et_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_name = (EditText) findViewById(R.id.et_name);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_email = (EditText) findViewById(R.id.et_email);
    }

    public void LookContact (View view) {
        List<Contact> list = ReadContactUtils.readContact(this);

        for (Contact contact : list) {
            Log.v("MainActivty", contact.toString());
        }
    }

    public void AddContact (View view) {
        String name = et_name.getText().toString().trim();
        String phone = et_phone.getText().toString().trim();
        String email = et_email.getText().toString().trim();

        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri dataUri = Uri.parse("content://com.android.contacts/data");

        // 先查询一下raw_contacts表中一共有几条数据  行数+1 就是contact_id的值
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        int count = cursor.getCount();
        int contact_id = count + 1; //代表当前联系人的id
        ContentValues values = new ContentValues();
        values.put("contact_id", contact_id);
        getContentResolver().insert(uri, values);

        // insert name
        ContentValues nameValues = new ContentValues();
        nameValues.put("data1", name); //把数据插入到data1列
        nameValues.put("raw_contact_id", contact_id); //告诉数据库我们插入的数据属于哪条联系人
        nameValues.put("mimetype", "vnd.android.cursor.item/name");//告诉数据库插入的数据的数据类型
        getContentResolver().insert(dataUri, nameValues);

        // insert phone
        ContentValues phoneValues = new ContentValues();
        phoneValues.put("data1", phone);
        phoneValues.put("raw_contact_id", contact_id);
        phoneValues.put("mimetype", "vnd.android.cursor.item/phone_v2");

        //insert email
        ContentValues emailValues = new ContentValues();
        emailValues.put("data1", email);
        emailValues.put("raw_contact_id", contact_id);
        emailValues.put("mimetype", "vnd.android.cursor.item/email_v2");
        getContentResolver().insert(dataUri, emailValues);

        cursor.close();
    }
}
