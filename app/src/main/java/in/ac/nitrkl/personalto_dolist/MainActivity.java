package in.ac.nitrkl.personalto_dolist;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class MainActivity extends Activity {

    private ListView todoList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        todoList = (ListView) findViewById(R.id.todoList);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}



class singleRow{
    int id;
    String message,time,place;
    singleRow(int id,String message,String time,String place){
        this.id=id;
        this.message=message;
        this.time=time;
        this.place=place;
    }
}




class customList extends BaseAdapter {
    ArrayList<singleRow> list;
    Context context;
    TextView message,time,place;
    ImageView close;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    customList(Context c){
        list = new ArrayList<singleRow>();
        this.context=c;

        int[] id = new int[100];
        String[] message = new String[100];
        String[] time = new String[100];
        String[] place = new String[100];

        for(int i=0;i<10;i++){
            id[i] = i;
            message[i] = "TEXT" +i;
            time[i] = ""+i+":00 AM";
            place[i] = "Street "+i;
        }


        for(int i=0;i<10;i++){
            singleRow obj = new singleRow(id[i],message[i],time[i],place[i]);
            list.add(obj);
        }
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
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.list_row, parent, false);

        message = (TextView)row.findViewById(R.id.message);
        time = (TextView)row.findViewById(R.id.time);
        place = (TextView)row.findViewById(R.id.place);
        close = (ImageView)row.findViewById(R.id.close);

        message.setText(list.get(position).message);
        time.setText(list.get(position).time);
        place.setText(list.get(position).place);
        close.setContentDescription(list.get(position).id+"");

        return row;
    }
}