package in.ac.nitrkl.personalto_dolist;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class MainActivity extends Activity {
    private DBAdapter myDB;
    private ListView todoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDB = new DBAdapter(this);
        todoList = (ListView) findViewById(R.id.todoList);

        todoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), itemDetails.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });

    }

    public void btnClose(View v) {
//        myDB.open();
//        int position = 0;
//        Cursor cursor = myDB.getAllRows();
//        cursor.moveToPosition(position);
//        cursor=myDB.getRow(cursor.getInt(0));
        Toast.makeText(this, v.getContentDescription(), Toast.LENGTH_SHORT).show();
//        myDB.close();
    }

    @Override
    protected void onResume() {
        super.onResume();

        todoList.setAdapter(new customList(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,itemDetails.class);
            intent.putExtra("add",true);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


class singleRow {
    long id;
    String message, time, location;

    singleRow(long id, String message, String time, String location) {
        this.id = id;
        this.message = message;
        this.time = time;
        this.location = location;
    }
}


class customList extends BaseAdapter {
    ArrayList<singleRow> list;
    Context context;
    TextView message, time, location;
    ImageView close;
    DBAdapter myDB;
    int count=0;
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    customList(Context c) {
        list = new ArrayList<singleRow>();
        this.context = c;

        updateList(c);
    }

    private void updateList(Context c) {
        myDB = new DBAdapter(c);
        myDB.open();
        long[] id = new long[1000];
        String[] message = new String[1000];
        String[] time = new String[1000];
        String[] location= new String[1000];

        Cursor cursor = myDB.getAllRows();
        if (cursor.moveToFirst()) {
            count = 0;
            do {
                id[count]=cursor.getInt(0);
                message[count]=cursor.getString(1);
                time[count]=cursor.getString(2);
                location[count]=cursor.getString(3);
                count++;
            } while (cursor.moveToNext());
        }
        list.clear();
        for (int i = 0; i < count; i++) {
            singleRow obj = new singleRow(id[i], message[i], time[i], location[i]);
            list.add(obj);
        }
        myDB.close();
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.list_row, parent, false);

        message = (TextView) row.findViewById(R.id.message);
        time = (TextView) row.findViewById(R.id.time);
        location = (TextView) row.findViewById(R.id.location);
        close = (ImageView) row.findViewById(R.id.close);

        message.setText(list.get(position).message);
        time.setText(list.get(position).time);
        location.setText(list.get(position).location);
        close.setContentDescription(list.get(position).id + "");

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDB.open();
                Cursor cursor = myDB.getAllRows();
                cursor.moveToPosition(position);
//                cursor=myDB.getRow(cursor.getInt(0));
                myDB.deleteRow(cursor.getInt(0));
                myDB.close();
                updateList(context);
                notifyDataSetChanged();
            }
        });

        return row;
    }

}