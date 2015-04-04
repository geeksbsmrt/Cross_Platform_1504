package com.adamcrawford.ejuicer;

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
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Author:  Adam Crawford
 * Project: EJuicer
 * Package: com.adamcrawford.ejuicer
 * File:    Fragment_NewJuice
 * Purpose: TODO Minimum 2 sentence description
 */
public class Fragment_NewJuice extends Fragment implements View.OnClickListener {

    private JuiceItem juice;
    private EditText juiceFlavorView;
    private RatingBar juiceRatingBar;
    private Button deleteButton;

    public Fragment_NewJuice() {
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
                    try {
                        juice.save();
                    } catch (ParseException e) {
                        e.printStackTrace();
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
                try {
                    juice.delete();
                    getFragmentManager().popBackStack();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            }
            default: {
                break;
            }
        }
    }
}
