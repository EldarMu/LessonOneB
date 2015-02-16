package com.eldar.lessononeb;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;


public class EnterDate extends ActionBarActivity {
    private DatePicker datePicker;
    private TimePicker timePicker;
    private TextView textLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_date);
        final Button button = (Button)findViewById(R.id.doTheStuffButton);
        button.setOnClickListener(new View.OnClickListener(){
                                      public void onClick (View v){
                //perform action on click
                String dateString = String.format("%04d/%02d/%02d %02d:%02d:00",
                       datePicker.getYear(), datePicker.getMonth() + 1,
                       datePicker.getDayOfMonth(), timePicker.getCurrentHour(),
                       timePicker.getCurrentMinute());
                SpecialDate date = new SpecialDate(textLabel.getText().toString(), dateString);
                       ItemAdapter adapter = new ItemAdapter(EnterDate.this);
                       adapter.addDate(date);
                finish();    }
                                  });
        textLabel = (TextView)findViewById(R.id.dateString); //test
        timePicker = (TimePicker)findViewById(R.id.timePicker);
        datePicker = (DatePicker)findViewById(R.id.datePicker);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_dates, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
