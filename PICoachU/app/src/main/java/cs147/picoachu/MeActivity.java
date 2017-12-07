package cs147.picoachu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;


public class MeActivity extends AppCompatActivity {

    private int userid = Data.currentUserId;

    BottomNavigationView navigation;
    Menu navigation_menu;
    TabHost tabHost;
    Typeface type;
    Typeface type_reg;

    // Switch to other activities
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Intent i;
            switch (item.getItemId()) {
                case R.id.navigation_camera:
                    i = new Intent(MeActivity.this, CameraActivity.class);
                    finish();
                    startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_explore:
                    i = new Intent(MeActivity.this, ExploreActivity.class);
                    finish();
                    startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_challenges:
                    i = new Intent(MeActivity.this, ChallengesActivity.class);
                    finish();
                    startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_me:
                    return true;
            }
            return false;

        }
    };

    // Create this activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);
        type = Typeface.createFromAsset(getAssets(),"fonts/DIN Condensed Bold.ttf");
        type_reg = Typeface.createFromAsset(getAssets(),"fonts/DIN Condensed Reg.ttf");

        // Set up bottom bar
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation_menu = navigation.getMenu();
        MenuItem menuItem = navigation_menu.getItem(3);
        menuItem.setChecked(true);

        // Set up profile information
        ImageView imageView = (ImageView) findViewById(R.id.profilePhotoView);
        int resID = getResources().getIdentifier(Data.getUser(userid).profilePhotoName, "drawable", "cs147.picoachu");
        imageView.setImageResource(resID);
//        imageView.setOnLongClickListener(new OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                Data.nextUser();
//                recreate();
//                return true;
//            }
//        });
        TextView userNameView = (TextView) findViewById(R.id.userNameView);
        userNameView.setText(Data.getUser(userid).userName);
        userNameView.setTypeface(type);
        TextView userLocationView = (TextView) findViewById(R.id.userLocationView);
        userLocationView.setText(Data.getUser(userid).location);
        userLocationView.setTypeface(type_reg);

        // Set up tabs
        // Set up photos display
        TableLayout table = (TableLayout)findViewById(R.id.photosView);
        TableRow tr = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(1, 1, 1, 1);
        RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        Resources resource = this.getResources();
        SquareImageView image;
        int counter = 0;
        ArrayList<Integer> photoids = Data.getUser(Data.currentUserId).photos;
        for (final Integer photoid : photoids){
            if (counter%3 == 0){
                tr = new TableRow(this);
                tr.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            RelativeLayout r = new RelativeLayout(this);
            image = new SquareImageView(this);
            Photo photo = Data.getPhoto(photoid);
            if (photo.userPhotoName.equals("")){
                String imgPath = getApplicationContext().getExternalFilesDir(null)+ Integer.toString(photo.ownerid)+'/'+photo.photoName+".jpg";
                File imgFile = new  File(imgPath);

                if(imgFile.exists()){
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    image.setImageBitmap(myBitmap);
                }
            }
            else {
                resID = getResources().getIdentifier(Data.getPhoto(photoid).userPhotoName, "drawable", "cs147.picoachu");
                image.setImageResource(resID);
            }
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MeActivity.this, PhotoDetailActivity.class);
                    Bundle extras = new Bundle();
                    extras.putInt("photoid", photoid);
                    extras.putInt("from", 3);
                    i.putExtras(extras);
                    startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                }
            });
            r.addView(image);
            ImageButton deleteButton = new ImageButton(this);
            resID = getResources().getIdentifier("delete_white_s", "drawable", "cs147.picoachu");
            deleteButton.setImageResource(resID);
            deleteButton.setMinimumHeight(0);
            deleteButton.setMinimumWidth(0);
            int px = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, resource.getDisplayMetrics());
            deleteButton.setPadding(px,px,px,px);
            deleteButton.setBackgroundColor(Color.TRANSPARENT);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Show confirmation dialog window
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MeActivity.this);
                    builder1.setMessage("Are you sure you want to remove this photo?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    User currentUser = Data.getUser(Data.currentUserId);
                                    currentUser.photos.remove(Integer.valueOf(photoid));
                                    currentUser.completedChallenges.remove(Integer.valueOf(Data.getPhoto(photoid).forChallenge));
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
            r.addView(deleteButton, rp);
            tr.addView(r, lp);
            if (counter%3 == 2 || counter == photoids.size()-1){
                table.addView(tr);
            }
            counter++;
        }

        // Set up accepted list
        ListView acceptedList = (ListView)findViewById(R.id.acceptedList);
        AcceptedListAdapter adapter1 = new AcceptedListAdapter(this,
                R.layout.row_accepted_challenge, Data.getUser(Data.currentUserId).acceptedChallenges, type);
        acceptedList.setAdapter(adapter1);

        // Set up completed list
        ListView completedList = (ListView)findViewById(R.id.completedList);
        CompletedListAdapter adapter2 = new CompletedListAdapter(this,
                R.layout.row_completed_challenge, Data.getUser(Data.currentUserId).completedChallenges, type);
        completedList.setAdapter(adapter2);

        // Set up tabs
        TabHost host = (TabHost)findViewById(R.id.tabhost);
        host.setup();
        // Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Photos");
        spec.setContent(R.id.photosTab);
        spec.setIndicator("Photos");
        host.addTab(spec);
        // Tab 2
        spec = host.newTabSpec("Accepted Challenges");
        spec.setContent(R.id.acceptedTab);
        spec.setIndicator("Accepted Challenges");
        host.addTab(spec);
        // Tab 3
        spec = host.newTabSpec("Completed Challenges");
        spec.setContent(R.id.completedTab);
        spec.setIndicator("Completed Challenges");
        host.addTab(spec);

        // Set font
        for (int i = 0; i < host.getTabWidget().getChildCount(); i++) {
            final TextView tv = (TextView) host.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTypeface(type_reg);
            tv.setTextSize(20);
            tv.setGravity(Gravity.CENTER);
        }

    }

    // When this activity is resumed
    @Override
    protected void onRestart() {
        super.onRestart();
        MenuItem menuItem = navigation_menu.getItem(3);
        menuItem.setChecked(true);
    }

    // Remove transition animation
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

}

