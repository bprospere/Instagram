package com.example.instagramclone;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("5IJWi0JjtRmQtYcpmLSDxBJutTxFx44mJiPtXsk3")
                .clientKey("EgzCyj9iX3eYR96md86Si1fZV28u3OGaAERmbfCX")
                .server("https://parseapi.back4app.com")
                .build()
        );

    }
}
