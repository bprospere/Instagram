package com.example.instagramclone;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String TAG = "DetailActivity";
    protected ImageView ivProfileImage1, ivPost1;
    TextView tvUserName1, tvDescription1, tvDate1, tvLike;
    ImageButton ic_more1, ic_heart, ic_chat1, ic_send1, ic_bookmark1;
    RecyclerView rvComment;
    protected List<String> commentsParse;
    protected List<Comment> comments;
    protected CommentAdapter adapter;
    Context context;
    public static int like;
    public static ArrayList<String> listUserLike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ParseUser currentUser = ParseUser.getCurrentUser();



        ivProfileImage1 = findViewById(R.id.ivProfileImage1);
        ivPost1 = findViewById(R.id.ivPost1);
        tvUserName1 = findViewById(R.id.tvUserName1);
        tvDate1 = findViewById(R.id.tvDate1);
        tvLike = findViewById(R.id.tvLike);
        tvDescription1 = findViewById(R.id.tvDescription1);
        ic_more1 = findViewById(R.id.ic_more1);
        ic_heart = findViewById(R.id.ic_heart);
        ic_chat1 = findViewById(R.id.ic_chat1);
        ic_send1 = findViewById(R.id.ic_send1);
        ic_bookmark1 = findViewById(R.id.ic_bookmark1);
        rvComment = findViewById(R.id.rvComment);

        Post post = Parcels.unwrap(getIntent().getParcelableExtra(MainActivity.POST));
        post.getListComment();
        try {
            commentsParse = Comment.fromJsonArray(post.getListComment());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            listUserLike = Post.fromJsonArray(post.getListLike());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try{
            if (listUserLike.contains(currentUser.getObjectId())) {
                Drawable drawable = ContextCompat.getDrawable(DetailActivity.this, R.drawable.heartcolor);
                ic_heart.setImageDrawable(drawable);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        comments = new ArrayList<>();
        adapter = new CommentAdapter(context, comments);
        rvComment.setAdapter(adapter);
        rvComment.setLayoutManager(new LinearLayoutManager(context));
        tvUserName1.setText(post.getUser().getUsername());
        tvDescription1.setText(post.getDescription());
        tvLike.setText(String.valueOf(post.getNumberLike()) + " likes");
        tvDate1.setText(TimeFormatter.getTimeStamp(post.getCreatedAt().toString()));
//        Glide.with(DetailActivity.this).load(post.getUser().getParseFile(User.KEY_PROFILE_IMAGE).getUrl()).transform(new RoundedCorners(100)).into(ivProfileImage1);

        ParseFile image = post.getImage();
        if(image != null){
            Glide.with(DetailActivity.this).load(image.getUrl()).transform(new RoundedCorners(30)).into(ivPost1);
        }


        ic_chat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetailActivity.this, CommentActivity.class);
                i.putExtra("post", Parcels.wrap(post));
                startActivity(i);
            }
        });

        ic_heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                like = post.getNumberLike();
                int index;

                if (!listUserLike.contains(currentUser.getObjectId())){
                    Drawable drawable = ContextCompat.getDrawable(DetailActivity.this, R.drawable.heartcolor);
                    ic_heart.setImageDrawable(drawable);
                    like++;
                    index = -1;

                }else {
                    Drawable drawable = ContextCompat.getDrawable(DetailActivity.this, R.drawable.ic_heart);
                    ic_heart.setImageDrawable(drawable);
                    like--;
                    index = listUserLike.indexOf(currentUser.getObjectId());
                }

                tvLike.setText(String.valueOf(like) + " likes");
                saveLike(post, like, index, currentUser);
            }
        });

        queryPost();
    }

    protected void queryPost() {
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
        query.include(Comment.KEY_USER);
        query.whereContainedIn("objectId", commentsParse);
        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> commentList, ParseException e) {
                if (e != null){
                    Log.e(TAG, "Issue with getting Posts", e);
                    Toast.makeText(context, "Issue with getting Posts", Toast.LENGTH_SHORT).show();
                    return;
                }

                comments.addAll(commentList);
                adapter.notifyDataSetChanged();

            }
        });
    }


    private void saveLike(Post post, int like, int index, ParseUser currentUser) {
        post.setNumberLike(like);

        if (index == -1){
            post.setListLike(currentUser);
            listUserLike.add(currentUser.getObjectId());
        }else {
            listUserLike.remove(index);
            post.removeItemListLike(listUserLike);
        }

        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(context, "Error while saving", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.i(TAG, listUserLike.toString());
            }
        });
    }

}