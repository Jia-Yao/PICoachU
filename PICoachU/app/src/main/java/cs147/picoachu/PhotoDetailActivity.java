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
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PhotoDetailActivity extends AppCompatActivity {

    private int userid = Data.currentUserId;
    private int photoid;
    private int from;

    BottomNavigationView navigation;
    Menu navigation_menu;
    RelativeLayout userPhotoFrame;
    EditText commentInput;
    ImageButton sendButton;
    ListView commentsList;
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
                    i = new Intent(PhotoDetailActivity.this, CameraActivity.class);
                    startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_explore:
                    i = new Intent(PhotoDetailActivity.this, ExploreActivity.class);
                    startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_challenges:
                    i = new Intent(PhotoDetailActivity.this, ChallengesActivity.class);
                    startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_me:
                    i = new Intent(PhotoDetailActivity.this, MeActivity.class);
                    startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
            }
            return false;

        }
    };

    // Remove transition animation
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    // Create this activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        type = Typeface.createFromAsset(getAssets(),"fonts/DIN Condensed Bold.ttf");
        type_reg = Typeface.createFromAsset(getAssets(),"fonts/DIN Condensed Reg.ttf");
        final Resources r = this.getResources();

        // Get the arguments passed to this activity
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            photoid = extras.getInt("photoid");
            from = extras.getInt("from");
        }

        // Set up bottom bar
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation_menu = navigation.getMenu();
        MenuItem menuItem = navigation_menu.getItem(from);
        menuItem.setChecked(true);

        userPhotoFrame = (RelativeLayout) findViewById(R.id.userPhotoFrame);
        final ImageView imageView = (ImageView) findViewById(R.id.userPhotoView);
        Photo photo = Data.getPhoto(photoid);
            if (photo.userPhotoName.equals("")){
                String imgPath = getApplicationContext().getExternalFilesDir(null)+ Integer.toString(photo.ownerid)+'/'+photo.photoName+".jpg";
                File imgFile = new  File(imgPath);

                if(imgFile.exists()){
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    imageView.setImageBitmap(myBitmap);
                }
            }
            else {
            int resID = getResources().getIdentifier(Data.getPhoto(photoid).userPhotoName, "drawable", "cs147.picoachu");
            imageView.setImageResource(resID);
        }
        imageView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                commentInput.clearFocus();
                int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, r.getDisplayMetrics());
                final int imageX = (int) event.getX()-px/2;
                final int imageY = (int) event.getY()-px;
                AlertDialog.Builder builder = new AlertDialog.Builder(PhotoDetailActivity.this);
                TextView title = new TextView(PhotoDetailActivity.this);
                title.setText("Popup Comment");
                title.setTypeface(type);
                title.setPadding(0, 30, 0, 10);
                title.setGravity(Gravity.CENTER);
                title.setTextSize(22);
                builder.setCustomTitle(title);
                // Set up the input
                final EditText input = new EditText(PhotoDetailActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setTypeface(type_reg);
                input.setTextSize(20);
                input.setHint("Add a comment on where was clicked");
                builder.setView(input);
                builder.setCancelable(true);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayList<String> comment = new ArrayList<String>();
                        comment.add(Data.getUser(Data.currentUserId).userName + ": " + input.getText().toString());
                        comment.add(String.valueOf(imageX));
                        comment.add(String.valueOf(imageY));
                        Data.getPhoto(photoid).comments.add(comment);
                        ImageButton commentPop = new ImageButton(PhotoDetailActivity.this);
                        commentPop.setImageResource(R.drawable.person_pin_white);
                        commentPop.setBackgroundColor(Color.TRANSPARENT);
                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                        lp.setMargins(imageX, imageY, 0, 0);
                        System.out.println("Comment pop up at " + imageX + " " + imageY);
                        userPhotoFrame.addView(commentPop, lp);
                        dialog.cancel();
                        // Hide keyboard
                        InputMethodManager inputManager = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                        // Scroll list view to bottom
                        commentsList.post(new Runnable() {
                            @Override
                            public void run() {
                                commentsList.setSelection(commentsList.getCount() - 1);
                            }
                        });
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
                return false;
            }
        });
        for (ArrayList<String> comment : Data.getPhoto(photoid).comments){
            if (comment.size() == 3){
                ImageView commentPop = new ImageView(this);
                commentPop.setImageResource(R.drawable.person_pin_white);
                commentPop.setBackgroundColor(Color.TRANSPARENT);
                int imageX = Integer.parseInt(comment.get(1));
                int imageY = Integer.parseInt(comment.get(2));
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                lp.setMargins(imageX, imageY, 0, 0);
                commentPop.setVisibility(View.INVISIBLE);
                userPhotoFrame.addView(commentPop, lp);
            }
        }


        TextView ownerNameView = (TextView) findViewById(R.id.ownerNameView);
        ownerNameView.setText("Published by " + Data.getUser(Data.getPhoto(photoid).ownerid).userName);
        ownerNameView.setTypeface(type_reg);
        ownerNameView.setTextSize(18);
        TextView textView = (TextView) findViewById(R.id.tags);
        textView.setTypeface(type_reg);
        textView.setTextSize(18);
        textView = (TextView) findViewById(R.id.comments);
        textView.setTypeface(type_reg);
        textView.setTextSize(18);

        LinearLayout tagsView = (LinearLayout) findViewById(R.id.tagsView);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(4, 0, 4, 0);
        for (String tag : Data.getPhoto(photoid).tags){
            TextView tagView = new TextView(this);
            tagView.setText(tag);
            tagView.setTypeface(type_reg);
            tagView.setTextSize(18);
            tagView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            tagView.setBackgroundResource(R.drawable.tag_rounded_corners);
            tagsView.addView(tagView, lp);
        }

        commentsList = (ListView) findViewById(R.id.commentsList);
        CommentsListAdapter adapter = new CommentsListAdapter(this,
                R.layout.row_comment, Data.getPhoto(photoid).comments, photoid, userPhotoFrame, type);
        commentsList.setAdapter(adapter);

        sendButton = (ImageButton) findViewById(R.id.sendButton);
        sendButton.setEnabled(false);
        commentInput = (EditText) findViewById(R.id.commentInput);
        commentInput.setTypeface(type_reg);
        commentInput.setTextSize(20);
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
                ArrayList<String> comment = new ArrayList<String>();
                comment.add(Data.getUser(Data.currentUserId).userName + ": " + commentInput.getText().toString());
                Data.getPhoto(photoid).comments.add(comment);
                commentInput.setText("");
                // Hide keyboard
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                // Scroll list view to bottom
                commentsList.post(new Runnable() {
                    @Override
                    public void run() {
                        commentsList.setSelection(commentsList.getCount() - 1);
                    }
                });
            }
        });
    }

    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }
}

