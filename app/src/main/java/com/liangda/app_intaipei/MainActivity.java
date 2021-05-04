package com.liangda.app_intaipei;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {

    // 資料庫變數宣告
    private SQLiteDatabase db = null;
    // 資料表的變數
    private final static String CREATE_TABLE =
            "CREATE TABLE table02(_id INTEGER PRIMARY KEY, num INTEGER, data TEXT)";

    private ListView listSQL;
    private Button btnDo;
    private EditText  editSQL;
    int item = 0;
    String result = "";
    String detail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listSQL = (ListView)findViewById(R.id.listSQL);
        btnDo = (Button)findViewById(R.id.btnDo);
        editSQL = (EditText)findViewById(R.id.editSQL);

        item++;
        detail = "項目名稱" + item;

        result = "INSERT INTO table02 (num, data)values("+item+", '"+detail+"')";
        editSQL.setText(result);

        // 建立SQLite 資料庫的實體檔案
        db = openOrCreateDatabase("db1.db", MODE_PRIVATE, null);
        // 建立資料表
        try {
            db.execSQL(CREATE_TABLE);
        } catch (Exception e) {
            Log.d("mylog", "資料表已建立");
            getAll();
        }
        btnDo.setOnClickListener(btnDoOCL);
    }

    private View.OnClickListener btnDoOCL = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            try {
                String INSERT = editSQL.getText().toString();

                //String INSERT = "INSERT INTO table01 (num, data)values(123, '項目名稱1')";
                db.execSQL(INSERT);

                getAll();
                item++;
                detail = "項目名稱" + item;

                result = "INSERT INTO table02 (num, data)values("+item+", '"+detail+"')";
                editSQL.setText(result);


                Log.d("mylog", "資料新增成功");
            } catch(Exception e) {
                Log.d("mylog", "資料新增失敗");
            }
        }
    };

    public void getAll() {
        Cursor cursor = db.rawQuery("SELECT * FROM table02", null);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2, // 包含2個資料項
                cursor, // 資料庫的 Cursors 物件
                new String[] {"_id", "data"},// num, data 欄位
                new int[] {android.R.id.text1, android.R.id.text2}, // 與 num, data 對應的元件
                0); // adapter 最佳化

        listSQL.setAdapter(adapter);
    }

    protected void onDestroy() {
        super.onDestroy();
//        String drop = "DROP TABLE table02";
//        db.execSQL(drop);
        db.close();
    }
}