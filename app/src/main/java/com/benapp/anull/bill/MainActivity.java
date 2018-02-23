package com.benapp.anull.bill;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;


public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private AlertDialog alertDialogBuilder;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
//                    HomeClick();
                    changePage(false);
                    return true;
                case R.id.navigation_dashboard:
//                    DachClick();
                    changePage(true);
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

//        DachClick();
//        HomeClick();
        bulidPage();
        bulidAlertOne();
        bulidPopUp();

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


    private void bulidAlertOne(){
        alertDialogBuilder = new AlertDialog.Builder(this)
                .setPositiveButton(android.R.string.ok, null) //Set to null. We override the onclick
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        alertDialogBuilder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = ((AlertDialog) alertDialogBuilder).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        ArrayList<String> names =getlilist();


                        String n = ((AutoCompleteTextView)poptpLayout.findViewById(R.id.Name)).getText().toString();
                        n=n.trim();

                        if(!n.isEmpty())
                            names.add(n);

                        String d = ((EditText)poptpLayout.findViewById(R.id.Description)).getText().toString();
                        String a = ((EditText)poptpLayout.findViewById(R.id.Amount)).getText().toString();
                        String p = ((EditText)poptpLayout.findViewById(R.id.Price)).getText().toString();


                        d=d.trim();
                        a=a.trim();
                        p=p.trim();

                        boolean ok = true;

                        if(names.size()==0)
                            ok=false;


                        try{
                            Double.parseDouble(a);
                            ((EditText)poptpLayout.findViewById(R.id.Amount)).setTextColor(Color.BLACK);
                        } catch (Exception e){ok=false;
                            ((EditText)poptpLayout.findViewById(R.id.Amount)).setTextColor(Color.RED);}

                        try{
                            Double.parseDouble(p);
                            ((EditText)poptpLayout.findViewById(R.id.Price)).setTextColor(Color.BLACK);
                        } catch (Exception e){ok=false;
                            ((EditText)poptpLayout.findViewById(R.id.Price)).setTextColor(Color.RED);}



                        if(ok) {
                            addItem(names, d, a, p);

                            int am = Integer.parseInt(a);
                            double pr = Double.parseDouble(p);

                            refreshSummary(names, d, am, pr);

                            //Dismiss once everything is OK.
                            alertDialogBuilder.dismiss();
                            bulidAlertOne();
                            bulidPopUp();

                        }
                    }
                });
            }
        });
    }

    private void bulidPopUp(){

        poptpLayout = new LinearLayout(this);

        popupcreate();
        LayoutInflater inflater = (LayoutInflater)      this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popup = inflater.inflate(R.layout.popup,
                    (ViewGroup) findViewById(R.id.popupid));
        poptpLayout.addView(popup);

        alertDialogBuilder.setView(poptpLayout);
        dropdown();



        final EditText a = (EditText)poptpLayout.findViewById(R.id.Amount);
        final EditText p = (EditText)poptpLayout.findViewById(R.id.Price);



        a.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                try{
                    Double.parseDouble(a.getText().toString().trim());
                    a.setTextColor(Color.BLACK);
                } catch (Exception e){
                    a.setTextColor(Color.RED);}
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        p.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                try{
                    Double.parseDouble(p.getText().toString().trim());
                    p.setTextColor(Color.BLACK);
                } catch (Exception e){
                    p.setTextColor(Color.RED);}
            }


            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });


    }

    private ArrayList<String> getlilist(){
        ArrayList<String> names = new ArrayList<String>();
        View child;
        LinearLayout namescroll = (LinearLayout) poptpLayout.findViewById(R.id.tinynames);
        for(int i=0; i<namescroll.getChildCount(); i++){
            child = namescroll.getChildAt(i);
            TextView tv = (TextView) child.findViewById(R.id.textView);
            names.add(tv.getText().toString().trim());
        }
        return names;
    }



    String[] nameList;
    ArrayList <String> namesforList = new ArrayList <String>();

    public void dropdown(){
        nameList = new String[namesforList.size()];
        for (int i=0; i<nameList.length; i++){
           nameList[i] = namesforList.get(i);
        }

        Log.d("Ben", "nameLists: "+nameList.length);
        Log.d("Ben", "friends: "+friends.size());
        Log.d("Ben", "namesforList: "+namesforList.size());
        creatAutotxt();
    }


    void creatAutotxt(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1, nameList);
        final AutoCompleteTextView textView = (AutoCompleteTextView)
                poptpLayout.findViewById(R.id.Name);
        textView.setAdapter(adapter);

        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                String item = (String) parent.getItemAtPosition(position);
                addNameToTiny(item);

            }
        });



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {}}, 500);

        textView.setThreshold(1);
        textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    textView.showDropDown();
                }
            }
        });
        textView.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                textView.showDropDown();
                return false;
            }
        });
    }

    private void addNameToTiny(String name){
        boolean newname = true;
        ArrayList <String> lillidt = getlilist();

        String n = name;
        for (int i=0; i<lillidt.size(); i++){
            if (lillidt.get(i).equals(n))
                newname = false;
        }
        LinearLayout items = (LinearLayout) poptpLayout.findViewById(R.id.tinynames);

        View child = getLayoutInflater().inflate(R.layout.tinyname, null);

        if(!n.isEmpty() && newname) {

            ((EditText) poptpLayout.findViewById(R.id.Name)).setText("");
            TextView fo = (TextView) child.findViewById(R.id.textView);
            fo.setText(n);

            for (int i = 0; i < namesforList.size(); i++) {
                if (namesforList.get(i).equals(n)) {
                    namesforList.remove(i);
                    dropdown();
                    break;
                }
            }
            items.addView(child);
        }

    }


    public void addName(View view){
        LinearLayout items = (LinearLayout) poptpLayout.findViewById(R.id.tinynames);

        View child = getLayoutInflater().inflate(R.layout.tinyname, null);

        String n = ((EditText)poptpLayout.findViewById(R.id.Name)).getText().toString();
        n=n.trim();
        boolean newname = true;
        ArrayList <String> lillidt = getlilist();

        for (int i=0; i<lillidt.size(); i++){
            if (lillidt.get(i).equals(n))
                newname = false;
        }


        if(!n.isEmpty() && newname) {

            ((EditText) poptpLayout.findViewById(R.id.Name)).setText("");
            TextView fo = (TextView) child.findViewById(R.id.textView);
            fo.setText(n);

            for (int i = 0; i < namesforList.size(); i++) {
                if (namesforList.get(i).equals(n)) {
                    namesforList.remove(i);
                    dropdown();
                    break;
                }
            }
            items.addView(child);
        }
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
                String[] newf = {names.get(j), "" + priceforone};
                friends.add(newf);
                friendIndex = friends.size() - 1;

                nameList = new String[namesforList.size()];
                for (int i = 0; i < nameList.length; i++) {
                    nameList[i] = namesforList.get(i);
                }

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

        View child = getLayoutInflater().inflate(R.layout.nitem, null);

        TextView f = (TextView) child.findViewById(R.id.first);
        TextView s = (TextView) child.findViewById(R.id.second);
        TextView t = (TextView) child.findViewById(R.id.third);
        TextView fo = (TextView) child.findViewById(R.id.four);
        String name="";

        f.setText(names.get(0));

        for(int i=1; i<names.size(); i++){
           TextView na = new TextView(this);
            na.setText(", "+names.get(i));
            na.setTextSize(18);

            LinearLayout itmnm = child.findViewById(R.id.itemname);
            itmnm.addView(na);
        }


        s.setText(description);
        t.setText(amount);
        fo.setText(price);

        String count =  ""+items.getChildCount();

        items.addView(child);
//        Log.d("Ben","add item");
//        Log.d("Ben","size: "+count);
    }


    public void addItem(View view){
        // create alert dialog
        // show it

        namesforList = new ArrayList<String>();
        for(int i=0; i<friends.size(); i++)
            namesforList.add(friends.get(i)[0]);

        alertDialogBuilder.show();

        dropdown();
    }


    View summarypage =null;
    View homepage =null;
    protected void bulidPage(){
        if(summarypage == null) {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
            summarypage = inflater.inflate(R.layout.summary,
                    (ViewGroup) findViewById(R.id.summaryid));
        }
        if(homepage == null) {
            LayoutInflater inflater = (LayoutInflater)      this.getSystemService(LAYOUT_INFLATER_SERVICE);
            homepage = inflater.inflate(R.layout.additem,
                    (ViewGroup) findViewById(R.id.additemid));
        }
    }
    protected void changePage(boolean summ){
        FrameLayout frame = (FrameLayout) findViewById(R.id.content);
        if(summ){
            frame.removeView(homepage);
            frame.addView(summarypage);
        }
        if(!summ){
            frame.removeView(summarypage);
            frame.addView(homepage);
        }
    }



