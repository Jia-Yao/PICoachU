package cs147.picoachu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class PublishActivity extends AppCompatActivity {

    private int userid = Data.currentUserId;

    BottomNavigationView navigation;
    Menu navigation_menu;
    Typeface type;
    Typeface type_reg;
    ArrayList<String> inputTags;
    LinearLayout addTagsView;

    // Switch to other activities
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Intent i;
            switch (item.getItemId()) {
                case R.id.navigation_camera:
                    return true;
                case R.id.navigation_explore:
                    i = new Intent(PublishActivity.this, ExploreActivity.class);
//                    getSupportFragmentManager().beginTransaction()
//                            .remove(cameraFragment)
//                            .commit();
//                    finish();
                    startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_challenges:
                    i = new Intent(PublishActivity.this, ChallengesActivity.class);
//                    getSupportFragmentManager().beginTransaction()
//                            .remove(cameraFragment)
//                            .commit();
//                    finish();
                    startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_me:
                    i = new Intent(PublishActivity.this, MeActivity.class);
//                    getSupportFragmentManager().beginTransaction()
//                            .remove(cameraFragment)
//                            .commit();
//                    finish();
                    startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
            }
            return false;

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        type = Typeface.createFromAsset(getAssets(),"fonts/DIN Condensed Bold.ttf");
        type_reg = Typeface.createFromAsset(getAssets(),"fonts/DIN Condensed Reg.ttf");
        inputTags = new ArrayList<String>();

        // Set up bottom bar
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation_menu = navigation.getMenu();
        MenuItem menuItem = navigation_menu.getItem(0);
        menuItem.setChecked(true);

        // get img_path
        Bundle extras = getIntent().getExtras();
        String img_path;
        img_path = extras.getString("img_path");
//        byte[]  bs = getIntent().getByteArrayExtra("bytes");
//        Bitmap b = BitmapFactory.decodeByteArray(
//                bs,0,bs.length);
//        if (b != null){
//            ImageView myImage = (ImageView) findViewById(R.id.userPhotoView);
//
//            myImage.setImageBitmap(b);
//        }

        if (img_path!=null){
            File imgFile = new  File(img_path);

            if(imgFile.exists()){

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                ImageView myImage = (ImageView) findViewById(R.id.userPhotoView);

                myImage.setImageBitmap(myBitmap);

            }
        }

        // set spinner
        Spinner spinner = (Spinner) findViewById(R.id.challengeDropDown);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayList<String> challenges = new ArrayList<String>();
        challenges.add("None");
        for (int chaId : Data.getUser(userid).acceptedChallenges){
            challenges.add(Data.getChallenge(chaId).title);
        }
        for (int chaId : Data.getUser(userid).completedChallenges){
            challenges.add(Data.getChallenge(chaId).title);
        }
        ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, challenges);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new SpinnerActivity());

        // Set add tag button
        addTagsView = (LinearLayout) findViewById(R.id.addTagsView);
        final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(4, 0, 4, 0);
        ImageButton addTagButton = (ImageButton)findViewById(R.id.addTagButton);
        addTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PublishActivity.this);
                TextView title = new TextView(PublishActivity.this);
                title.setText("Enter Tag");
                title.setTypeface(type);
                title.setPadding(0, 30, 0, 10);
                title.setGravity(Gravity.CENTER);
                title.setTextSize(22);
                builder.setCustomTitle(title);
                // Set up the input
                final EditText input = new EditText(PublishActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setTypeface(type_reg);
                input.setTextSize(20);
                builder.setView(input);
                builder.setCancelable(true);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        inputTags.add(input.getText().toString());
                        TextView tagView = new TextView(PublishActivity.this);
                        tagView.setText(input.getText().toString());
                        tagView.setTypeface(type_reg);
                        tagView.setTextSize(18);
                        tagView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        tagView.setBackgroundResource(R.drawable.tag_rounded_corners);
                        addTagsView.addView(tagView, lp);
                        // Hide keyboard
                        InputMethodManager inputManager = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(v.getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                // Disable ok button if input is empty
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                final Button okButton = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                input.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable arg0) {}
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(TextUtils.isEmpty(input.getText())) {
                            okButton.setEnabled(false);
                        } else {
                            okButton.setEnabled(true);
                        }
                    }
                });
            }
        });
    }

    // When this activity is resumed
    @Override
    protected void onRestart() {
        super.onRestart();
        MenuItem menuItem = navigation_menu.getItem(0);
        menuItem.setChecked(true);
    }

    // Remove transition animation
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    // properties for new photo
    private int forChallenge = -1;
    private int photoid;

    private String photoName;

    private int ownerid;

    private String userPhotoName;

    private ArrayList<String> tags;

    private ArrayList<ArrayList<String>> comments;

    public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            // An item was selected. You can retrieve the selected item using
            // parent.getItemAtPosition(pos)
            String challenge = (String) parent.getItemAtPosition(pos);
            if (challenge!="None"){
                for (int chaId : Data.getUser(userid).acceptedChallenges){
                    if (Data.getChallenge(chaId).title == challenge){
                        forChallenge = chaId;
                    }
                }
                for (int chaId : Data.getUser(userid).completedChallenges){
                    if (Data.getChallenge(chaId).title == challenge){
                        forChallenge = chaId;
                    }
                }
            }


        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
        }

    }

    /* TODO: call back for publish button:
        0. add new tag?
        1. build photo object with tags + challenges, id, name, owner
        2. add photo to user
        3. dump json file
        4. change challenge status in user
    */



}
