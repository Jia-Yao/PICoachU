package cs147.picoachu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class challengeDetail extends AppCompatActivity {

    private int userid = Data.currentUserId;
    private User currUser = Data.getUser(userid);
    BottomNavigationView navigation;
    Menu navigation_menu;

    TextView title;
    TextView challengeDescription;
    TextView challengeDetail;

    Button acceptButton;
    Button declineButton;

    TextView sampleTitle;
    ImageView sample1;

    // Switch to other activities
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Intent i;
            switch (item.getItemId()) {
                case R.id.navigation_camera:
                    i = new Intent(challengeDetail.this, CameraActivity.class);
                    finish();
                    startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_explore:
                    i = new Intent(challengeDetail.this, ExploreActivity.class);
                    finish();
                    startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_challenges:
                    i = new Intent(challengeDetail.this, ChallengesActivity.class);
                    finish();
                    startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_me:
                    i = new Intent(challengeDetail.this, MeActivity.class);
                    finish();
                    startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
            }
            return false;

        }
    };

    // Create this activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.err.println(currUser.acceptedChallenges.size());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_detail);
        int challengeid = 0;

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            challengeid = extras.getInt("challengeid");
        }

        final Challenge currChallenge = getChallenge(challengeid);
        title = (TextView)findViewById(R.id.title);
        acceptButton = (Button)findViewById(R.id.acceptButton);
        declineButton = (Button)findViewById(R.id.declineButton);

        Typeface boldFace = Typeface.createFromAsset(getAssets(), "fonts/DIN Condensed Bold.ttf");
        Typeface regFace = Typeface.createFromAsset(getAssets(), "fonts/DIN Condensed Reg.ttf");
        title.setTypeface(boldFace);
        title.setTextColor(Color.BLACK);

        challengeDescription = (TextView)findViewById(R.id.challengeDescription);
        challengeDescription.setText((CharSequence)currChallenge.title);
        challengeDescription.setTypeface(boldFace);
        challengeDescription.setTextColor(Color.BLACK);

        challengeDetail = (TextView)findViewById(R.id.challengeDetail);
        challengeDetail.setText((CharSequence)currChallenge.description);
        challengeDetail.setTypeface(regFace);
        challengeDetail.setTextColor(Color.BLACK);

        sampleTitle = (TextView)findViewById(R.id.sampleTitle);
        sampleTitle.setTypeface(regFace);
        sampleTitle.setTextColor(Color.BLACK);

        sample1 = (ImageView)findViewById(R.id.sample1);
        int resID = getResources().getIdentifier(currChallenge.examplePhotoName, "drawable", "cs147.picoachu");
        sample1.setImageResource(resID);

        acceptButton.setTypeface(boldFace);
        declineButton.setTypeface(boldFace);

        ArrayList<Challenge> activeChallenges = new ArrayList<Challenge>();
        for(Challenge c : Data.challenges) {
            if(!( currUser.acceptedChallenges.contains(c.challengeid) ||
                    currUser.completedChallenges.contains(c.challengeid))) {
                activeChallenges.add(c);
            }
        }

        if(activeChallenges.contains(currChallenge)) {

            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Show confirmation dialog window
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(challengeDetail.this);
                    builder1.setMessage("Are you sure you want to accept this challenge?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Data.getUser(userid).acceptedChallenges.add(currChallenge.challengeid);
                                    System.err.println(currUser.acceptedChallenges.size());
                                    recreate();
                                    dialog.cancel();
                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            });
        }
        else if(Data.getUser(userid).acceptedChallenges.contains(currChallenge.challengeid)){
            acceptButton.setBackgroundColor(Color.parseColor("#b3b3b3"));
            acceptButton.setText((CharSequence)"ACCEPTED");
            declineButton.setBackgroundColor(Color.parseColor("#ff6666"));

            declineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Show confirmation dialog window
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(challengeDetail.this);
                    builder1.setMessage("Are you sure you want to decline this challenge?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Data.getUser(userid).acceptedChallenges.remove(new Integer(currChallenge.challengeid));
                                    recreate();
                                    dialog.cancel();
                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            });

        }

        else {
            acceptButton.setBackgroundColor(Color.parseColor("#b3b3b3"));
            acceptButton.setText((CharSequence)"COMPLETED");
        }
        // Set up bottom bar
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation_menu = navigation.getMenu();
        MenuItem menuItem = navigation_menu.getItem(2);
        menuItem.setChecked(true);

    }

    // When this activity is resumed
    @Override
    protected void onRestart() {
        super.onRestart();
        MenuItem menuItem = navigation_menu.getItem(2);
        menuItem.setChecked(true);
    }

    // Remove transition animation
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    private Challenge getChallenge(int challengeid) {
        switch (challengeid) {
            case 1:
                return Data.challenge1;
            case 2:
                return Data.challenge2;
            case 3:
                return Data.challenge3;
            case 4:
                return Data.challenge4;
            case 5:
                return Data.challenge5;
            case 6:
                return Data.challenge6;
            case 7:
                return Data.challenge7;
            case 8:
                return Data.challenge8;
            default:
                return null;
        }
    }
}