//    View summarypage =null;
//    protected void DachClick() {
//
//        FrameLayout frame = (FrameLayout) findViewById(R.id.content);
//        removeOld(frame);
//
//        if(summarypage == null) {
//            LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
//            summarypage = inflater.inflate(R.layout.summary,
//                    (ViewGroup) findViewById(R.id.summaryid));
//        }
//        frame.addView(summarypage);
//    }
//
//    View homepage =null;
//    protected void HomeClick() {
//        FrameLayout frame = (FrameLayout) findViewById(R.id.content);
//        removeOld(frame);
//
//        LayoutInflater inflater = (LayoutInflater)      this.getSystemService(LAYOUT_INFLATER_SERVICE);
//
//        if(homepage == null) {
//            homepage = inflater.inflate(R.layout.additem,
//                    (ViewGroup) findViewById(R.id.additemid));
//        }
//        frame.addView(homepage);
//    }
//
//    /**
//     * remove old child from main frame
//     * @param frame
//     */
//    private void removeOld(FrameLayout frame){
//        try{
//            LayoutInflater oldinflater = (LayoutInflater)      this.getSystemService(LAYOUT_INFLATER_SERVICE);
//            View oldchildLayout = oldinflater.inflate(R.layout.summary,
//                    (ViewGroup) findViewById(R.id.summaryid));
//            frame.removeView(oldchildLayout);
//        }catch(Exception e){}
//
//        try {
//            LayoutInflater oldinflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
//            View oldchildLayout = oldinflater.inflate(R.layout.additem,
//                    (ViewGroup) findViewById(R.id.additemid));
//            frame.removeView(oldchildLayout);
//        }catch(Exception e){}
//
//    }


}
