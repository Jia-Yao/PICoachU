package cs147.picoachu;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.ContactsContract;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SelectTaskActivity extends AppCompatActivity {


    private int userid = Data.currentUserId;
    private User currUser = Data.getUser(userid);

    BottomNavigationView navigation;
    Menu navigation_menu;

    TextView task;
    TextView topic;
    TextView topicDescription;
    TextView progress;
    TextView taskDescription;
    TextView empty;

    ImageView sample;
    ImageView leftarrow;
    ImageView rightarrow;

    ImageButton backButton;

    Button moreButton;

    // Switch to other activities
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Intent i;
            switch (item.getItemId()) {
                case R.id.navigation_camera:
                    i = new Intent(SelectTaskActivity.this, CameraActivity.class);
                    startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_explore:
                    i = new Intent(SelectTaskActivity.this, ExploreActivity.class);
                    startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_challenges:
                    i = new Intent(SelectTaskActivity.this, ChallengesActivity.class);
                    startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_me:
                    i = new Intent(SelectTaskActivity.this, MeActivity.class);
                    startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
            }
            return false;

        }
    };

    // Create this activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_task);
        String tempid = "";
        int index = 0;

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            tempid = extras.getString("topicid");
            index = extras.getInt("challengeIndex");
        }

        final String topicid = tempid;
        String topicString = getTopic(topicid);
        String description = getDescription(topicid);

        Typeface boldFace = Typeface.createFromAsset(getAssets(), "fonts/DIN Condensed Bold.ttf");
        Typeface regFace = Typeface.createFromAsset(getAssets(), "fonts/DIN Condensed Reg.ttf");

        topic = (TextView)findViewById(R.id.topic);
        topic.setText((CharSequence)topicString);
        topic.setTypeface(boldFace);

        topicDescription = (TextView)findViewById(R.id.topicdescription);
        topicDescription.setText((CharSequence)description);
        topicDescription.setTypeface(boldFace, Typeface.ITALIC);

        ArrayList<Challenge> list = getList(topicid);
        ArrayList<Challenge> activeChallenges = new ArrayList<Challenge>();
        for(Challenge c : list) {
            if(!( currUser.acceptedChallenges.contains(c.challengeid) ||
                  currUser.completedChallenges.contains(c.challengeid))) {
                activeChallenges.add(c);
            }
        }

        progress = (TextView)findViewById(R.id.progress);
        String progressString = (list.size()-activeChallenges.size())+"/"+list.size();
        progress.setText((CharSequence)progressString);
        progress.setTypeface(boldFace);

        if(activeChallenges.size() == 0) {
            empty = (TextView) findViewById(R.id.empty);
            empty.setText("All challenges under this topic are either in process or completed.");
            empty.setTypeface(boldFace);
            empty.setTextColor(Color.BLACK);
            task = (TextView) findViewById(R.id.taskText);
            moreButton = (Button) findViewById(R.id.moreButton);
            leftarrow = (ImageView) findViewById(R.id.leftarrow);
            rightarrow = (ImageView) findViewById(R.id.rightarrow);
            taskDescription = (TextView)findViewById(R.id.taskdescription);
            sample = (ImageView)findViewById(R.id.sample);
            ViewGroup layout = (ViewGroup) moreButton.getParent();
            if(null!=layout) {//for safety only  as you are doing onClick
                layout.removeView(task);
                layout.removeView(moreButton);
                layout.removeView(leftarrow);
                layout.removeView(rightarrow);
                layout.removeView(taskDescription);
                layout.removeView(sample);
            }
        }
        else {
            final Challenge currChallenge = activeChallenges.get(index);

            final int leftIndex = (index > 0) ? (index - 1) : (activeChallenges.size() - 1);
            final int rightIndex = (index < activeChallenges.size() - 1) ? (index + 1) : (0);

            task = (TextView) findViewById(R.id.taskText);
            task.setText((CharSequence) currChallenge.title);
            task.setTypeface(boldFace);
            task.setTextColor(Color.BLACK);

            taskDescription = (TextView)findViewById(R.id.taskdescription);
            taskDescription.setText((CharSequence)currChallenge.shortDescription);
            taskDescription.setTypeface(regFace);
            taskDescription.setTextColor(Color.BLACK);

            sample = (ImageView)findViewById(R.id.sample);
            int resID = getResources().getIdentifier(currChallenge.examplePhotoName, "drawable", "cs147.picoachu");
            sample.setImageResource(resID);

            moreButton = (Button) findViewById(R.id.moreButton);
            moreButton.setTypeface(boldFace);

            leftarrow = (ImageView) findViewById(R.id.leftarrow);
            rightarrow = (ImageView) findViewById(R.id.rightarrow);

            if(activeChallenges.size() == 1) {
                ViewGroup layout = (ViewGroup) leftarrow.getParent();
                if(layout != null) {
                    layout.removeView(leftarrow);
                    layout.removeView(rightarrow);
                }
            }

            else {
                leftarrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(SelectTaskActivity.this, SelectTaskActivity.class);
                        i.putExtra("topicid", topicid);
                        i.putExtra("challengeIndex", leftIndex);
                        finish();
                        startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    }
                });

                rightarrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(SelectTaskActivity.this, SelectTaskActivity.class);
                        i.putExtra("topicid", topicid);
                        i.putExtra("challengeIndex", rightIndex);
                        finish();
                        startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    }
                });
            }
            moreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(SelectTaskActivity.this, challengeDetail.class);
                    i.putExtra("challengeid", currChallenge.challengeid);
                    startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                }
            });
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

    private ArrayList<Challenge> getList(String topicid) {
        switch (topicid) {
            case "symmetry":
                return Data.symmetryChallenges;

            case "depth":
                return Data.depthChallenges;

            case "brightness":
                return Data.brightnessChallenges;

            case "line":
                return Data.lineChallenges;

            default:
                return new ArrayList<Challenge>();
        }
    }

    private String getTopic(String topicid) {
        switch (topicid) {
            case "symmetry":
                return "Symmetry";

            case "depth":
                return "Depth";

            case "brightness":
                return "Brightness";

            case "line":
                return "Line";

            default:
                return "";
        }
    }

    private String getDescription(String topicid) {
        switch (topicid) {
            case "symmetry":
                return "All about balance.";

            case "depth":
                return "Depth and beauty.";

            case "brightness":
                return "Brilliance of colour.";

            case "line":
                return "Skeleton of the photo.";

            default:
                return "";
        }
    }

}

