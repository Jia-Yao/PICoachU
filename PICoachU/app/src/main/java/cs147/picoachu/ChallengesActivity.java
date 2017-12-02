package cs147.picoachu;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ChallengesActivity extends AppCompatActivity {

    private int userid = Data.currentUserId;

    BottomNavigationView navigation;
    Menu navigation_menu;
    TextView title;
    TextView symmetryText;
    ImageView symmetry;

    TextView depthText;
    ImageView depth;

    TextView brightnessText;
    ImageView brightness;

    TextView lineText;
    ImageView line;

    // Switch to other activities
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Intent i;
            switch (item.getItemId()) {
                case R.id.navigation_camera:
                    i = new Intent(ChallengesActivity.this, CameraActivity.class);
                    finish();
                    startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_explore:
                    i = new Intent(ChallengesActivity.this, ExploreActivity.class);
                    finish();
                    startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_challenges:
                    return true;
                case R.id.navigation_me:
                    i = new Intent(ChallengesActivity.this, MeActivity.class);
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

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges);

        title=(TextView)findViewById(R.id.title);
        Typeface face= Typeface.createFromAsset(getAssets(), "fonts/DIN Condensed Reg.ttf");
        title.setTypeface(face);
        title.setTextColor(Color.BLACK);

        symmetryText = (TextView)findViewById(R.id.symmetryText);
        symmetryText.setTypeface(face);
        symmetryText.setTextColor(Color.BLACK);

        symmetry = (ImageView)findViewById(R.id.symmetry);
        symmetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChallengesActivity.this, SelectTaskActivity.class);
                i.putExtra("topicid", "symmetry");
                i.putExtra("challengeIndex", 0);
                startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });

        depthText = (TextView)findViewById(R.id.depthText);
        depthText.setTypeface(face);
        depthText.setTextColor(Color.BLACK);

        depth = (ImageView)findViewById(R.id.depth);
        depth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChallengesActivity.this, SelectTaskActivity.class);
                i.putExtra("topicid", "depth");
                i.putExtra("challengeIndex", 0);
                startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });


        brightnessText = (TextView)findViewById(R.id.brightnessText);
        brightnessText.setTypeface(face);
        brightnessText.setTextColor(Color.BLACK);

        brightness = (ImageView)findViewById(R.id.brightness);
        brightness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChallengesActivity.this, SelectTaskActivity.class);
                i.putExtra("topicid", "brightness");
                i.putExtra("challengeIndex", 0);
                startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });

        lineText = (TextView)findViewById(R.id.lineText);
        lineText.setTypeface(face);
        lineText.setTextColor(Color.BLACK);

        line = (ImageView)findViewById(R.id.line);
        line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChallengesActivity.this, SelectTaskActivity.class);
                i.putExtra("topicid", "line");
                i.putExtra("challengeIndex", 0);
                startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });


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

}
