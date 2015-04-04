package com.adamcrawford.ejuicer;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

/**
 * Author:  Adam Crawford
 * Project: EJuicer
 * Package: com.adamcrawford.ejuicer
 * File:    Fragment_JuiceList
 * Purpose: TODO Minimum 2 sentence description
 */
public class Fragment_JuiceList extends ListFragment {

    ParseQueryAdapter<JuiceItem> juiceAdapter;

    public Fragment_JuiceList() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        MainActivity.actionBar.setHomeButtonEnabled(false);
        MainActivity.actionBar.setDisplayHomeAsUpEnabled(false);

        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ParseQueryAdapter.QueryFactory<JuiceItem> factory = new ParseQueryAdapter.QueryFactory<JuiceItem>() {
            @Override
            public ParseQuery<JuiceItem> create() {
                ParseQuery<JuiceItem> query = null;
                try {
                    query = JuiceItem.getQuery();
                    query.orderByDescending(JuiceItem.RATING);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return query;
            }
        };

        juiceAdapter = new ParseQueryAdapter<JuiceItem>(getActivity(), factory) {
            @Override
            public View getItemView(JuiceItem juice, View view, ViewGroup parent) {
                if (view == null) {
                    view = View.inflate(getContext(), R.layout.item_juice, null);
                }
                TextView flavorView = (TextView) view.findViewById(R.id.JI_juiceFlavor);
                RatingBar ratingView = (RatingBar) view.findViewById(R.id.JI_juiceRating);
                flavorView.setText(juice.getFlavor());
                ratingView.setRating(juice.getRating().floatValue());
                return view;
            }
        };
        setListAdapter(juiceAdapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("JL", "LongClick");
                return false;
            }
        });
    }

    @Override
    public void onListItemClick(ListView list, View view, int position, long id) {
        JuiceItem juice = juiceAdapter.getItem(position);
        Fragment_NewJuice fnj = new Fragment_NewJuice();
        Bundle juiceBundle = new Bundle();
        juiceBundle.putSerializable("juice", juice);
        fnj.setArguments(juiceBundle);
        getFragmentManager().beginTransaction().replace(R.id.container, fnj).addToBackStack(null).commit();
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_add).setVisible(true);
    }
}
