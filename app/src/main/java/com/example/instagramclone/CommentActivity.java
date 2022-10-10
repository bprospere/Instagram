package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.Objects;


public class CommentActivity extends AppCompatActivity {

    public static final String TAG = "CommentActivity";
    private TextView tvUserName;
    private ImageView imageProile2;
    private EditText etComment;
    private Button btnComment;
    Context context;
    Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Toolbar toolbar = findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);


        tvUserName = findViewById(R.id.tvUserName);
        imageProile2 = findViewById(R.id.imageProile2);
        etComment = findViewById(R.id.etComment);
        btnComment = findViewById(R.id.btnComment);

        post = Parcels.unwrap(getIntent().getParcelableExtra(MainActivity.POST));
        ParseUser currentUser = ParseUser.getCurrentUser();
        tvUserName.setText(currentUser.getUsername());
        Glide.with(CommentActivity.this).load((Objects.requireNonNull(currentUser.getParseFile(User.KEY_PROFILE_IMAGE))).getUrl()).into(imageProile2);



        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = etComment.getText().toString();
                if (comment.isEmpty()){
                    Toast.makeText(context, "Comment cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                ParseUser currentUser = ParseUser.getCurrentUser();
                SaveComment(comment, currentUser);
            }
        });
    }

    private void SaveComment(String description, ParseUser currentUser) {
        ParseObject object = ParseObject.create("Comment");
        object.put("user", currentUser);
        object.put("comment", description);
        post.setListComment(object);

        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(context, "Error while saving", Toast.LENGTH_SHORT).show();
                    return;
                }

                etComment.setText("");
            }
        });
    }
}