class CommentsListAdapter extends ArrayAdapter<ArrayList<String>> {

    private final Context context;
    private final int resourceID;
    private final ArrayList<ArrayList<String>> values;
    private RelativeLayout userPhotoFrame;
    private Typeface type;
    private int photoid;

    public CommentsListAdapter(Context context, int resource, ArrayList<ArrayList<String>> values, int _photoid, RelativeLayout _userPhotoFrame, Typeface _type) {
        super(context, resource, values);
        this.context = context;
        this.resourceID = resource;
        this.values = values;
        this.photoid = _photoid;
        this.userPhotoFrame = _userPhotoFrame;
        this.type = _type;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ArrayList<String> comment = values.get(position);

        View rowView = inflater.inflate(resourceID, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.commentText);
        textView.setText(comment.get(0));
        textView.setTypeface(type);
        textView.setTextSize(22);
        textView.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        final Switch toggle = (Switch) rowView.findViewById(R.id.toggle);
        if (comment.size() == 3){
            int counter = 0;
            for (int i = 0; i < position; i++){
                if (values.get(i).size() == 3){
                    counter++;
                }
            }
            final int positionInSpecialComments = counter;
            if (userPhotoFrame.getChildAt(positionInSpecialComments+1).getVisibility() == View.VISIBLE){
                toggle.setChecked(true);
            }
            toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        userPhotoFrame.getChildAt(positionInSpecialComments+1).setVisibility(View.VISIBLE);
                    } else {
                        userPhotoFrame.getChildAt(positionInSpecialComments+1).setVisibility(View.INVISIBLE);
                    }
                }
            });
            if (comment.get(0).indexOf(Data.getUser(Data.currentUserId).userName) == 0){
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Show confirmation dialog window
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                        builder1.setMessage("Do you want to remove your comment?");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Data.getPhoto(photoid).comments.remove(position);
                                        ImageView i = (ImageView)userPhotoFrame.getChildAt(positionInSpecialComments+1);
                                        userPhotoFrame.removeView(i);
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
            }
        } else {
            toggle.setVisibility(View.GONE);
            if (comment.get(0).indexOf(Data.getUser(Data.currentUserId).userName) == 0){
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Show confirmation dialog window
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                        builder1.setMessage("Do you want to remove your comment?");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Data.getPhoto(photoid).comments.remove(position);
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
            }
        }
        return rowView;

    }
}
