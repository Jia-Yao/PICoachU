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
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Debug;
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
import android.util.Log;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class PublishActivity extends AppCompatActivity {

    private int userid = Data.currentUserId;

    BottomNavigationView navigation;
    Menu navigation_menu;
    Typeface type;
    Typeface type_reg;
    LinearLayout addTagsView;

    // properties for new photo
    ArrayList<String> inputTags;
    private int forChallenge = -1;
    private int photoid;
    private String photoName = "";
    private int ownerid;
    private String userPhotoName = "";
    private ArrayList<ArrayList<String>> comments = new ArrayList<>();

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
        String extPhotoName = extras.getString("img_name");
//        byte[]  bs = getIntent().getByteArrayExtra("bytes");
//        Bitmap b = BitmapFactory.decodeByteArray(
//                bs,0,bs.length);
//        if (b != null){
//            ImageView myImage = (ImageView) findViewById(R.id.userPhotoView);
//
//            myImage.setImageBitmap(b);
//        }

        if (img_path!=null){
            photoName = extPhotoName;

            File imgFile = new  File(img_path);

            if(imgFile.exists()){

                ExifInterface exif = null;
                try {
                    exif = new ExifInterface(imgFile.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                Bitmap bmRotated = Data.rotateBitmap(myBitmap, orientation);

                ImageView myImage = (ImageView) findViewById(R.id.userPhotoView);

                myImage.setImageBitmap(bmRotated);

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

        // set publish button
        Button publishButton = (Button) findViewById(R.id.publishButton) ;
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/DIN Condensed Bold.ttf");
        publishButton.setTypeface(face);

        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userid = Data.currentUserId;
                ownerid = userid;
                // add new photo to user's list
                Photo newPhoto = FlushNewPhoto();
                // write to json
                Data.DumpUserPhoto(getExternalFilesDir(null).toString(),Data.getUser(userid), newPhoto);
                // update challenge
                if (Data.getUser(userid).acceptedChallenges.contains(forChallenge)){
                    Data.getUser(userid).acceptedChallenges.remove(new Integer(forChallenge));
                    Data.getUser(userid).completedChallenges.add(forChallenge);
                }
                Intent i = new Intent(PublishActivity.this, MeActivity.class);
                startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });

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


    // call back for publish button - save photo and infomation
    private Photo FlushNewPhoto(){
        Photo newPhoto = new Photo(photoName,ownerid,forChallenge,userPhotoName,inputTags,comments);
        Data.AddNewPhoto(userid, newPhoto);
        return newPhoto;
    }

    // listener for spinner
    public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            // An item was selected. You can retrieve the selected item using
            // parent.getItemAtPosition(pos)
            ((TextView) view).setTextColor(Color.BLACK);
            String challenge = (String) parent.getItemAtPosition(pos);
            if (challenge!="None"){
                Log.d("publish",challenge);
                inputTags.add(challenge);
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
