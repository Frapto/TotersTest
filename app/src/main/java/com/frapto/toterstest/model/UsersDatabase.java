package com.frapto.toterstest.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.frapto.toterstest.model.entity.Chatroom;
import com.frapto.toterstest.model.entity.Message;
import com.frapto.toterstest.model.entity.User;
import com.frapto.toterstest.utils.Utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * access the database as a singleton
 * The context isn't usually saved as a variable, but in this case, I had to in order to save the bitmaps
 * when the database is created.
 * Another option is to save them in another class, but we risk mismatching the timing with the users and chatrooms inserts
 * Images are rarely generated inside apps in practice, they are usually acquired from a backend or camera or somewhere else.
 * */
@Database(entities = {User.class, Message.class, Chatroom.class}, version = 1, exportSchema = false)
public abstract class UsersDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    private static UsersDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    private static Context context;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    public static UsersDatabase getDatabase(final Context appContext) {
        if (INSTANCE == null) {
            synchronized (UsersDatabase.class) {
                if (INSTANCE == null) {
                    context = appContext.getApplicationContext();
                    INSTANCE = Room.databaseBuilder(appContext.getApplicationContext(),
                            UsersDatabase.class, "user_database")
                            .addCallback(rdc)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback rdc = new RoomDatabase.Callback(){
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

        }
        /**
         * called only when there is no database (first time or when storage cleared), pre-populates the db
         * due to bitmap generation and volume of data, this takes a bit of time. if the user closes (not pauses) the app before it is done,
         * the app no longer works because the database is always empty and is created (it will not re-create).
         * the solution is to either clear the storage or uninstall then re-install the app.
         * both solutions are sub-optimal, but there doesn't seem to be some sort of "database created" event
         * that we can just listen to
        */
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(()->{
                final int GENERATION_COUNT = 201;
                final int BITMAP_SIZE = 100;
                User[] Users = new User[GENERATION_COUNT];
                //200 chatrooms (cant have a chatroom with myself)
                Chatroom[] chatrooms = new Chatroom[GENERATION_COUNT-1];
                String[] bitmapPaths = new String[GENERATION_COUNT/4];
                String filePathFormatString = context.getFilesDir().getPath().toString()+"/img%d.jpg";
                for(int i=0; i< bitmapPaths.length; i++){
                    String path = String.format(filePathFormatString, i);
                    Bitmap bmp = Utilities.generateRandomBitmap(BITMAP_SIZE, BITMAP_SIZE);
                    bitmapPaths[i] = path;
                    Utilities.saveBitmap(bmp,path);
                }
                Random random = new Random();
                for(int i=0;i<GENERATION_COUNT;i++){

                    Users[i] = new User();
                    Users[i].setName("User "+i);
                    int randIndex = random.nextInt(bitmapPaths.length);
                    String bmppath = bitmapPaths[randIndex];
                    Users[i].setImage(bmppath);
                }
                long[] ids = INSTANCE.userDao().insertUsers(Users);
                for(int i=1;i<GENERATION_COUNT;i++){
                    chatrooms[i-1] = new Chatroom();
                    chatrooms[i-1].setOtherUserId(ids[i]);
                }
                INSTANCE.userDao().insertChatrooms(chatrooms);
            });
        }
    };
}
