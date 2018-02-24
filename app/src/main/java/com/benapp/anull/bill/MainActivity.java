package com.benapp.anull.bill;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
        resizeIcons();
        bulidPage();
        changePage(false);
        bulidAlertOne();
        bulidPopUp();
    }

    View popup =null;
    protected void popupcreate() {

//        LayoutInflater inflater = (LayoutInflater)    this.getSystemService(LAYOUT_INFLATER_SERVICE);

        if(popup == null) {
            popup = getLayoutInflater().inflate(R.layout.popup, null);
            popup = popup.findViewById(R.id.popupid);

//            popup = inflater.inflate(R.layout.popup,
//                    (ViewGroup) findViewById(R.id.popupid));
        }
    }




    LinearLayout poptpLayout = null;



    private void bulidAlertOne(){
        alertDialogBuilder = new AlertDialog.Builder(this ,R.style.MyDialogTheme)
                .setPositiveButton(android.R.string.ok, null) //Set to null. We override the onclick
                .setNegativeButton(android.R.string.cancel, null)
                .create();



        poptpLayout = new LinearLayout(this);
        alertDialogBuilder.setView(poptpLayout);



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
                            ((EditText)poptpLayout.findViewById(R.id.Amount)).setTextColor(getResources().getColor(R.color.textcolor));
                        } catch (Exception e){ok=false;
                            ((EditText)poptpLayout.findViewById(R.id.Amount)).setTextColor(Color.RED);}

                        try{
                            Double.parseDouble(p);
                            ((EditText)poptpLayout.findViewById(R.id.Price)).setTextColor(getResources().getColor(R.color.textcolor));
                        } catch (Exception e){ok=false;
                            ((EditText)poptpLayout.findViewById(R.id.Price)).setTextColor(Color.RED);}



                        if(ok) {
                            addItem(names, d, a, p);

                            int am = Integer.parseInt(a);
                            double pr = Double.parseDouble(p);

                            refreshFriendsList(names, d, am, pr);

                            //Dismiss once everything is OK.
                            alertDialogBuilder.dismiss();
                            updateSummrayScreen();
//                            bulidAlertOne();
                            bulidPopUp();
                        }
                    }
                });
            }
        });
    }

    /**
     * build the pop up
     */
    private void bulidPopUp(){

        poptpLayout.removeView(popup);
        popup=null;
        popupcreate();
        poptpLayout.addView(popup);

        dropdown();

        final EditText a = (EditText)poptpLayout.findViewById(R.id.Amount);
        final EditText p = (EditText)poptpLayout.findViewById(R.id.Price);



        a.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                try{
                    Double.parseDouble(a.getText().toString().trim());
                    a.setTextColor(getResources().getColor(R.color.textcolor));
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
                    p.setTextColor(getResources().getColor(R.color.textcolor));
                } catch (Exception e){
                    p.setTextColor(Color.RED);}
            }


            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });


    }

    /**
     * get the tiny names from pop-up
     * @return list with tiny -names
     */
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

    /**
     * update the list of the autocomplete
     */
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


    /**
     * create the autoComplete list
     */
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

    /**
     * add name to tiny if item was selected from autoComplete
     * @param name
     */
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
            fo.setTextColor(getResources().getColor(R.color.textcolor));
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



    /**
     * add tiny name when '+' button in popup clicked.
     * @param view
     */
    public void addTinyName(View view){
        LinearLayout items = (LinearLayout) poptpLayout.findViewById(R.id.tinynames);

        FrameLayout child = (FrameLayout)getLayoutInflater().inflate(R.layout.tinyname, null);




//        ImageView IM = child.findViewById(R.id.backpic);
//        IM.getLayoutParams().height = child.getHeight();
//        IM.getLayoutParams().width = child.getWidth();
//        IM.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rectangle));
//        ImageView iv = new ImageView(getApplicationContext());
//        iv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rectangle));
//        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams (child.getWidth(), child.getHeight());
//        iv.setLayoutParams(lp);
//        child.addView(iv);



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

            Log.d("Ben", "HEREREE");


        }
    }
    /**
     * remove tiny name when 'x' button clicked in pop up.
     * @param view
     */
    public void removeTintyName(View view){
        RelativeLayout parent = (RelativeLayout) view.getParent();

        TextView tv = parent.findViewById(R.id.textView);
        FrameLayout fl =(FrameLayout) parent.getParent();
        LinearLayout items = (LinearLayout) poptpLayout.findViewById(R.id.tinynames);

        items.removeView(fl);
//        fl.getLayoutParams();
    }


    ArrayList<String[]> friends = new ArrayList<String[]>();
    /**
     * update Friends list after a change (what pop-up closed.)
     * @param names
     * @param desc
     * @param amount
     * @param price
     */
    void refreshFriendsList(ArrayList<String> names, String desc, int amount, double price) {
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

        }
    }


    /**
     * update summary frame - by the friends List.
     */
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





    /**
     * add an item to main screen, when pop-up closed.
     * @param names
     * @param description
     * @param amount
     * @param price
     */
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

            na.setTextAppearance(this, R.style.textstyle);


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


    /**
     * main screen button to add item. open the pop-up.
     * @param view
     */
    public void addItem(View view){

        namesforList = new ArrayList<String>();
        for(int i=0; i<friends.size(); i++)
            namesforList.add(friends.get(i)[0]);

        bulidPopUp();

        alertDialogBuilder.show();
        dropdown();
    }


    /**
     * change the main frame.
     * @param summ true if switch to summary, false if swith to home screen
     */
    protected void changePage(boolean summ){
        FrameLayout frame = (FrameLayout) findViewById(R.id.content);
        if(summ){
            try {
                frame.removeView(homepage);
                frame.addView(summarypage);
            }catch (Exception e){}
        }
        if(!summ){
            try{
                frame.removeView(summarypage);
                frame.addView(homepage);
            }catch (Exception e){}
        }
    }




    View summarypage =null;
    View homepage =null;

    /**
     * build the Summary frame and homepage frame. only on create
     */
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



    /**
     * resize navigation icons size
     */
    public void resizeIcons() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 42, displayMetrics);
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 42, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }
    }




}
