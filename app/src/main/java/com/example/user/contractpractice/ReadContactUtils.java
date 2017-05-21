package com.example.user.contractpractice;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ReadContactUtils {

    public static List<Contact> readContact(Context context) {
        List<Contact> list = new ArrayList<>();

        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri uri2 = Uri.parse("content://com.android.contacts/data");

        // 先查询raw_contacts表  contact_id列
        Cursor cursor = context.getContentResolver().query(
                uri, new String[] {"contact_id"}, null, null, null);
        if (cursor != null && cursor.getCount() > 0 ) {
            while (cursor.moveToNext()) {
                String contact_id = cursor.getString(0);
                if (contact_id != null) {
                    Contact contact = new Contact();
                    contact.setId(contact_id);

                    // 根据 raw_contact_id 去查询 data表 data1列和 mimetype 列
                    Cursor cursor2 = context.getContentResolver().query(
                            uri2,
                            new String[]{"data1", "mimetype"},
                            "raw_contact_id=?",
                            new String[]{contact_id},
                            null);

                    if (cursor2 != null && cursor2.getCount() > 0) {
                        while (cursor2.moveToNext()) {
                            String data = cursor2.getString(0);
                            String mimetype = cursor2.getString(1);

                            if ("vnd.android.cursor.item/name".equals(mimetype)) {
                                Log.v("MainActivity", "姓名:" + data);
                                contact.setName(data);
                            } else if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
                                Log.v("MainActivity", "电话号码:" + data);
                                contact.setPhone(data);
                            } else if ("vnd.android.cursor.item/email_v2".equals(mimetype)) {
                                Log.v("MainActivity", "邮箱:" + data);
                                contact.setEmail(data);
                            }
                        }
                    }
                    cursor2.close();
                    list.add(contact);
                }
            }
        }
        cursor.close();

        return list;
    }
}
