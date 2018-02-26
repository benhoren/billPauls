package com.benapp.anull.bill;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

import org.w3c.dom.Text;

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

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
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

            //        LayoutInflater inflater = (LayoutInflater)    this.getSystemService(LAYOUT_INFLATER_SERVICE);
//            popup = inflater.inflate(R.layout.popup,
//                    (ViewGroup) findViewById(R.id.popupid));
        }
    }


    int service =10;
    public void servRate(View view){

        ImageView button = (ImageView) view;

        switch (service){
            case 10:
                service =15;
                button.setImageResource(R.drawable.ic_action_sentiment_neutral);
                break;

            case 15:
                service =20;
                button.setImageResource(R.drawable.ic_action_sentiment_satisfied);
                break;

            case 20:
                service =10;
                button.setImageResource(R.drawable.ic_action_sentiment_dissatisfied);
                break;
        }

        double sum = 0;
        for(int i=0; i<friends.size(); i++){
            sum+=Double.parseDouble(friends.get(i)[1]);
        }

        TextView tipview = (TextView) total.findViewById(R.id.tip);
        int tip = (int)(sum * ( (double)service/100.0)) ;
        tipview.setText(tip+"");

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

                        if(a.isEmpty())
                            a="1";

                        try{
                            Integer.parseInt(a);
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
                    Integer.parseInt(a.getText().toString().trim());
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
        double p=0;
        String _p="";
        double sum = 0;
        for(int i=0 ; i<friends.size(); i++){
            _p="";
            p=0;
            View child = getLayoutInflater().inflate(R.layout.friend, null);

            TextView n = (TextView) child.findViewById(R.id.name);
            TextView s = (TextView) child.findViewById(R.id.sum);

            n.setText(friends.get(i)[0]);

             p = Double.parseDouble(friends.get(i)[1]);
             sum += p;
            if(p!= (int)p){
                p=p*10;
                p = (int) p;
                p = p/10;
                _p = "0";
            }
            _p = p + _p;

            s.setText(_p);


            items.addView(child);
        }


        if(total == null) {
            View sumlay = getLayoutInflater().inflate(R.layout.sum, null);
            total = sumlay.findViewById(R.id.totalBill);
        }

        TextView sumview = (TextView) total.findViewById(R.id.friendsum);
        sumview.setText(sum+"");

        TextView tipview = (TextView) total.findViewById(R.id.tip);
        int tip = (int)(sum * ( (double)service/100.0)) ;
        tipview.setText(tip+"");

        items.addView(total);

//        total.setOnTouchListener(new OnSwipeTouchListener(this) {
//            @Override
//            public void onSwipeLeft() {
//                // Whatever
//            }
//        });

    }



    FrameLayout total ;



    public static Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        view.draw(c);
        return bitmap;
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

        View child = getLayoutInflater().inflate(R.layout.item, null);

        TextView f = (TextView) child.findViewById(R.id.first);
        TextView s = (TextView) child.findViewById(R.id.second);
        TextView t = (TextView) child.findViewById(R.id.third);
        TextView fo = (TextView) child.findViewById(R.id.four);
        String name="";


        child.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {


                return true;
            }
        });


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
//        final View iconView = menuView.getChildAt(2).findViewById(android.support.design.R.id.icon);
//        final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
//        final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//        layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 43, displayMetrics);
//        layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 43, displayMetrics);
//        iconView.setLayoutParams(layoutParams);
    }








    /**
     * Stack Blur v1.0 from
     * http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
     * Java Author: Mario Klingemann <mario at quasimondo.com>
     * http://incubator.quasimondo.com
     *
     * created Feburary 29, 2004
     * Android port : Yahel Bouaziz <yahel at kayenko.com>
     * http://www.kayenko.com
     * ported april 5th, 2012
     *
     * This is a compromise between Gaussian Blur and Box blur
     * It creates much better looking blurs than Box Blur, but is
     * 7x faster than my Gaussian Blur implementation.
     *
     * I called it Stack Blur because this describes best how this
     * filter works internally: it creates a kind of moving stack
     * of colors whilst scanning through the image. Thereby it
     * just has to add one new block of color to the right side
     * of the stack and remove the leftmost color. The remaining
     * colors on the topmost layer of the stack are either added on
     * or reduced by one, depending on if they are on the right or
     * on the left side of the stack.
     *
     * If you are using this algorithm in your code please add
     * the following line:
     * Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>
     */

    public Bitmap fastblur(Bitmap sentBitmap, float scale, int radius) {

        int width = Math.round(sentBitmap.getWidth() * scale);
        int height = Math.round(sentBitmap.getHeight() * scale);
        sentBitmap = Bitmap.createScaledBitmap(sentBitmap, width, height, false);

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = ( 0xff000000 & pix[yi] ) | ( dv[rsum] << 16 ) | ( dv[gsum] << 8 ) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }
}
