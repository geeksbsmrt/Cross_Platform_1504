package com.adamcrawford.ejuicer;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseUser;

/**
 * Author:  Adam Crawford
 * Project: EJuicer
 * Package: com.adamcrawford.ejuicer
 * File:    Fragment_NewJuice
 * Purpose: TODO Minimum 2 sentence description
 */
public class Fragment_NewJuice extends Fragment implements View.OnClickListener {

    onNewJuice mListener;
    private JuiceItem juice;
    private EditText juiceFlavorView;
    private RatingBar juiceRatingBar;
    private Button deleteButton;

    public Fragment_NewJuice() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            if (getTargetFragment() == null) {
                Log.i("FNJ", "null");
            }
            mListener = (onNewJuice) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement onNewJuice");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_juice, container, false);

        MainActivity.actionBar.setTitle(R.string.app_name);
        setHasOptionsMenu(true);
        MainActivity.actionBar.setHomeButtonEnabled(true);
        MainActivity.actionBar.setDisplayHomeAsUpEnabled(true);

        juiceFlavorView = (EditText) rootView.findViewById(R.id.NJ_juiceFlavor);
        juiceRatingBar = (RatingBar) rootView.findViewById(R.id.NJ_juiceRating);
        deleteButton = (Button) rootView.findViewById(R.id.NJ_delete);
        deleteButton.setOnClickListener(this);

        try {
            juice = (JuiceItem) getArguments().getSerializable("juice");
            juiceFlavorView.setText(juice.getFlavor());
            juiceRatingBar.setRating(juice.getRating().floatValue());
            deleteButton.setVisibility(View.VISIBLE);
        } catch (NullPointerException e) {
            Log.i("FNJ", "No juice passed");
        }

        return rootView;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_add).setVisible(false);
        menu.findItem(R.id.action_save).setVisible(true);
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save: {
                Log.i("NJ", "Saving");
                hideKeyboard();

                if (!juiceFlavorView.getText().toString().equals("")) {
                    if (juice == null) {
                        juice = new JuiceItem();
                        juice.setACL(new ParseACL(ParseUser.getCurrentUser()));
                    }
                    juice.setFlavor(juiceFlavorView.getText().toString());
                    juice.setRating(juiceRatingBar.getRating());
                    if (MainActivity.isConnected(MainActivity.mContext)) {
                        juice.saveInBackground();
                    } else {
                        juice.pinInBackground();
                        mListener.onJuiceSave();
                    }
                    getFragmentManager().popBackStack();
                } else {
                    Toast.makeText(MainActivity.mContext, getString(R.string.noFlavor), Toast.LENGTH_LONG).show();
                }
                return false;
            }
            default: {
                return false;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.NJ_delete: {
                if (MainActivity.isConnected(MainActivity.mContext)) {
                    juice.deleteInBackground();
                } else {
                    juice.setDeleting(true);
                    juice.unpinInBackground();
                    juice.deleteEventually();
                }
                getFragmentManager().popBackStack();
                break;
            }
            default: {
                break;
            }
        }
    }

    public interface onNewJuice {
        void onJuiceSave();
    }
}
