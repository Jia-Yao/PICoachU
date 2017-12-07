package cs147.picoachu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class PublishActivity extends AppCompatActivity {

    private int userid = Data.currentUserId;

    BottomNavigationView navigation;
    Menu navigation_menu;

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
