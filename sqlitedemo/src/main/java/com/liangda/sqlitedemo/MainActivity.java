package com.liangda.sqlitedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    // 資料庫變數宣告
    private SQLiteDatabase db = null;
    // 資料表的變數
    private final static String CREATE_TABLE =
            "CREATE TABLE table01(_id INTEGER PRIMARY KEY, name TEXT , price INTEGER)";

    private Button btnSelect, btnSelectAll;
    private Button btnFirst, btnNext, btnPrev, btnLast;
    private EditText editInput;
    private ListView listView;
    Cursor cursor;
    int posNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSelect = (Button)findViewById(R.id.btnSelect);
        btnSelectAll = (Button)findViewById(R.id.btnSelectAll);
        editInput = (EditText)findViewById(R.id.editInput);
        listView = (ListView)findViewById(R.id.listView);
        btnFirst = (Button)findViewById(R.id.btnFirst);
        btnNext = (Button)findViewById(R.id.btnNext);
        btnPrev = (Button)findViewById(R.id.btnPrev);
        btnLast = (Button)findViewById(R.id.btnLast);

        btnSelect.setOnClickListener(listener);
        btnSelectAll.setOnClickListener(listener);
        btnFirst.setOnClickListener(listener);
        btnNext.setOnClickListener(listener);
        btnPrev.setOnClickListener(listener);
        btnLast.setOnClickListener(listener);

        db = openOrCreateDatabase("db2.db", MODE_PRIVATE, null);

        try {
            db.execSQL(CREATE_TABLE);
            db.execSQL("INSERT INTO table01(name, price)values('香蕉', 30)");
            db.execSQL("INSERT INTO table01(name, price)values('芭樂', 40)");
            db.execSQL("INSERT INTO table01(name, price)values('檸檬', 50)");
            db.execSQL("INSERT INTO table01(name, price)values('蘋果', 60)");
            db.execSQL("INSERT INTO table01(name, price)values('蓮霧', 70)");
        } catch(Exception e) {
            cursor = getAll();
            UpdateAdapter(cursor);
        }

        listView.setOnItemClickListener(listViewOICL);
    }

    private AdapterView.OnItemClickListener listViewOICL = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Cursor c = get(id);
            String s = "position = " + position + "\r\n" +
                    "id = " + id + "\r\n" +
                    "name" + c.getString(1) + "\r\n" +
                    "price" + c.getInt(2);
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
        }
    };

    public void UpdateAdapter(Cursor cursor) {
        if(cursor != null && cursor.getCount() >= 0) {
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    this,
                    R.layout.mylayout,
                    cursor,
                    new String[] {"_id", "name", "price"},
                    new int[] {R.id.txtId, R.id.txtName, R.id.txtPrice},
                    0
            ) ;
            listView.setAdapter(adapter);
        }
    }

    public Cursor get(long rowId) throws SQLException {
        Cursor cursor = db.rawQuery("SELECT * FROM table01 WHERE _id="+rowId, null);
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
        } else {
            Toast.makeText(getApplicationContext(), "查無此資料！", Toast.LENGTH_SHORT).show();
        }
        return cursor;
    }

    public Cursor getAll() {
        Cursor cursor = db.rawQuery("SELECT * FROM table01", null);
        //Cursor cursor = db.rawQuery("SELECT _id || '.' || name pname, price FROM table01", null);
        return cursor;
    }


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                switch (v.getId()) {
                    case R.id.btnSelect:
                        long id = Integer.parseInt(editInput.getText().toString());
                        cursor = getOne(id);
                        posNow = cursor.getInt(0);
                        UpdateAdapter(cursor);
                        editInput.selectAll();
                        break;
                    case R.id.btnSelectAll:
                        cursor = getAll();
                        UpdateAdapter(cursor);
                        editInput.setText("");
                        break;
                    case R.id.btnNext:
                        posNow++;
                        cursor = get(posNow);
                        UpdateAdapter(cursor);
                        break;
                    case R.id.btnFirst:
                        posNow = 1;
                        cursor = get(posNow);
                        UpdateAdapter(cursor);
                        break;
                    case R.id.btnPrev:
                        posNow --;
                        cursor = get(posNow);
                        UpdateAdapter(cursor);
                        break;
                    case R.id.btnLast:
                        Cursor cursor = getAll();
                        posNow = cursor.getCount();
                        cursor = get(posNow);
                        UpdateAdapter(cursor);
                        break;
                }
            } catch (Exception e) {
                editInput.setText("");
                Toast.makeText(getApplicationContext(), "查無此資料！", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public Cursor getOne(long rwid)throws SQLException {
        Cursor cursor = db.rawQuery("SELECT * FROM table01 WHERE _id=" + rwid, null);
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
        } else {
            Toast.makeText(getApplicationContext(), "查無此資料！請重新輸入！", Toast.LENGTH_SHORT).show();
        }
        return cursor;
    }


}