class AcceptedListAdapter extends ArrayAdapter<Integer> {

    private final Context context;
    private final int resourceID;
    private final ArrayList<Integer> values;
    private Typeface type;

    public AcceptedListAdapter(Context context, int resource, ArrayList<Integer> values, Typeface _type) {
        super(context, resource, values);
        this.context = context;
        this.resourceID = resource;
        this.values = values;
        this.type = _type;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final Integer challengeid = values.get(position);

        View rowView = inflater.inflate(resourceID, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.challengeTitleView);
        textView.setText(Data.getChallenge(challengeid).title);
        textView.setTypeface(type);
        textView.setTextSize(21);
        textView.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, challengeDetail.class);
                i.putExtra("challengeid", challengeid);
                context.startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });

        Button removeButton = (Button) rowView.findViewById(R.id.removeButton);
        removeButton.setTypeface(type);
        removeButton.setTextSize(18);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show confirmation dialog window
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Are you sure you want to decline \"" + Data.getChallenge(challengeid).title + "\" challenge?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                User currentUser = Data.getUser(Data.currentUserId);
                                currentUser.acceptedChallenges.remove(Integer.valueOf(challengeid));
                                notifyDataSetChanged();
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

        return rowView;

    }
}

class CompletedListAdapter extends ArrayAdapter<Integer> {

    private final Context context;
    private final int resourceID;
    private final ArrayList<Integer> values;
    private Typeface type;

    public CompletedListAdapter(Context context, int resource, ArrayList<Integer> values, Typeface _type) {
        super(context, resource, values);
        this.context = context;
        this.resourceID = resource;
        this.values = values;
        this.type = _type;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final Integer challengeid = values.get(position);

        View rowView = inflater.inflate(resourceID, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.challengeTitleView);
        textView.setText(Data.getChallenge(challengeid).title);
        textView.setTypeface(type);
        textView.setTextSize(21);
        textView.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, challengeDetail.class);
                i.putExtra("challengeid", challengeid);
                context.startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });

        Resources r = getContext().getResources();
        for (final Integer photoid : Data.getUser(Data.currentUserId).photos){
            Photo photo = Data.getPhoto(photoid);
            if (photo.forChallenge == challengeid){
                LinearLayout row = (LinearLayout) rowView.findViewById(R.id.row);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.16f);
                int px = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, r.getDisplayMetrics());
                lp.setMargins(0, px, 0, px);
                SquareImageView image = new SquareImageView(context);
                if (photo.userPhotoName.equals("")){
                    String imgPath = getContext().getExternalFilesDir(null)+ Integer.toString(photo.ownerid)+'/'+photo.photoName+".jpg";
                    File imgFile = new  File(imgPath);

                    if(imgFile.exists()){
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        image.setImageBitmap(myBitmap);
                    }
                }
                else{
                    if (photo.userPhotoName.equals("")){
                        String imgPath = getContext().getExternalFilesDir(null)+ Integer.toString(photo.ownerid)+'/'+photo.photoName+".jpg";
                        File imgFile = new  File(imgPath);

                        if(imgFile.exists()){
                            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                            image.setImageBitmap(myBitmap);
                        }
                    }
                    else {
                        int resID = context.getResources().getIdentifier(Data.getPhoto(photoid).userPhotoName, "drawable", "cs147.picoachu");
                        image.setImageResource(resID);
                    }
                }

                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, PhotoDetailActivity.class);
                        Bundle extras = new Bundle();
                        extras.putInt("photoid", photoid);
                        extras.putInt("from", 3);
                        i.putExtras(extras);
                        context.startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    }
                });
                row.addView(image, lp);
            }
        }

        return rowView;

    }
}
