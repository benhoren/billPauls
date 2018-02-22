package com.benapp.anull.bill;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;


public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private AlertDialog.Builder alertDialogBuilder;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    HomeClick();
                    return true;
                case R.id.navigation_dashboard:
                    DachClick();
                    return true;
//                case R.id.navigation_notifications:
//                    mTextMessage.setText(R.string.title_notifications);
//                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        DachClick();
        HomeClick();
        bulidPopUp();
//        popupcreate();
    }

    View popup =null;
    protected void popupcreate() {

        LayoutInflater inflater = (LayoutInflater)      this.getSystemService(LAYOUT_INFLATER_SERVICE);

        if(popup == null) {
            popup = inflater.inflate(R.layout.popup,
                    (ViewGroup) findViewById(R.id.popupid));
        }
    }




    LinearLayout poptpLayout = null;

    private void bulidPopUp(){
        alertDialogBuilder = new AlertDialog.Builder(this);

        poptpLayout = new LinearLayout(this);

        popupcreate();

        LayoutInflater inflater = (LayoutInflater)      this.getSystemService(LAYOUT_INFLATER_SERVICE);

        final View popup = inflater.inflate(R.layout.popup,
                    (ViewGroup) findViewById(R.id.popupid));



        poptpLayout.addView(popup);
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(poptpLayout);




        // set dialog message
        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {


                View child;
                ArrayList<String> names = new ArrayList<String>();
                LinearLayout namescroll = (LinearLayout) poptpLayout.findViewById(R.id.tinynames);
                for(int i=0; i<namescroll.getChildCount(); i++){
                    child = namescroll.getChildAt(i);
                    TextView tv = (TextView) child.findViewById(R.id.textView);
                    names.add(tv.getText().toString());
                }

                String n = ((EditText)poptpLayout.findViewById(R.id.Name)).getText().toString();
                if(!n.isEmpty())
                    names.add(n);

                String d = ((EditText)poptpLayout.findViewById(R.id.Description)).getText().toString();
                String a = ((EditText)poptpLayout.findViewById(R.id.Amount)).getText().toString();
                String p = ((EditText)poptpLayout.findViewById(R.id.Price)).getText().toString();

                addItem(names,d,a,p);

                int am = Integer.parseInt(a);
                double pr = Double.parseDouble(p);

                refreshSummary(names,d,am,pr);

                bulidPopUp();
            }
        });

    }


    void fun(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.countries_list);
        textView.setAdapter(adapter);
    }



    public void addName(View view){
        LinearLayout items = (LinearLayout) poptpLayout.findViewById(R.id.tinynames);

        View child = getLayoutInflater().inflate(R.layout.tinyname, null);

        String n = ((EditText)poptpLayout.findViewById(R.id.Name)).getText().toString();

        ((EditText)poptpLayout.findViewById(R.id.Name)).setText("");
        TextView fo = (TextView) child.findViewById(R.id.textView);
        fo.setText(n);

        items.addView(child);
    }


    void refreshSummary(ArrayList<String> names, String desc, int amount, double price) {
        int friendIndex = -1;

        double priceforone =(((double)amount) * price)/(names.size());

        for( int j=0; j< names.size(); j++){
            for (int i = 0; i < friends.size(); i++) {
                if (friends.get(i)[0].equals(names.get(j))) {
                    friendIndex = i;
                    i = friends.size();;
                }
            }

            if (friendIndex == -1) {
                String[] newf = {names.get(j),""+ priceforone};
                friends.add(newf);
                friendIndex = friends.size()-1;
            }

            else {
                double oldp = Double.parseDouble(friends.get(friendIndex)[1]);
                String newp = (oldp + priceforone) + "";
                friends.get(friendIndex)[1] = newp;
            }
            friendIndex=-1;
//            updateSummrayScreen(friendIndex);
        }
        updateSummrayScreen();
    }

    void updateSummrayScreen(){
        LinearLayout items = (LinearLayout) summarypage.findViewById(R.id.friendsContainer);

        items.removeAllViews();

        for(int i=0 ; i<friends.size(); i++){
            View child = getLayoutInflater().inflate(R.layout.friend, null);

            TextView n = (TextView) child.findViewById(R.id.name);
            TextView s = (TextView) child.findViewById(R.id.sum);

            n.setText(friends.get(i)[0]);
            s.setText(friends.get(i)[1]);

            items.addView(child);
        }

    }

    void updateSummrayScreen(int friendIndex){

        LinearLayout items = (LinearLayout) summarypage.findViewById(R.id.friendsContainer);

        if(friendIndex == items.getChildCount()) {
            View child = getLayoutInflater().inflate(R.layout.friend, null);

            Log.d("Ben", "here" + friendIndex);
            TextView n = (TextView) child.findViewById(R.id.name);
            TextView s = (TextView) child.findViewById(R.id.sum);

            n.setText(friends.get(friendIndex)[0]);
            s.setText(friends.get(friendIndex)[1]);
//            n.setText("adsd");
//            s.setText("adsd");

            items.addView(child);
        }

        else{
            View child = items.getChildAt(friendIndex);
            TextView n = (TextView) child.findViewById(R.id.name);
            TextView s = (TextView) child.findViewById(R.id.sum);

            n.setText(friends.get(friendIndex)[0]);
            s.setText(friends.get(friendIndex)[1]);
        }

        Log.d("Ben","add friend");
        Log.d("Ben","size: "+friends.size());
        Log.d("Ben","size items: "+items.getChildCount());
    }

    ArrayList<String[]> friends = new ArrayList<String[]>();

    private void addItem(ArrayList<String> names,String description , String amount, String price){
        LinearLayout items = (LinearLayout) findViewById(R.id.itemContainer);

        View child = getLayoutInflater().inflate(R.layout.item, null);

        TextView f = (TextView) child.findViewById(R.id.first);
        TextView s = (TextView) child.findViewById(R.id.second);
        TextView t = (TextView) child.findViewById(R.id.third);
        TextView fo = (TextView) child.findViewById(R.id.four);
        String name="";
        for(int i=0; i<names.size(); i++){
            name+=names.get(i);
        }

        f.setText(name);
        s.setText(description);
        t.setText(amount);
        fo.setText(price);

        String count =  ""+items.getChildCount();

        items.addView(child);
        Log.d("Ben","add item");
        Log.d("Ben","size: "+count);
    }


    public void addItem(View view){

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }




    View summarypage =null;
    protected void DachClick() {

        FrameLayout frame = (FrameLayout) findViewById(R.id.content);
        removeOld(frame);

        if(summarypage == null) {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
            summarypage = inflater.inflate(R.layout.summary,
                    (ViewGroup) findViewById(R.id.summaryid));
        }
        frame.addView(summarypage);

    }

    View homepage =null;
    protected void HomeClick() {
        FrameLayout frame = (FrameLayout) findViewById(R.id.content);
        removeOld(frame);

        LayoutInflater inflater = (LayoutInflater)      this.getSystemService(LAYOUT_INFLATER_SERVICE);

        if(homepage == null) {
            homepage = inflater.inflate(R.layout.additem,
                    (ViewGroup) findViewById(R.id.additemid));
        }
        frame.addView(homepage);
    }

    private void removeOld(FrameLayout frame){
        try{
            LayoutInflater oldinflater = (LayoutInflater)      this.getSystemService(LAYOUT_INFLATER_SERVICE);
            View oldchildLayout = oldinflater.inflate(R.layout.summary,
                    (ViewGroup) findViewById(R.id.summaryid));
            frame.removeView(oldchildLayout);
        }catch(Exception e){}

        try {
            LayoutInflater oldinflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
            View oldchildLayout = oldinflater.inflate(R.layout.additem,
                    (ViewGroup) findViewById(R.id.additemid));
            frame.removeView(oldchildLayout);
        }catch(Exception e){}

    }


}
