package malkia.malkiaunesco.example.com.malkia.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import malkia.malkiaunesco.example.com.malkia.R;
import malkia.malkiaunesco.example.com.malkia.cardstream.CardStreamFragment;
import malkia.malkiaunesco.example.com.malkia.cardstream.CardStreamState;
import malkia.malkiaunesco.example.com.malkia.cardstream.OnCardClickListener;
import malkia.malkiaunesco.example.com.malkia.cardstream.PlacePickerFragment;
import malkia.malkiaunesco.example.com.malkia.cardstream.StreamRetentionFragment;

/**
 * Created by User on 12/13/2017.
 */

public class ReportFragment extends android.support.v4.app.Fragment{
    public static final String TAG = "MainActivity";
    public static final String FRAGTAG = "PlacePickerFragment";


    private CardStreamFragment mCardStreamFragment;

    private StreamRetentionFragment mRetentionFragment;
    private static final String RETENTION_TAG = "retention";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        FragmentManager fm = getChildFragmentManager();
        PlacePickerFragment fragment =
                (PlacePickerFragment) fm.findFragmentByTag(FRAGTAG);

        if (fragment == null) {
            FragmentTransaction transaction = fm.beginTransaction();
            fragment = new PlacePickerFragment();
            transaction.add(fragment, FRAGTAG);
            transaction.commit();
        }

        // Use fragment as click listener for cards, but must implement correct interface
        if (!(fragment instanceof OnCardClickListener)){
            throw new ClassCastException("PlacePickerFragment must " +
                    "implement OnCardClickListener interface.");
        }
        OnCardClickListener clickListener = (OnCardClickListener) fm.findFragmentByTag(FRAGTAG);

        mRetentionFragment = (StreamRetentionFragment) fm.findFragmentByTag(RETENTION_TAG);
        if (mRetentionFragment == null) {
            mRetentionFragment = new StreamRetentionFragment();
            fm.beginTransaction().add(mRetentionFragment, RETENTION_TAG).commit();
        } else {
            // If the retention fragment already existed, we need to pull some state.
            // pull state out
            CardStreamState state = mRetentionFragment.getCardStream();

            // dump it in CardStreamFragment.
            mCardStreamFragment =
                    (CardStreamFragment) fm.findFragmentById(R.id.fragment_cardstream);
            mCardStreamFragment.restoreState(state, clickListener);
        }
        return view;
    }

    public CardStreamFragment getCardStream() {
        if (mCardStreamFragment == null) {
            mCardStreamFragment = (CardStreamFragment)
                    getChildFragmentManager().findFragmentById(R.id.fragment_cardstream);
        }
        return mCardStreamFragment;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        CardStreamState state = getCardStream().dumpState();
        mRetentionFragment.storeCardStream(state);
    }


}


