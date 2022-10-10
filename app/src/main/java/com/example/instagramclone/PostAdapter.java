package com.example.instagramclone;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import org.json.JSONException;
import org.parceler.Parcels;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    public static final String TAG = "PostAdapter";
    public static Context context;
    List<Post> posts;
    private static ArrayList<String> listLikes;

    public PostAdapter(Context context1, List<Post> posts) {
        this.context = context1;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        try {
            holder.bind(post);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();

    }


    public void clear(){
        posts.clear();
        notifyDataSetChanged();
    }


    public void addAll(List<Post> postList){
        posts.addAll(postList);
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        protected ImageView imageProfile1, imagePost;
        TextView tvUsername, tvDescription, tvDate, tvLike;
        ImageButton ic_more, ic__heart_color, ic_chat, ic_send, ic_bookmark;
        RelativeLayout container1, container;
        public static int like;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProfile1 = itemView.findViewById(R.id.imageProfile1);
            imagePost = itemView.findViewById(R.id.imagePost);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvLike = itemView.findViewById(R.id.tvLike);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ic_more = itemView.findViewById(R.id.ic_more);
            ic__heart_color = itemView.findViewById(R.id.ic__heart_color);
            ic_chat = itemView.findViewById(R.id.ic_chat);
            ic_send = itemView.findViewById(R.id.ic_send);
            ic_bookmark = itemView.findViewById(R.id.ic_bookmark);
            container1 = itemView.findViewById(R.id.container1);
            container = itemView.findViewById(R.id.container);
        }

        public void bind(Post post) throws JSONException {
            ParseUser currentUser = ParseUser.getCurrentUser();
            listLikes = Post.fromJsonArray(post.getListLike());

                Glide.with(context).load(post.getUser().getParseFile(User.KEY_PROFILE_IMAGE).getUrl()).transform(new RoundedCorners(100)).into(imageProfile1);

            tvUsername.setText(post.getUser().getUsername());
            tvDescription.setText(post.getDescription());
            tvDate.setText(TimeFormatter.getTimeStamp(post.getCreatedAt().toString()));


            ParseFile image = post.getImage();
            if(image != null){
                Glide.with(context).load(image.getUrl()).into(imagePost);
            }

            try{
                if (listLikes.contains(currentUser.getObjectId())) {
                    Drawable drawable = ContextCompat.getDrawable(context, R.drawable.heartcolor);
                    ic__heart_color.setImageDrawable(drawable);
                }else {
                    Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_heart);
                    ic__heart_color.setImageDrawable(drawable);
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }

            ic__heart_color.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    like = post.getNumberLike();
                    int index;

                    if (!listLikes.contains(currentUser.getObjectId())){
                        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.heartcolor);
                        ic__heart_color.setImageDrawable(drawable);
                        like++;
                        index = -1;

                    }else {
                        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_heart);
                        ic__heart_color.setImageDrawable(drawable);
                        like--;
                        index = listLikes.indexOf(currentUser.getObjectId());
                    }

                    tvLike.setText(String.valueOf(like) + " likes");
                    saveLike(post, like, index, currentUser);
                }
            });


            container1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, DetailActivity.class);
                    i.putExtra("post", Parcels.wrap(post));
                    context.startActivity(i);
                }
            });

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();


                    ProfileFragment profileFragment = ProfileFragment.newInstance("");
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(MainActivity.POST, Parcels.wrap(post));
                    profileFragment.setArguments(bundle);

                    fragmentManager.beginTransaction().replace(R.id.frame, profileFragment).commit();
                }
            });

            ic_chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, CommentActivity.class);
                    i.putExtra("post", Parcels.wrap(post));
                    context.startActivity(i);
                }
            });
        }


        private void saveLike(Post post, int like, int index, ParseUser currentUser) {
            post.setNumberLike(like);

            if (index == -1){
                post.setListLike(currentUser);
                listLikes.add(currentUser.getObjectId());
            }else {
                listLikes.remove(index);
                post.removeItemListLike(listLikes);
            }

            post.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null){
                        Log.e(TAG, "Error while saving", e);
                        Toast.makeText(context, "Error while saving", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Log.i(TAG, listLikes.toString());

                }
            });
        }


    }




}

