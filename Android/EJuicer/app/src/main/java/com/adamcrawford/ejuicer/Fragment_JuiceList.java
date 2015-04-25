package com.adamcrawford.ejuicer;

import android.app.ListFragment;
import android.os.Bundle;
import android.os.Handler;
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

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.List;

/**
 * Author:  Adam Crawford
 * Project: EJuicer
 * Package: com.adamcrawford.ejuicer
 * File:    Fragment_JuiceList
 * Purpose: TODO Minimum 2 sentence description
 */
public class Fragment_JuiceList extends ListFragment implements Fragment_NewJuice.onNewJuice {

    ParseQueryAdapter<JuiceItem> juiceAdapter;
    ParseQuery<JuiceItem> query;
    private Runnable delayUpdate = new Runnable() {
        @Override
        public void run() {
            juiceAdapter.loadObjects();
        }
    };

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
                try {
                    query = JuiceItem.getQuery();
                    query.orderByDescending(JuiceItem.RATING);
                    if (!MainActivity.isConnected(MainActivity.mContext)) {
                        query.fromLocalDatastore();
                        query.whereNotEqualTo(JuiceItem.DELETING, true);
                    }
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

        juiceAdapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<JuiceItem>() {
            @Override
            public void onLoading() {

            }

            @Override
            public void onLoaded(final List<JuiceItem> list, Exception e) {
                if (MainActivity.isConnected(MainActivity.mContext)) {
                    ParseObject.unpinAllInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            ParseObject.pinAllInBackground(list);
                        }
                    });
                }
            }
        });

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
        fnj.setTargetFragment(this, 0);
        getFragmentManager().beginTransaction().replace(R.id.container, fnj).addToBackStack(null).commit();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_add).setVisible(true);
    }

    @Override
    public void onJuiceSave() {
        Log.i("FJL", "onJuiceSave");
        Handler myHandler = new Handler();
        myHandler.postDelayed(delayUpdate, 500);
    }
}
