package malkia.malkiaunesco.example.com.malkia.fragment;

/**
 * Created by Tabitha on 12/13/2017.
 */


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import malkia.malkiaunesco.example.com.malkia.R;
import malkia.malkiaunesco.example.com.malkia.activitys.MainActivity;
import malkia.malkiaunesco.example.com.malkia.models.UserAccountSettings;
import malkia.malkiaunesco.example.com.malkia.models.UserSettings;
import malkia.malkiaunesco.example.com.malkia.util.FirebaseMethods;



public class ProfileFragment extends android.support.v4.app.Fragment
        implements AppBarLayout.OnOffsetChangedListener {
    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean mIsAvatarShown = true;
    private String TAG = MainActivity.class.getSimpleName();
    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;

    private TextView mPoints, mUsername;
    private ImageView mBackgroundPhoto;
    private Toolbar toolbar;
    private ImageView profileMenu;
    private ImageView profileSettings;
    private Button file;
    private Context mContext;

    private ImageView mProfileImage;
    private int mMaxScrollSize;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RequestManager mRequestManager= Glide.with(this);



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.materialup_tabs);
        AppBarLayout appbarLayout = (AppBarLayout) view.findViewById(R.id.materialup_appbar);

        ViewPager viewPager  = (ViewPager) view.findViewById(R.id.materialup_viewpager);
        mProfileImage = (ImageView) view.findViewById(R.id.materialup_profile_image);
        mBackgroundPhoto = (ImageView) view.findViewById(R.id.background_profile);
        mPoints = (TextView) view.findViewById(R.id.buntu);
        mUsername = (TextView) view.findViewById(R.id.username);
        profileSettings = (ImageView) view.findViewById(R.id.account_settings);


        appbarLayout.addOnOffsetChangedListener(this);
        mMaxScrollSize = appbarLayout.getTotalScrollRange();
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        mContext = getActivity();
        mFirebaseMethods = new FirebaseMethods(getActivity());
        //Log.d(TAG, "onCreateView: stared.");


        setupFirebaseAuth();
        return view;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int percentage = (Math.abs(i)) * 100 / mMaxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;

            mProfileImage.animate()
                    .scaleY(0).scaleX(0)
                    .setDuration(200)
                    .start();
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;

            mProfileImage.animate()
                    .scaleY(1).scaleX(1)
                    .start();
        }
    }
    private void setupViewPager(ViewPager viewPager) {
        ProfileFragment.ViewPagerAdapter adapter = new ProfileFragment.ViewPagerAdapter(getChildFragmentManager());


        adapter.addFragment(new PostFragment(), "Settings");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    private void setProfileWidgets(UserSettings userSettings){

        UserAccountSettings settings = userSettings.getSettings();


        mUsername.setText(settings.getUsername());
        mPoints.setText(String.valueOf(settings.getPoints()));


    }




    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();


                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //retrieve user information from the database
                setProfileWidgets(mFirebaseMethods.getUserSettings(dataSnapshot));

                //retrieve images for the user in question

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}

