package cs147.picoachu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.io.File;

public class ExploreActivity extends AppCompatActivity {

    private int userid = Data.currentUserId;

    BottomNavigationView navigation;
    Menu navigation_menu;
    EditText commentInput;
    ImageButton sendButton;

    // Switch to other activities
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Intent i;
            switch (item.getItemId()) {
                case R.id.navigation_camera:
                    i = new Intent(ExploreActivity.this, CameraActivity.class);
                    finish();
                    startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_explore:
                    return true;
                case R.id.navigation_challenges:
                    i = new Intent(ExploreActivity.this, ChallengesActivity.class);
                    finish();
                    startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_me:
                    i = new Intent(ExploreActivity.this, MeActivity.class);
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
        setContentView(R.layout.activity_explore);

        // Set up bottom bar
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation_menu = navigation.getMenu();
        MenuItem menuItem = navigation_menu.getItem(1);
        menuItem.setChecked(true);




        sendButton = (ImageButton) findViewById(R.id.sendButton);
        sendButton.setEnabled(false);
        commentInput = (EditText) findViewById(R.id.commentInput);
        commentInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(s)){
                    sendButton.setEnabled(false);
                } else {
                    sendButton.setEnabled(true);
                }
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Set up photo display
                TableLayout table = (TableLayout)findViewById(R.id.photosView);
                table.removeAllViews();
                TableRow tr = new TableRow(ExploreActivity.this);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.setMargins(1, 1, 1, 1);
                SquareImageView image;
                int counter = 0, resID;
                //ArrayList<Integer> photoids = Data.getUser(Data.currentUserId).photos;
                for (int i =1; i<= 10; i++){
                    if (counter%3 == 0){
                        tr = new TableRow(ExploreActivity.this);
                        tr.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    }
                    final Integer photoid = i;
                    image = new SquareImageView(ExploreActivity.this);

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
                    boolean check = false;
                    for (String s: Data.getPhoto(photoid).tags) {
                        //System.out.println(s + " " + commentInput.getText().toString());
                        if (s.toLowerCase().equals(commentInput.getText().toString().toLowerCase())){
                            //System.out.println(s + "!!!!!!!!!");
                            check = true;
                        }
                    }
                    if (check == false){

                        continue;
                    }
                    System.out.println(Data.getPhoto(photoid).tags);
                    image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(ExploreActivity.this, PhotoDetailActivity.class);
                            Bundle extras = new Bundle();
                            extras.putInt("photoid", photoid);
                            extras.putInt("from", 1);
                            i.putExtras(extras);
                            ExploreActivity.this.startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        }
                    });
                    tr.addView(image, lp);
                    if (tr.getChildCount() == 3 || counter == 10-1){
                        table.addView(tr);
                    }
                    counter++;
                }






                //    ArrayList<String> comment = new ArrayList<String>();
                //    comment.add(Data.getUser(Data.currentUserId).userName + ": " + commentInput.getText().toString());
                //  Data.getPhoto(photoid).comments.add(comment);
                //    commentInput.setText("");
                // Hide keyboard
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);




                // Scroll list view to bottom
                // commentsList.post(new Runnable() {
                //  @Override
                //  public void run() {
                //         commentsList.setSelection(commentsList.getCount() - 1);
                //     }
                //});
            }
        });




        // Set up photo display
        TableLayout table = (TableLayout)findViewById(R.id.photosView);
        TableRow tr = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(1, 1, 1, 1);
        SquareImageView image;
        int counter = 0, resID;
        //ArrayList<Integer> photoids = Data.getUser(Data.currentUserId).photos;
        for (int i =1; i<= 10; i++){
            if (counter%3 == 0){
                tr = new TableRow(this);
                tr.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            final Integer photoid = i;
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
                    Intent i = new Intent(ExploreActivity.this, PhotoDetailActivity.class);
                    Bundle extras = new Bundle();
                    extras.putInt("photoid", photoid);
                    extras.putInt("from", 1);
                    i.putExtras(extras);
                    ExploreActivity.this.startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                }
            });
            tr.addView(image, lp);
            if (counter%3 == 2 || counter == 10-1){
                table.addView(tr);
            }
            counter++;
        }

    }

    // When this activity is resumed
    @Override
    protected void onRestart() {
        super.onRestart();
        MenuItem menuItem = navigation_menu.getItem(1);
        menuItem.setChecked(true);
    }

    // Remove transition animation
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

}
