package com.example.instagramclone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.parse.ParseFile;
import java.util.List;

public class GridAdapter extends BaseAdapter {

    public static final String TAG = "GridAdapter";
    public static Context context;
    List<ParseFile> posts;
    LayoutInflater inflater;

    public GridAdapter(Context context1, List<ParseFile> posts) {
        this.context = context1;
        this.posts = posts;
    }


    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (inflater == null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (view == null){
            view = inflater.inflate(R.layout.item_post_profile,null);
        }

        ImageView ivPostImage = view.findViewById(R.id.ivPostImage);
        ParseFile image = posts.get(i);
       // Glide.with(context).load(image.getUrl()).transform(new RoundedCorners(100)).into(ivPostImage);

        return view;
    }
}