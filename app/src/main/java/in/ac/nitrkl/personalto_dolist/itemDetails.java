package in.ac.nitrkl.personalto_dolist;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;


/**
 * Created by dibya on 04-05-2016.
 */
public class itemDetails extends Activity {

    TextView message,time,place;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_details);

        int position = getIntent().getIntExtra("position",-1);

        message = (TextView) findViewById(R.id.message);
        time = (TextView) findViewById(R.id.time);
        place = (TextView) findViewById(R.id.place);

        //update the data from datatbase

        message.setText("TEXT"+position);
        time.setText(position+":00 AM");
        place.setText("Street "+position);
    }


    public void save(View v){
        finish();
    }
}
