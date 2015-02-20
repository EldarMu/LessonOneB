package com.eldar.lessononeb;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.drm.DrmStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Vector;

/**
 * Created by EldarM on 10/23/2014.
 */
public class ItemAdapter extends BaseAdapter {
    private static final String LOG_TAG = ItemAdapter.class.getCanonicalName();
    private static final String dataFileName = "data_my_dates.txt";

    private final Context context;
    private final LayoutInflater layoutInflater;
    private Vector<SpecialDate> dates;

    public ItemAdapter(Context c) {
        context = c;

        layoutInflater = LayoutInflater.from(context);
        dates = new Vector<SpecialDate>();
    }

    public void addDate(SpecialDate date){
        dates.add(date);
    }

    public void deleteDate(String label){
        Log.d(LOG_TAG, "passing label: " + label);
        Vector<SpecialDate> newDates = new Vector<SpecialDate>();
            for (SpecialDate date : dates){
                if (!label.equals(date.getLabel())){
                    Log.d(LOG_TAG, "label received: " + date.getLabel());
                    newDates.add(date);
                }
            }
        setDates(newDates); //setDates notifies data set changed (tells the UI)
    }

    class ItemDeleter implements DialogInterface.OnClickListener{

        String label;
        public ItemDeleter(String label){
        this.label = label;
        }
        public void onClick(DialogInterface dialog, int id) {
            Toast.makeText(context, "Deleting...", Toast.LENGTH_LONG)
                    .show();
            deleteDate(label);
            saveDates();
        }

    }

    public void setDates(Vector<SpecialDate> dates){
        if (dates != null){
            this.dates = dates;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return dates.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override

    public View getView(int i, View view, ViewGroup parent) {
        //Log.d(LOG_TAG, String.format("getView() for item %d", i));
        LinearLayout itemView;
        if (view == null) { // Create a new view if no recycled view is available
                    itemView = (LinearLayout) layoutInflater.inflate(
                    R.layout.list_item, parent,
                    false /* attachToRoot */);
        } else {
            itemView = (LinearLayout) view;
        }
        TextView textView = (TextView)itemView.findViewById(R.id.textLabel);
        textView.setText(String.format(dates.elementAt(i).getLabel()));

        textView = (TextView)itemView.findViewById(R.id.textDate);
        textView.setText(dates.elementAt(i).toString());

        textView = (TextView)itemView.findViewById(R.id.textValue);
        textView.setText(dates.elementAt(i).timeSince());

        itemView.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent){
                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN: //Touch item
                        TextView textView = (TextView)view.findViewById(R.id.textLabel);
                    new AlertDialog.Builder(context)
                            .setTitle("Delete date?")
                            .setMessage("'Yes' to delete " + textView.getText())
                            .setPositiveButton("Yes",
                                    new ItemDeleter(textView.getText().toString()))
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Toast.makeText(context, "Retaining", Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })
                            .create()
                            .show();
                        break;
                    case MotionEvent.ACTION_MOVE: //do nothing while finger moving on item
                    case MotionEvent.ACTION_UP: //do nothing if finger lifted off item
                        break;
                }
                return false;
            }
        });
        return itemView;
    }
    public void saveDates(){
        try{
            FileOutputStream outputStream =
                    context.openFileOutput(dataFileName, Context.MODE_PRIVATE);
            SpecialDate.writeDatesList(outputStream, dates);
            outputStream.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadDates() {
        try {
            Log.d(LOG_TAG, "Loading the dates.");
            FileInputStream is = context.openFileInput(dataFileName);
            setDates(SpecialDate.readDatesList(is));
            is.close();
        } catch (Exception e) {
            Log.d(LOG_TAG, "Loading the dates.... oops!");
            e.printStackTrace();
        }
    }
}
