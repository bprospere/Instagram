package com.example.instagramclone;


import android.app.Application;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class ParseApplication extends Application {

    public static final String TAG = "ParseApplication";
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Comment.class);
        ParseUser.registerSubclass(User.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build());
    }
}




//package com.example.instagramclone;
//
//import android.app.Application;
//
//import com.parse.Parse;
//import com.parse.ParseObject;
//import com.parse.ParseUser;
//
//import okhttp3.OkHttpClient;
//import okhttp3.logging.HttpLoggingInterceptor;
//
//public class ParseApplication extends Application {
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
//
//        // Use for monitoring Parse OkHttp traffic
//        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
//        // See https://square.github.io/okhttp/3.x/logging-interceptor/ to see the options.
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
//        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        builder.networkInterceptors().add(httpLoggingInterceptor);
//
//        ParseObject.registerSubclass(Post.class);
//        ParseObject.registerSubclass(User.class);
//        ParseObject.registerSubclass(Comment.class);
//
//
//        Parse.initialize(new Parse.Configuration.Builder(this)
//                .applicationId("5IJWi0JjtRmQtYcpmLSDxBJutTxFx44mJiPtXsk3")
//                .clientKey("EgzCyj9iX3eYR96md86Si1fZV28u3OGaAERmbfCX")
//                .server("https://parseapi.back4app.com")
//                .build()
//        );
//
//    }
//}
