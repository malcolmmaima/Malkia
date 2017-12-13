package malkia.malkiaunesco.example.com.malkia.app;

/**
 * Created by Tabitha on 12/12/2017.
 */

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by divyanshu on 25-08-2016.
 */
public class FirebaseApp extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}