package in.ac.nitrkl.personalto_dolist;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;


/**
 * Created by dibya on 04-05-2016.
 */
public class itemDetails extends Activity {

    TextView message,time,dates,location;
    Boolean add;
    int position;
    DBAdapter myDB;
    Cursor cursor;
    Date date;
    Calendar calendar;
    int year,month,day,hour,minute;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_details);
        myDB = new DBAdapter(this);
        add = getIntent().getBooleanExtra("add",false);
        position = getIntent().getIntExtra("position",-1);

        message = (TextView) findViewById(R.id.message);
        time = (TextView) findViewById(R.id.time);
        dates = (TextView) findViewById(R.id.date);
        location = (TextView) findViewById(R.id.location);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        showDate(year, month, day);
        showTime(hour,minute);

        if(position==-1){
            location.setText("home");
        }
        else {
            myDB.open();
            cursor = myDB.getAllRows();
            cursor.moveToPosition(position);
            cursor=myDB.getRow(cursor.getInt(0));
            String t = cursor.getString(2);
            int l = t.length();

            message.setText(cursor.getString(1));
            time.setText(t.substring(10,19));
            dates.setText(t.substring(0,10));
            location.setText(cursor.getString(3));
            myDB.close();
        }

    }



    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(1);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 1) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        if (id == 2) {
            return new TimePickerDialog(this, myTimeListener, hour,minute,true);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            // TODO Auto-generated method stub
            showDate(year, month, day);
        }
    };

    private void showDate(int year, int month, int day) {
        dates.setText(new StringBuilder().append(day).append("/")
                .append(month+1).append("/").append(year));
        this.year = year;
        this.month = month;
        this.day = day;
    }

    @SuppressWarnings("deprecation")
    public void setTime(View view) {
        showDialog(2);
    }

    private TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            showTime(hourOfDay, minute);
        }
    };

    private void showTime(int hour, int minute) {
        time.setText(new StringBuilder().append(hour).append(":")
                .append(minute));
        this.hour = hour;
        this.minute = minute;
    }

    public void mapfrag(View v){
        //create fragment
//        Fragment fragment;
//        fragment = new mapFragment();
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.frag, fragment);
//        fragmentTransaction.commit();
    }

    public void save(View v){

        date = new Date();
        date.setYear(year);
        date.setMonth(month);
        date.setDate(day);
        date.setHours(hour);
        date.setMinutes(minute);
        if(add==true){
            openDB();
            myDB.insertRow(message.getText().toString(),date,location.getText().toString());
            closeDB();
        }
        else{
            openDB();
            cursor = myDB.getAllRows();
            cursor.moveToPosition(position);
            cursor=myDB.getRow(cursor.getInt(0));
            myDB.updateRow(cursor.getInt(0),message.getText().toString(),date,location.getText().toString());
            closeDB();
        }
        finish();
    }
    public void openDB() {
        myDB.open();
    }

    public void closeDB() {
        myDB.close();
    }
}
